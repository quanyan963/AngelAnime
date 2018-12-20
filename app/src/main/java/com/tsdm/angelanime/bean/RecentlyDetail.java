package com.tsdm.angelanime.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Quan on 2018/12/18.
 */

public class RecentlyDetail implements Serializable {
    private List<String> date;
    private List<String> Url;
    private List<String> name;
    private List<String> setNum;

    public RecentlyDetail(List<String> date, List<String> url, List<String> name, List<String> setNum) {
        this.date = date;
        Url = url;
        this.name = name;
        this.setNum = setNum;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getUrl() {
        return Url;
    }

    public void setUrl(List<String> url) {
        Url = url;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getSetNum() {
        return setNum;
    }

    public void setSetNum(List<String> setNum) {
        this.setNum = setNum;
    }
}
