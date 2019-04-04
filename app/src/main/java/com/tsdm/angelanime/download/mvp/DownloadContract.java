package com.tsdm.angelanime.download.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.DownloadStatue;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/3/27.
 */

public interface DownloadContract {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{

        List<DownloadStatue> geDownloadStatue();
    }
}
