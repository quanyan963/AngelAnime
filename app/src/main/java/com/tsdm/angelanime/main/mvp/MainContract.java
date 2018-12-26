package com.tsdm.angelanime.main.mvp;

import android.view.View;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.TopEight;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public interface MainContract {
    interface View extends BaseView {

        void toSearchActivity(android.view.View v);
    }

    interface Presenter extends BasePresenter<View> {

        List<TopEight> geTopEight();

        List<RecentlyDetail> getRecently();

        void onClick(android.view.View view);
    }
}
