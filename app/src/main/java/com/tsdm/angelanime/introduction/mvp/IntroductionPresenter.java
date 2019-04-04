package com.tsdm.angelanime.introduction.mvp;

import android.content.Context;
import android.content.Intent;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.service.DownloadInterface;
import com.tsdm.angelanime.service.DownloadService;
import com.tsdm.angelanime.service.MyServiceConn;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class IntroductionPresenter extends RxPresenter<IntroductionContract.View> implements
        IntroductionContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private int id;
    private MyServiceConn service;
    private Intent intent;
    private Context context;
    private DownloadInterface downloadInterface;

    @Inject
    public IntroductionPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public VideoState geVideoState() {
        return mDataManagerModel.getVideoState();
    }

    @Override
    public void download(String url) {
        id += 1;
        downloadInterface = service.getInterface();
        service.setTask(mDataManagerModel.download(context,url,downloadInterface,id));
        downloadInterface.createNotification(context,id);
    }

    @Override
    public void bind(Context context) {
        this.context = context;
        service = new MyServiceConn();
        intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        context.bindService(intent,service,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unBind() {
        context.unbindService(service);
    }
}
