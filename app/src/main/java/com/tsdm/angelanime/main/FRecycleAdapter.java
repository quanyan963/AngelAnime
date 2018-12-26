package com.tsdm.angelanime.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.RecentlyDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public class FRecycleAdapter extends RecyclerView.Adapter<FRecycleAdapter.FRecycleViewHolder> {

    private RecentlyDetail data;
    private Context mContext;
    private onRecycleItemClick listener;

    public FRecycleAdapter(RecentlyDetail data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_animation_list, null);
        return new FRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FRecycleViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(data.getDate().get(i) + data.getName().get(i));
        viewHolder.tvUpdate.setText(data.getSetNum().get(i));
        if (i < 2) {
            viewHolder.vTop.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vTop.setVisibility(View.GONE);
        }
        if (i % 2 == 1) {
            viewHolder.vLeft.setVisibility(View.GONE);
            viewHolder.vRight.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vLeft.setVisibility(View.VISIBLE);
            viewHolder.vRight.setVisibility(View.GONE);
        }
        viewHolder.rlMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    listener.onItemClick(i);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.getDate().size();
    }

    public class FRecycleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_update)
        TextView tvUpdate;
        @BindView(R.id.v_right)
        View vRight;
        @BindView(R.id.v_left)
        View vLeft;
        @BindView(R.id.v_top)
        View vTop;
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;

        public FRecycleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClick(onRecycleItemClick listener) {
        this.listener = listener;
    }

    public interface onRecycleItemClick {
        void onItemClick(int position);
    }
}
