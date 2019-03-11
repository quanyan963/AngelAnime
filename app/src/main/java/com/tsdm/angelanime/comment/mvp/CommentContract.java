package com.tsdm.angelanime.comment.mvp;

import android.content.Context;
import android.webkit.WebView;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public interface CommentContract {
    interface View extends BaseView{

        void getReply(ReplyList replyList);

        void ok();
    }

    interface Presenter extends BasePresenter<View>{

        void getReply(String html, WebResponseListener listener);

        void getWebHtml(String s, WebResponseListener listener, Context context, Object script);

        void getLike(String id, String action, WebResponseListener listener);
    }
}
