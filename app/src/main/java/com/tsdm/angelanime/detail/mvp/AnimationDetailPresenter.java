package com.tsdm.angelanime.detail.mvp;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.event.AnimationDetail;
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

import static com.tsdm.angelanime.utils.Constants.ERROR;
import static com.tsdm.angelanime.utils.Constants.OK;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetailPresenter extends RxPresenter<AnimationDetailContract.View>
        implements AnimationDetailContract.Presenter{

    private DataManagerModel mDataManagerModel;
    private List<String> mListData;
    @Inject
    public AnimationDetailPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
        mListData = new ArrayList<>();
    }

    @Override
    public void getDetail(String url, final WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getDetail(url,listener)
                .map(new Function<Document, AnimationDetail>() {
                    @Override
                    public AnimationDetail apply(Document document) {
                        //列表
                        List<String> playList = new ArrayList<>();
                        List<String> playListTitle = new ArrayList<>();
                        //封面
                        String imgUrl = "";
                        //标题
                        String title = "";
                        //最新连载
                        String statue = "";
                        //最新更新时间
                        String updateTime = "";
                        //简介
                        String introduction = "";
                        try {
                            Elements els = document.getElementsByClass("bfdz");
                            if (els.size() != 0){
                                Elements elements = els.get(0).select("a[href]");
                                for (Element element:elements){
                                    playList.add(element.attr("href"));
                                    playListTitle.add(element.text());
                                }
                            }

                            els = document.getElementsByClass("mimg");
                            imgUrl = els.select("img").attr("src");

                            title = els.select("img").attr("alt");

                            els = document.getElementsByClass("mtext");
                            statue = els.select("em").text();
                            updateTime = els.select("li").get(2).text();

                            Element elt = document.getElementsByClass("ctext fix").first();
                            Elements e = elt.select("div");
                            if (e.size()<2){
                                e = elt.select("span");
                                if (e.size() != 0){
                                    for (int i = 0; i < e.size(); i++){
                                        if (!e.get(i).text().isEmpty()){
                                            introduction+=e.get(i).text()+"\n";
                                        }else {
                                            introduction+="\n";
                                        }
                                    }
                                }else {
                                    introduction = elt.text();
                                }
                            }else {
                                for (int i = 1; i < e.size(); i++){
                                    if (i == 1 && e.get(i).text().length() > 50){
                                        continue;
                                    }else {
                                        if (!e.get(i).text().isEmpty()){
                                            introduction+=e.get(i).text()+"\n";
                                        }else {
                                            introduction+="\n";
                                        }
                                    }

                                }
                            }
                            return new AnimationDetail(title,imgUrl,updateTime,statue,introduction,
                                    playList,playListTitle,OK);

                        }catch (Exception e){
                            listener.onParseError();
                            return new AnimationDetail(ERROR);
                        }
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
    public void getPlayUrl(int position, final WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getPlayUrl(mListData.get(position),listener)
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) {
                        String[] play = null;
                        String[] url = null;
                        try {
                            Elements elt = document.select("script");
                            String[] tempA = elt.get(elt.size()-1).toString().split("redirecturl = \\\"");
                            play = tempA[1].split("\\\"");
                            String[] tempB = elt.get(elt.size()-1).toString().split("main = \\\"");
                            url = tempB[1].split("\\?");
                        }catch (Exception e){
                            listener.onParseError();
                        }

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

    @Override
    public void getListUrl(String url, WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getListUrl(url,listener)
                .compose(RxUtil.<String[]>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<String[]>(view){

                    @Override
                    public void onNext(String[] s) {
                        for (int i = 1; i<s.length; i++){
                            if (i % 2 == 1){
                                mListData.add(s[i]);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        view.onComplete();
                    }
                }));
    }
}
