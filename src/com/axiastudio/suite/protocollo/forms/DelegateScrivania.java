/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.ui.Delegate;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QWidget;

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
            Object data = index.data();
            QImage inEvidence = new QImage("classpath:com/axiastudio/suite/resources/star.png");
            QImage notInEvidence = new QImage("classpath:com/axiastudio/suite/resources/bullet_black.png");
            if(  0==index.column() ){
                QRect rect = option.rect();
                QPoint center = rect.center();
                QSize size = inEvidence.size();
                QPoint delta = new QPoint(size.width()/2, size.height()/2);
                QPoint point = center.subtract(delta);
                QRect imageRect = new QRect(point, inEvidence.size());
                if( data != null && "E".equals((String) data) ){
                    painter.drawImage(imageRect, inEvidence);
                } else {
                    painter.drawImage(imageRect, notInEvidence);                    
                }
            } else
                super.paint(painter, option, index);
        }
}
