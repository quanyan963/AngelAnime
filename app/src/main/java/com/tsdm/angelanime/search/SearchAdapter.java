package com.tsdm.angelanime.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tsdm.angelanime.bean.SearchList;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/12/29.
 */

public class SearchAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<SearchList> mList;

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<SearchList> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
