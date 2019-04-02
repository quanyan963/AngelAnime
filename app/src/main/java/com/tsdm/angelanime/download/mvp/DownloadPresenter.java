package com.tsdm.angelanime.download.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2019/3/27.
 */

public class DownloadPresenter extends RxPresenter<DownloadContract.View> implements DownloadContract.Presenter {

    private DataManagerModel mDataManagerModel;
    @Inject
    public DownloadPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }


}
