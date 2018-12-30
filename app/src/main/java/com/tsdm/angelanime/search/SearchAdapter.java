package com.tsdm.angelanime.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

/**
 * Created by Mr.Quan on 2018/12/29.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;
    private List<SearchList> mList;

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<SearchList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search, null);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        if (mList.get(position).getImgUrl().contains("gif")){
            Utils.displayImage(mContext,mList.get(position).getImgUrl(),holder.rivImg,Utils.getImageOptions());
        }else {
            Utils.displayImage(mContext,Url.URL+mList.get(position).getImgUrl(),holder.rivImg,Utils.getImageOptions());
        }

        holder.tvTitle.setText(mList.get(position).getTitle());
        holder.tvStatue.setText(mList.get(position).getStatue());
        holder.tvUpdate.setText(mList.get(position).getUpdateTime());
        holder.rlBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        mContext.startActivity(new Intent(mContext, AnimationDetailActivity.class)
                                .putExtra(HREF_URL, Url.URL+mList.get(position).getHrefUrl()));
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.riv_img)
        RoundImageView rivImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_statue)
        TextView tvStatue;
        @BindView(R.id.tv_update)
        TextView tvUpdate;
        @BindView(R.id.rl_bg)
        RelativeLayout rlBg;
        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
