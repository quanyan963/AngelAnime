package com.tsdm.angelanime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/4/4.
 */

@Entity
public class DownloadStatue implements Serializable {
    static final long serialVersionUID = 42L;
    
    @Id
    private Long id;
    private int notifyId;
    private int statue;

    @Generated(hash = 322351948)
    public DownloadStatue(Long id, int notifyId, int statue) {
        this.id = id;
        this.notifyId = notifyId;
        this.statue = statue;
    }

    public DownloadStatue(int notifyId, int statue) {
        this.notifyId = notifyId;
        this.statue = statue;
    }

    @Generated(hash = 1852954831)
    public DownloadStatue() {
    }

    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
