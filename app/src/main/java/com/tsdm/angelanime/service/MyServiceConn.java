package com.tsdm.angelanime.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by Mr.Quan on 2019/3/26.
 */

public class MyServiceConn implements ServiceConnection {

    private static DownloadInterface downloadInterface;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        downloadInterface = (DownloadInterface) iBinder;
    }

    public DownloadInterface getInterface (){
        return downloadInterface;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
