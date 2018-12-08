package com.tsdm.angelanime.model;

import android.app.Activity;
import android.content.Context;


import com.tsdm.angelanime.bean.AnimationDetail;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.model.db.DBHelper;
import com.tsdm.angelanime.model.net.NetHelper;
import com.tsdm.angelanime.model.prefs.PreferencesHelper;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class DataManagerModel implements DBHelper,PreferencesHelper,NetHelper {
    private DBHelper mDBDbHelper;
    private PreferencesHelper mPreferencesHelper;
    private NetHelper mNetHelper;

    public DataManagerModel(DBHelper mDBDbHelper, PreferencesHelper
            mPreferencesHelper, NetHelper mNetHelper) {
        this.mDBDbHelper = mDBDbHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mNetHelper = mNetHelper;
    }

    @Override
    public int getPlayPosition() {
        return 0;
    }

    @Override
    public void setPlayPosition(int position) {

    }

    @Override
    public Flowable<Document> getHtml(WebResponseListener listener) {
        return mNetHelper.getHtml(listener);
    }

    @Override
    public List<TopEight> getTopEight(String url, WebResponseListener listener) {
        return mNetHelper.getTopEight(url,listener);
    }

    @Override
    public Flowable<Document> getDetail(String hrefUrl, WebResponseListener listener) {
        return mNetHelper.getDetail(hrefUrl,listener);
    }

    @Override
    public Flowable<Document> getPlayUrl(String hrefUrl, WebResponseListener listener) {
        return mNetHelper.getPlayUrl(hrefUrl, listener);
    }

    @Override
    public void insertTopEight(List<TopEight> value) {
        mDBDbHelper.insertTopEight(value);
    }

    @Override
    public List<TopEight> getTopEight() {
        return mDBDbHelper.getTopEight();
    }
}
