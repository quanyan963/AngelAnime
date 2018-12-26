package com.tsdm.angelanime.search.mvp;

import android.view.MenuItem;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/12/26.
 */

public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public SearchPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void onMenuClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_cancel:
                view.back();
                break;
        }
    }

    @Override
    public void search(String s) {

    }
}
