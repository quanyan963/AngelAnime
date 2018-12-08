package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.model.DataManagerModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter{

    private DataManagerModel mDataManagerModel;
    @Inject

    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public List<TopEight> geTopEight() {
        return mDataManagerModel.getTopEight();
    }
}
