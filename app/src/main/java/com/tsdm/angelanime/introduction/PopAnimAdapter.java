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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2018/12/22.
 */

public class PopAnimAdapter extends RecyclerView.Adapter<PopAnimAdapter.AnimViewHolder> {

    private List<String> data;
    private Context mContext;
    private PopItemClick listener;
    private int position;

    public PopAnimAdapter(List<String> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AnimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_list, parent, false);
        return new AnimViewHolder(view);
    }

    public void setPosition(int position) {
        int temp = this.position;
        this.position = position;
        notifyItemChanged(temp);
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimViewHolder holder, final int position) {
        holder.tvListName.setText(data.get(position));
        if (position % 3 == 0){
            holder.vLeft.setVisibility(View.VISIBLE);
        }else {
            holder.vLeft.setVisibility(View.GONE);
        }
        if (this.position == position) {
            holder.tvListName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.tvListName.setBackgroundResource(R.drawable.blue_rectangle_selected);
        } else {
            holder.tvListName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvListName.setBackgroundResource(R.drawable.blue_rectangle_unselected);
        }
        holder.tvListName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        view.setBackgroundResource(R.drawable.blue_rectangle_selecting);
                        break;
                    case MotionEvent.ACTION_UP:
                        ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                        view.setBackgroundResource(R.drawable.blue_rectangle_selected);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.black));
                        view.setBackgroundResource(R.drawable.blue_rectangle_unselected);
                        break;
                }
                return false;
            }
        });
        holder.tvListName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    listener.onClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class AnimViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_list_name)
        TextView tvListName;
        @BindView(R.id.v_left)
        View vLeft;
        public AnimViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnPopItemClickListener(PopItemClick listener) {
        this.listener = listener;
    }

    public interface PopItemClick {
        void onClick(int position);
    }
}
