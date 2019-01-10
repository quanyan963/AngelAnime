package com.tsdm.angelanime.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.ScheduleDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2019/1/10.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {


    private List<ScheduleDetail> mData;
    private Context mContext;
    private OnDayClickListener listener;

    public DayAdapter(List<ScheduleDetail> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_day, null);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, final int position) {
        holder.tvDayTitle.setText(mData.get(position).getTitle());
        holder.tvDayTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        listener.onDayClick(mData.get(position).getUrl());
                        return true;
                }
                return false;
            }
        });
    }

    public void setOnDayClickListener(OnDayClickListener listener){
        this.listener = listener;
    }

    public interface OnDayClickListener{
        void onDayClick(String url);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_day_title)
        TextView tvDayTitle;
        public DayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
