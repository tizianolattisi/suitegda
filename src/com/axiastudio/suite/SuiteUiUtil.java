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
package com.axiastudio.suite;

import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.generale.ITimeStamped;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class SuiteUiUtil {
    
    public static void showInfo(Window window){
        QDialog info = new QDialog(window);
        QVBoxLayout layout = new QVBoxLayout(info);
        QPixmap pix = new QPixmap("classpath:com/axiastudio/pypapi/ui/resources/pypapi64.png");
        QLabel pypapi = new QLabel();
        pypapi.setPixmap(pix);
        pypapi.setAlignment(Qt.AlignmentFlag.AlignHCenter);
        layout.addWidget(pypapi);
        String credits = "";
        Object currentEntity = window.getContext().getCurrentEntity();
        if( currentEntity instanceof ITimeStamped ){
            Date recordcreato = (Date) Resolver.valueFromFieldName(currentEntity, "recordcreato");
            if( recordcreato != null ){
                credits += "<br/>Creato: " + SuiteUtil.DATETIME_FORMAT.format(recordcreato);
            }
            Date recordmodificato = (Date) Resolver.valueFromFieldName(currentEntity, "recordmodificato");
            if( recordmodificato != null ){
                credits += "<br/>Modificato: " + SuiteUtil.DATETIME_FORMAT.format(recordmodificato);
            }
        }   
        QTextEdit text = new QTextEdit(credits);
        layout.addWidget(text);
        info.show();
    }
}
