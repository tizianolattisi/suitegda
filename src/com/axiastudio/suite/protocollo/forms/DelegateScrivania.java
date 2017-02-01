/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.ui.*;
import com.axiastudio.suite.protocollo.entities.StatoPec;
import com.trolltech.qt.core.*;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class DelegateScrivania extends Delegate {
    
        public DelegateScrivania(QTableView parent)
        {
            super(parent);
        }

        private QFont checkFont (QFont defaultFont, QModelIndex index) {
            if( CheckState.Unchecked.equals(index.child(index.row(),0).data(ItemDataRole.CheckStateRole)) ) {
                defaultFont.setBold(true);
            }
            return defaultFont;
        }

        @Override
        public void paint(QPainter painter, QStyleOptionViewItem option, QModelIndex index)
        {
            QImage inEvidence = new QImage("classpath:com/axiastudio/suite/resources/star.png");
            QImage notInEvidence = new QImage("classpath:com/axiastudio/suite/resources/bullet_black.png");
            QImage iconNull = new QImage("classpath:com/axiastudio/suite/resources/null.png");
            QImage iconNonGestito = new QImage("classpath:com/axiastudio/suite/resources/question.png");
            QImage iconConsegnato = new QImage("classpath:com/axiastudio/suite/resources/stargreen.png");
            QImage iconAnomalia = new QImage("classpath:com/axiastudio/suite/resources/starred.png");
            QImage iconDaInviare = new QImage("classpath:com/axiastudio/suite/resources/starwhite.png");
            QImage iconInviato = new QImage("classpath:com/axiastudio/suite/resources/starorange.png");
            QImage icon = iconNull;
            QRect rect = option.rect();
            QPoint center = rect.center();
            QSize size = inEvidence.size();
            QPoint delta = new QPoint(size.width()/2, size.height()/2);
            QPoint point = center.subtract(delta);
            QRect imageRect = new QRect(point, inEvidence.size());
            if (CellEditorType.BOOLEAN.equals(((TableModel) index.model()).getColumns().get(index.column()).getEditorType())) {
                Object checkState = index.data(ItemDataRole.CheckStateRole);
                if (index.column() == 5 && CheckState.Checked.equals(checkState) ) {
                    Object value = index.child(index.row(), 10).data();
                    if (value.equals(StatoPec.CONSEGNATO)) {
                        icon = iconConsegnato;
                    } else if (value.equals(StatoPec.ANOMALIA) || value.equals(StatoPec.ERRORE)) {
                        icon = iconAnomalia;
                    } else if (value.equals(StatoPec.DAINVIARE)) {
                        icon = iconDaInviare;
                    } else if (value.equals(StatoPec.INVIATO)) {
                        icon = iconInviato;
                    } else if (value.equals(StatoPec.NONGESTITO)) {
                        icon = iconNonGestito;
                    }
                } else {
                    if (checkState.equals(CheckState.Checked)) {
                        icon=inEvidence;
                    } else {
                        icon=notInEvidence;
                    }
                }
                painter.drawImage(imageRect, icon);
            } else {
                option.setFont(checkFont(option.font(), index));
                super.paint(painter, option, index);
            }
        }
}
