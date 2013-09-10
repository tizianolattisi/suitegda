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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.email;

import com.axiastudio.suite.protocollo.entities.Mailbox;
import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import java.util.Properties;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class EmailHelper {

    private Mailbox mailbox;
    IMAPFolder folder = null;
    Store store = null;

    public EmailHelper(Mailbox mailbox) {
        this.mailbox = mailbox;
    }

    public Boolean open() {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        try {
            store = session.getStore("imaps");
            store.connect(this.mailbox.getHost(),this.mailbox.getUsername(), this.mailbox.getPassword());
            folder = (IMAPFolder) store.getFolder(this.mailbox.getInbox());
            if(!folder.isOpen()){
                folder.open(Folder.READ_WRITE);
            }
            //Message[] messages = folder.getMessages();
            //System.out.println("No of Messages : " + folder.getMessageCount());
            return true;
        } catch (MessagingException e) {
            this.close();
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if( folder != null && folder.isOpen() ){
                folder.close(true);
            }
            if( store != null ){
                store.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Integer getUnreadMessageCount() {
        return getMessageCount(true);
    }

    public Integer getMessageCount() {
        return getMessageCount(false);
    }

    public Integer getMessageCount(Boolean unread) {
        if( folder != null && folder.isOpen() ){
            try {
                if( unread ){
                    return folder.getUnreadMessageCount();
                } else {
                    return folder.getMessageCount();
                }
            } catch (MessagingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return -1;
    }

}
