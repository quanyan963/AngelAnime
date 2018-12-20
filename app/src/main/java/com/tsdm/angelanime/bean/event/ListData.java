package com.tsdm.angelanime.bean.event;

import com.tsdm.angelanime.bean.RecentlyDetail;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2018/12/14.
 */

public class ListData implements Serializable {
    private RecentlyDetail detail;

    public ListData(RecentlyDetail detail) {
        this.detail = detail;
    }

    public RecentlyDetail getDetail() {
        return detail;
    }

    public void setDetail(RecentlyDetail detail) {
        this.detail = detail;
    }
}
