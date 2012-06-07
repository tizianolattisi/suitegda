/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.alfresco;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class AlfrescoObject {
    
    private String name;
    private String path;

    public AlfrescoObject(String name) {
        this(name, "");
    }

    public AlfrescoObject(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
    
    
}
