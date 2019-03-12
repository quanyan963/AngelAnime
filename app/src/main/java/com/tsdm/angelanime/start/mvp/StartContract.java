package com.tsdm.angelanime.start.mvp;

import android.app.Activity;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public interface StartContract {
    interface View extends BaseView{
        void toMain();
    }
    interface Presenter extends BasePresenter<View>{
        void getHtml(Activity activity, WebResponseListener listener);
    }
}
