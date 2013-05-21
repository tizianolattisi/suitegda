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
import com.axiastudio.pypapi.annotations.Adapter;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.MotivazioneAnnullamento;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.RiferimentoProtocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoRiservatoProtocollo;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloAdapters {
    
    @Adapter
    public static Attribuzione adaptUfficioToAttribuzione(Ufficio ufficio){
        Attribuzione ap = new Attribuzione();
        ap.setUfficio(ufficio);
        ap.setPrincipale(Boolean.FALSE);
        return ap;
    }
    
    @Adapter
    public static RiferimentoProtocollo adaptProtocolloToRiferimentoProtocollo(Protocollo protocollo){
        RiferimentoProtocollo rp = new RiferimentoProtocollo();
        rp.setPrecedente(protocollo);
        return rp;
    }
    
    @Adapter
    public static SoggettoProtocollo adaptSoggettoToSoggettoProtocollo(Soggetto soggetto){
        SoggettoProtocollo sp = new SoggettoProtocollo();
        sp.setSoggetto(soggetto);
        return sp;
    }

    @Adapter
    public static SoggettoRiservatoProtocollo adaptSoggettoToSoggettoRiservatoProtocollo(Soggetto soggetto){
        SoggettoRiservatoProtocollo srp = new SoggettoRiservatoProtocollo();
        srp.setSoggetto(soggetto);
        return srp;
    }
    
    @Adapter
    public static AnnullamentoProtocollo adaptMotivazioneAnnullamentoToAnnullamentoProtocollo(MotivazioneAnnullamento motivazione){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        AnnullamentoProtocollo ap = new AnnullamentoProtocollo();
        ap.setMotivazioneannullamento(motivazione);
        ap.setEsecutorerichiesta(autenticato.getLogin());
        ap.setDatarichiesta(today);
        return ap;
        
    }

}
