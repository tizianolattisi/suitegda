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
package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.Procedimento;

import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public interface IGestoreDeleghe {

    public Utente trovaTitolare(CodiceCarica codiceCarica, Servizio servizio);
    
    public TitoloDelega checkTitoloODelega(CodiceCarica codiceCarica);

    public TitoloDelega checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio);
    
    public TitoloDelega checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento);

    public TitoloDelega checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio);

    public TitoloDelega checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente);

    public TitoloDelega checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica);

}
