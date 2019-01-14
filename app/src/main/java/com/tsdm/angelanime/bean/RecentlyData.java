package com.tsdm.angelanime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.jsoup.select.Elements;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2018/12/17.
 */

@Entity
public class RecentlyData implements Serializable {
    static final long serialVersionUID = 42L;

    @Id
    private Long id;
    private String elements;
    private String schedule;
    private String classify;

    public RecentlyData() {
    }

    @Generated(hash = 2090676723)
    public RecentlyData(Long id, String elements, String schedule, String classify) {
        this.id = id;
        this.elements = elements;
        this.schedule = schedule;
        this.classify = classify;
    }

    public RecentlyData(String elements, String schedule, String classify) {
        this.elements = elements;
        this.schedule = schedule;
        this.classify = classify;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }
}
