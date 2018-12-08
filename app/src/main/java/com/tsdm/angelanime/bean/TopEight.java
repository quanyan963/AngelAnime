package com.tsdm.angelanime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2018/11/15.
 */
@Entity
public class TopEight implements Serializable {
    static final long serialVersionUID = 42L;

    @Id
    private Long id;
    private String HrefUrl;
    private String ImgUrl;

    public TopEight() {
    }

    public TopEight(String hrefUrl, String imgUrl) {
        HrefUrl = hrefUrl;
        ImgUrl = imgUrl;
    }

    @Generated(hash = 1291403667)
    public TopEight(Long id, String HrefUrl, String ImgUrl) {
        this.id = id;
        this.HrefUrl = HrefUrl;
        this.ImgUrl = ImgUrl;
    }

    public String getHrefUrl() {
        return HrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        HrefUrl = hrefUrl;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
