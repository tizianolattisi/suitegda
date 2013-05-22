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
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.IQuickInsertDialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.anagrafiche.AnagraficheAdapters;
import com.axiastudio.suite.anagrafiche.entities.AlboProfessionale;
import com.axiastudio.suite.anagrafiche.entities.Gruppo;
import com.axiastudio.suite.anagrafiche.entities.GruppoSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Indirizzo;
import com.axiastudio.suite.anagrafiche.entities.Relazione;
import com.axiastudio.suite.anagrafiche.entities.RelazioneSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Riferimento;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.Stato;
import com.axiastudio.suite.anagrafiche.entities.TitoloSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormIndirizzo;
import com.axiastudio.suite.anagrafiche.forms.FormQuickInsertSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormRelazioneSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormSoggetto;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.entities.MovimentoDetermina;
import com.axiastudio.suite.deliberedetermine.forms.FormDetermina;
import com.axiastudio.suite.finanziaria.entities.Capitolo;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.PraticaAdapters;
import com.axiastudio.suite.pratiche.PraticaCallbacks;
import com.axiastudio.suite.pratiche.PraticaPrivate;
import com.axiastudio.suite.pratiche.entities.Dipendenza;
import com.axiastudio.suite.pratiche.entities.DipendenzaPratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.pratiche.forms.FormDipendenzaPratica;
import com.axiastudio.suite.pratiche.forms.FormPratica;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.procedimenti.entities.Norma;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import com.axiastudio.suite.procedimenti.forms.FormDelega;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloCallbacks;
import com.axiastudio.suite.protocollo.ProtocolloPrivate;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.MotivazioneAnnullamento;
import com.axiastudio.suite.protocollo.entities.Oggetto;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoRiservatoProtocollo;
import com.axiastudio.suite.protocollo.entities.Titolo;
import com.axiastudio.suite.protocollo.forms.FormAnnullamentoProtocollo;
import com.axiastudio.suite.protocollo.forms.FormProtocollo;
import com.axiastudio.suite.protocollo.forms.FormSoggettoProtocollo;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.pubblicazioni.forms.FormPubblicazione;
import com.axiastudio.suite.sedute.entities.CaricaCommissione;
import com.axiastudio.suite.sedute.entities.Commissione;
import com.axiastudio.suite.sedute.entities.Seduta;
import com.axiastudio.suite.sedute.entities.TipoSeduta;
import com.axiastudio.suite.sedute.forms.FormTipoSeduta;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class Configure {
    private static Database db;
    
    public static void configure(Database db){

        db = db;
        adapters();
        callbacks();
        privates();

        forms(db);
        
    }

    private static void adapters() {
        Register.registerAdapters(Resolver.adaptersFromClass(AnagraficheAdapters.class));        
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerAdapters(Resolver.adaptersFromClass(PraticaAdapters.class));
    }

    private static void callbacks() {
        Register.registerCallbacks(Resolver.callbacksFromClass(ProtocolloCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(PraticaCallbacks.class));
    }

    private static void privates() {
        Register.registerPrivates(Resolver.privatesFromClass(PraticaPrivate.class));
        Register.registerPrivates(Resolver.privatesFromClass(ProtocolloPrivate.class));
    }

    private static void forms(Database db) {
        
        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Costante.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                              Ufficio.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/utente.ui",
                              Utente.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              AlboProfessionale.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              TitoloSoggetto.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Titolo.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Oggetto.class,
                              Window.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Stato.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/gruppo.ui",
                              Gruppo.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/relazione.ui",
                              Relazione.class,
                              Window.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/soggetto.ui",
                              Soggetto.class,
                              FormSoggetto.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/relazionesoggetto.ui",
                              RelazioneSoggetto.class,
                              FormRelazioneSoggetto.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/grupposoggetto.ui",
                              GruppoSoggetto.class,
                              Dialog.class);
        
        Register.registerUtility(FormQuickInsertSoggetto.class, IQuickInsertDialog.class, Soggetto.class.getName());
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/indirizzo.ui",
                              Indirizzo.class,
                              FormIndirizzo.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/riferimento.ui",
                              Riferimento.class,
                              Dialog.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/pratica.ui",
                              Pratica.class,
                              FormPratica.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/tipopratica.ui",
                              TipoPratica.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Dipendenza.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/motivazioneannullamento.ui",
                              MotivazioneAnnullamento.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/annullamentoprotocollo.ui",
                              AnnullamentoProtocollo.class,
                              FormAnnullamentoProtocollo.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/dipendenzapratica.ui",
                              DipendenzaPratica.class,
                              FormDipendenzaPratica.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/soggettoprotocollo.ui",
                              SoggettoProtocollo.class,
                              FormSoggettoProtocollo.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/soggettoprotocollo.ui",
                              SoggettoRiservatoProtocollo.class,
                              FormSoggettoProtocollo.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                              PraticaProtocollo.class,
                              Dialog.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
                              Protocollo.class,
                              FormProtocollo.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Fascicolo.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pubblicazioni/forms/pubblicazione.ui",
                              Pubblicazione.class,
                              FormPubblicazione.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Carica.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Commissione.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/caricacommissione.ui",
                              CaricaCommissione.class,
                              Window.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/tiposeduta.ui",
                              TipoSeduta.class,
                              FormTipoSeduta.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/seduta.ui",
                              Seduta.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/finanziaria/forms/servizio.ui",
                              Servizio.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Capitolo.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/deliberedetermine/forms/determina.ui",
                              Determina.class,
                              FormDetermina.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/deliberedetermine/forms/movimentodetermina.ui",
                              MovimentoDetermina.class,
                              Dialog.class);
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/procedimenti/forms/norma.ui",
                              Norma.class,
                              Window.class);
       
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/procedimenti/forms/procedimento.ui",
                              Procedimento.class,
                              Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/procedimenti/forms/delega.ui",
                              Delega.class,
                              FormDelega.class);
    }
    
}
