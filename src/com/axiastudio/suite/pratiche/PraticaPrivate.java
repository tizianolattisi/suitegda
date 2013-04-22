/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.pratiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Private;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.Pratica;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class PraticaPrivate {
    
    @Private
    public static Boolean praticaPrivata(Pratica pratica){
        
        // Se la pratica non è registrata, sicuramente non è privata
        if( pratica.getId() == null ){
            return false;
        }
        
        // Se la pratica non è riservata i suoi dati non sono privati
        if( !pratica.getRiservata() ){
            return false;
        }
        
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            Ufficio u = uu.getUfficio();
            if( u.equals(pratica.getGestione()) || u.equals(pratica.getAttribuzione()) || u.equals(pratica.getUbicazione()) ){
                if( uu.getRiservato() ){
                    return false;
                }
            } 
        }
        
        return true;
    }
}
