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
        this.insertButton("richiestaPrecedente", "Apri la richiesta precedente",
                "classpath:com/axiastudio/suite/resources/email_parent.png",
                "Apri la richiesta precedente", parent);
        this.insertButton("rispondiRichiesta", "Rispondi alla richiesta",
                "classpath:com/axiastudio/suite/resources/email_reply.png",
                "Rispondi alla richiesta", parent);
        this.insertButton("inoltraRichiesta", "Inoltra la richiesta",
                "classpath:com/axiastudio/suite/resources/email_forward.png",
                "Inoltra la richiesta", parent);
    }

}