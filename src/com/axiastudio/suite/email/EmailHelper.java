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
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
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

    public Folder getFolder() {
        if( folder != null && folder.isOpen() ){
            return folder;
        }
        return null;
    }

    public EMail getEmail(Integer number){
        EMail email=null;
        Message msg = getMessage(number);
        if( msg != null ){
            email = new EMail();
        }
        try {
            Object content = msg.getContent();
            if( content instanceof Multipart ){
                Multipart mp = (Multipart) content;
                for( int i=0; i<mp.getCount(); i++ ) {
                    BodyPart part = mp.getBodyPart(i);
                    if( part.getContent() instanceof MimeMultipart){
                        // attachments
                        MimeMultipart mmp = (MimeMultipart) part.getContent();
                        for( int j=0; j<mmp.getCount(); j++ ){
                            BodyPart bodyPart = mmp.getBodyPart(j);
                            if( bodyPart.getFileName() != null ){
                                InputStream stream = (InputStream) bodyPart.getContent();
                                email.putStream(bodyPart.getFileName(), stream);
                            }
                        }

                    } else {
                        // body
                        email.setBody(part.getContent().toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return email;
    }

    public Message getMessage(Integer number){
        if( folder != null && folder.isOpen() ){
            try {
                return folder.getMessage(number);
            } catch (MessagingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }

}
