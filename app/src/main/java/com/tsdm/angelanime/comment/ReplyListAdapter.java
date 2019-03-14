package com.tsdm.angelanime.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.ReplyItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2019/3/13.
 */

public class ReplyListAdapter extends RecyclerView.Adapter {
    private static final int COMMENT = 1;
    private static final int REPLY = 2;

    private Context mContext;
    private ReplyItem data;

    public ReplyListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(ReplyItem data){
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return COMMENT;
        } else {
            return REPLY;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == COMMENT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
            return new CommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply_detail, null);
            return new ReplyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            CommentViewHolder viewHolder = (CommentViewHolder) holder;
            viewHolder.tvName.setText(data.getName());
            viewHolder.tvTime.setText(data.getTime());
            viewHolder.tvMessage.setText(data.getCon());
            if (data.getAgree() == 0) {
                viewHolder.tvLike.setText("");
            } else {
                viewHolder.tvLike.setText(data.getAgree() + "");
            }
            if (data.getDisagree() == 0) {
                viewHolder.tvUnlike.setText("");
            } else {
                viewHolder.tvUnlike.setText(data.getDisagree() + "");
            }
        } else {
            ReplyViewHolder viewHolder = (ReplyViewHolder) holder;
            viewHolder.tvReplyName.setText(data.getReply().get(position - 1).getName());
            viewHolder.tvReplyCon.setText(data.getReply().get(position - 1).getCon());
        }

    }

    @Override
    public int getItemCount() {
        return data.getReply().size() + 1;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_unlike)
        TextView tvUnlike;
        @BindView(R.id.tv_like)
        TextView tvLike;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_reply_name)
        TextView tvReplyName;
        @BindView(R.id.tv_reply_con)
        TextView tvReplyCon;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
