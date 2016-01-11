package com.axiastudio.suite.pec;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UploadAllegatoReponse {
    private long id;
    private String link;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
