/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.axiastudio.suite.menjazo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class AlfrescoHelper {
    
    private String user;
    private String password;
    private String url;
    private String path = "/";

    public AlfrescoHelper(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
    public Session createSession(){
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put(SessionParameter.USER, this.user);
        parameter.put(SessionParameter.PASSWORD, this.password);
        parameter.put(SessionParameter.ATOMPUB_URL, this.url);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true" );
        parameter.put(SessionParameter.COOKIES, "true" );        
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl"); // Alfresco
        SessionFactoryImpl factory = SessionFactoryImpl.newInstance();
        Repository repository = factory.getRepositories(parameter).get(0);
        return repository.createSession();
    }
    
    public List<HashMap> children() {
        return children("");
    }
    
    public List<HashMap> children(String subpath) {
        List<HashMap> children = new ArrayList();
        Folder folder;
        Session session = this.createSession();
        try{
            folder = (Folder) session.getObjectByPath(this.path+subpath);
        } catch (CmisObjectNotFoundException ex) {
            folder = this.createFolder(this.path+subpath);
        }
        ItemIterable<CmisObject> cmisChildren = folder.getChildren();
        for (CmisObject o : cmisChildren) {
            HashMap map = mapFromCmisObject(o);
            children.add(map);
        }
        return children;
    }
    
    public List<HashMap> getAllVersions(String objectId){
        List<HashMap> versions = new ArrayList();
        Document document = getDocument(objectId);
        for(Document version: document.getAllVersions()){
            HashMap map = mapFromCmisObject(version);
            versions.add(map);
        }
        
        return versions;
    }
    
    private HashMap mapFromCmisObject(CmisObject o) {
        HashMap map = new HashMap();
        // cmis properties
        for( Property<?> property: o.getProperties() ){
            String cmisName = property.getLocalName();
            map.put(cmisName, property.getValueAsString());
        }
        // Alfresco properties
        if( "cmis:document".equals(o.getPropertyValue(PropertyIds.OBJECT_TYPE_ID) ) ){
            AlfrescoDocument doc = (AlfrescoDocument) o;
            if( doc.hasAspect("P:cm:titled") ) {    
                for( Property<?> property: doc.getProperties() ){
                    String cmisName = property.getLocalName();
                    map.put(cmisName, property.getValueAsString());
                }
            }
        }
        return map;
    }

    public Folder createFolder(){
        return createFolder(this.path);
    }

    public Folder createFolder(String path){
        Session session = this.createSession();
        String[] split = path.split("/");
        String folderName = split[split.length-1];
        String parentPath = "";
        for( int i=0; i<split.length-1; i++ ){
                parentPath += split[i]+"/";
        }
        Folder parentFolder;
        try{
            parentFolder = (Folder) session.getObjectByPath(parentPath);
        } catch (CmisObjectNotFoundException ex) {
            parentFolder = this.createFolder(parentPath);
        }
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        properties.put(PropertyIds.NAME, folderName);
        Folder folder = parentFolder.createFolder(properties);
        return folder;
    }
    
    public Document createDocument(String subpath, String name, byte[] content){
        return createDocument(subpath, name, content, "application/pdf", "", "");
    }

    public Document createDocument(String subpath, String name, byte[] content, String filter, String title) {
        return createDocument(subpath, name, content, filter, title, "");
    }

    public Document createDocument(String subpath, String name, byte[] content, String filter) {
        return createDocument(subpath, name, content, filter, "", "");
    }

    public Document createDocument(String subpath, String name, byte[] content, String filter, String title, String description) {
        Folder folder;
        Session session = this.createSession();
        folder = (Folder) session.getObjectByPath(this.path+subpath);
        ByteArrayInputStream stream = new ByteArrayInputStream(content);
        // XXX
        ContentStream contentStream = new ContentStreamImpl(name, BigInteger.valueOf(content.length), filter, stream);
        
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        properties.put(PropertyIds.NAME, name);
        Document document=null;
        try{
            document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        } catch (CmisConstraintException ex){
            // conflict
            document = (Document) session.getObjectByPath(this.path+subpath+name);
            createVersion(document.getId(), content);
        }
        if( document != null && "cmis:document".equals(document.getPropertyValue(PropertyIds.OBJECT_TYPE_ID) ) ){
            AlfrescoDocument doc = (AlfrescoDocument) document;
            if( !doc.hasAspect("P:cm:titled") ) {
                doc.addAspect("P:cm:titled");
            }
            Map<String, Object> aspectProperties = new HashMap();
            aspectProperties.put("cm:title", title);
            aspectProperties.put("cm:description", description);
            doc.updateProperties(aspectProperties);
        }
        return document;
    }

    public Document createVersion(String objectId, byte[] content){
        return createVersion(objectId, content, "", null);
    }

    public Document createVersion(String objectId, byte[] content, String versionDescription){
            return createVersion(objectId, content, versionDescription, null);
    }

    public Document createVersion(String objectId, byte[] content, String versionDescription, String fileName){
        Session session = this.createSession();
        CmisObject object = session.getObject(objectId);
        Document document = (Document) object;
        if( document.getAllowableActions().getAllowableActions().contains(org.apache.chemistry.opencmis.commons.enums.Action.CAN_CHECK_OUT) ){
            document.refresh();
            ObjectId checkOutId = document.checkOut();
            Document workingDocument = (Document) session.getObject(checkOutId);
            InputStream stream = new ByteArrayInputStream(content);
            ContentStream contentStream = session.getObjectFactory().createContentStream(document.getContentStreamFileName(), Long.valueOf(content.length), document.getContentStreamMimeType(), stream);
            ObjectId newObjectId = workingDocument.checkIn(false, null, contentStream, versionDescription);
            document = (Document) session.getObject(newObjectId);
            if( fileName != null && !fileName.equals(document.getContentStreamFileName()) ){
                AlfrescoDocument doc = (AlfrescoDocument) document;
                Map<String, Object> properties = new HashMap();
                properties.put("cmis:name", fileName);
                doc.updateProperties(properties);
            }
            return document;
        } else {
            return null;
        }
    }
    
    public InputStream getDocumentStream(String objectId){
        Document document = getDocument(objectId);
        InputStream stream = document.getContentStream().getStream();
        return stream;
    }

    public Document getDocument(String objectId) {
        Session session = this.createSession();
        CmisObject object = session.getObject(objectId);
        return (Document) object;
    }

    public void deleteDocument(String objectId){
        deleteObject(objectId);
    }

    public void deleteFolder(String objectId){
        deleteObject(objectId);
    }
    
    public void deleteObject(String objectId){
        Session session = this.createSession();
        CmisObject object = session.getObject(objectId);
        object.delete(true);
    }

    public Document copyDocument(String objectId, String path){
        Session session = this.createSession();
        Document toCopy = (Document) session.getObject(objectId);
        Folder folder = (Folder) session.getObjectByPath(path);
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        properties.put(PropertyIds.NAME, toCopy.getName());
        Document document; // destination
        try{
            document = folder.createDocument(properties, toCopy.getContentStream(), VersioningState.MAJOR);
        } catch (CmisConstraintException ex){
            // conflict
            document = (Document) session.getObjectByPath(path + "/" + toCopy.getName());
            InputStream is = getDocumentStream(objectId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                int reads = is.read();
                while(reads != -1){
                    baos.write(reads);
                    reads = is.read();
                }
                baos.toByteArray();
                createVersion(document.getId(), baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;
    }
    
    public HashMap getDocumentProperties(String objectId){
        HashMap<String, Object> map = new HashMap();
        Session session = this.createSession();
        CmisObject object = session.getObject(objectId);
        if( "cmis:document".equals(object.getPropertyValue(PropertyIds.OBJECT_TYPE_ID) ) ){
            AlfrescoDocument doc = (AlfrescoDocument) object;
            if( doc.hasAspect("P:cm:titled") ) {
                for( Property<?> property: doc.getProperties() ){
                    String cmisName = property.getLocalName();
                    map.put(cmisName, property.getValueAsString());
                }
            }
        }
        return map;
    }
    
    public void setDocumentProperties(String objectId, Map<String, Object> map){
        Session session = this.createSession();
        CmisObject object = session.getObject(objectId);
        if( "cmis:document".equals(object.getPropertyValue(PropertyIds.OBJECT_TYPE_ID) ) ){
            AlfrescoDocument doc = (AlfrescoDocument) object;
            if( !doc.hasAspect("P:cm:titled") ) {
                doc.addAspect("P:cm:titled");
            }
            Map<String, Object> properties = new HashMap();
            properties.put("cm:title", map.get("cm:title"));
            properties.put("cm:description", map.get("cm:description"));
            doc.updateProperties(properties);
        }
    }
    
}
