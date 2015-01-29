package com.axiastudio.suite.urp.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.urp.entities.ServizioAlCittadino;
import com.axiastudio.suite.urp.entities.Sportello;
import com.axiastudio.suite.urp.entities.Ticket;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: tiziano
 * Date: 23/01/15
 * Time: 09:33
 */

public class TicketApplet extends QDialog {

    private final Sportello sportello;
    private QLabel labelSportello;
    private QLabel labelChiamato;
    private QToolButton buttonInfo;
    private QToolButton buttonChiama;
    private QToolButton buttonAnnulla;
    private QToolButton buttonServito;
    private QToolButton buttonProssimo;

    public TicketApplet(Sportello sportello) {

        super(null, new Qt.WindowFlags(Qt.WindowType.Tool, Qt.WindowType.WindowStaysOnTopHint));

        // setup dello sportello
        this.sportello = sportello;

        // geometry
        setGeometry(60, 60, 200, 26);

        QHBoxLayout hBox = new QHBoxLayout();
        hBox.setMargin(0);

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
            for( ServizioAlCittadino sevizio: sportello.getServizialcittadino() ){
                if( ticket.getServizioalcittadino().equals(sevizio) ){
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

}
