package com.tsdm.angelanime.introduction.mvp;

import android.content.Context;

import com.lzy.okgo.model.Progress;
import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public interface IntroductionContract {
    interface View extends BaseView {

        void postDownloading(Progress progress, int notificationId);
    }

    interface Presenter extends BasePresenter<View> {
        VideoState geVideoState();

        void download(Context context, String url);
    }
}
