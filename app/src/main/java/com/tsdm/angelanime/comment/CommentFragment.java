package com.tsdm.angelanime.comment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.bean.event.Comment;
import com.tsdm.angelanime.comment.mvp.CommentContract;
import com.tsdm.angelanime.comment.mvp.CommentPresenter;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.tsdm.angelanime.utils.Constants.LIKE;
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
    private int position;
    private String action;
    private List<ReplyItem> data;

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
        startShimmer();
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
                    data = replyList.getData();
                    commentAdapter.setData(data);
                }
            }
        });
    }

    @Override
    public void ok() {
        if (action == LIKE){
            data.get(position).setAgree();
        }else {
            data.get(position).setDisagree();
        }
        commentAdapter.reFlush(position,action);
    }

    @Override
    public void onLikeClick(int position, String id, String action) {
        this.position = position;
        this.action = action;
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFragmentThread(final Comment comment) {

        if (comment.getState() == OK) {
            stopShimmer();
            if (!comment.getUrl().isEmpty()) {
                rlvCommentList.setVisibility(View.VISIBLE);
                tvHint.setVisibility(View.GONE);
                presenter.getWebHtml(comment.getUrl() + Url.COMMENT_END,CommentFragment
                        .this,getContext(),new MyJavaScriptInterface(),REPLY);
            }else {
                rlvCommentList.setVisibility(View.GONE);
                tvHint.setVisibility(View.VISIBLE);
            }
        } else if (comment.getState() == RETRY) {
            startShimmer();
        } else {
            stopShimmer();
        }

    }

    private void startShimmer() {

    }

    private void stopShimmer() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onParseError() {

    }
}
