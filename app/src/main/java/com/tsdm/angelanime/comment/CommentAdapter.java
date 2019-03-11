package com.tsdm.angelanime.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.LIKE;
import static com.tsdm.angelanime.utils.Constants.UNLIKE;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context mContext;
    private List<ReplyItem> data;
    private ReplyAdapter adapter;
    private OnLikeClickListener listener;
    private int position = -1;
    private String action;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent,false);
        return new CommentViewHolder(view);
    }

    public void setData(List<ReplyItem> data){
        this.data = data;
        notifyDataSetChanged();
    }
    public void reFlush(int position, String action){
        this.action = action;
        this.position = position;
        if (action == LIKE){
            data.get(position).setAgree();
        }else {
            data.get(position).setDisagree();
        }
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.tvTime.setText(data.get(position).getTime());
        holder.tvMessage.setText(data.get(position).getCon());
        if (data.get(position).getAgree() == 0){
            holder.tvLike.setText("");
        }else {
            holder.tvLike.setText(data.get(position).getAgree()+"");
        }
        if (data.get(position).getDisagree() == 0){
            holder.tvUnlike.setText("");
        }else {
            holder.tvUnlike.setText(data.get(position).getDisagree()+"");
        }
        if (data.get(position).getReply().isEmpty()){
            holder.rlReply.setVisibility(View.GONE);
        }else {

            adapter = new ReplyAdapter(mContext,data.get(position).getReply());
            holder.rlvTopReply.setAdapter(adapter);
            holder.rlReply.setVisibility(View.VISIBLE);
            if (data.get(position).getReply().size() > 2){
                holder.tvMore.setVisibility(View.VISIBLE);
                holder.tvMore.setText(String.format(mContext.getResources().getString(R.string.
                        reply), data.get(position).getReply().size() + ""));
            }else {
                holder.tvMore.setVisibility(View.GONE);
            }
        }
        holder.tvUnlike.setCompoundDrawablesWithIntrinsicBounds(Utils
                        .changeSVGColor(R.drawable.unlike,R.color.low_grey,mContext)
                ,null,null,null);
        holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(Utils
                        .changeSVGColor(R.drawable.like,R.color.low_grey,mContext)
                ,null,null,null);
        if (this.position == position){
            if (action == LIKE){
                holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(Utils
                        .changeSVGColor(R.drawable.like,R.color.red,mContext)
                        ,null,null,null);
                holder.tvUnlike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                .changeSVGColor(R.drawable.unlike,R.color.low_grey,mContext)
                        ,null,null,null);
            }else {
                holder.tvUnlike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                .changeSVGColor(R.drawable.unlike,R.color.red,mContext)
                        ,null,null,null);
                holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                .changeSVGColor(R.drawable.like,R.color.low_grey,mContext)
                        ,null,null,null);
            }
        }
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReplyClick(position);
            }
        });
        holder.tvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLikeClick(position,data.get(position).getId(),LIKE);
            }
        });
        holder.tvUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLikeClick(position,data.get(position).getId(),UNLIKE);
            }
        });
    }

    public interface OnLikeClickListener{
        void onLikeClick(int position, String id, String action);
        void onReplyClick(int position);
    }

    public void setLikeClickListener(OnLikeClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
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
        @BindView(R.id.rlv_top_reply)
        RecyclerView rlvTopReply;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.rl_reply)
        RelativeLayout rlReply;
        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            rlvTopReply.setHasFixedSize(true);
            rlvTopReply.setLayoutManager(new LinearLayoutManager(mContext));
            rlvTopReply.addItemDecoration(new DividerItemDecoration(mContext,
                    DividerItemDecoration.HORIZONTAL_LIST, mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dp_8_y),
                    mContext.getResources().getColor(R.color.grey_back)));
        }
    }
}
