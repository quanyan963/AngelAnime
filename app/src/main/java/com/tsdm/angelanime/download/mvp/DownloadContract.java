package com.tsdm.angelanime.download.mvp;

import androidx.annotation.Nullable;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.DownloadStatue;
import com.tsdm.angelanime.bean.FileInformation;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/3/27.
 */

public interface DownloadContract {
    interface View extends BaseView{
        void getFileList(@Nullable List<FileInformation> informationList);
    }

    interface Presenter extends BasePresenter<View>{

        List<DownloadStatue> geDownloadStatue();

        void getFileList(String path);
    }
}
