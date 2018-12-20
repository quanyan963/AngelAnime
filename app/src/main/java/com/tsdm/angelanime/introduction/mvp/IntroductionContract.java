package com.tsdm.angelanime.introduction.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public interface IntroductionContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
