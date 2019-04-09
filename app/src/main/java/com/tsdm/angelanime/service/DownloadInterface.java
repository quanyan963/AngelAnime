package com.tsdm.angelanime.service;

import com.lzy.okgo.model.Progress;

/**
 * Created by Mr.Quan on 2019/3/22.
 */

public interface DownloadInterface {
    void createNotification(String url, int id);
    void progressChange(Progress progress);
    void complete(Progress progress);
//    void onPause();
//    void onStart();
    void onRemove(Progress progress);
    void onError(Progress progress);
    void removeNotify(int id);
    void exit();
    void stop();
}
