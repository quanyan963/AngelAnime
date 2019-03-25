package com.tsdm.angelanime.service;

import android.content.Context;

/**
 * Created by Mr.Quan on 2019/3/22.
 */

public interface DownloadInterface {
    void createNotification(Context context, String title);
    void progressChange(int progress);
    void complete();
}
