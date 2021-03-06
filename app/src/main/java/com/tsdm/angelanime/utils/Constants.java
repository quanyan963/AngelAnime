package com.tsdm.angelanime.utils;

import android.Manifest;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class Constants {
    public static String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public static final String HREF_URL = "href_url";
    public static final String POSITION = "position";
    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final int RETRY = 2;
    public static final String LIKE = "2";
    public static final String UNLIKE = "3";
    public static final String URL_HOME = "URL_HOME";
    public static final String INTRODUCTION = "introduction";
    public static final String NEW_ANIM = "新番";
    public static final String NEW_ANIM_URL = "/xinfandongman/";
    public static final String AMERICA = "美剧";
    public static final String AMERICA_URL = "/meiju/";
    public static final String INTRO = "INTRO";
    public static final String PLAY_URL = "play_url";
    public static final String REPLY = "REPLY";
    public static final String DOWNLOAD_TASK = "download_task";
    public static final String NOT_FOUND = "找不到网页";
    public static final String ON_CLICK = "on_click";
    public static final String ON_PAUSE = "on_pause";
    public static final String ON_CANCEL = "on_cancel";
    public static final String ON_DESTROY = "on_destroy";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION_CLICK = "notification_click";
    public static final String ACTIVITY_SEND = "activity_send";
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "下载通知";
}
