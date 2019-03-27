package com.tsdm.angelanime.service;

import android.content.Context;

import com.lzy.okgo.model.Progress;

/**
 * Created by Mr.Quan on 2019/3/22.
 */

public interface DownloadInterface {
    void createNotification(Context context, int id);
    void progressChange(Progress progress);
    void complete();
//    void onPause();
//    void onStart();
    void onRemove();
    void onError();
}
