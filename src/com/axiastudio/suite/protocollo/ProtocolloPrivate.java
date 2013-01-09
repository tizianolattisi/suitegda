/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Private;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloPrivate {
    
    @Private
    public static Boolean protocolloPrivato(Protocollo protocollo){
        
        // Se il protocollo non è registrato, sicuramente non è privato
        if( protocollo.getId() == null ){
            return false;
        }
        
        // L'utente
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        
        // L'utente è un ricercatore o un supervisore protocollo
        if( utente.getRicercatoreprotocollo() == true ||
                utente.getSupervisoreprotocollo() == true){
            return false;
        }
        
        // Gli uffici in cui l'utente è inserito con flag "ricerca"
        List<Ufficio> uffici = new ArrayList();
        for( UfficioUtente uu: utente.getUfficioUtenteCollection() ){
            if( uu.getRicerca() == true ){
                uffici.add(uu.getUfficio());
            }
        }
        
        // L'utente è presente nello sportello?
        if( uffici.contains(protocollo.getSportello()) ){
            return false;
        }
        
        // L'utente è presente in almeno una attribuzione?
        for( Attribuzione attribuzione: protocollo.getAttribuzioneCollection() ){
            if( uffici.contains(attribuzione.getUfficio())){
                return false;
            }
        }
        
        return true;
    }
    
}
