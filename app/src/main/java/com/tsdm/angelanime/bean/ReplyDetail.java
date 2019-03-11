package com.tsdm.angelanime.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/2/19.
 */

public class ReplyDetail implements Serializable {
    private String name;
    private String con;

    public ReplyDetail(String name, String con) {
        this.name = name;
        this.con = con;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}
