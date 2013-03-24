# encoding: utf-8
#
# Estrazione titolario

TEMPLATE = "INSERT INTO PROTOCOLLO.FASCICOLO (CATEGORIA, CLASSE, FASCICOLO, DESCRIZIONE, NOTE) VALUES (%s, %s, %s, '%s', '%s');"

CATEGORIE= """AFFARI GIURIDICO-ISTITUZIONALI, ORGANIZZAZIONE E COMUNICAZIONE
ORGANI DI GOVERNO, GESTIONE, CONTROLLO E CONSULENZA; CONSIGLIO PROVINCIALE
PATRIMONIO, DEMANIO, LOGISTICA E RISORSE STRUMENTALI
PERSONALE
RISORSE FINANZIARIE, GESTIONE CONTABILE E FISCALE
SISTEMA ARCHIVISTICO, STATISTICA, SERVIZI INFORMATIVI E TELECOMUNICAZIONI
AFFARI LEGALI, CONTENZIOSO, RESPONSABILITÀ CIVILE E P A TRIMONIALE
PROGRAMMAZIONE, COORDINAMENTO E CONTROLLO
POLIZIA AMMINISTRATIVA E PUBBLICA SICUREZZA
AGRICOLTURA, ZOOTECNIA E ALIMENTAZIONE
RISORSE FORESTALI, FAUNISTICHE E MONTANE
INDUSTRIA E ATTIVITÀ ESTRATTIVE
ARTIGIANATO
COMMERCIO
TURISMO
COOPERAZIONE
AMBIENTE, RISORSE NATURALI E ENERGIA
PIANIFICAZIONE E ASSETTO DEL TERRITORIO
EDILIZIA PUBBLICA E INFRASTRUTTURE
TRASPORTI E MOTORIZZAZIONE CIVILE
PROTEZIONE CIVILE
SANITÀ, IGIENE E VETERINARIA
POLITICHE SOCIALI E PER IL BENESSERE
LAVORO
BENI E ATTIVITÀ CULTURALI
SISTEMA EDUCATIVO PROVINCIALE, UNIVERSITÀ, RICERCA SCIENTIFICA E INNOVAZIONE
CATASTO
LIBRO FONDIARIO
OGGETTI DIVERSI"""

print TEMPLATE % ("0", "0", "0", "NON CLASSIFICATO", "")

i = 0
for row in CATEGORIE.split("\n"):
    i += 1
    print TEMPLATE % (i, "0", "0", row, "")

f = open("titolario_utf8.txt", "r")
content = f.read()
f.close()
note = ""
cercaNote = False
categoria = classe = fascicolo = None

for row in content.split("\n"):

    if len(row)==0:
        continue

    try:
        int(row[0])
        rigaFascicolo= True
    except ValueError:
        rigaFascicolo= False

    if rigaFascicolo is False:
        if cercaNote is True:
            if row[:6] == "TITOLO":
                cercaNote = False
            elif row[:10] != "_"*10:
                if note != "":
                    note += "\n"
                note += row.replace("'", "''")
        continue
    elif rigaFascicolo is True and categoria is not None:
        print TEMPLATE % (categoria, classe or "0", fascicolo or "0", txt, note)
        note = ""
        rigaNote = False
        cercaNote = True

#     try:
#         int(row[0])
#         skip = False
#     except ValueError:
#         if cercaNote is True:
#             note += row
#             continue
#         else:
#             skip = True
# 
#     if skip:
#         continue



    n = row.split(" ")[0]
    tkns = n.split(".")
    while tkns[-1] == "":
        tkns.pop(-1)
    txt = " ".join(row.split(" ")[1:])

    if len(tkns) > 0:
        categoria = int(tkns[0])
    else:
        categoria = None
    if len(tkns) > 1:
        classe = int(tkns[1])
    else:
        classe = None
    if len(tkns) > 2:
        fascicolo = int(tkns[2])
    else:
        fascicolo = None

#    if categoria is not None:
#        cercaNote = True
#        #print TEMPLATE % (categoria or "NULL", classe or "NULL", fascicolo or "NULL", txt, note)

