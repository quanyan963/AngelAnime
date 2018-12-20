package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public interface FragmentContract {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View> {

    }
}
