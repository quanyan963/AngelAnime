package com.tsdm.angelanime.model.db;



import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.TopEight;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface DBHelper {
    void insertTopEight(List<TopEight> value);
    List<TopEight> getTopEight();

    void insertRecently(RecentlyData data);
    RecentlyData getRecently();
}
