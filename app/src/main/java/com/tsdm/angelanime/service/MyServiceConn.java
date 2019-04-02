package com.tsdm.angelanime.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lzy.okserver.download.DownloadTask;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.okgo.model.Progress.ERROR;
import static com.lzy.okgo.model.Progress.LOADING;
import static com.lzy.okgo.model.Progress.PAUSE;
import static com.tsdm.angelanime.utils.Constants.NOTIFICATION_ID;
import static com.tsdm.angelanime.utils.Constants.ON_CANCEL;
import static com.tsdm.angelanime.utils.Constants.ON_CLICK;
import static com.tsdm.angelanime.utils.Constants.ON_PAUSE;

/**
 * Created by Mr.Quan on 2019/3/26.
 */

public class MyServiceConn implements ServiceConnection {

    private static List<DownloadInterface> downloadInterface = new ArrayList<>();
    private static List<DownloadTask> task = new ArrayList<>();
    private static long time = 0;

    public void setTask(DownloadTask task) {
        this.task.add(task);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        downloadInterface.add((DownloadInterface) iBinder);
        task.get(task.size() - 1).start();
    }
    public DownloadInterface getInterface (int position){
        return downloadInterface.get(position - 1);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public static class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long temp = System.currentTimeMillis();
            int position = intent.getIntExtra(NOTIFICATION_ID,-1) - 1;
            if (temp - time > 300){
                if (intent.getAction().contains(ON_PAUSE)){
                    if (task.get(position).progress.status == LOADING){
                        task.get(position).pause();
                        time = System.currentTimeMillis();
                    }else if (task.get(position).progress.status == PAUSE){
                        task.get(position).start();
                        time = System.currentTimeMillis();
                    }else if (task.get(position).progress.status == ERROR){
                        task.get(position).restart();
                        time = System.currentTimeMillis();
                    }
                }else if (intent.getAction().contains(ON_CANCEL)){
                    task.get(position).remove(true);
                }else if (intent.getAction().contains(ON_CLICK)){
                    downloadInterface.get(position).removeNotify();
                }
            }
        }
    }
}
