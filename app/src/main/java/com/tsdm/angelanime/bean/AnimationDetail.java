package com.tsdm.angelanime.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetail implements Serializable {

    private String title;
    private String titleImg;
    private String updateTime;
    private String statue;
    private String introduction;
    private List<String> playList;
    private List<String> playListTitle;

    public AnimationDetail(String title, String titleImg, String updateTime, String statue,
                           String introduction, List<String> playList, List<String> playListTitle) {
        this.title = title;
        this.titleImg = titleImg;
        this.updateTime = updateTime;
        this.statue = statue;
        this.introduction = introduction;
        this.playList = playList;
        this.playListTitle = playListTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<String> getPlayList() {
        return playList;
    }

    public void setPlayList(List<String> playList) {
        this.playList = playList;
    }

    public List<String> getPlayListTitle() {
        return playListTitle;
    }

    public void setPlayListTitle(List<String> playListTitle) {
        this.playListTitle = playListTitle;
    }
}
