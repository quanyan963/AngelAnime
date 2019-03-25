package com.tsdm.angelanime.introduction.mvp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.service.DownloadInterface;
import com.tsdm.angelanime.service.DownloadService;
import com.tsdm.angelanime.utils.Constants;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.io.File;
import java.util.List;

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

    @Override
    public void download(final Context context, String url, final WebResponseListener listener) {
        final MyServiceConn service = new MyServiceConn();
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        GetRequest<File> request = OkGo.<File>get("http://services.gradle.org/distributions/gradle-5.3-src.zip");
        DownloadTask task = OkDownload.request(Constants.DOWNLOADTASK, request)
                .save()
                .register(new DownloadListener(Constants.DOWNLOADTASK) {
                    @Override
                    public void onStart(Progress progress) {

                        service.getInterface().createNotification(context,progress.fileName);
                    }

                    @Override
                    public void onProgress(Progress progress) {
                        service.getInterface().progressChange((int) (progress.fraction * 100));
                    }

                    @Override
                    public void onError(Progress progress) {
                        listener.onError();
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        service.getInterface().complete();
                        context.unbindService(service);

                    }

                    @Override
                    public void onRemove(Progress progress) {

                    }
                });
        service.setTask(task);
        context.bindService(intent,service,Context.BIND_AUTO_CREATE);
    }

    private class MyServiceConn implements ServiceConnection{

        private DownloadInterface downloadInterface;
        private DownloadTask task;

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadInterface = (DownloadInterface) iBinder;
            task.start();
        }
        public DownloadInterface getInterface (){
            return downloadInterface;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
