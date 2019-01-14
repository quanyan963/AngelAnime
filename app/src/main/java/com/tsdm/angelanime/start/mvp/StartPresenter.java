package com.tsdm.angelanime.start.mvp;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public class StartPresenter extends RxPresenter<StartContract.View> implements StartContract.Presenter {

    private DataManagerModel mManagerModel;
    @Inject
    public StartPresenter(DataManagerModel mManagerModel) {
        this.mManagerModel = mManagerModel;
    }

    @Override
    public void getHtml(final WebResponseListener listener) {
        addSubscribe(mManagerModel.getHtml(listener)
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) throws Exception {
                        Elements els = document.getElementsByClass("box720 fl");
                        Element elt = els.get(0).select("iframe").first();
                        String imgUrl = elt.attr("src");

                        //小分类
                        Elements data = document.getElementsByClass("serial");
                        //时间表
                        Elements schedule = document.getElementsByClass("contect_week");

                        //大分类
                        elt = document.getElementById("naviin");
                        els = elt.select("ul[style]");
                        Elements classify = els.get(0).select("a[href]");

                        mManagerModel.insertRecently(new RecentlyData(data.toString(),
                                schedule.toString(),classify.toString()));
                        if (imgUrl != null){
                            return imgUrl;
                        }else {
                            return null;
                        }
                    }
                })
                .compose(RxUtil.<String>rxSchedulerHelper())
                .observeOn(Schedulers.io())
                .subscribeWith(new CommonSubscriber<String>(view){
                        @Override
                        public void onNext(String url) {
                            List<TopEight> data = mManagerModel.getTopEight(url,listener);
                            if (data!= null){
                                mManagerModel.insertTopEight(data);
                            }
                        }

                    @Override
                    public void onComplete() {
                        view.toMain();
                    }
                }));
    }
}
