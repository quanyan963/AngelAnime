package com.tsdm.angelanime.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.tsdm.angelanime.R;

/**
 * Created by Mr.Quan on 2019/3/22.
 */

public class DownloadService extends Service {


    private NotificationManager manager;
    private Notification notification;
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

        @Override
        public void createNotification(Context context, String title) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notification = new Notification();
            // 这个参数是通知提示闪出来的值.
            notification.tickerText = getString(R.string.start);
            // 这里面的参数是通知栏view显示的内容
//            PendingIntent pi = PendingIntent.getActivity(context, 0x001,
//                    new Intent(context, TargetActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView = new RemoteViews(getPackageName(), R.layout.remoteview_main);
            remoteView.setTextViewText(R.id.tv_remote_title, title);
            remoteView.setImageViewResource(R.id.iv_remote_start, R.drawable.download);
            remoteView.setProgressBar(R.id.pb_download,100,0,false);
            //remoteView.setOnClickPendingIntent(R.id.remoteview_main_view, pi);
            Notification.Builder nb = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.download) // 小图标
                    //.setCustomContentView(remoteView) // 设置自定义的RemoteView，需要API最低为24
                    .setAutoCancel(true) // 点击通知后通知在通知栏上消失
                    ; // 设置默认的提示音、振动方式、灯光等  .setDefaults(Notification.FLAG_NO_CLEAR)
            notification = nb.build();
            notification.contentView = remoteView;

            manager.notify(1, notification);

        }

        @Override
        public void progressChange(int progress) {
            remoteView.setProgressBar(R.id.pb_download,100,progress,false);
            notification.contentView = remoteView;
            manager.notify(1,notification);
        }

        @Override
        public void complete() {
            notification.tickerText = getString(R.string.complete);
            manager.cancel(1);
        }
    }
}
