package com.tsdm.angelanime.comment.mvp;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.CommentInput;
import com.tsdm.angelanime.bean.ReplyDetail;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.comment.CommentAdapter;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.utils.Constants;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2019/2/15.
 */

public class CommentPresenter extends RxPresenter<CommentContract.View> implements
        CommentContract.Presenter {
    private DataManagerModel mDataManagerModel;
    //    private long mCount;
    private int mTotal;
    private String mName;
    //用来标记是否正在向上滑动
    private boolean isSlidingUpward = false;
    private int page = 1;
    private String url;
    private int mCount;

    @Inject
    public CommentPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void getReply(String html, WebResponseListener listener) {
        Document document = Jsoup.parse(html);
        try {

            Element element = document.getElementById("comment");
            Elements rows = element.getElementsByClass("row");
            Elements menus = element.getElementsByClass("menu");
            Elements pager = element.getElementsByClass("pager");
            Element input = document.getElementById("form2");
            List<ReplyItem> items = new ArrayList<>();
            CommentInput commentInput = null;
            if (rows.isEmpty()) {
                //view.getReply(new ReplyList(0));
                mCount += 1;
                if (mCount > 5){
                    view.getReply(new ReplyList(0));
                }else {
                    mDataManagerModel.reLoad(mName);
                }
            } else {

                //评论数据
                Elements data = input.select("input");
                commentInput = new CommentInput(data.get(0).attr("value"),
                        data.get(1).attr("value"),
                        data.get(2).attr("value"),
                        data.get(3).attr("value"),
                        data.get(4).attr("value"),
                        data.get(5).attr("value"),
                        data.get(6).attr("value"),
                        data.get(7).attr("value"),
                        data.get(8).attr("value"),
                        data.get(9).attr("value"),
                        data.get(10).attr("value"), "");
                mCount = 0;
                pager = pager.select("a[href]");
                //总页数
                if (pager.select("a[href]").isEmpty()) {
                    mTotal = 1;
                } else {
                    mTotal = Integer.parseInt(pager.select("a[href]")
                            .get(pager.select("a[href]").size() - 1)
                            .attr("onclick").split(",")[2].split("\\)")[0]);
                }
                for (int i = 0; i < rows.size(); i++) {
                    //楼主昵称
                    String name = rows.get(i).select("h3").get(0)
                            .select("span").text();
                    //楼主评论时间
                    String time = rows.get(i).select("h3").get(0)
                            .select("label").text();
                    //楼主评论内容
                    String con = rows.get(i).getElementsByClass("mycon")
                            .get(0).text();
                    List<ReplyDetail> details = new ArrayList<>();

                    //回复数据
                    Elements els = rows.get(i).getElementsByClass("reply");
                    //同意 反对人数及评论人id
                    String[] disagree = menus.get(i).getElementsByClass("item3")
                            .attr("onclick").split(",");
                    String[] agree = menus.get(i).getElementsByClass("item2")
                            .attr("onclick").split(",");

                    if (!els.isEmpty()) {
                        int size = els.size();
                        for (int j = 0; j < size; j++) {
                            //回复人昵称
                            String reName = els.get(j).select("h4")
                                    .get(size - j - 1).select("span").text();
                            //回复内容
                            String reCon = els.get(j).select("p")
                                    .get(size - j - 1).text();
                            details.add(new ReplyDetail(reName, reCon));
                        }
                    }
                    items.add(new ReplyItem(name, time, con, agree[1], Integer.parseInt(agree[2])
                            , Integer.parseInt(disagree[2]), details));
                }
                view.getReply(new ReplyList(mTotal, items, commentInput));
                mDataManagerModel.release();
            }
        } catch (Exception e) {
            if (document.body().toString().contains(Constants.NOT_FOUND)) {
                listener.onError();
            } else {
                listener.onParseError();
            }
        }
    }

    @Override
    public void getWebHtml(String s, WebResponseListener listener, Context context, Object script, String name) {
        mName = name;
        url = s;
        mDataManagerModel.getWebHtml(s + Url.AND + Url.PAGE + page + Url.RANDOM
                + Math.random(), listener, context, script, name);
    }

    @Override
    public void getLike(String id, String action, WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getHtmlResponse(Url.COMMENT + Url.GID + id + Url.ACTION + action
                + Url.RANDOM + Math.random(), listener)
                .compose(RxUtil.<Document>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Document>(view) {

                    @Override
                    public void onNext(Document document) {
                        view.ok();
                    }
                }));
    }

    @Override
    public void onStateChanged(RecyclerView recyclerView, int newState, CommentAdapter adapter
            , WebResponseListener listener, Context context, Object script) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            MyApplication.getImageLoader(context).resume();
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();

            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == (manager.getItemCount() - 1) && isSlidingUpward) {
                //加载更多
                adapter.setLoadState(adapter.LOADING);
                if (page < mTotal) {
                    page += 1;
                    view.setScrollLoading();
                    getWebHtml(url, listener, context, script, mName);
                } else {
                    adapter.setLoadState(adapter.LOADING_END);
                }

            }
        } else {
            MyApplication.getImageLoader(context).pause();
        }
    }

    @Override
    public void onScrolled(boolean b) {
        isSlidingUpward = b;
    }

    @Override
    public void submit(CommentInput input, WebResponseListener listener, final Context context) {
        addSubscribe(mDataManagerModel.submit(input,listener)
            .compose(RxUtil.<String>rxSchedulerHelper())
            .subscribeWith(new CommonSubscriber<String>(view){

                @Override
                public void onNext(String s) {
                    Document document = Jsoup.parse(s);
                    WebView webView = new WebView(context);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadDataWithBaseURL(null, document.toString()
                            , "text/html", "gb2312", null);
                }
            }));
    }
}
