/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.db.Adapter;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.RiferimentoProtocollo;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloAdapters {
    
    @Adapter
    public static Attribuzione adaptUfficioToAttribuzione(Ufficio ufficio){
        Attribuzione ap = new Attribuzione();
        ap.setUfficio(ufficio);
        ap.setPrincipale(Boolean.TRUE);
        return ap;
    }
    
    @Adapter
    public static RiferimentoProtocollo adaptProtocolloToRiferimentoProtocollo(Protocollo protocollo){
        RiferimentoProtocollo rp = new RiferimentoProtocollo();
        rp.setPrecedente(protocollo);
        return rp;
    }
    
}
