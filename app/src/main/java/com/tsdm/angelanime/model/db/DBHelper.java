package com.tsdm.angelanime.model.db;


import com.tsdm.angelanime.bean.DownloadStatue;
import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.bean.VideoState;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface DBHelper {
    void insertTopEight(List<TopEight> value);

    List<TopEight> getTopEight();

    void insertRecently(RecentlyData data);

    RecentlyData getRecently();

    void insertHistory(History data);

    List<History> getHistory();

    void deleteAllHistory();

    void deleteHistory(int position);

    void insertVideoState(VideoState value);

    VideoState getVideoState();

    void insertDownloadStatue(DownloadStatue statue);

    List<DownloadStatue> getDownloadStatue();

    void deleteDownload(DownloadStatue statue);

    void updateStatue(DownloadStatue statue);

    void deleteAllStatue();
}
