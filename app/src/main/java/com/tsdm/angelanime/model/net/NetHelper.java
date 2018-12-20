package com.tsdm.angelanime.model.net;

import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2018/11/12.
 */

public interface NetHelper {
    Flowable<Document> getHtml(WebResponseListener listener);

    List<TopEight> getTopEight(String url, WebResponseListener listener);

    Flowable<Document> getDetail(String hrefUrl, WebResponseListener listener);

    Flowable<String[]> getListUrl(String hrefUrl, WebResponseListener listener);

    Flowable<Document> getPlayUrl(String hrefUrl, WebResponseListener listener);
}
