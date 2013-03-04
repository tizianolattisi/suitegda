/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
