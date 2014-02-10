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
    List<String> fileNames = new ArrayList<String>();
    Map<String, InputStream> streams = new HashMap<String, InputStream>();

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void putStream(String name, InputStream stream){
        streams.put(name, stream);
    }

    public InputStream getStream(String name){
        return streams.get(name);
    }

}
