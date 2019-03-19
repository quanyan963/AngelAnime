package com.tsdm.angelanime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2019/3/19.
 */
@Entity
public class VideoState implements Serializable {
    static final long serialVersionUID = 42L;

    @Id
    private Long id;
    private String title;
    private int listPosition;
    private Long videoPosition;

    public VideoState() {
    }

    @Generated(hash = 703390121)
    public VideoState(Long id, String title, int listPosition, Long videoPosition) {
        this.id = id;
        this.title = title;
        this.listPosition = listPosition;
        this.videoPosition = videoPosition;
    }

    public VideoState(String title, int listPosition, Long videoPosition) {
        this.title = title;
        this.listPosition = listPosition;
        this.videoPosition = videoPosition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public Long getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(Long videoPosition) {
        this.videoPosition = videoPosition;
    }
}
