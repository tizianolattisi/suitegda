#!/bin/bash

# es.
# ./conigliera.sh gda pass 127.0.0.1 5432 gda gda pass 127.0.0.1 8080 /Sites/comune/documentLibrary 102 2016-06-28

# Postgres access
pglogin=$1
pgpass=$2
pghost=$3
pgport=$4
dbname=$5

# Alfresco access
alflogin=$6
alfpass=$7
alfhost=$8
alfport=$9

folderprotocollo=${10}
attribuzione=${11}
mailbox=${12}
fromdate=${13}


#
onlyone=''
#onlyone='LIMIT 1'

export PGPASSWORD=$pgpass

pre="psql -h $pghost -p $pgport -U $pglogin -t -z -F, -A"

rows=`$pre -c "select to_char(p.dataprotocollo, 'YYYY,MM,DD'), a.protocollo from protocollo.attribuzione a join protocollo.protocollo p on a.protocollo=p.iddocumento where a.ufficio=$attribuzione and a.rec_creato>'$fromdate' $onlyone;" $dbname`

for row in $rows
do
	IFS=',' read -r -a array <<< "$row"
	anno="${array[0]}"
	mese="${array[1]}"
	giorno="${array[2]}"
	protocollo="${array[3]}"
	
    echo "Processo protocollo $protocollo su attribuzione $attribuzione"


    folder=`curl -X POST --user "$alflogin":"$alfpass" "http://$alfhost:$alfport/alfresco/service/api/node/folder/workspace/SpacesStore/$mailbox" -H "Content-Type: application/json" -d'{"name":"'"$protocollo"'"}' 2>/dev/null | grep -i 'nodeRef' | cut -d'"' -f4`
    
    # allegati
    documenti=`curl -X GET --user "$alflogin":"$alfpass" http://$alfhost:$alfport/alfresco/service/api/path/workspace/SpacesStore/$folderprotocollo/$anno/$mese/$giorno/$protocollo/children 2>/dev/null | grep -i '<cmisra:pathSegment>' | cut -d'<' -f2 | cut -c20-`
    for ((i = 0; i < ${#documenti[@]}; i++))
    do	
    	documento="${documenti[$i]}"
    	docurl=${documento// /%20}

    	uuid=`curl -X GET --user "$alflogin":"$alfpass" http://$alfhost:$alfport/alfresco/service/api/path/workspace/SpacesStore/$folderprotocollo/$anno/$mese/$giorno/$protocollo/$docurl 2>/dev/null | grep -i 'urn:uuid' | cut -d'"' -f4`
    	echo " > $documento"
    	
    	# scarico il documento in locale
		curl -X GET --user "$alflogin":"$alfpass" "$uuid" -o "$documento" 2>/dev/null
		
		# carico il documento
    	curl -X POST --user "$alflogin":"$alfpass" --form "filedata=@$documento" --form "destination=$folder" "http://$alfhost:$alfport/alfresco/service/api/upload" 2>/dev/null
		rm "$documento"
				
    done

done
