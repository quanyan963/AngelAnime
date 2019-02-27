package com.tsdm.angelanime.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsdm.angelanime.R;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment,null);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
