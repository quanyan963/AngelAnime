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
    private long Id;
    private String elements;

    public RecentlyData() {
    }

    public RecentlyData(String elements) {
        this.elements = elements;
    }

    @Generated(hash = 1726737336)
    public RecentlyData(long Id, String elements) {
        this.Id = Id;
        this.elements = elements;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }
}
