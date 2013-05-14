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

import com.axiastudio.pypapi.ui.Delegate;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTableView;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class DelegateScrivania extends Delegate {
    
        public DelegateScrivania(QTableView parent)
        {
            super(parent);
        }

        @Override
        public void paint(QPainter painter, QStyleOptionViewItem option, QModelIndex index)
        {
            QImage inEvidence = new QImage("classpath:com/axiastudio/suite/resources/star.png");
            QImage notInEvidence = new QImage("classpath:com/axiastudio/suite/resources/bullet_black.png");
            QRect rect = option.rect();
            QPoint center = rect.center();
            QSize size = inEvidence.size();
            QPoint delta = new QPoint(size.width()/2, size.height()/2);
            QPoint point = center.subtract(delta);
            QRect imageRect = new QRect(point, inEvidence.size());
            if(  0==index.column() ){
                Object data = index.data();
                if( data != null && "E".equals((String) data) ){
                    painter.drawImage(imageRect, inEvidence);
                } else {
                    painter.drawImage(imageRect, notInEvidence);                    
                }
            } else if( 5==index.column() ) {
                Object checkState = index.data(ItemDataRole.CheckStateRole);
                if( checkState.equals(CheckState.Checked) ){
                    painter.drawImage(imageRect, inEvidence);
                } else {
                    painter.drawImage(imageRect, notInEvidence);                    
                }
            } else {
                super.paint(painter, option, index);
            }
        }
}
