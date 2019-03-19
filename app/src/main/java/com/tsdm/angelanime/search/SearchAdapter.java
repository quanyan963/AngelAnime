package com.tsdm.angelanime.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

/**
 * Created by Mr.Quan on 2018/12/29.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;
    //网络异常
    public final int LOADING_ERROR = 4;
    //解析异常
    public final int PARSE_ERROR = 5;
    private Context mContext;
    private List<SearchList> mList;


    public SearchAdapter(Context mContext) {
        mList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setList(List<SearchList> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void reSetList(List<SearchList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_search, null);
            return new SearchViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.footer_view, null);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SearchViewHolder) {
            SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
            if (position == 0 || position == 1) {
                searchViewHolder.vTop.setVisibility(View.VISIBLE);
            } else {
                searchViewHolder.vTop.setVisibility(View.GONE);
            }
            if (position % 2 == 1){
                searchViewHolder.vRight.setVisibility(View.VISIBLE);
            }else {
                searchViewHolder.vRight.setVisibility(View.GONE);
            }
            if (mList.get(position).getImgUrl().contains("gif")) {
                Utils.displayImage(mContext, mList.get(position).getImgUrl(), searchViewHolder
                        .rivImg, Utils.getImageOptions(R.mipmap.defult_img,0));
            } else {
                Utils.displayImage(mContext, Url.URL + mList.get(position).getImgUrl()
                        , searchViewHolder.rivImg, Utils.getImageOptions(R.mipmap.defult_img,0));
            }

            searchViewHolder.tvTitle.setText(mList.get(position).getTitle());
            searchViewHolder.tvStatue.setText(mList.get(position).getStatue());
            searchViewHolder.tvUpdate.setText(mList.get(position).getUpdateTime());
            searchViewHolder.rlBg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_UP:
                            mContext.startActivity(new Intent(mContext, AnimationDetailActivity.class)
                                    .putExtra(HREF_URL, Url.URL + mList.get(position).getHrefUrl()));
                            return true;
                    }
                    return false;
                }
            });
        } else {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (loadState) {
                case LOADING:
                    footerViewHolder.rlLoading.setVisibility(View.VISIBLE);
                    footerViewHolder.rlEnd.setVisibility(View.INVISIBLE);
                    break;

                case LOADING_END:
                    footerViewHolder.rlLoading.setVisibility(View.INVISIBLE);
                    footerViewHolder.rlEnd.setVisibility(View.VISIBLE);
                    footerViewHolder.tvEnd.setText(R.string.end);
                    break;

                case LOADING_COMPLETE:
                    footerViewHolder.rlLoading.setVisibility(View.INVISIBLE);
                    footerViewHolder.rlEnd.setVisibility(View.INVISIBLE);
                    break;
                case LOADING_ERROR:
                    footerViewHolder.rlLoading.setVisibility(View.INVISIBLE);
                    footerViewHolder.rlEnd.setVisibility(View.VISIBLE);
                    footerViewHolder.tvEnd.setText(R.string.network_error);
                    break;
                case PARSE_ERROR:
                    footerViewHolder.rlLoading.setVisibility(View.INVISIBLE);
                    footerViewHolder.rlEnd.setVisibility(View.VISIBLE);
                    footerViewHolder.tvEnd.setText(R.string.parse_error);
                    break;
            }
        }

    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyItemChanged(getItemCount() - 1);
    }

    //网格布局添加footer关键一步
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) manager;
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据一行单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 1 : mList.size() + 1;
    }

    public void removeAll() {
        setLoadState(LOADING_COMPLETE);
        mList.clear();
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.v_top)
        View vTop;
        @BindView(R.id.v_right)
        View vRight;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_loading)
        RelativeLayout rlLoading;
        @BindView(R.id.tv_end)
        TextView tvEnd;
        @BindView(R.id.rl_end)
        RelativeLayout rlEnd;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
