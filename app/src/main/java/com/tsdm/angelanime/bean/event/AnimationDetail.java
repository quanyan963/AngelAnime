package com.tsdm.angelanime.bean.event;

import com.tsdm.angelanime.bean.DownloadUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetail implements Serializable {

    private String title;
    private String titleImg;
    private String updateTime;
    private String statue;//最新连载
    private String introduction;
    private List<String> playList;
    private List<String> playListTitle;
    private List<DownloadUrl> baiduUrls;
    private List<DownloadUrl> TorrentUrls;
    private String replyUrl;
    private int requestStatue;

    public AnimationDetail(String title, String titleImg, String updateTime, String statue,
                           String introduction, List<String> playList, List<String> playListTitle,
                           List<DownloadUrl> baiduUrls, List<DownloadUrl> torrentUrls,
                           String replyUrl, int requestStatue) {
        this.title = title;
        this.titleImg = titleImg;
        this.updateTime = updateTime;
        this.statue = statue;
        this.introduction = introduction;
        this.playList = playList;
        this.playListTitle = playListTitle;
        this.baiduUrls = baiduUrls;
        TorrentUrls = torrentUrls;
        this.replyUrl = replyUrl;
        this.requestStatue = requestStatue;
    }

    public AnimationDetail(String title, String titleImg, String updateTime, String statue,
                           String introduction, String replyUrl, int requestStatue) {
        this.title = title;
        this.titleImg = titleImg;
        this.updateTime = updateTime;
        this.statue = statue;
        this.introduction = introduction;
        this.replyUrl = replyUrl;
        this.requestStatue = requestStatue;
    }

    public List<DownloadUrl> getBaiduUrls() {
        return baiduUrls;
    }

    public void setBaiduUrls(List<DownloadUrl> baiduUrls) {
        this.baiduUrls = baiduUrls;
    }

    public List<DownloadUrl> getTorrentUrls() {
        return TorrentUrls;
    }

    public void setTorrentUrls(List<DownloadUrl> torrentUrls) {
        TorrentUrls = torrentUrls;
    }

    public String getReplyUrl() {
        return replyUrl;
    }

    public void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    public AnimationDetail(int requestStatue) {
        this.requestStatue = requestStatue;
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

    public int getRequestStatue() {
        return requestStatue;
    }

    public void setRequestStatue(int requestStatue) {
        this.requestStatue = requestStatue;
    }

    public String getVideoNum() {
        return replyUrl;
    }

    public void setVideoNum(String replyUrl) {
        this.replyUrl = replyUrl;
    }
}
