package com.tsdm.angelanime.detail.mvp;

import android.util.Log;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.AnimationDetail;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetailPresenter extends RxPresenter<AnimationDetailContract.View>
        implements AnimationDetailContract.Presenter{

    private DataManagerModel mDataManagerModel;
    @Inject
    public AnimationDetailPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void getDetail(String url, WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getDetail(url,listener)
                .map(new Function<Document, AnimationDetail>() {
                    @Override
                    public AnimationDetail apply(Document document) throws Exception {
                        //列表
                        Elements els = document.getElementsByClass("bfdz");
                        Elements elements = els.get(0).select("a[href]");
                        List<String> playList = new ArrayList<>();
                        List<String> playListTitle = new ArrayList<>();
                        for (Element element:elements){
                            playList.add(element.attr("href"));
                            playListTitle.add(element.text());
                        }
                        //封面
                        els = document.getElementsByClass("mimg");
                        String imgUrl = els.select("img").attr("src");
                        //标题
                        String title = els.select("img").attr("alt");
                        //最新连载
                        els = document.getElementsByClass("mtext");
                        String statue = els.select("em").text();
                        //最新更新时间
                        String updateTime = els.select("li").get(2).text();

                        Element elt = document.getElementsByClass("ctext fix").first();
                        //简介
                        String introduction = "";
                        Elements e = elt.select("div");
                        for (int i = 1; i < e.size(); i++){
                            if (!e.get(i).text().isEmpty()){
                                introduction+=e.get(i).text()+"\n";
                            }else {
                                introduction+="\n";
                            }
                        }

                        return new AnimationDetail(title,imgUrl,updateTime,statue,introduction,
                                playList,playListTitle);
                    }
                })
                .compose(RxUtil.<AnimationDetail>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<AnimationDetail>(view){

                    @Override
                    public void onNext(AnimationDetail animationDetail) {
                        view.getDetail(animationDetail);
                    }
                }));
    }

    @Override
    public void getPlayUrl(String url,WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getPlayUrl(url,listener)
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) throws Exception {
                        Elements elt = document.select("script");
                        String[] tempA = elt.get(elt.size()-1).toString().split("redirecturl = \\\"");
                        String[] play = tempA[1].split("\\\"");
                        String[] tempB = elt.get(elt.size()-1).toString().split("main = \\\"");
                        String[] url = tempB[1].split("\\?");
                        return play[0]+url[0];
                    }
                })
                .compose(RxUtil.<String>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<String>(view){

                    @Override
                    public void onNext(String s) {
                        view.getPlayUrl(s);
                    }
                }));
    }
}
