package com.tsdm.angelanime.introduction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.DownloadUrl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2018/12/12.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ListItemHolder> {
    private List<DownloadUrl> mData;
    private Context mContext;
    private DownloadClickListener listener;

    public DownloadAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addList(List<DownloadUrl> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_day, null);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, final int position) {
        if (position < 3){
            holder.vLeft.setVisibility(View.VISIBLE);
        }else {
            holder.vLeft.setVisibility(View.GONE);
        }
        if (position > mData.size() - 4){
            holder.vRight.setVisibility(View.VISIBLE);
        }else {
            holder.vRight.setVisibility(View.GONE);
        }
        holder.tvDayTitle.setText(mData.get(position).getTitle());

        holder.vRight.setVisibility(View.GONE);

        holder.tvDayTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        listener.onItemClick(mData.get(position).getUrl());
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_day_title)
        TextView tvDayTitle;
        @BindView(R.id.v_left)
        View vLeft;
        @BindView(R.id.v_right)
        View vRight;
        public ListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnClickListener(DownloadClickListener listener) {
        this.listener = listener;
    }

    public interface DownloadClickListener {
        void onItemClick(String url);
    }
}
