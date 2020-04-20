package com.tsdm.angelanime.main;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.detail.AnimationDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

/**
 * Created by Mr.Quan on 2019/1/10.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<List<ScheduleDetail>> mData;
    private String[] mTitle;
    private Context mContext;

    public ScheduleAdapter(List<List<ScheduleDetail>> mData, String[] mTitle, Context mContext) {
        this.mData = mData;
        this.mTitle = mTitle;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.tvDay.setText(mTitle[position]);


        DayAdapter adapter = new DayAdapter(mData.get(position), mContext);
        adapter.setOnDayClickListener(new DayAdapter.OnDayClickListener() {
            @Override
            public void onDayClick(String url) {
                mContext.startActivity(new Intent(mContext, AnimationDetailActivity.class)
                        .putExtra(HREF_URL,url));
            }
        });
        holder.rlvDayList.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.rlv_day_list)
        RecyclerView rlvDayList;
        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rlvDayList.setHasFixedSize(true);
            rlvDayList.setLayoutManager(manager);
//            rlvDayList.addItemDecoration(new DividerItemDecoration(mContext,
//                    DividerItemDecoration.VERTICAL_LIST,
//                    mContext.getResources().getDimensionPixelSize(R.dimen.dp_8_x),
//                    mContext.getResources().getColor(R.color.light_grey)));
        }
    }
}
