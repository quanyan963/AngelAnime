package com.tsdm.angelanime.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lzy.okserver.download.DownloadTask;

import static com.lzy.okgo.model.Progress.LOADING;
import static com.lzy.okgo.model.Progress.PAUSE;
import static com.tsdm.angelanime.utils.Constants.ON_CANCEL;
import static com.tsdm.angelanime.utils.Constants.ON_CLICK;

/**
 * Created by Mr.Quan on 2019/3/26.
 */

public class MyServiceConn implements ServiceConnection {

    private static DownloadInterface downloadInterface;
    private static DownloadTask task;
    private static long time = 0;

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

    public static class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long temp = System.currentTimeMillis();
            if (temp - time > 300){
                if (intent.getAction().contains(ON_CLICK)){
                    if (task.progress.status == LOADING){
                        task.pause();
                        time = System.currentTimeMillis();
                    }else if (task.progress.status == PAUSE){
                        task.start();
                        time = System.currentTimeMillis();
                    }
                }else if (intent.getAction().contains(ON_CANCEL)){
                    task.remove(true);
                }
            }
        }
    }
}
