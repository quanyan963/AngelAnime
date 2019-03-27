package com.tsdm.angelanime.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.lzy.okgo.model.Progress;
import com.tsdm.angelanime.R;

import static com.lzy.okgo.model.Progress.ERROR;
import static com.lzy.okgo.model.Progress.LOADING;
import static com.lzy.okgo.model.Progress.PAUSE;
import static com.lzy.okgo.model.Progress.WAITING;
import static com.tsdm.angelanime.utils.Constants.ON_CANCEL;
import static com.tsdm.angelanime.utils.Constants.ON_CLICK;
import static com.tsdm.angelanime.utils.Constants.ON_PAUSE;

/**
 * Created by Mr.Quan on 2019/3/22.
 */

public class DownloadService extends Service {


    private NotificationManager manager;
    private Notification notification;
    private Notification.Builder nb;
    private RemoteViews remoteView;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new NotificationControl();
    }

    private class NotificationControl extends Binder implements DownloadInterface {

        private MyServiceConn.MyBroadcastReceiver receiver;
        private int id;
        boolean isNotifyRemoved = false;
        @Override
        public void createNotification(Context context, int id) {
            this.id = id;
            receiver = new MyServiceConn.MyBroadcastReceiver();
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //侧滑取消
            IntentFilter deleteFilter = new IntentFilter();
            deleteFilter.addAction(ON_CLICK);
            registerReceiver(receiver, deleteFilter);
            Intent deleteIntent = new Intent(ON_CLICK);

            //notification点击事件
            //Intent itemIntent = new Intent(this,DownloadActivity.class);

            notification = new Notification();
            // 这个参数是通知提示闪出来的值.
            notification.tickerText = getString(R.string.start);
            remoteView = new RemoteViews(getPackageName(), R.layout.remoteview_main);
            remoteView.setProgressBar(R.id.pb_download, 100, 0, false);
            nb = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.download) // 小图标
                    //.setCustomContentView(remoteView) // 设置自定义的RemoteView，需要API最低为24
                    //.setAutoCancel(true) // 点击通知后通知在通知栏上消失
                    .setDeleteIntent(PendingIntent.getBroadcast(context,0,deleteIntent,0))//侧滑删除
                    ;//点击通知  .setContentIntent(PendingIntent.getActivity(context,0,itemIntent,0))
            notification = nb.build();
            notification.contentView = remoteView;

            IntentFilter pauseFilter = new IntentFilter();
            pauseFilter.addAction(ON_PAUSE);
            registerReceiver(receiver, pauseFilter);
            Intent pauseIntent = new Intent(ON_PAUSE);
            //给pause添加点击事件
            notification.contentView.setOnClickPendingIntent(R.id.tv_start, PendingIntent
                    .getBroadcast(context, 0, pauseIntent, 0));

            IntentFilter cancelFilter = new IntentFilter();
            cancelFilter.addAction(ON_CANCEL);
            registerReceiver(receiver, cancelFilter);
            Intent cancelIntent = new Intent(ON_CANCEL);
            //给cancel添加点击事件
            notification.contentView.setOnClickPendingIntent(R.id.tv_cancel, PendingIntent
                    .getBroadcast(context, 0, cancelIntent, 0));
            manager.notify(id, notification);

        }

        @Override
        public void progressChange(Progress progress) {
            if (!isNotifyRemoved){
                notification.contentView.setTextViewText(R.id.tv_remote_title, progress.fileName);
                if (progress.status == LOADING){
                    notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.pause));
                }else if (progress.status == PAUSE){
                    notification.contentView.setTextViewText(R.id.tv_start, getString(R.string._continue));
                }else if (progress.status == WAITING){
                    notification.contentView.setTextViewText(R.id.tv_remote_title, getString(R.string.initing));
                    notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.pause));
                }else if (progress.status == ERROR){
                    notification.contentView.setTextViewText(R.id.tv_remote_title, getString(R.string.parse_error));
                    notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.retry));
                }
                notification.contentView.setProgressBar(R.id.pb_download, 100, (int) (progress.fraction * 100), false);
                //notification.contentView = remoteView;
                manager.notify(id, notification);
            }
        }

        @Override
        public void complete() {
            notification.tickerText = getString(R.string.complete);
            notification.contentView.setTextViewText(R.id.tv_start, getString(R.string.com));
            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }
            manager.notify(id, notification);
            //manager.cancel(1);
        }

        @Override
        public void onRemove() {
            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }
            manager.cancel(id);
        }

        @Override
        public void onError() {
            remoteView.setTextViewText(R.id.tv_remote_title, getString(R.string.network_error));
            remoteView.setTextViewText(R.id.tv_start, getString(R.string.retry));
        }

        @Override
        public void removeNotify() {
            manager.cancel(id);
            isNotifyRemoved = true;
        }

//        @Override
//        public void onPause() {
//            remoteView.setImageViewResource(R.id.iv_remote_start, R.mipmap.play);
//            notification.contentView = remoteView;
//            manager.notify(id, notification);
//        }
//
//        @Override
//        public void onStart() {
//            remoteView.setImageViewResource(R.id.iv_remote_start, R.mipmap.pause);
//            notification.contentView = remoteView;
//            manager.notify(id, notification);
//        }
    }
}
