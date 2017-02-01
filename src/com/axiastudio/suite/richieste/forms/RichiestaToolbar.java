package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 27/11/13
 * Time: 10.45
 * To change this template use File | Settings | File Templates.
 */
public class RichiestaToolbar extends PyPaPiToolBar {

    public RichiestaToolbar(String title, Window parent){
        super(title, parent);
        this.insertButton("rispondi", "Rispondi alla richiesta",
                "classpath:com/axiastudio/suite/resources/email_reply.png",
                "Rispondi alla richiesta", parent);
        this.insertButton("rispondiATutti", "Rispondi a tutti",
                "classpath:com/axiastudio/suite/resources/email_replyall.png",
                "Rispondi a tutti", parent);
        this.insertButton("inoltraRichiesta", "Inoltra la richiesta",
                "classpath:com/axiastudio/suite/resources/email_forward.png",
                "Inoltra la richiesta", parent);
        this.insertButton("apriDocumenti", "Apri documenti",
                "classpath:com/axiastudio/suite/menjazo/resources/menjazo.png",
                "Apre lo spazio documenti", parent);
        this.insertSeparator(this.actionByName("invia"));
        this.insertButton("invia", "Invia messaggio",
                "classpath:com/axiastudio/suite/resources/email_go1.png",
                "Invia il messaggio", parent);
    }

}