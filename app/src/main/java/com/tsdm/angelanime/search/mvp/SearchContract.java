package com.tsdm.angelanime.search.mvp;

import android.view.MenuItem;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;

/**
 * Created by Mr.Quan on 2018/12/26.
 */

public interface SearchContract {
    interface View extends BaseView {

        void back();
    }

    interface Presenter extends BasePresenter<View> {

        void onMenuClick(MenuItem item);

        void search(String s);
    }
}
