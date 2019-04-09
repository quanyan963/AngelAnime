package com.tsdm.angelanime.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.bean.DownloadStatue;
import com.tsdm.angelanime.bean.FileInformation;
import com.tsdm.angelanime.download.DownloadActivity;
import com.tsdm.angelanime.model.DataManagerModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lzy.okgo.model.Progress.ERROR;
import static com.lzy.okgo.model.Progress.LOADING;
import static com.lzy.okgo.model.Progress.NONE;
import static com.lzy.okgo.model.Progress.PAUSE;
import static com.lzy.okgo.model.Progress.WAITING;
import static com.tsdm.angelanime.utils.Constants.CHANNEL_ID;
import static com.tsdm.angelanime.utils.Constants.CHANNEL_NAME;
import static com.tsdm.angelanime.utils.Constants.NOTIFICATION_ID;
import static com.tsdm.angelanime.utils.Constants.ON_CANCEL;
import static com.tsdm.angelanime.utils.Constants.ON_CLICK;
import static com.tsdm.angelanime.utils.Constants.ON_DESTROY;
import static com.tsdm.angelanime.utils.Constants.ON_PAUSE;

/**
 * Created by Mr.Quan on 2019/3/22.
 */

public class DownloadService extends Service implements XExecutor.OnAllTaskEndListener {


    private NotificationManager manager;
    private NotificationCompat.Builder nb;
    private RemoteViews remoteView;
    private MyBroadcastReceiver receiver;
    private List<Notification> notifyList = new ArrayList<>();
    private static List<DownloadTask> taskList = new ArrayList<>();
    private DataManagerModel mDataManagerModel;
    private boolean isExit = false;
    private static DownloadInterface downloadInterface;

    @Override
    public void onCreate() {
        OkDownload.getInstance().addOnAllTaskEndListener(this);
        mDataManagerModel = MyApplication.getAppComponent().getDataManagerModel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        }
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        receiver = new MyBroadcastReceiver();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        NotificationControl control = new NotificationControl();
        downloadInterface = control;
        return control;
    }

    @Override
    public void onAllTaskEnd() {
        downloadInterface.stop();
    }

    private class NotificationControl extends Binder implements DownloadInterface {

        private Notification notification;
        private DownloadTask task;
        //boolean isNotifyRemoved = false;
        @Override
        public void createNotification(String url, int id) {
            task = mDataManagerModel.download(getApplicationContext(),url,this,id);
            taskList.add(task);
            task.start();
            //侧滑取消
            IntentFilter deleteFilter = new IntentFilter();
            deleteFilter.addAction(ON_CLICK+id);
            registerReceiver(receiver, deleteFilter);
            Intent deleteIntent = new Intent(ON_CLICK+id);

            //notification点击事件
            Intent itemIntent = new Intent(DownloadService.this,DownloadActivity.class);

            notification = new Notification();
            // 这个参数是通知提示闪出来的值.
            notification.tickerText = getString(R.string.start);
            remoteView = new RemoteViews(getPackageName(), R.layout.remoteview_main);
            remoteView.setProgressBar(R.id.pb_download, 100, 0, false);
            nb = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                    .setSmallIcon(R.drawable.download) // 小图标
                    //.setCustomContentView(remoteView) // 设置自定义的RemoteView，需要API最低为24
                    .setAutoCancel(false) // 点击通知后通知在通知栏上消失
                    .setDeleteIntent(PendingIntent.getBroadcast(getApplicationContext(),0,deleteIntent,0))//侧滑删除
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(),0,itemIntent,0));//点击通知
            notification = nb.build();
            notification.contentView = remoteView;

            IntentFilter pauseFilter = new IntentFilter();
            pauseFilter.addAction(ON_PAUSE+id);
            registerReceiver(receiver, pauseFilter);
            Intent pauseIntent = new Intent(ON_PAUSE+id);
            pauseIntent.putExtra(NOTIFICATION_ID,id);
            //给pause添加点击事件
            notification.contentView.setOnClickPendingIntent(R.id.tv_start, PendingIntent
                    .getBroadcast(getApplicationContext(), 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            IntentFilter cancelFilter = new IntentFilter();
            cancelFilter.addAction(ON_CANCEL+id);
            registerReceiver(receiver, cancelFilter);
            Intent cancelIntent = new Intent(ON_CANCEL+id);
            cancelIntent.putExtra(NOTIFICATION_ID,id);
            //给cancel添加点击事件
            notification.contentView.setOnClickPendingIntent(R.id.tv_cancel, PendingIntent
                    .getBroadcast(getApplicationContext(), 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            notification.flags |= Notification.FLAG_NO_CLEAR;
            manager.notify(id, notification);
            notifyList.add(notification);
            mDataManagerModel.insertDownloadStatue(new DownloadStatue(id,WAITING));
            //EventBus.getDefault().post(new FileInformation("",WAITING,0,id));

        }

        @Override
        public void progressChange(Progress progress) {
            int id = Integer.parseInt(progress.tag);
            Notification notification = notifyList.get(id - 1);
            notification.contentView.setTextViewText(R.id.tv_remote_title, progress.fileName);
            if (progress.status == LOADING){
                notification.flags |= Notification.FLAG_NO_CLEAR;
                notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.pause));
            }else if (progress.status == PAUSE){
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.contentView.setTextViewText(R.id.tv_start, getString(R.string._continue));
            }else if (progress.status == WAITING){
                notification.flags |= Notification.FLAG_NO_CLEAR;
                notification.contentView.setTextViewText(R.id.tv_remote_title, getString(R.string.initing));
                notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.pause));
            }else if (progress.status == ERROR){
                notification.flags |= Notification.FLAG_NO_CLEAR;
                notification.contentView.setTextViewText(R.id.tv_remote_title, getString(R.string.parse_error));
                notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.retry));
            }

            notification.contentView.setProgressBar(R.id.pb_download, 100,
                    (int) (progress.fraction * 100), false);
            //notification.contentView = remoteView;
            manager.notify(id, notification);
            EventBus.getDefault().post(new FileInformation(progress.fileName
                    ,progress.status, (int) (progress.fraction * 100),id));
        }

        @Override
        public void complete(Progress progress) {
            int id = Integer.parseInt(progress.tag);
            Notification notification = notifyList.get(id - 1);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.tickerText = getString(R.string.complete);
            notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.com));
            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }
            manager.notify(id, notification);
            //manager.cancel(1);
            List<DownloadStatue> downloadStatues = mDataManagerModel.getDownloadStatue();
            for (int i = 0; i < downloadStatues.size(); i++) {
                if (downloadStatues.get(i).getId() == id){
                    mDataManagerModel.deleteDownload(downloadStatues.get(i));
                    break;
                }
            }
            EventBus.getDefault().post(new FileInformation(progress.fileName,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm")
                            .format(new Date(progress.date)),id));
        }

        @Override
        public void onRemove(Progress progress) {
            int id = Integer.parseInt(progress.tag);
            Notification notification = notifyList.get(id - 1);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            manager.cancel(id);
            List<DownloadStatue> downloadStatues = mDataManagerModel.getDownloadStatue();
            for (int i = 0; i < downloadStatues.size(); i++) {
                if (downloadStatues.get(i).getId() == id){
                    mDataManagerModel.deleteDownload(downloadStatues.get(i));
                    break;
                }
            }
            EventBus.getDefault().post(new FileInformation(""
                    ,NONE, 0,id));
        }

        @Override
        public void onError(Progress progress) {
            int id = Integer.parseInt(progress.tag);
            Notification notification = notifyList.get(id - 1);
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.contentView.setTextViewText(R.id.tv_remote_title, getString(R.string.network_error));
            notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.retry));
            List<DownloadStatue> downloadStatues = mDataManagerModel.getDownloadStatue();
            for (int i = 0; i < downloadStatues.size(); i++) {
                if (downloadStatues.get(i).getId() == id){
                    downloadStatues.get(i).setStatue(progress.status);
                    mDataManagerModel.updateStatue(downloadStatues.get(i));
                    break;
                }
            }
            EventBus.getDefault().post(new FileInformation(""
                    ,progress.status, (int) (progress.fraction * 100),id));
        }

        @Override
        public void removeNotify(int id) {
            Notification notification = notifyList.get(id - 1);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            //unregisterReceiver(receiver);
            manager.cancel(id);
            //isNotifyRemoved = true;
        }

        @Override
        public void exit() {
            isExit = true;
        }

        @Override
        public void stop() {
            if (isExit)
                stopSelf();
        }


    }

    @Override
    public void unbindService(ServiceConnection conn) {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        mDataManagerModel.deleteAllStatue();
        super.onDestroy();
    }

    private static long time = 0;

    public static class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long temp = System.currentTimeMillis();
            int position = intent.getIntExtra(NOTIFICATION_ID,-1) - 1;
            if (temp - time > 300){
                if (intent.getAction().contains(ON_PAUSE)){
                    if (taskList.get(position).progress.status == LOADING){
                        taskList.get(position).pause();
                        time = System.currentTimeMillis();
                    }else if (taskList.get(position).progress.status == PAUSE){
                        taskList.get(position).start();
                        time = System.currentTimeMillis();
                    }else if (taskList.get(position).progress.status == ERROR){
                        taskList.get(position).start();
                        time = System.currentTimeMillis();
                    }
                }else if (intent.getAction().contains(ON_CANCEL)){
                    if (taskList.size() != 0 ){
                        try{
                            taskList.get(position).remove(true);
                            taskList.get(position).unRegister(String.valueOf(position + 1));
                        }catch (Exception e){

                        }
                    }
                }else if (intent.getAction().contains(ON_CLICK)){
                    downloadInterface.removeNotify(position);
                }else if (intent.getAction().contains(ON_DESTROY)){
                    if (position == -1){
                        downloadInterface.stop();
                    }else {
                        downloadInterface.exit();
                    }

                }
            }
        }
    }

    @Subscribe
    public void onEventMainThread(FileInformation information) {

    }
}
