package com.tsdm.angelanime.classify.mvp;

import android.support.v7.widget.RecyclerView;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.search.SearchAdapter;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/1/12.
 */

public interface ClassifyDContract {
    interface View extends BaseView {

        void getList(List<SearchList> searchList);
    }

    interface Presenter extends BasePresenter<View> {

        void getClassifyDetail(String url, WebResponseListener listener);

        void onStateChanged(RecyclerView recyclerView, int newState, SearchAdapter adapter
                , WebResponseListener listener);

        void onScrolled(boolean b);
    }
}