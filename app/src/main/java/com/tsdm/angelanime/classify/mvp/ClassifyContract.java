package com.tsdm.angelanime.classify.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.ClassifyDetail;
import com.tsdm.angelanime.bean.SearchList;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/1/11.
 */

public interface ClassifyContract {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View> {

        List<ClassifyDetail> getClassify();
    }
}
