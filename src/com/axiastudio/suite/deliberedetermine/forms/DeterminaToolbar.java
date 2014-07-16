package com.axiastudio.suite.deliberedetermine.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;

/**
 * User: tiziano
 * Date: 19/12/13
 * Time: 16:47
 */
public class DeterminaToolbar extends PyPaPiToolBar {

    public DeterminaToolbar(String title, Window parent){
        super(title, parent);
        this.insertButton("apriDocumenti", "Apri documenti",
                "classpath:com/axiastudio/suite/menjazo/resources/menjazo.png",
                "Apre lo spazio documenti", parent);
        this.insertButton("vistoLiquidazione", "Visto avvenuta liquidazione",
                "classpath:com/axiastudio/suite/resources/money.png",
                "Inserisce il visto di avvenuta liquidazione della determina", parent);
    }
}

