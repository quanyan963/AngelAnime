package com.tsdm.angelanime.introduction.mvp;

import android.content.Context;
import android.content.Intent;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.service.DownloadInterface;
import com.tsdm.angelanime.service.DownloadService;
import com.tsdm.angelanime.service.MyServiceConn;
import com.tsdm.angelanime.utils.Constants;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class IntroductionPresenter extends RxPresenter<IntroductionContract.View> implements
        IntroductionContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private int id;

    @Inject
    public IntroductionPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public VideoState geVideoState() {
        return mDataManagerModel.getVideoState();
    }

    @Override
    public void download(final Context context, String url, final WebResponseListener listener) {
        id += 1;
        final MyServiceConn service = new MyServiceConn();
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        GetRequest<File> request = OkGo.<File>get(url);//"http://services.gradle.org/distributions/gradle-5.3-src.zip"
        final DownloadTask task = OkDownload.request(Constants.DOWNLOADTASK, request)
                .save()
                .register(new DownloadListener(Constants.DOWNLOADTASK) {
                    DownloadInterface downloadInterface;
                    @Override
                    public void onStart(Progress progress) {
                        downloadInterface = service.getInterface();
                        downloadInterface.createNotification(context,id);
                    }

                    @Override
                    public void onProgress(Progress progress) {
                        downloadInterface.progressChange(progress);
                    }

                    @Override
                    public void onError(Progress progress) {
                        downloadInterface.onError();
                        listener.onError();
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        downloadInterface.complete();
                        context.unbindService(service);
                    }

                    @Override
                    public void onRemove(Progress progress) {
                        downloadInterface.onRemove();
                        context.unbindService(service);
                    }
                });
        service.setTask(task);
        context.bindService(intent,service,Context.BIND_AUTO_CREATE);
    }
}
