package com.tsdm.angelanime.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/1/10.
 */

public class ScheduleDetail implements Serializable {
    private String url;
    private String title;

    public ScheduleDetail(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
