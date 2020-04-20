package com.tsdm.angelanime.comment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.CommentInput;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.bean.event.Comment;
import com.tsdm.angelanime.comment.mvp.CommentContract;
import com.tsdm.angelanime.comment.mvp.CommentPresenter;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.utils.AlertUtils;
import com.tsdm.angelanime.utils.HiddenAnimUtils;
import com.tsdm.angelanime.widget.listener.CaptchaListener;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        CommentContract.View, WebResponseListener, CommentAdapter.OnLikeClickListener, CaptchaListener {
    @BindView(R.id.rlv_comment_list)
    RecyclerView rlvCommentList;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.rlv_reply_list)
    RecyclerView rlvReplyList;
    @BindView(R.id.et_con)
    EditText etCon;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private CommentAdapter commentAdapter;
    private ReplyListAdapter replyAdapter;
    private List<ReplyItem> data;
    private boolean isScrollLoading;
    private CommentInput input;

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

        rlvReplyList.setHasFixedSize(true);
        rlvReplyList.setLayoutManager(new LinearLayoutManager(getContext()));
        replyAdapter = new ReplyListAdapter(getContext());
        rlvReplyList.setAdapter(replyAdapter);


        initListener();
    }

    @Override
    public void getReply(final ReplyList replyList) {
        input = replyList.getInput();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (replyList.getTotal() == 0) {
                    rlvCommentList.setVisibility(View.GONE);
                    tvHint.setVisibility(View.VISIBLE);
                    tvHint.setText(R.string.no_reply);
                } else {
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
        presenter.getLike(id, action, this);
    }

    @Override
    public void onReplyClick(int position) {
        replyAdapter.setData(data.get(position));
        rlvCommentList.setVisibility(View.GONE);
        HiddenAnimUtils.newInstance(getContext(),rlvReplyList,rlvCommentList.getHeight()).hidOrShow();
    }

    public boolean hidReply(){
        if (rlvReplyList.getVisibility() == View.VISIBLE){
            rlvCommentList.setVisibility(View.VISIBLE);
            HiddenAnimUtils.newInstance(getContext(),rlvReplyList,rlvCommentList.getHeight()).hidOrShow();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void submit(String captcha) {

        try {
            input.setCaptcha(captcha);
            input.setTalkWhat(URLEncoder.encode(etCon.getText().toString(),"gb2312"));
        } catch (UnsupportedEncodingException e) {
            onParseError();
        }
        etCon.setText("");
        //presenter.submit(input,this, getContext());
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
                        CommentFragment.this, getContext(), new MyJavaScriptInterface());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrolled(dy > 0);
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = tvSubmit.getText().toString().trim();
                if (!TextUtils.isEmpty(key))
                    AlertUtils.showCaptChaDialog(getContext(), CommentFragment.this);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFragmentThread(final Comment comment) {

        if (comment.getState() == OK) {
            if (!comment.getUrl().isEmpty()) {
                rlvCommentList.setVisibility(View.VISIBLE);
                tvHint.setVisibility(View.GONE);
                presenter.getWebHtml(comment.getUrl(), CommentFragment
                        .this, getContext(), new MyJavaScriptInterface(), REPLY);
            } else {
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
                if (isScrollLoading) {
                    commentAdapter.setLoadState(commentAdapter.LOADING_ERROR);
                } else {
                    ((AnimationDetailActivity) getActivity()).onError();
                }
            }
        });
    }

    @Override
    public void onParseError() {
        if (isScrollLoading) {
            commentAdapter.setLoadState(commentAdapter.PARSE_ERROR);
        } else {
            ((AnimationDetailActivity) getActivity()).onParseError();
        }
    }
}
