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
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.bean.event.Comment;
import com.tsdm.angelanime.comment.mvp.CommentContract;
import com.tsdm.angelanime.comment.mvp.CommentPresenter;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.tsdm.angelanime.utils.Constants.OK;
import static com.tsdm.angelanime.utils.Constants.RETRY;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentFragment extends MvpBaseFragment<CommentPresenter> implements CommentContract.View, WebResponseListener {
    @BindView(R.id.rlv_comment_list)
    RecyclerView rlvCommentList;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    private CommentAdapter commentAdapter;
    //private WebView mWebView;

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
//        mWebView = new WebView(getContext());
//        WebSettings settings = mWebView.getSettings();
//        // 此方法需要启用JavaScript
//        settings.setJavaScriptEnabled(true);
//
//        // 把刚才的接口类注册到名为HTMLOUT的JavaScript接口
//        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
//
//        // 必须在loadUrl之前设置WebViewClient
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // 这里可以过滤一下url
//                view.loadUrl("javascript:window.HTMLOUT.processHTML(document.documentElement.outerHTML);");
//            }
//        });
        startShimmer();
        rlvCommentList.setHasFixedSize(true);
        rlvCommentList.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext());
        rlvCommentList.setAdapter(commentAdapter);
        initListener();
    }

    @Override
    public void getReply(ReplyList replyList) {
        if (replyList != null){

        }
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 在这里处理html源码
            presenter.getReply(html);


        }
    }

    private void initListener() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFragmentThread(final Comment comment) {

        if (comment.getState() == OK) {
            stopShimmer();
            if (!comment.getUrl().isEmpty()) {
                rlvCommentList.setVisibility(View.VISIBLE);
                tvHint.setVisibility(View.GONE);
                presenter.getWebHtml(comment.getUrl() + Url.COMMENT_END,CommentFragment
                        .this,getContext(),new MyJavaScriptInterface());
//                mWebView.loadUrl(comment.getUrl() +
//                        Url.COMMENT_END);
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
