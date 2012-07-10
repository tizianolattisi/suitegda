/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.db.Private;
import com.axiastudio.suite.protocollo.entities.Protocollo;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloPrivate {
    
    @Private
    public static Boolean protocolloPrivato(Protocollo protocollo){
        // TODO
        if( protocollo.getOggetto().equals("Oggetto riservato") ){
            return true;
        }
        return false;
    }
    
}
