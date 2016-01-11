package com.axiastudio.suite.pec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class UploadAllegatoRequest {
    private String fileName;
    private String contentType;
    private long size;

    private long idMessaggio;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getIdMessaggio() {
        return idMessaggio;
    }

    public void setIdMessaggio(long idMessaggio) {
        this.idMessaggio = idMessaggio;
    }


}
