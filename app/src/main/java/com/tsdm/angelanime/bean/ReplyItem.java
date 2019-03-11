package com.tsdm.angelanime.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Quan on 2019/3/1.
 */

public class ReplyItem implements Serializable{
    private String name;
    private String time;
    private String con;
    private String id;
    private int agree;
    private int disagree;
    private List<ReplyDetail> reply;

    public ReplyItem(String name, String time, String con, String id, int agree, int disagree, List<ReplyDetail> reply) {
        this.name = name;
        this.time = time;
        this.con = con;
        this.id = id;
        this.agree = agree;
        this.disagree = disagree;
        this.reply = reply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree() {
        this.agree += 1;
    }

    public int getDisagree() {
        return disagree;
    }

    public void setDisagree() {
        this.disagree += 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public List<ReplyDetail> getReply() {
        return reply;
    }

    public void setReply(List<ReplyDetail> reply) {
        this.reply = reply;
    }
}
