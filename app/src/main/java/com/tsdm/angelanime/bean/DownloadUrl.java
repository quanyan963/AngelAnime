package com.tsdm.angelanime.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/3/21.
 */

public class DownloadUrl implements Serializable {
    private String title;
    private String url;

    public DownloadUrl(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
