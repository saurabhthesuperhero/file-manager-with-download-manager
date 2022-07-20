package com.example.filemanagerwithdownloadfunction;

public class model_file {
    String fileId;
    String fileName;
    String fileExt;
    String fileUrl;

    public model_file() {

    }


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public model_file(String fileId, String fileName, String fileExt, String fileUrl) {

        this.fileId = fileId;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.fileUrl = fileUrl;
    }
}
