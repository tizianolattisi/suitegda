package com.axiastudio.suite.plugins.cmis;

import com.axiastudio.pypapi.Application;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipoSoggetto;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiDocumento;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatoDocer;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pivamichela on 01/06/17.
 */
public class DocerUtil {


    static public Boolean protocollaDocumentiDocer(Protocollo protocollo)
    {
        Application app = Application.getApplicationInstance();
        DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"));

        List<Map<String, String>> documenti=new ArrayList<Map<String, String>>();
        Map<MetadatiDocumento, String> order=new HashMap<MetadatiDocumento, String>() {{
            put(MetadatiDocumento.CREATION_DATE, MetadatoDocer.SORT_ASC); }};

        // Documento da protocollare?
        try {
            Map<MetadatiDocumento, String> searchCriteria = new HashMap<MetadatiDocumento, String>();
            searchCriteria.put(MetadatiDocumento.NUM_PG, protocollo.getIddocumento());
            documenti=docerHelper.searchDocuments(Arrays.asList(searchCriteria), Arrays.asList(order));
            if ( documenti.size() > 0 ) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            Map<MetadatiDocumento, String> searchCriteria = new HashMap<MetadatiDocumento, String>();
            searchCriteria.put(MetadatiDocumento.EXTERNAL_ID, "protocollo_"+protocollo.getId());
            searchCriteria.put(MetadatiDocumento.TIPO_COMPONENTE, MetadatiDocumento.TIPO_COMPONENTE_PRINCIPALE);
            documenti=docerHelper.searchDocuments(Arrays.asList(searchCriteria), Arrays.asList(order));
            if ( documenti.size() == 0 ) {
                System.err.print("Manca il documento principale!!!");
                return false;
            } else if ( documenti.size()>1 ) {
                System.err.print("Esiste più di un documento principale!!!");
                return false;
            } else {
                for ( Map<String, String> documento: documenti ) {
                    Boolean res;
                    try {
                        Map<MetadatiDocumento, String> metadata = new HashMap<MetadatiDocumento, String>();
                        metadata.put(MetadatiDocumento.COD_ENTE, "c_h330");
                        metadata.put(MetadatiDocumento.COD_AOO, "RSERVIZI");
                        metadata.put(MetadatiDocumento.NUM_PG, protocollo.getIddocumento());
                        metadata.put(MetadatiDocumento.ANNO_PG, protocollo.getAnno().toString());
                        metadata.put(MetadatiDocumento.DATA_PG, (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(protocollo.getDataprotocollo()));
                        metadata.put(MetadatiDocumento.OGGETTO_PG, protocollo.getOggetto());
                        metadata.put(MetadatiDocumento.REGISTRO_PG, "ProtocolloRiva");
                        metadata.put(MetadatiDocumento.TIPO_PROTOCOLLAZIONE, protocollo.getTipo().toString().substring(0, 1));
                        metadata.put(MetadatiDocumento.TIPO_FIRMA, "NF");
                        if ( protocollo.getTipo() == TipoProtocollo.ENTRATA ) {
                            metadata.put(MetadatiDocumento.MITTENTI, mittentiDocer(protocollo.getSoggettoProtocolloCollection()));
//                            System.out.print(mittentiDocer(protocollo.getSoggettoProtocolloCollection()));
                        } else if ( protocollo.getTipo() == TipoProtocollo.USCITA ) {
                            metadata.put(MetadatiDocumento.DESTINATARI, destinatariDocer(protocollo.getSoggettoProtocolloCollection()));
//                            System.out.print(destinatariDocer(protocollo.getSoggettoProtocolloCollection()));
                        }
                        String docnum = documento.get("DOCNUM");
                        res = docerHelper.protocollaDocumento(docnum, Arrays.asList(metadata));
//                        res = docerHelper.protocollaDocumento(documento.get(MetadatiDocumento.DOCNUM), Arrays.asList(metadata));
                        if (!res) {
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static String mittentiDocer(Collection<SoggettoProtocollo> elencoMittenti){
        String mittentiXml="<Mittenti>";

        for (SoggettoProtocollo mittente: elencoMittenti) {
            Soggetto soggetto=mittente.getSoggetto();
            String mittenteXml="<Mittente>";
            if ( soggetto.getTipo()== TipoSoggetto.PERSONA ) {
                String cf;
                if ( soggetto.getCodicefiscale()!= null ) {
                    cf=soggetto.getCodicefiscale();
                } else {
                    cf=String.format("%016d", soggetto.getId());
                }
                mittenteXml += "<Persona id=\"" + cf + "\">";
                mittenteXml += "<Nome>" + soggetto.getNome() + "</Nome>";
                mittenteXml += "<Cognome>" + soggetto.getCognome() + "</Cognome>";
                mittenteXml += "</Persona>";
            } else if ( soggetto.getTipo()== TipoSoggetto.AZIENDA ) {
                String cf;
                if ( soggetto.getCodicefiscale()!= null ) {
                    cf=soggetto.getCodicefiscale();
                } else if ( soggetto.getPartitaiva()!= null ) {
                    cf=soggetto.getPartitaiva();
                } else {
                    cf=String.format("%011d", soggetto.getId());
                }
                mittenteXml += "<PersonaGiuridica tipo=\"CodiceFiscalePG\" id=\"" + cf + "\">";
                mittenteXml += "<Denominazione>" + soggetto.getRagionesociale() + "</Denominazione>";
                mittenteXml += "<IndirizzoPostale><Denominazione></Denominazione></IndirizzoPostale>";
                mittenteXml += "</PersonaGiuridica>";
            } else {
                mittenteXml += "<Amministrazione>";
                mittenteXml += "<Denominazione>" + soggetto.getDescrizione() + "</Denominazione>";
                mittenteXml += "<CodiceAmministrazione>" + soggetto.getIndicepao() + "</CodiceAmministrazione>";
                mittenteXml += "</Amministrazione>";
            }
            mittentiXml += mittenteXml + "</Mittente>";
        }

        return mittentiXml + "</Mittenti>";
    }

    static String destinatariDocer(Collection<SoggettoProtocollo> elencoDestinatari){
        String destinatariXml="<Destinatari>";

        for (SoggettoProtocollo destinatario: elencoDestinatari) {
            Soggetto soggetto=destinatario.getSoggetto();
            String destinatarioXml="<Destinatario>";
            if ( soggetto.getTipo()== TipoSoggetto.PERSONA ) {
                String cf;
                if ( soggetto.getCodicefiscale()!= null ) {
                    cf=soggetto.getCodicefiscale();
                } else {
                    cf=String.format("%016d", soggetto.getId());
                }
                destinatarioXml += "<Persona id=\"" + cf + "\">";
                destinatarioXml += "<Nome>" + soggetto.getNome() + "</Nome>";
                destinatarioXml += "<Cognome>" + soggetto.getCognome() + "</Cognome>";
                destinatarioXml += "</Persona>";
            } else if ( soggetto.getTipo()== TipoSoggetto.AZIENDA ) {
                String cf;
                if ( soggetto.getCodicefiscale()!= null ) {
                    cf=soggetto.getCodicefiscale();
                } else if ( soggetto.getPartitaiva()!= null ) {
                    cf=soggetto.getPartitaiva();
                } else {
                    cf=String.format("%011d", soggetto.getId());
                }
                destinatarioXml += "<PersonaGiuridica tipo=\"CodiceFiscalePG\" id=\"" + cf + "\">";
                destinatarioXml += "<Denominazione>" + soggetto.getRagionesociale() + "</Denominazione>";
                destinatarioXml += "<IndirizzoPostale><Denominazione></Denominazione></IndirizzoPostale>";
                destinatarioXml += "</PersonaGiuridica>";
            } else {
                destinatarioXml += "<Amministrazione>";
                destinatarioXml += "<Denominazione>" + soggetto.getDescrizione() + "</Denominazione>";
                destinatarioXml += "<CodiceAmministrazione>" + soggetto.getIndicepao() + "</CodiceAmministrazione>";
                destinatarioXml += "</Amministrazione>";
            }
            destinatariXml += destinatarioXml + "</Destinatario>";
        }
        return destinatariXml + "</Destinatari>";
    }

}
