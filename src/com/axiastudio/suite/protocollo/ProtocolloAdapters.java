/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.db.Adapter;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.protocollo.entities.Attribuzione;

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
}
