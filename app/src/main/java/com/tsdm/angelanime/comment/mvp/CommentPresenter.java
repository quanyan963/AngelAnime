package com.tsdm.angelanime.comment.mvp;

import android.content.Context;
import android.webkit.WebView;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;

import io.reactivex.functions.Function;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentPresenter extends RxPresenter<CommentContract.View> implements
        CommentContract.Presenter {
    private DataManagerModel mDataManagerModel;

    @Inject
    public CommentPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void getReply(String html) {
        ReplyList replyList = new ReplyList();
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("comment");
        Elements elements = element.getElementsByClass("row");
        if (elements.isEmpty()){
            mDataManagerModel.reLoad();
        }else {
            mDataManagerModel.release();
            view.getReply(replyList);
        }
    }

    @Override
    public void getWebHtml(String s, WebResponseListener listener, Context context, Object script) {
        mDataManagerModel.getWebHtml(s, listener, context, script);
    }
}
