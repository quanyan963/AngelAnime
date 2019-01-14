package com.tsdm.angelanime.search.mvp;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.search.SearchAdapter;
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

        void showSearchView();
    }

    interface Presenter extends BasePresenter<View> {

        void onMenuClick(MenuItem item);

        void insertHistory(String s);

        boolean onSearch(TextView textView, int i, Activity activity, List<History> mList);

        void onViewClick(android.view.View view);

        List<History> getHistory();

        void deleteHistory(History data, boolean isNone);
        void search(@Nullable String word, WebResponseListener listener);

        void onStateChanged(RecyclerView recyclerView, int newState, SearchAdapter searchAdapter
                , WebResponseListener listener);

        void onScrolled(boolean isSlidingUpward);

        void initPage();
    }
}
