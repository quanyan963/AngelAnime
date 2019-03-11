package com.tsdm.angelanime.comment.mvp;

import android.content.Context;
import android.os.Handler;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.ReplyDetail;
import com.tsdm.angelanime.bean.ReplyItem;
import com.tsdm.angelanime.bean.ReplyList;
import com.tsdm.angelanime.model.DataManagerModel;
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
    private long mCount;
    private int mTotal;

    @Inject
    public CommentPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void getReply(String html, WebResponseListener listener) {
        try {
            Document document = Jsoup.parse(html);
            Element element = document.getElementById("comment");
            Elements rows = element.getElementsByClass("row");
            Elements menus = element.getElementsByClass("menu");
            Elements pager = element.getElementsByClass("pager");
            List<ReplyItem> items = new ArrayList<>();
            if (rows.isEmpty()){
                mCount += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCount > 5){
                            view.getReply(new ReplyList(0));
                        }else {
                            mDataManagerModel.reLoad();
                        }

                    }
                },500);
            }else {
                mCount = 0;
                pager = pager.select("a[href]");
                //总页数
                if (pager.select("a[href]").isEmpty()){
                    mTotal = 1;
                }else {
                    mTotal = Integer.parseInt(pager.select("a[href]")
                            .get(pager.select("a[href]").size()-1)
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

                    if (!els.isEmpty()){
                        int size = els.size();
                        for (int j = 0; j < size; j++) {
                            //回复人昵称
                            String reName = els.get(j).select("h4")
                                    .get(size - j - 1).select("span").text();
                            //回复内容
                            String reCon = els.get(j).select("p")
                                    .get(size - j - 1).text();
                            details.add(new ReplyDetail(reName,reCon));
                        }
                    }
                    items.add(new ReplyItem(name,time,con,agree[1],Integer.parseInt(agree[2])
                            ,Integer.parseInt(disagree[2]),details));
                }
                view.getReply(new ReplyList(mTotal,items));
                mDataManagerModel.release();
            }
        }catch (Exception e){
            listener.onParseError();
        }
    }

    @Override
    public void getWebHtml(String s, WebResponseListener listener, Context context, Object script) {
        mDataManagerModel.getWebHtml(s, listener, context, script);
    }

    @Override
    public void getLike(String id, String action, WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getHtmlResponse(Url.COMMENT+Url.GID+id+Url.ACTION+action
                +Url.RANDOM+Math.random(),listener)
                .compose(RxUtil.<Document>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Document>(view){

                    @Override
                    public void onNext(Document document) {
                        view.ok();
                    }
                }));
    }
}
