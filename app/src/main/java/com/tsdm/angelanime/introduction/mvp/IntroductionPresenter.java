package com.tsdm.angelanime.introduction.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class IntroductionPresenter extends RxPresenter<IntroductionContract.View> implements
        IntroductionContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public IntroductionPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public VideoState geVideoState() {
        return mDataManagerModel.getVideoState();
    }
}
