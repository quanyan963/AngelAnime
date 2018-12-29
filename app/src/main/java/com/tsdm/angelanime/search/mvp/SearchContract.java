package com.tsdm.angelanime.search.mvp;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/12/26.
 */

public interface SearchContract {
    interface View extends BaseView {

        void back();

        void getSearchList(List<SearchList> searchLists);

        void hideHistory();

        void updateHistory(String value);
    }

    interface Presenter extends BasePresenter<View> {

        void onMenuClick(MenuItem item);

        void insertHistory(String s);

        boolean onSearch(TextView textView, int i, Activity activity);

        void onViewClick(android.view.View view);

        List<History> getHistory();

        void deleteHistory(History data);

        void search(String s, WebResponseListener listener);
    }
}
