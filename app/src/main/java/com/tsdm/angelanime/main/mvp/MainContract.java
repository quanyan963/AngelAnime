package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.TopEight;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public interface MainContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

        List<TopEight> geTopEight();
    }
}
