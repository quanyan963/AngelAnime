package com.tsdm.angelanime.model.net;

import android.content.Context;

import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Mr.Quan on 2018/11/12.
 */

public interface NetHelper {

    void getWebHtml(String s, WebResponseListener listener, Context context,Object script);

    void reLoad();

    void release();

    List<TopEight> getTopEight(String url, WebResponseListener listener);

    Flowable<String[]> getListUrl(String hrefUrl, WebResponseListener listener);

    Flowable<Document> getPlayUrl(String hrefUrl, WebResponseListener listener);

    Flowable<Document> getSearch(int page, String s, WebResponseListener listener);

    Flowable<Document> getHtmlResponse(String s, WebResponseListener listener);

}
