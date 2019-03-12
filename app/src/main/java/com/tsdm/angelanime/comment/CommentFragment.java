package com.tsdm.angelanime.comment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.bean.event.Comment;
import com.tsdm.angelanime.comment.mvp.CommentContract;
import com.tsdm.angelanime.comment.mvp.CommentPresenter;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.tsdm.angelanime.utils.Constants.OK;
import static com.tsdm.angelanime.utils.Constants.REPLY;
import static com.tsdm.angelanime.utils.Constants.RETRY;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentFragment extends MvpBaseFragment<CommentPresenter> implements
        CommentContract.View, WebResponseListener, CommentAdapter.OnLikeClickListener {
    @BindView(R.id.rlv_comment_list)
    RecyclerView rlvCommentList;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    private CommentAdapter commentAdapter;
    private List<ReplyItem> data;
    private boolean isScrollLoading;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, null);
    }

    @Override
    public void init() {
        isScrollLoading = false;
        data = new ArrayList<>();
        rlvCommentList.setHasFixedSize(true);
        rlvCommentList.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext());
        rlvCommentList.setAdapter(commentAdapter);
        commentAdapter.setLoading(6);
        initListener();
    }

    @Override
    public void getReply(final ReplyList replyList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (replyList.getTotal() == 0){
                    rlvCommentList.setVisibility(View.GONE);
                    tvHint.setVisibility(View.VISIBLE);
                    tvHint.setText(R.string.no_reply);
                }else {
                    tvHint.setVisibility(View.GONE);
                    rlvCommentList.setVisibility(View.VISIBLE);
                    data.addAll(replyList.getData());
                    commentAdapter.setData(replyList.getData());
                }
            }
        });
    }

    @Override
    public void ok() {
        commentAdapter.reFlush();
    }

    @Override
    public void setScrollLoading() {
        isScrollLoading = true;
    }

    @Override
    public void onLikeClick(int position, String id, String action) {
        presenter.getLike(id,action,this);
    }

    @Override
    public void onReplyClick(int position) {

    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 在这里处理html源码
            presenter.getReply(html, CommentFragment.this);
        }
    }

    private void initListener() {
        commentAdapter.setLikeClickListener(this);
        rlvCommentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.onStateChanged(recyclerView, newState, commentAdapter,
                        CommentFragment.this,getContext(), new MyJavaScriptInterface());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrolled(dy > 0);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFragmentThread(final Comment comment) {

        if (comment.getState() == OK) {
            if (!comment.getUrl().isEmpty()) {
                rlvCommentList.setVisibility(View.VISIBLE);
                tvHint.setVisibility(View.GONE);
                presenter.getWebHtml(comment.getUrl(),CommentFragment
                        .this,getContext(),new MyJavaScriptInterface(),REPLY);
            }else {
                rlvCommentList.setVisibility(View.GONE);
                tvHint.setVisibility(View.VISIBLE);
            }
        } else if (comment.getState() == RETRY) {
            commentAdapter.changeShimmer(commentAdapter.LOADING);
        } else {
            commentAdapter.changeShimmer(commentAdapter.LOADING_END);
        }

    }

    @Override
    public void onError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isScrollLoading){
                    commentAdapter.setLoadState(commentAdapter.LOADING_ERROR);
                }else {
                    ((AnimationDetailActivity)getActivity()).onError();
                }
            }
        });
    }

    @Override
    public void onParseError() {
        if (isScrollLoading){
            commentAdapter.setLoadState(commentAdapter.PARSE_ERROR);
        }else {
            ((AnimationDetailActivity)getActivity()).onParseError();
        }
    }
}
