package com.tsdm.angelanime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2018/12/27.
 */

@Entity
public class History implements Serializable {
    static final long serialVersionUID = 42L;

    @Id
    private Long id;
    private String history;

    public History() {
    }

    @Generated(hash = 2123561833)
    public History(Long id, String history) {
        this.id = id;
        this.history = history;
    }

    public History(String history) {
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
