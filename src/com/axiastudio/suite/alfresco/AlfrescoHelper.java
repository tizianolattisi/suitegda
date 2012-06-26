/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.alfresco;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class AlfrescoHelper {
    
    //private static final String ATOMPUB_URL = "http://192.168.64.54:8080/alfresco/service/cmis";
    private String user;
    private String password;
    private final String url;

    public AlfrescoHelper(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
    }
    
    
    public Session createSession(){
        SessionFactoryImpl factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put(SessionParameter.USER, this.user);
        parameter.put(SessionParameter.PASSWORD, this.password);
        parameter.put(SessionParameter.ATOMPUB_URL, this.url);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true" );
        parameter.put(SessionParameter.COOKIES, "true" );
        Repository repository = factory.getRepositories(parameter).get(0);
        return repository.createSession();
    }
    
    /*
     * Oggetti contenuti in una cartella
     */
    public List<AlfrescoObject> childrenNames(String path) {
        List<AlfrescoObject> children = new ArrayList<AlfrescoObject>();
        Folder folder;
        Session session = this.createSession();
        try{
            folder = (Folder) session.getObjectByPath(path);
        } catch (CmisObjectNotFoundException ex) {
            folder = this.createFolder(path);
        }
        ItemIterable<CmisObject> cmisChildren = folder.getChildren();

        for (CmisObject o : cmisChildren) {
            List<Property<?>> properties = o.getProperties();
            String objectId = o.getProperty("cmis:objectId").getValue().toString();
            children.add(new AlfrescoObject(o.getName(), objectId));
        }
        return children;
    }
    
    /*
     * Crea la cartella path, creando eventualmente tutto il percorso
     */
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
    
    /*
     * Crea un documento (upload)
     */
    public Document createDocument(String path, String name, byte[] content){
        Folder folder;
        Session session = this.createSession();
        folder = (Folder) session.getObjectByPath(path);
        
        ByteArrayInputStream stream = new ByteArrayInputStream(content);
        ContentStream contentStream = new ContentStreamImpl(name, BigInteger.valueOf(content.length), "application/pdf", stream);
        
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        properties.put(PropertyIds.NAME, name);

        Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        
        return document;
        
    }
    
}
