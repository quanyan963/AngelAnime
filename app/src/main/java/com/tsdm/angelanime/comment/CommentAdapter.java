package com.tsdm.angelanime.comment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

import static com.tsdm.angelanime.utils.Constants.LIKE;
import static com.tsdm.angelanime.utils.Constants.UNLIKE;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    //加载中动画
    private final int TYPE_LOADING = 3;
    // 当前加载状态，默认为加载完成
    private int loadState = 1;
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
    private List<ReplyItem> data;
    private int loading;
    private ReplyAdapter adapter;
    private OnLikeClickListener listener;
    private int position = -1;
    private String action;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
        data = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && loading == 0) {
            return TYPE_FOOTER;
        } else if (loading != 0) {
            return TYPE_LOADING;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.footer_view, null);
            return new FooterViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_loading, null);
            return new LoadingViewHolder(view);
        }


    }

    public void setLoading(int loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }

    public void setData(List<ReplyItem> data) {
        this.data.addAll(data);
        loading = 0;
        notifyDataSetChanged();
    }

    public void reFlush() {
        if (action == LIKE) {
            data.get(position).setAgree();
        } else {
            data.get(position).setDisagree();
        }
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CommentViewHolder) {
            CommentViewHolder searchViewHolder = (CommentViewHolder) holder;
            searchViewHolder.tvName.setText(data.get(position).getName());
            searchViewHolder.tvTime.setText(data.get(position).getTime());
            searchViewHolder.tvMessage.setText(data.get(position).getCon());
            if (data.get(position).getAgree() == 0) {
                searchViewHolder.tvLike.setText("");
            } else {
                searchViewHolder.tvLike.setText(data.get(position).getAgree() + "");
            }
            if (data.get(position).getDisagree() == 0) {
                searchViewHolder.tvUnlike.setText("");
            } else {
                searchViewHolder.tvUnlike.setText(data.get(position).getDisagree() + "");
            }
            if (data.get(position).getReply().isEmpty()) {
                searchViewHolder.rlReply.setVisibility(View.GONE);
            } else {

                adapter = new ReplyAdapter(mContext, data.get(position).getReply());
                searchViewHolder.rlvTopReply.setAdapter(adapter);
                searchViewHolder.rlReply.setVisibility(View.VISIBLE);
                if (data.get(position).getReply().size() > 2) {
                    searchViewHolder.tvMore.setVisibility(View.VISIBLE);
                    searchViewHolder.tvMore.setText(String.format(mContext.getResources().getString(R.string.
                            reply), data.get(position).getReply().size() + ""));
                } else {
                    searchViewHolder.tvMore.setVisibility(View.GONE);
                }
            }
            searchViewHolder.tvUnlike.setCompoundDrawablesWithIntrinsicBounds(Utils
                            .changeSVGColor(R.drawable.unlike, R.color.low_grey, mContext)
                    , null, null, null);
            searchViewHolder.tvLike.setCompoundDrawablesWithIntrinsicBounds(Utils
                            .changeSVGColor(R.drawable.like, R.color.low_grey, mContext)
                    , null, null, null);
            if (this.position == position) {
                if (action == LIKE) {
                    searchViewHolder.tvLike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                    .changeSVGColor(R.drawable.like, R.color.red, mContext)
                            , null, null, null);
                    searchViewHolder.tvUnlike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                    .changeSVGColor(R.drawable.unlike, R.color.low_grey, mContext)
                            , null, null, null);
                } else {
                    searchViewHolder.tvUnlike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                    .changeSVGColor(R.drawable.unlike, R.color.red, mContext)
                            , null, null, null);
                    searchViewHolder.tvLike.setCompoundDrawablesWithIntrinsicBounds(Utils
                                    .changeSVGColor(R.drawable.like, R.color.low_grey, mContext)
                            , null, null, null);
                }
            }
            searchViewHolder.rlvTopReply.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            listener.onReplyClick(position);
                            return false;
                    }
                    return false;
                }
            });
            searchViewHolder.tvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentAdapter.this.position = position;
                    CommentAdapter.this.action = LIKE;
                    listener.onLikeClick(position, data.get(position).getId(), LIKE);
                }
            });
            searchViewHolder.tvUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentAdapter.this.position = position;
                    CommentAdapter.this.action = UNLIKE;
                    listener.onLikeClick(position, data.get(position).getId(), UNLIKE);
                }
            });
        } else if (holder instanceof FooterViewHolder) {
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
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            switch (loadState){
                case LOADING:
                    loadingViewHolder.slMessage.startShimmerAnimation();
                    loadingViewHolder.slName.startShimmerAnimation();
                    loadingViewHolder.slTime.startShimmerAnimation();
                    break;
                case LOADING_END:
                    loadingViewHolder.slMessage.stopShimmerAnimation();
                    loadingViewHolder.slName.stopShimmerAnimation();
                    loadingViewHolder.slTime.stopShimmerAnimation();
                    break;
            }

        }

    }

    public interface OnLikeClickListener {
        void onLikeClick(int position, String id, String action);

        void onReplyClick(int position);
    }

    public void setLikeClickListener(OnLikeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (loading != 0) {
            return loading;
        } else {
            return data == null ? 1 : data.size() + 1;
        }

    }

    public void changeShimmer(int loadState){
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyItemChanged(getItemCount() - 1);
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
            ButterKnife.bind(this, itemView);
            rlvTopReply.setHasFixedSize(true);
            rlvTopReply.setLayoutManager(new LinearLayoutManager(mContext));
            rlvTopReply.addItemDecoration(new DividerItemDecoration(mContext,
                    DividerItemDecoration.HORIZONTAL_LIST, mContext.getResources()
                    .getDimensionPixelSize(R.dimen.dp_8_y),
                    mContext.getResources().getColor(R.color.grey_back)));
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

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sl_name)
        ShimmerLayout slName;
        @BindView(R.id.sl_time)
        ShimmerLayout slTime;
        @BindView(R.id.sl_message)
        ShimmerLayout slMessage;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
