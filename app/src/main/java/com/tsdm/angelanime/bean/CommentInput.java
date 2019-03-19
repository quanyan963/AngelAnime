package com.tsdm.angelanime.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/3/15.
 */

public class CommentInput implements Serializable {
    private String cType;
    private String cParent;
    private String gid;
    private String uid;
    private String uName;
    private String uNick;
    private String uTmpName;
    private String pPath;
    private String pVote;
    private String aNony;
    private String captcha;
    private String talkWhat;

    public CommentInput(String cType, String cParent, String gid, String uid, String uName
            , String uNick, String uTmpName, String pPath, String pVote, String aNony
            , String captcha, String talkWhat) {
        this.cType = cType;
        this.cParent = cParent;
        this.gid = gid;
        this.uid = uid;
        this.uName = uName;
        this.uNick = uNick;
        this.uTmpName = uTmpName;
        this.pPath = pPath;
        this.pVote = pVote;
        this.aNony = aNony;
        this.captcha = captcha;
        this.talkWhat = talkWhat;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getcParent() {
        return cParent;
    }

    public void setcParent(String cParent) {
        this.cParent = cParent;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuNick() {
        return uNick;
    }

    public void setuNick(String uNick) {
        this.uNick = uNick;
    }

    public String getuTmpName() {
        return uTmpName;
    }

    public void setuTmpName(String uTmpName) {
        this.uTmpName = uTmpName;
    }

    public String getpPath() {
        return pPath;
    }

    public void setpPath(String pPath) {
        this.pPath = pPath;
    }

    public String getpVote() {
        return pVote;
    }

    public void setpVote(String pVote) {
        this.pVote = pVote;
    }

    public String getaNony() {
        return aNony;
    }

    public void setaNony(String aNony) {
        this.aNony = aNony;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getTalkWhat() {
        return talkWhat;
    }

    public void setTalkWhat(String talkWhat) {
        this.talkWhat = talkWhat;
    }
}
