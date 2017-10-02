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

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.IQuickInsertDialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.anagrafiche.AnagraficheAdapters;
import com.axiastudio.suite.anagrafiche.SoggettoCallbacks;
import com.axiastudio.suite.anagrafiche.entities.*;
//import com.axiastudio.suite.anagrafiche.forms.FormIndirizzo;
import com.axiastudio.suite.anagrafiche.forms.FormQuickInsertSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormRelazioneSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormSoggetto;
import com.axiastudio.suite.base.entities.Giunta;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.DeterminaCallbacks;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.entities.MovimentoDetermina;
import com.axiastudio.suite.deliberedetermine.entities.SpesaImpegnoEsistente;
import com.axiastudio.suite.deliberedetermine.forms.FormDetermina;
import com.axiastudio.suite.finanziaria.entities.Capitolo;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.generale.entities.Etichetta;
import com.axiastudio.suite.modelli.entities.Modello;
import com.axiastudio.suite.modelli.entities.Segnalibro;
import com.axiastudio.suite.modelli.forms.FormModello;
import com.axiastudio.suite.plugins.cmis.AlfrescoCmisPlugin;
import com.axiastudio.suite.plugins.cmis.AlfrescoCmisStreamProvider;
import com.axiastudio.suite.plugins.docer.DocerPlugin;
import com.axiastudio.suite.plugins.ooops.FileStreamProvider;
import com.axiastudio.suite.plugins.ooops.OoopsPlugin;
import com.axiastudio.suite.plugins.ooops.RuleSet;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.suite.pratiche.PraticaAdapters;
import com.axiastudio.suite.pratiche.PraticaCallbacks;
import com.axiastudio.suite.pratiche.PraticaPrivate;
import com.axiastudio.suite.pratiche.entities.*;
import com.axiastudio.suite.pratiche.forms.FormDipendenzaPratica;
import com.axiastudio.suite.pratiche.forms.FormPratica;
import com.axiastudio.suite.pratiche.forms.FormTipoPratica;
import com.axiastudio.suite.procedimenti.DelegaCallbacks;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.procedimenti.entities.*;
import com.axiastudio.suite.procedimenti.forms.FormDelega;
import com.axiastudio.suite.procedimenti.forms.FormFaseProcedimento;
import com.axiastudio.suite.protocollo.AttribuzioneCallbacks;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloCallbacks;
import com.axiastudio.suite.protocollo.ProtocolloPrivate;
import com.axiastudio.suite.protocollo.entities.*;
import com.axiastudio.suite.protocollo.forms.*;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.pubblicazioni.entities.TipoAttoPubblicazione;
import com.axiastudio.suite.pubblicazioni.forms.FormPubblicazione;
import com.axiastudio.suite.richieste.RichiestaCallbacks;
import com.axiastudio.suite.richieste.RichiestaPrivate;
import com.axiastudio.suite.richieste.entities.*;
import com.axiastudio.suite.richieste.forms.FormDestinatarioUfficio;
import com.axiastudio.suite.richieste.forms.FormDestinatarioUtente;
import com.axiastudio.suite.richieste.forms.FormRichiesta;
import com.axiastudio.suite.sedute.entities.CaricaCommissione;
import com.axiastudio.suite.sedute.entities.Commissione;
import com.axiastudio.suite.sedute.entities.Seduta;
import com.axiastudio.suite.sedute.entities.TipoSeduta;
import com.axiastudio.suite.sedute.forms.FormTipoSeduta;
import com.axiastudio.suite.urp.entities.AperturaURP;
import com.axiastudio.suite.urp.entities.NotiziaURP;
import com.axiastudio.suite.urp.entities.ServizioAlCittadino;
import com.axiastudio.suite.urp.entities.Sportello;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class Configure {

    public static void configure(Database db){

        adapters();
        callbacks();
        privates();

        forms(db);
        plugins();
        templates();

        // gestore deleghe
        GestoreDeleghe gestoreDeleghe = new GestoreDeleghe();
        Register.registerUtility(gestoreDeleghe, IGestoreDeleghe.class);

    }

    private static void adapters() {
        Register.registerAdapters(Resolver.adaptersFromClass(AnagraficheAdapters.class));        
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerAdapters(Resolver.adaptersFromClass(PraticaAdapters.class));
    }

    private static void callbacks() {
        Register.registerCallbacks(Resolver.callbacksFromClass(SoggettoCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(ProtocolloCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(PraticaCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(DeterminaCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(RichiestaCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(AttribuzioneCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(DelegaCallbacks.class));
    }

    private static void privates() {
        Register.registerPrivates(Resolver.privatesFromClass(PraticaPrivate.class));
        Register.registerPrivates(Resolver.privatesFromClass(ProtocolloPrivate.class));
        Register.registerPrivates(Resolver.privatesFromClass(RichiestaPrivate.class));
    }

    private static void plugins() {

        /* CMIS */

        Application app = Application.getApplicationInstance();
        String cmisUrl = (String) app.getConfigItem("cmis.url");
        String cmisUser = (String) app.getConfigItem("cmis.user");
        String cmisPassword = (String) app.getConfigItem("cmis.password");
//        String alfrescoPathProtocollo = (String) app.getConfigItem("alfrescopath.protocollo");
        String alfrescoPathPratica = (String) app.getConfigItem("alfrescopath.pratica");
//        String alfrescoPathRichiesta = (String) app.getConfigItem("alfrescopath.richiesta");
//        String alfrescoPathPubblicazione = (String) app.getConfigItem("alfrescopath.pubblicazione");

//        AlfrescoCmisPlugin cmisPlugin = new AlfrescoCmisPlugin();
//        String templateCmisProtocollo = alfrescoPathProtocollo + "/${dataprotocollo,date,yyyy}/${dataprotocollo,date,MM}/${dataprotocollo,date,dd}/${iddocumento}/";
//        cmisPlugin.setup(cmisUrl, cmisUser, cmisPassword,
//                templateCmisProtocollo,
//                Boolean.FALSE);
//        Register.registerPlugin(cmisPlugin, FormProtocollo.class);
//        Register.registerPlugin(cmisPlugin, Protocollo.class);
//        Register.registerPlugin(cmisPlugin, FormScrivania.class);
//
//        AlfrescoCmisPlugin cmisPluginPubblicazioni = new AlfrescoCmisPlugin();
//        cmisPluginPubblicazioni.setup(cmisUrl, cmisUser, cmisPassword,
//                alfrescoPathPubblicazione + "/${id}/");
//        Register.registerPlugin(cmisPluginPubblicazioni, FormPubblicazione.class);
//        Register.registerPlugin(cmisPluginPubblicazioni, Pubblicazione.class);
//
        AlfrescoCmisPlugin cmisPluginPratica = new AlfrescoCmisPlugin();
        cmisPluginPratica.setup(cmisUrl, cmisUser, cmisPassword,
                alfrescoPathPratica + "/${datapratica,date,yyyy}/${datapratica,date,MM}/${idpratica}/",
                Boolean.FALSE);
        Register.registerPlugin(cmisPluginPratica, FormPratica.class);
        Register.registerPlugin(cmisPluginPratica, Pratica.class);
        Register.registerPlugin(cmisPluginPratica, FormDetermina.class);
        Register.registerPlugin(cmisPluginPratica, Determina.class);
//
//        AlfrescoCmisPlugin cmisPluginRichiesta = new AlfrescoCmisPlugin();
//        cmisPluginRichiesta.setup(cmisUrl, cmisUser, cmisPassword,
//                alfrescoPathRichiesta + "/${pathdocumento}/",
//                Boolean.FALSE);
//        Register.registerPlugin(cmisPluginRichiesta, Richiesta.class);

        /* Doc/ER */
        DocerPlugin docerPlugin = new DocerPlugin();
        docerPlugin.setup((String)app.getConfigItem("docer.url"),
                (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"),
                (String) app.getConfigItem("docer.urlpath"),
                Boolean.FALSE);
        Register.registerPlugin(docerPlugin, FormProtocollo.class);
        Register.registerPlugin(docerPlugin, Protocollo.class);
        Register.registerPlugin(docerPlugin, FormScrivania.class);
        Register.registerPlugin(docerPlugin, FormPubblicazione.class);
        Register.registerPlugin(docerPlugin, Pubblicazione.class);
//        Register.registerPlugin(docerPlugin, FormPratica.class);
//        Register.registerPlugin(docerPlugin, Pratica.class);
//        Register.registerPlugin(docerPlugin, FormDetermina.class);
//        Register.registerPlugin(docerPlugin, Determina.class);
        Register.registerPlugin(docerPlugin, Richiesta.class);
        Register.registerPlugin(docerPlugin, Utente.class);
        Register.registerPlugin(docerPlugin, Ufficio.class);
        Register.registerPlugin(docerPlugin, UfficioUtente.class);

        /* OOOPS (OpenOffice) */
        String ooopsConnString = (String) app.getConfigItem("ooops.connection");
        OoopsPlugin ooopsPlugin = new OoopsPlugin();
        ooopsPlugin.setup(ooopsConnString, Boolean.FALSE);
        Register.registerPlugin(ooopsPlugin, FormPratica.class);
        Register.registerPlugin(ooopsPlugin, Pratica.class);
        Register.registerPlugin(ooopsPlugin, FormDetermina.class);
        Register.registerPlugin(ooopsPlugin, Determina.class);

    }

    private static void templates() {

        /* CMIS */
        Application app = Application.getApplicationInstance();
        String cmisUrl = (String) app.getConfigItem("cmis.url");
        String cmisUser = (String) app.getConfigItem("cmis.user");
        String cmisPassword = (String) app.getConfigItem("cmis.password");

        OoopsPlugin ooopsPlugin = (OoopsPlugin) Register.queryPlugin(FormPratica.class, "Ooops");
        List<Modello> modelli = SuiteUtil.elencoModelli();
        for( Modello modello: modelli ){
            HashMap<String,String> map = new HashMap<String, String>();
            Modello modellopadre = modello.getModellopadre();
            if( modellopadre != null ){
                for( Segnalibro segnalibro: modellopadre.getSegnalibroCollection() ){
                    map.put(segnalibro.getSegnalibro(), segnalibro.getCodice());
                }
            }
            for( Segnalibro segnalibro: modello.getSegnalibroCollection() ){
                map.put(segnalibro.getSegnalibro(), segnalibro.getCodice());
            }
            RuleSet ruleSet = new RuleSet(map);
            IStreamProvider streamProvider = null;
            if( modello.getUri() != null && modello.getUri().startsWith("workspace:") ){
                streamProvider = new AlfrescoCmisStreamProvider(cmisUrl, cmisUser, cmisPassword, modello.getUri());
            } else {
                streamProvider = new FileStreamProvider(modello.getUri());
            }
            Template template = new Template(streamProvider, modello.getTitolo(), modello.getDescrizione(), null, ruleSet);
            // Uso UserData per indicare se un modello è protocollabile
            template.setUserData(modello.getProtocollabile());
            ooopsPlugin.addTemplate(template);
        }
    }

    private static void forms(Database db) {

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                Costante.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                Etichetta.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/base/forms/giunta.ui",
                Giunta.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                Ufficio.class,
                SuiteBaseForm.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/base/forms/utente.ui",
                Utente.class,
                SuiteBaseForm.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/base/forms/ufficioutente.ui",
                UfficioUtente.class,
                Dialog.class);

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
                SuiteBaseForm.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/anagrafiche/forms/relazione.ui",
                Relazione.class,
                SuiteBaseForm.class);

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
                Dialog.class);

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
                FormTipoPratica.class);

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                Dipendenza.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/protocollo/forms/motivazioneannullamento.ui",
                MotivazioneAnnullamento.class,
                SuiteBaseForm.class);

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
                FormPraticaProtocollo.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
                Protocollo.class,
                FormProtocollo.class);

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                Fascicolo.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                TipoRiferimentoMittente.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/pubblicazioni/forms/pubblicazione.ui",
                Pubblicazione.class,
                FormPubblicazione.class);

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                TipoAttoPubblicazione.class,
                Window.class);

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
                SuiteBaseForm.class);

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
                SuiteBaseForm.class);

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
                "classpath:com/axiastudio/suite/deliberedetermine/forms/spesaimpegnoesistente.ui",
                SpesaImpegnoEsistente.class,
                Dialog.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/procedimenti/forms/norma.ui",
                Norma.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/procedimenti/forms/procedimento.ui",
                Procedimento.class,
                SuiteBaseForm.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/procedimenti/forms/faseprocedimento.ui",
                FaseProcedimento.class,
                FormFaseProcedimento.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/procedimenti/forms/delega.ui",
                Delega.class,
                FormDelega.class);

        Register.registerForm(db.getEntityManagerFactory(),
                null,
                Fase.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/anagrafiche/forms/titolostudio.ui",
                TitoloStudio.class,
                SuiteBaseForm.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/anagrafiche/forms/provincia.ui",
                Provincia.class,
                Dialog.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/anagrafiche/forms/titolostudiosoggetto.ui",
                TitoloStudioSoggetto.class,
                Dialog.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/modelli/forms/modello.ui",
                Modello.class,
                FormModello.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/modelli/forms/segnalibro.ui",
                Segnalibro.class,
                Dialog.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/richieste/forms/richiesta.ui",
                Richiesta.class,
                FormRichiesta.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/richieste/forms/destinatariorichiesta.ui",
                DestinatarioUfficio.class,
                FormDestinatarioUfficio.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/richieste/forms/destinatariorichiesta.ui",
                DestinatarioUtente.class,
                FormDestinatarioUtente.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                RichiestaPratica.class,
                FormPraticaProtocollo.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                RichiestaProtocollo.class,
                FormPraticaProtocollo.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/urp/forms/servizioalcittadino.ui",
                ServizioAlCittadino.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/urp/forms/sportello.ui",
                Sportello.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/urp/forms/aperturaurp.ui",
                AperturaURP.class,
                Window.class);

        Register.registerForm(db.getEntityManagerFactory(),
                "classpath:com/axiastudio/suite/urp/forms/notiziaurp.ui",
                NotiziaURP.class,
                Window.class);

    }
    
}
