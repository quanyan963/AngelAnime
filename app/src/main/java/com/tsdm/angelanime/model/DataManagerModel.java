package com.tsdm.angelanime.model;


import android.content.Context;

import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.model.db.DBHelper;
import com.tsdm.angelanime.model.net.NetHelper;
import com.tsdm.angelanime.model.prefs.PreferencesHelper;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;

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

//    @Override
//    public Flowable<Document> getHtml(String s, WebResponseListener listener) {
//        return mNetHelper.getHtml(s,listener);
//    }

    @Override
    public void getWebHtml(String s, WebResponseListener listener, Context context, Object script) {
        mNetHelper.getWebHtml(s, listener, context, script);
    }

    @Override
    public void reLoad() {
        mNetHelper.reLoad();
    }

    @Override
    public void release() {
        mNetHelper.release();
    }

    @Override
    public List<TopEight> getTopEight(String url, WebResponseListener listener) {
        return mNetHelper.getTopEight(url,listener);
    }

    @Override
    public Flowable<String[]> getListUrl(String hrefUrl, WebResponseListener listener) {
        return mNetHelper.getListUrl(hrefUrl, listener);
    }

    @Override
    public Flowable<Document> getPlayUrl(String hrefUrl, WebResponseListener listener) {
        return mNetHelper.getPlayUrl(hrefUrl,listener);
    }

    @Override
    public Flowable<Document> getSearch(int page, String s, WebResponseListener listener) {
        return mNetHelper.getSearch(page,s,listener);
    }

    @Override
    public Flowable<Document> getHtmlResponse(String s, WebResponseListener listener) {
        return mNetHelper.getHtmlResponse(s,listener);
    }

    @Override
    public void insertTopEight(List<TopEight> value) {
        mDBDbHelper.insertTopEight(value);
    }

    @Override
    public List<TopEight> getTopEight() {
        return mDBDbHelper.getTopEight();
    }

    @Override
    public void insertRecently(RecentlyData data) {
        mDBDbHelper.insertRecently(data);
    }

    @Override
    public RecentlyData getRecently() {
        return mDBDbHelper.getRecently();
    }

    @Override
    public void insertHistory(History data) {
        mDBDbHelper.insertHistory(data);
    }

    @Override
    public List<History> getHistory() {
        return mDBDbHelper.getHistory();
    }

    @Override
    public void deleteAllHistory() {
        mDBDbHelper.deleteAllHistory();
    }

    @Override
    public void deleteHistory(int position) {
        mDBDbHelper.deleteHistory(position);
    }
}
