package com.tsdm.angelanime.bean.event;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class Comment implements Serializable {
    private String url;
    private int state;

    public Comment() {
    }

    public Comment(String url, int state) {
        this.url = url;
        this.state = state;
    }

    public Comment(int state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
