package com.axiastudio.suite.email;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 10/02/14
 * Time: 21:10
 */
public class EMail {

    String body;
    String subject;
    List<String> froms = new ArrayList<String>();
    List<String> tos = new ArrayList<String>();
    List<String> fileNames = new ArrayList<String>();
    Map<String, InputStream> streams = new HashMap<String, InputStream>();

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void putStream(String name, InputStream stream){
        streams.put(name, stream);
    }

    public InputStream getStream(String name){
        if( streams.containsKey(name) ){
            return streams.get(name);
        }
        return null;
    }

    public void addFrom(String emailAddress){
        froms.add(emailAddress);
    }

    public List<String> getFroms() {
        return froms;
    }

    public void addTo(String emailAddress){
        tos.add(emailAddress);
    }

    public List<String> getTos() {
        return tos;
    }
}
