package com.axiastudio.menjazo;

/**
 * User: tiziano
 * Date: 08/10/13
 * Time: 16:02
 */
public class TempFile {

    private String objectId;
    private String localPath;
    private Boolean toUpload;
    private String hash;

    public TempFile(String objectId, String localPath, Boolean toUpload, String hash) {
        this.objectId = objectId;
        this.localPath = localPath;
        this.toUpload = toUpload;
        this.hash = hash;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getLocalPath() {
        return localPath;
    }

    public Boolean getToUpload() {
        return toUpload;
    }

    public String getHash() {
        return hash;
    }
}
