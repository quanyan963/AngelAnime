package com.tsdm.angelanime.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2018/12/27.
 */

public class SearchList implements Serializable {

    private String imgUrl;
    private String title;
    private String statue;
    private String updateTime;
    private String hrefUrl;

    public SearchList(String imgUrl, String title, String statue, String updateTime, String hrefUrl) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.statue = statue;
        this.updateTime = updateTime;
        this.hrefUrl = hrefUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }
}
