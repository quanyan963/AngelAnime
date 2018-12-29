package com.tsdm.angelanime.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.History;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2018/12/28.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context mContext;
    private List<History> mList;
    private HistoryClickListener listener;
    private int newPosition = -1;

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
        this.listener = (HistoryClickListener) mContext;
    }

    public void setList(List<History> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history, null);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder holder, final int position) {
        holder.tvTitle.setText(mList.get(position).getHistory());
        if (newPosition == position){
            holder.ivRight.setVisibility(View.VISIBLE);
        }else {
            holder.ivRight.setVisibility(View.INVISIBLE);
        }
        holder.ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClick(mList.get(position),mList.size() == 1 ? true : false);
                mList.remove(position);
                newPosition = -1;
                notifyItemRemoved(position);
            }
        });
        holder.rlBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onHistoryItemClick(mList.get(position));
            }
        });
        holder.rlBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int temp = newPosition;
                newPosition = position;
                if (newPosition != -1){
                    notifyItemChanged(temp);
                }
                notifyItemChanged(newPosition);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void addItem(String value) {
        mList.add(new History(value));
        notifyItemInserted(mList.size()-1);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_right)
        ImageView ivRight;
        @BindView(R.id.rl_background)
        RelativeLayout rlBackground;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface HistoryClickListener{
        void onDeleteClick(History data, boolean isNone);
        void onHistoryItemClick(History data);
    }
}
