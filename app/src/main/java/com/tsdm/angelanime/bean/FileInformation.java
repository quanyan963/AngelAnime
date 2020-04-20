package com.tsdm.angelanime.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/3/29.
 */

public class FileInformation implements Serializable {
    private String fileName;
    private String createDate;
    private int state;
    private int progress;
    private int id;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FileInformation(String fileName, String createDate) {

        this.fileName = fileName;
        this.createDate = createDate;
    }

    public FileInformation(String fileName, String createDate, int id) {
        this.fileName = fileName;
        this.createDate = createDate;
        this.id = id;
    }

    public FileInformation(String fileName, int state, int progress, int id) {
        this.fileName = fileName;
        this.state = state;
        this.progress = progress;
        this.id = id;
    }
}
