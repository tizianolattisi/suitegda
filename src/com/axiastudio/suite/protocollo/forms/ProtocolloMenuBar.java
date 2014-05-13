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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.trolltech.qt.gui.QKeySequence;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class ProtocolloMenuBar extends PyPaPiToolBar {
    
    public ProtocolloMenuBar(String title, Window parent){
        super(title, parent);
        this.insertButton("convalidaAttribuzioni", "Convalida attribuzioni",
                "classpath:com/axiastudio/suite/resources/lock_group.png",
                "Convalida degli uffici attribuiti al protocollo", parent);
        this.insertButton("convalidaProtocollo", "Convalida protocollo",
                "classpath:com/axiastudio/suite/resources/lock_mail.png",
                "Convalida della registrazione del protocollo", parent);
        this.insertButton("consolidaDocumenti", "Consolida documenti",
                "classpath:com/axiastudio/suite/resources/lock_folder.png",
                "Consolida i documenti allegati al protocollo", parent);
        this.insertButton("apriDocumenti", "Apri documenti",
                "classpath:com/axiastudio/menjazo/resources/menjazo.png",
                "Apre lo spazio documenti", parent);
        this.insertButton("pubblicaProtocollo", "Crea una pubblicazione dal protocollo",
                "classpath:com/axiastudio/suite/resources/newspaper.png",
                "Crea una nuova pubblicazione all'albo telematico a partire dal protocollo selezionato", parent);
        this.insertButton("cercaDaEtichetta", "Ricerca da etichetta",
                          "classpath:com/axiastudio/suite/resources/datamatrix_find.png",
                          "Ricerca da etichetta", parent, new QKeySequence(tr("F9")));
        this.insertButton("stampaEtichetta", "Stampa etichetta",
                          "classpath:com/axiastudio/suite/resources/datamatrix.png",
                          "Stampa etichetta", parent);
        this.insertButton("scanNDo", "Apri l'utilità scan'n'do",
                "classpath:com/axiastudio/suite/resources/email_go.png",
                "Apri l'utilità scan'n'do", parent);
        // XXX: in attesa di pubblicare gli altri componenti
        /*
        this.insertButton("segnaturaXml", "Crea Segnatura.xml",
                "classpath:com/axiastudio/suite/resources/page_white_code.png",
                "Crea e allega il file di segnatura xml.", parent);
                */
    }
    
}