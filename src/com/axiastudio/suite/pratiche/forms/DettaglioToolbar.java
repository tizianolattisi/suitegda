package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;

/**
 * User: tiziano
 * Date: 12/12/13
 * Time: 11:54
 */
public class DettaglioToolbar extends PyPaPiToolBar {

    public DettaglioToolbar(String title, Window parent){
        super(title, parent);
        this.insertButton("apriPratica", "Apri la pratica collegata",
                "classpath:com/axiastudio/suite/resources/arrow_turn_left.png",
                "Apri la pratica collegata", parent);
    }
}
