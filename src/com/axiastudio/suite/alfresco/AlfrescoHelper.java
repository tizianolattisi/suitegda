/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.alfresco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

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
            children.add(new AlfrescoObject(o.getName()));
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
    
}
