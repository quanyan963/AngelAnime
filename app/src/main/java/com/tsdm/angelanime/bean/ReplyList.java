package com.tsdm.angelanime.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Quan on 2019/2/19.
 */

public class ReplyList implements Serializable {
    private int total;
    private List<ReplyItem> data;

    public ReplyList() {
    }

    public ReplyList(int total, List<ReplyItem> data) {
        this.total = total;
        this.data = data;
    }

    public ReplyList(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ReplyItem> getData() {
        return data;
    }

    public void setData(List<ReplyItem> data) {
        this.data = data;
    }
}
