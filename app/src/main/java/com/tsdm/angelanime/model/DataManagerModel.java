package com.tsdm.angelanime.model;


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
    public Flowable<String[]> getListUrl(String hrefUrl, WebResponseListener listener) {
        return mNetHelper.getListUrl(hrefUrl, listener);
    }

    @Override
    public Flowable<Document> getPlayUrl(String hrefUrl, WebResponseListener listener) {
        return mNetHelper.getPlayUrl(hrefUrl,listener);
    }

    @Override
    public Flowable<Document> getSearch(String s, WebResponseListener listener) {
        return mNetHelper.getSearch(s,listener);
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
    public void deleteHistory(History history) {
        mDBDbHelper.deleteHistory(history);
    }
}
