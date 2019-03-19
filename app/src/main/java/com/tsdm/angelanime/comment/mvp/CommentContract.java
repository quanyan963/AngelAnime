package com.tsdm.angelanime.comment.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.CommentInput;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.comment.CommentAdapter;
import com.tsdm.angelanime.comment.CommentFragment;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public interface CommentContract {
    interface View extends BaseView{

        void getReply(ReplyList replyList);

        void ok();

        void setScrollLoading();
    }

    interface Presenter extends BasePresenter<View>{

        void getReply(String html, WebResponseListener listener);

        void getWebHtml(String s, WebResponseListener listener, Context context, Object script, String name);

        void getLike(String id, String action, WebResponseListener listener);

        void onStateChanged(RecyclerView recyclerView, int newState, CommentAdapter commentAdapter,
                            WebResponseListener listener, Context context, Object script);

        void onScrolled(boolean b);

        void submit(CommentInput input, WebResponseListener listener, Context context);
    }
}
