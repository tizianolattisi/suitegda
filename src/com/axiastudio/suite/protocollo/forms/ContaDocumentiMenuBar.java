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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class ContaDocumentiMenuBar extends QToolBar {

    protected Map<String, QAction> actions = new HashMap<String, QAction>();

    public ContaDocumentiMenuBar(String title, QMainWindow parent){
        super(title, parent);

        this.insertButton("apriProtocollo", "Apri",
                "classpath:com/axiastudio/suite/resources/email.png",
                "Apri la finestra di dettaglio e gestione", parent);
        this.insertButton("apriDocumenti", "Apri documenti",
                "classpath:com/axiastudio/suite/plugins/docer/docer.png",
                "Apre lo spazio documenti", parent);
        this.insertButton("search", tr("SEARCH"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/find.png",
                tr("SEARCH_DESCRIPTION"), parent,
                new QKeySequence(QKeySequence.StandardKey.Find));

        actionByName("apriProtocollo").setEnabled(false);
        actionByName("apriDocumenti").setEnabled(false);
    }

    protected QAction insertButton(String actionName, String text, String iconName,
                                   String toolTip, QObject agent){
        return this.insertButton(actionName, text, iconName, toolTip, agent, null, null);
    }

    private QAction insertButton(String actionName, String text, String iconName,
                                 String toolTip, QObject agent, QKeySequence qks){
        return this.insertButton(actionName, text, iconName, toolTip, agent, qks, null);
    }

    private QAction insertButton(String actionName, String text, String iconName,
                                 String toolTip, QObject agent, QKeySequence qks, QKeySequence.StandardKey sk){
        QAction action = new QAction(agent);
        QIcon icon = new QIcon(iconName);
        action.setObjectName(actionName);
        action.setText(text);
        action.setToolTip(toolTip);
        action.setIcon(icon);
        action.triggered.connect(agent, actionName+"()");
        action.triggered.connect(this, "refresh()");
        if( qks != null ){
            action.setShortcut(qks);
        } else if( sk != null ){
            action.setShortcut(sk);
        }
        this.addAction(action);
        this.actions.put(actionName, action);
        return action;
    }

    public QAction actionByName(String name){
        return this.actions.get(name);
    }

    public void refresh() {}

}