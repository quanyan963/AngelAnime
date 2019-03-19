package com.tsdm.angelanime.detail.mvp;

import android.content.Context;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.bean.event.AnimationDetail;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public interface AnimationDetailContract {
    interface View extends BaseView {

        void getDetail(AnimationDetail animationDetail);

        void getPlayUrl(String s);

        void onComplete();
    }

    interface Presenter extends BasePresenter<View> {

        //void getDetail(String url, WebResponseListener listener);

        void getDetail(String s, WebResponseListener listener, Context context, Object script, String name);

        void getPlayUrl(int position, WebResponseListener listener);

        void getListUrl(String url, WebResponseListener listener);

        void getData(String html, WebResponseListener listener);

        void insertVideoState(VideoState value);
    }
}
