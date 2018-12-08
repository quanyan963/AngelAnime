package com.tsdm.angelanime.detail.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.AnimationDetail;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public interface AnimationDetailContract {
    interface View extends BaseView {

        void getDetail(AnimationDetail animationDetail);

        void getPlayUrl(String s);
    }

    interface Presenter extends BasePresenter<View> {

        void getDetail(String url, WebResponseListener listener);

        void getPlayUrl(String url, WebResponseListener listener);
    }
}
