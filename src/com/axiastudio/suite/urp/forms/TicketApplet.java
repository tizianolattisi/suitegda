package com.axiastudio.suite.urp.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.urp.entities.ServizioAlCittadino;
import com.axiastudio.suite.urp.entities.ServizioAlCittadinoSportello;
import com.axiastudio.suite.urp.entities.Sportello;
import com.axiastudio.suite.urp.entities.Ticket;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * User: tiziano
 * Date: 23/01/15
 * Time: 09:33
 */

public class TicketApplet extends QDialog {

    private Sportello sportello;
    private QLabel labelSportello;
    private QLabel labelChiamato;
    private QToolButton buttonConfig;
    private QToolButton buttonInfo;
    private QToolButton buttonChiama;
    private QToolButton buttonAnnulla;
    private QToolButton buttonServito;
    private QToolButton buttonProssimo;

    public TicketApplet(Sportello sportello) {

        super(null, new Qt.WindowFlags(Qt.WindowType.Tool, Qt.WindowType.WindowStaysOnTopHint));

        // setup dello sportello
        this.sportello = sportello;
        sportello.setAttivo(Boolean.TRUE);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        db.createController(Sportello.class).commit(this.sportello);

        // geometry
        setGeometry(60, 60, 200, 26);

        QHBoxLayout hBox = new QHBoxLayout();
        hBox.setMargin(0);

        buttonConfig = new QToolButton();
        buttonConfig.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        buttonConfig.clicked.connect(this, "configurazione()");
        hBox.addWidget(buttonConfig);

        buttonInfo = new QToolButton();
        buttonInfo.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/information.png"));
        buttonInfo.clicked.connect(this, "informazioni()");
        hBox.addWidget(buttonInfo);

        labelSportello = new QLabel();
        labelSportello.setText(sportello.getDescrizione());
        hBox.addWidget(labelSportello);

        labelChiamato = new QLabel();
        labelChiamato.setToolTip("-");
        hBox.addWidget(labelChiamato);

        buttonChiama = new QToolButton();
        buttonChiama.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/bell.png"));
        buttonChiama.clicked.connect(this, "chiama()");
        hBox.addWidget(buttonChiama);

        buttonAnnulla = new QToolButton();
        buttonAnnulla.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
        buttonAnnulla.clicked.connect(this, "annulla()");
        hBox.addWidget(buttonAnnulla);

        buttonServito = new QToolButton();
        buttonServito.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
        buttonServito.clicked.connect(this, "servito()");
        hBox.addWidget(buttonServito);

        buttonProssimo = new QToolButton();
        buttonProssimo.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_last.png"));
        buttonProssimo.clicked.connect(this, "prossimo()");
        hBox.addWidget(buttonProssimo);

        setLayout(hBox);

    }

    private void refreshSportello(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        sportello = (Sportello) db.createController(Sportello.class).get(sportello.getId());
    }

    private void configurazione(){

        refreshSportello();

        // numero di sportelli agganciati ad ogni servizio
        Map<ServizioAlCittadino, Integer> ns = getSportelliPerServizio();

        QDialog dialog = new QDialog(this);
        QVBoxLayout layout = new QVBoxLayout(dialog);
        List<QCheckBox> checkBoxes = new ArrayList<>();

        List<ServizioAlCittadinoSportello> scsc = new ArrayList<>(sportello.getServizioalcittadinosportelloCollection());

        for( ServizioAlCittadinoSportello scs: scsc){
            ServizioAlCittadino servizio = scs.getServizioalcittadino();
            QCheckBox checkBox = new QCheckBox();
            checkBox.setText(servizio.getDescrizione());
            if( scs.getAttivo() ) {
                checkBox.setCheckState(Qt.CheckState.Checked);
                checkBox.setEnabled(ns.get(servizio) > 1);
            } else {
                checkBox.setCheckState(Qt.CheckState.Unchecked);
            }
            layout.addWidget(checkBox);
            checkBoxes.add(checkBox);
        }

        QPushButton button = new QPushButton();
        button.setText("Conferma");
        button.clicked.connect(dialog, "accept()");
        layout.addWidget(button);
        int exec = dialog.exec();
        if( exec == 1 ){

            for( int i=0; i<scsc.size(); i++ ){
                scsc.get(i).setAttivo(Qt.CheckState.Checked.equals(checkBoxes.get(i).checkState()));
            }
            Database db = (Database) Register.queryUtility(IDatabase.class);
            db.createController(Sportello.class).commit(sportello);

        }
    }

    private void passaServizio(ServizioAlCittadino servizio){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Store scscTutti = db.createController(ServizioAlCittadinoSportello.class).createFullStore();
        for( Object obj: scscTutti ){
            ServizioAlCittadinoSportello scs = (ServizioAlCittadinoSportello) obj;
            if( !scs.getSportello().equals(sportello) && scs.getSportello().getAttivo() ){
                if( scs.getServizioalcittadino().equals(servizio) && !scs.getAttivo() ){
                    scs.setAttivo(Boolean.TRUE);
                    db.createController(ServizioAlCittadinoSportello.class).commit(scs);
                }
            }
        }
    }

    private Map<ServizioAlCittadino, Integer> getSportelliPerServizio() {
        Store scscTutti = ((Database) Register.queryUtility(IDatabase.class)).createController(ServizioAlCittadinoSportello.class).createFullStore();
        Map<ServizioAlCittadino, Integer> ns = new HashMap<>();
        for( Object obj: scscTutti ){

            ServizioAlCittadinoSportello scs = (ServizioAlCittadinoSportello) obj;
            if (!ns.keySet().contains(scs.getServizioalcittadino())) {
                ns.put(scs.getServizioalcittadino(), 0);
            }
            if( scs.getAttivo() ){
                ns.put(scs.getServizioalcittadino(), ns.get(scs.getServizioalcittadino()) + 1);
            }
        }
        return ns;
    }

    private void informazioni(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> from = cq.from(Ticket.class);
        cq.select(from);
        cq = cq.where(cb.and(
                cb.equal(from.get("chiamato"), false),
                cb.equal(from.get("servito"), false),
                cb.equal(from.get("annullato"), false),
                cb.greaterThanOrEqualTo(from.<Date>get("tsemesso"), zeroMilliseconds(new Date()))
        ));
        cq.orderBy(cb.asc(from.get("tsemesso")));
        TypedQuery<Ticket> query = em.createQuery(cq);
        List<Ticket> resultList = query.getResultList();
        String msg = "Ticket prenotati non ancora chiamati:\n\n";
        for( Ticket ticket: resultList ){
            msg += ticket.getServizioalcittadino().getCodiceservizio() + " " + ticket.getNumero() +
                    " - " + ticket.getServizioalcittadino().getDescrizione() + "\n";
        }
        QMessageBox.information(this, "Ticket in coda", msg, QMessageBox.StandardButton.Ok);

    }

    private void prossimo(){
        prossimo(Boolean.TRUE);
    }

    private void prossimo(Boolean closeTicket){
        Ticket ticket;
        if( closeTicket ){
            ticket = chiamaSuccessivo();
        } else {
            ticket = ticketAttuale();
        }
        if( ticket != null ){
            String text = ticket.getServizioalcittadino().getCodiceservizio() +
                    " " + ticket.getNumero();
            String tooltip = "Ticket nr. " + ticket.getNumero() + " (" + ticket.getServizioalcittadino().getDescrizione() + ")";
            labelChiamato.setText(text);
            labelChiamato.setToolTip(tooltip);
        } else {
            labelChiamato.setText("-");
            labelChiamato.setToolTip("-");
        }
        updateButton("chiamato");
    }

    private void servito(){
        chiudiTicketAttuale();
        labelChiamato.setText("-");
        labelChiamato.setToolTip("-");
        updateButton("servito");
    }

    private void annulla(){
        annullaTicketAttuale();
        labelChiamato.setText("-");
        labelChiamato.setToolTip("-");
        updateButton("annullato");
    }

    private void chiama(){
        // XXX: sollecito
    }

    private void updateButton(String name){
        if( "servito".equals(name) || "annullato".equals(name) ){
            buttonServito.setEnabled(false);
            buttonAnnulla.setEnabled(false);
            buttonChiama.setEnabled(false);
        } else if( "chiamato".equals(name) ){
            buttonServito.setEnabled(true);
            buttonAnnulla.setEnabled(true);
            buttonChiama.setEnabled(true);
        }
    }

    /*
     *  Restituisce il ticket attuale: odierno, chiamato ma non servito o annullato
     */
    private Ticket ticketAttuale(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> from = cq.from(Ticket.class);
        cq.select(from);
        cq = cq.where(cb.and(
                cb.equal(from.get("sportello"), sportello),
                cb.equal(from.get("chiamato"), true),
                cb.equal(from.get("servito"), false),
                cb.equal(from.get("annullato"), false),
                cb.greaterThanOrEqualTo(from.<Date>get("tsemesso"), zeroMilliseconds(new Date()))
        ));
        cq.orderBy(cb.asc(from.get("tschiamato")));
        TypedQuery<Ticket> query = em.createQuery(cq);
        List<Ticket> resultList = query.getResultList();
        if( resultList.size()>0 ){
            return resultList.get(0);
        }
        return null;
    }

    /*
     *  Chiude il ticket attuale
     */
    private void chiudiTicketAttuale(){
        Ticket ticket = ticketAttuale();
        if( ticket != null ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            ticket.setServito(Boolean.TRUE);
            ticket.setTschiuso(new Date());
            em.getTransaction().begin();
            em.merge(ticket);
            em.getTransaction().commit();
        }
    }

    /*
     *  Annulla il ticket attuale
     */
    private void annullaTicketAttuale(){
        Ticket ticket = ticketAttuale();
        if( ticket != null ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            ticket.setAnnullato(Boolean.TRUE);
            ticket.setTschiuso(new Date());
            em.getTransaction().begin();
            em.merge(ticket);
            em.getTransaction().commit();
        }
    }

    /*
     *  Chiama il ticket successivo:
     */
    private Ticket chiamaSuccessivo(){
        refreshSportello();
        chiudiTicketAttuale();
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> from = cq.from(Ticket.class);
        cq.select(from);
        cq = cq.where(cb.and(
                //cb.equal(from.get("sportello"), null),
                cb.equal(from.get("chiamato"), false),
                cb.equal(from.get("servito"), false),
                cb.equal(from.get("annullato"), false),
                cb.greaterThanOrEqualTo(from.<Date>get("tsemesso"), zeroMilliseconds(new Date()))
        ));
        cq.orderBy(cb.asc(from.get("numero")));
        TypedQuery<Ticket> query = em.createQuery(cq);
        List<Ticket> resultList = query.getResultList();
        for( Ticket ticket: resultList ){
            for( ServizioAlCittadinoSportello scs: sportello.getServizioalcittadinosportelloCollection() ){
                if( scs.getAttivo() ) {
                    ServizioAlCittadino servizio = scs.getServizioalcittadino();
                    if (ticket.getServizioalcittadino().equals(servizio)) {
                        ticket.setChiamato(Boolean.TRUE);
                        ticket.setSportello(sportello);
                        ticket.setTschiamato(new Date());
                        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
                        ticket.setUtente(autenticato);
                        em.getTransaction().begin();
                        em.merge(ticket);
                        em.getTransaction().commit();
                        return ticket;
                    }
                }
            }
        }
        return null;
    }

    private Date zeroMilliseconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        String msg = "Desideri chiudere lo sportello? I servizi non più gestiti verranno trasferiti ai rimanenti sportelli aperti.";
        Boolean res = Util.questionBox(this, "Chiusura sportello", msg);
        if( !res ){
            event.ignore();
            return;
        }
        Map<ServizioAlCittadino, Integer> ns = getSportelliPerServizio();
        for( ServizioAlCittadinoSportello scs: sportello.getServizioalcittadinosportelloCollection() ){
            if( scs.getAttivo() ){
                if( ns.get(scs.getServizioalcittadino())<2 ){
                    passaServizio(scs.getServizioalcittadino());
                }
                scs.setAttivo(Boolean.FALSE);
            }
        }
        sportello.setAttivo(Boolean.FALSE);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        db.createController(Sportello.class).commit(sportello);
        super.closeEvent(event);
    }
}
