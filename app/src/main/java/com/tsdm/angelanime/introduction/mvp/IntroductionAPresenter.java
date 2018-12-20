package com.tsdm.angelanime.introduction.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/12/14.
 */

public class IntroductionAPresenter extends RxPresenter<IntroductionAContract.View> implements
        IntroductionAContract.Presenter {
    private DataManagerModel mDataManagerModel;

    @Inject
    public IntroductionAPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }
}
