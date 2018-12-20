package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public class FragmentPresenter extends RxPresenter<FragmentContract.View> implements FragmentContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public FragmentPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }
}
