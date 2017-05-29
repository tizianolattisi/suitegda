package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.base.entities.*;
import com.axiastudio.suite.plugins.cmis.DocerPlugin;
import com.axiastudio.suite.richieste.entities.*;
import com.trolltech.qt.gui.*;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiDocumento.TIPO_COMPONENTE_VALUES;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FormRichiesta extends Window {

    private static final String TMP_PATH_TEMPLATE = "yyyy-MM-SSSSSS";
    Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
    public RichiestaToolbar richiestaToolbar;
    DocerPlugin docerPlugin;

    public FormRichiesta(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        this.richiestaToolbar = new RichiestaToolbar("Richiesta", this);
        this.addToolBar(richiestaToolbar);
        PyPaPiTableView tblDestinatari=((PyPaPiTableView) this.findChild(QWidget.class, "tableViewPersone"));
        tblDestinatari.setOpen(false);
        tblDestinatari.entityInserted.connect(this, "destinatarioInserito(Object)");
        tblDestinatari=((PyPaPiTableView) this.findChild(QWidget.class, "tableViewUffici"));
        tblDestinatari.setOpen(false);
        tblDestinatari.entityInserted.connect(this, "destinatarioInserito(Object)");
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableViewPrecedenti"), Boolean.TRUE);
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableViewSuccessivi"), Boolean.TRUE);

        docerPlugin = (DocerPlugin) Register.queryPlugin(Richiesta.class, "DocER");

        try {
            Method storeFactory = this.getClass().getMethod("storeMittente");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Mittente");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormRichiesta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormRichiesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        /* disabilito una buona quantità di campi se richiesta già inserita */
        Richiesta richiesta=(Richiesta) this.getContext().getCurrentEntity();
        Boolean nuovaRichiesta = richiesta.getId() == null;
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableViewPersone"), !nuovaRichiesta);
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableViewUffici"), !nuovaRichiesta);
        ((PyPaPiTableView) this.findChild(QWidget.class, "tableViewPratiche")).setAddAndRead(!nuovaRichiesta);
        ((PyPaPiTableView) this.findChild(QWidget.class, "tableViewProtocolli")).setAddAndRead(!nuovaRichiesta);

        ((QWidget) this.findChild(QGroupBox.class, "groupBoxRichiesta")).setEnabled(nuovaRichiesta);

        super.getNavigationBar().actionByName("commitChanges").setVisible(!nuovaRichiesta);
        this.richiestaToolbar.actionByName("rispondi").setEnabled(!nuovaRichiesta);
        this.richiestaToolbar.actionByName("rispondiATutti").setEnabled(!nuovaRichiesta);
        this.richiestaToolbar.actionByName("inoltraRichiesta").setEnabled(!nuovaRichiesta);
        this.richiestaToolbar.actionByName("invia").setEnabled(nuovaRichiesta);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();

        super.closeEvent(event);

//        DocerHelper docerHelper = docerPlugin.createDocerHelper(richiesta);
        Application app = Application.getApplicationInstance();
        DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"));
        if ( richiesta.getVarPathDocumento()!=null && richiesta.getVarPathDocumento().length()>0 ) {
            List<Map<String, String>> documents=new ArrayList<Map<String, String>>();
            try {
                documents = docerHelper.searchDocumentsByExternalIdAll(richiesta.getVarPathDocumento());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for ( Map<String, String> doc:documents ) {
                try {
                    docerHelper.deleteDocument(doc.get("DOCNUM"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void destinatarioInserito(Object obj){
        IDestinatarioRichiesta inserito = (IDestinatarioRichiesta) obj;
        inserito.setRichiestacancellabile(((Richiesta) this.getContext().getCurrentEntity()).getCancellabile());
    }

    private void inoltraRichiesta(){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();
        Richiesta inoltra = new Richiesta();
        inoltra.setRichiestaprecedente(richiesta);
        inoltra.setMittente(autenticato);
        inoltra.setCancellabile(richiesta.getCancellabile());
        inoltra.setDatascadenza(richiesta.getDatascadenza());
        inoltra.setTesto(" ----- Messaggio inoltrato ----- \n\n" + richiesta.getTesto());
        inoltra.setGiornipreavviso(richiesta.getGiornipreavviso());
        inoltra.setPathdocumento("richiestatmp_" + (new SimpleDateFormat(TMP_PATH_TEMPLATE)).format(new Date()));

//        DocerHelper docerHelper = docerPlugin.createDocerHelper(richiesta);
        Application app = Application.getApplicationInstance();
        DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"));
        try {
            List<Map<String, String>> documents = docerHelper.searchDocumentsByExternalIdFirstAndRelated("richiesta_"+richiesta.getId());
            for( Map<String, String> doc: documents){
                byte[] bytes = docerHelper.getDocument(doc.get("DOCNUM"), "1");
                TIPO_COMPONENTE_VALUES tipoComponente;
                if( "PRINCIPALE".equals(doc.get("TIPO_COMPONENTE")) ){
                    tipoComponente = TIPO_COMPONENTE_VALUES.PRINCIPALE;
                } else if( "ALLEGATO".equals(doc.get("TIPO_COMPONENTE")) ){
                    tipoComponente = TIPO_COMPONENTE_VALUES.ALLEGATO;
                } else {
                    continue;
                }
                docerHelper.createDocumentTypeDocumentoAndRelateToExternalId(
                        doc.get("DOCNAME"),
                        bytes,
                        tipoComponente,
                        doc.get("ABSTRACT"),
                        inoltra.getVarPathDocumento()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inoltra.setIdConversazione(richiesta.getIdConversazione());

        IForm win = Util.formFromEntity(inoltra);
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) win);
        }
        win.show();
    }

    private void rispondi(){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();
        DestinatarioUtente dest = new DestinatarioUtente();
        dest.setDestinatario(richiesta.getMittente());
        rispondiRichiesta(new ArrayList<DestinatarioUtente>(Collections.singletonList(dest)), null);
    }

    private void rispondiATutti(){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();

        List<DestinatarioUfficio> tmpDestUfficio = new ArrayList();
        for ( DestinatarioUfficio du:richiesta.getDestinatarioUfficioCollection() ) {
            DestinatarioUfficio newdu = new DestinatarioUfficio();
            newdu.setDestinatario(du.getDestinatario());
            newdu.setConoscenza(du.getConoscenza());
            newdu.setRichiestacancellabile(du.getRichiestacancellabile());
            tmpDestUfficio.add(newdu);
        }
        List<DestinatarioUtente> tmpdestUtente = new ArrayList();
        for ( DestinatarioUtente du: richiesta.getDestinatarioUtenteCollection() ) {
            DestinatarioUtente newdu = new DestinatarioUtente();
            if ( !du.getDestinatario().equals(autenticato) ) {
                newdu.setDestinatario(du.getDestinatario());
                newdu.setConoscenza(du.getConoscenza());
                newdu.setRichiestacancellabile(du.getRichiestacancellabile());
                tmpdestUtente.add(newdu);
            }
        }
        DestinatarioUtente dest = new DestinatarioUtente();
        dest.setDestinatario(richiesta.getMittente());
        tmpdestUtente.add(dest);
        rispondiRichiesta(tmpdestUtente, tmpDestUfficio);
    }

    private void rispondiRichiesta(Collection<DestinatarioUtente> destinatariUtente, Collection<DestinatarioUfficio> destinatariUfficio){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();
        Richiesta risposta = new Richiesta();
        risposta.setRichiestaprecedente(richiesta);
        risposta.setMittente(autenticato);
        risposta.setDestinatarioUtenteCollection(destinatariUtente);
        risposta.setDestinatarioUfficioCollection(destinatariUfficio);
        List<RichiestaPratica> tmpRichiestaPratica = new ArrayList();
        for ( RichiestaPratica rp:richiesta.getRichiestaPraticaCollection() ) {
            RichiestaPratica newrp = new RichiestaPratica();
            newrp.setPratica(rp.getPratica());
            tmpRichiestaPratica.add(newrp);
        }
        risposta.setRichiestaPraticaCollection(tmpRichiestaPratica);
        List<RichiestaProtocollo> tmpRichiestaProtocollo = new ArrayList();
        for ( RichiestaProtocollo rp:richiesta.getRichiestaProtocolloCollection() ) {
            RichiestaProtocollo newrp = new RichiestaProtocollo();
            newrp.setProtocollo(rp.getProtocollo());
            tmpRichiestaProtocollo.add(newrp);
        }
        risposta.setRichiestaProtocolloCollection(tmpRichiestaProtocollo);
        risposta.setIdConversazione(richiesta.getIdConversazione());
        IForm win = Util.formFromEntity(risposta);
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) win);
        }
        win.show();
    }

    private void richiestaPrecedente(){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();
        Richiesta precedente = richiesta.getRichiestaprecedente();
        if( precedente != null ){
            IForm win = Util.formFromEntity(precedente);
            QMdiArea workspace = Util.findParentMdiArea(this);
            if( workspace != null ){
                workspace.addSubWindow((QMainWindow) win);
            }
            win.show();
        }

    }

    private void apriDocumenti(){
        Richiesta richiesta = (Richiesta) this.getContext().getCurrentEntity();
        Boolean view = false;
        Boolean delete = false;
        Boolean download = false;
        Boolean parent = false;
        Boolean upload = false;
        Boolean version = false;
        String richiestaExternalId = "";
        if( richiesta == null || richiesta.getId() == null ){
            // apri location temporanea; si possono aggiungere documenti
            if ( richiesta.getVarPathDocumento()== null || richiesta.getVarPathDocumento().isEmpty() ) {
                richiesta.setPathdocumento("richiestatmp_" + (new SimpleDateFormat(TMP_PATH_TEMPLATE)).format(new Date()));
            }
            view = delete = download = upload = true;
            richiestaExternalId = richiesta.getVarPathDocumento();
        } else {
            richiestaExternalId = "richiesta_" + richiesta.getId();
            Boolean isEmpty=false;
            try {
//                DocerHelper docerHelper = docerPlugin.createDocerHelper(richiesta);
                Application app = Application.getApplicationInstance();
                DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                        (String) app.getConfigItem("docer.password"));
                isEmpty=docerHelper.searchDocumentsByExternalIdFirst(richiestaExternalId).isEmpty();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ( isEmpty ) {
                QMessageBox.information(this, "Attenzione", "Nessun documento collegato al messaggio/richiesta.");
                return;
            }
            if ( autenticato.equals(richiesta.getMittente()) ) {
                view = download = true;
            }
            if ( !view ) {
                for (DestinatarioUtente du : richiesta.getDestinatarioUtenteCollection()) {
                    if (autenticato.equals(du.getDestinatario())) {
                        view = download = true;
                        break;
                    }
                }
            }
            if ( !view ) {
                for ( DestinatarioUfficio du: richiesta.getDestinatarioUfficioCollection() ) {
                    for (UfficioUtente uu: du.getDestinatario().getUfficioUtenteCollection()) {
                        if ( autenticato.equals(uu.getUtente()) && !uu.getOspite() && uu.getVisualizza() ) {
                            view = download = true;
                            break;
                        }
                    }
                }
            }
            if ( !view ) {
                if ( !richiesta.getRichiestaProtocolloCollection().isEmpty()) {
                    view = download = true;
                }
            }
        }
        if (view) {
            String url = "#?externalId=" + richiestaExternalId;
            String flags="";
            for( Boolean flag: Arrays.asList(view, delete, download, parent, upload, version) ){
                flags += flag ? "1" : "0";
            }
            url += "&flags=" + flags;
            docerPlugin.showForm(richiesta, url);
        } else {
            QMessageBox.warning(this, "Attenzione", "Non disponi dei permessi per visualizzare i documenti");
            return;
        }
    }

    private void invia() {
        if (this.getContext().commitChanges()) {
            QMessageBox.information(this, " ", "Messaggio inviato");
//            this.hide();
//            this.close();
//            this.destroy(true, true);
            //super.closeEvent(new QCloseEvent()); //TODO
        }
    }


    public Store storeMittente(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Utente> cq = cb.createQuery(Utente.class);
        Root<Utente> root = cq.from(Utente.class);
        cq=cq.select(root);
        cq=cq.where(cb.isFalse(root.get(Utente_.disabilitato)));

        TypedQuery<Utente> query = em.createQuery(cq);
        return new Store(query.getResultList());
    }

}
