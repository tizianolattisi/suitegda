package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.base.entities.*;
import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.richieste.entities.*;
import com.trolltech.qt.gui.*;

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

    private static final String TMP_PATH_TEMPLATE = "yyyy/MM/'tmp'SSSSSS";
    Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
    public RichiestaToolbar richiestaToolbar;

    public FormRichiesta(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        this.richiestaToolbar = new RichiestaToolbar("Richiesta", this);
        this.addToolBar(richiestaToolbar);
//        ((PyPaPiTableView) this.findChild(QWidget.class, "tableViewPersone")).setOpen(false);
//        ((PyPaPiTableView) this.findChild(QWidget.class, "tableViewUffici")).setOpen(false);
        PyPaPiTableView tblDestinatari=((PyPaPiTableView) this.findChild(QWidget.class, "tableViewPersone"));
        tblDestinatari.setOpen(false);
        tblDestinatari.entityInserted.connect(this, "destinatarioInserito(Object)");
        tblDestinatari=((PyPaPiTableView) this.findChild(QWidget.class, "tableViewUffici"));
        tblDestinatari.setOpen(false);
        tblDestinatari.entityInserted.connect(this, "destinatarioInserito(Object)");
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableViewPrecedenti"), Boolean.TRUE);
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableViewSuccessivi"), Boolean.TRUE);

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

        if ( richiesta.getVarPathDocumento()!=null && richiesta.getVarPathDocumento().length()>0 ) {
            CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(Richiesta.class, "CMIS");
            AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(richiesta);
            for (Map map : alfrescoHelper.children()) {
                alfrescoHelper.deleteDocument((String) map.get("objectId"));
            }
            alfrescoHelper.deleteFolder(alfrescoHelper.folderFromPath(alfrescoHelper.getPath()).getId());
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
        inoltra.setTesto("----- Messaggio inoltrato -----\n\n" + richiesta.getTesto());
        inoltra.setGiornipreavviso(richiesta.getGiornipreavviso());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(Richiesta.class, "CMIS");
        AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(richiesta);
        if ( !alfrescoHelper.children().isEmpty() ) {
            inoltra.setPathdocumento((new SimpleDateFormat(TMP_PATH_TEMPLATE)).format(new Date()));
            // copia dei documenti contenuti nella cartella Alfresco
            CmisPlugin cmisPluginInoltra = (CmisPlugin) Register.queryPlugin(Richiesta.class, "CMIS");
            AlfrescoHelper helperInoltra = cmisPluginInoltra.createAlfrescoHelper(inoltra);
            helperInoltra.createFolder();
            for (Map map : alfrescoHelper.children()) {
                alfrescoHelper.copyDocument((String) map.get("objectId"), "/Siti/richieste/documentLibrary/" + inoltra.getPathdocumento() + "/");
            }
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
        List<IPlugin> plugins = (List) Register.queryPlugins(Richiesta.class);
        for(IPlugin plugin: plugins) {
            if ("CMIS".equals(plugin.getName())) {
                Boolean view = false;
                Boolean delete = false;
                Boolean download = false;
                Boolean parent = false;
                Boolean upload = false;
                Boolean version = false;
                if( richiesta == null || richiesta.getId() == null ){
                    // apri location temporanea; si possono aggiungere documenti
                    if ( richiesta.getVarPathDocumento()== null || richiesta.getVarPathDocumento().isEmpty() ) {
                        richiesta.setPathdocumento((new SimpleDateFormat(TMP_PATH_TEMPLATE)).format(new Date()));
                    }
                    view = delete = download = upload = true;
                } else {
                    AlfrescoHelper alfrescoHelper = ((CmisPlugin) plugin).createAlfrescoHelper(richiesta);
                    if ( alfrescoHelper.numberOfDocument()==0 ) {
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
                    ((CmisPlugin) plugin).showForm(richiesta, delete, download, parent, upload, version, null, Boolean.TRUE);
                } else {
                    QMessageBox.warning(this, "Attenzione", "Non disponi dei permessi per visualizzare i documenti");
                    return;
                }
            }
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
