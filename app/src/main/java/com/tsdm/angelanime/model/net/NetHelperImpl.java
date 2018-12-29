package com.tsdm.angelanime.model.net;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.utils.FileEncodingDetect;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static com.tsdm.angelanime.utils.Constants.URL_HOME;

/**
 * Created by Mr.Quan on 2018/11/12.
 */

public class NetHelperImpl implements NetHelper {

    private List<TopEight> mData;

    @Inject
    public NetHelperImpl() {
    }

    @Override
    public Flowable<Document> getHtml(final WebResponseListener listener) {

        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(final FlowableEmitter<Document> e) {
                try {
                    e.onNext(Jsoup.connect(Url.URL + Url.HOME_PAGE).get());
                    e.onComplete();
                } catch (IOException e1) {
                    listener.onError();
                    e1.printStackTrace();
                }
//                OkGo.<String>get(Url.URL + Url.HOME_PAGE)
//                        .cacheKey(URL_HOME)
//                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
//                        .tag(this)
//                        .execute(new StringCallback() {
//
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                try {
//                                    String s = new String(response.body().getBytes("8859_1"),"gb2312");
//                                } catch (UnsupportedEncodingException e1) {
//                                    e1.printStackTrace();
//                                }
//                                Document document = Jsoup.parse(response.body());
//
//                            }
//
//                            @Override
//                            public void onError(Response<String> response) {
//                                listener.onError();
//                            }
//
//                            @Override
//                            public void onCacheSuccess(Response<String> response) {
//                                Document document = Jsoup.parse(response.body());
//                                e.onNext(document);
//                                e.onComplete();
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public List<TopEight> getTopEight(String url, final WebResponseListener listener) {

        try {
            mData = new ArrayList<>();
            Document doc = Jsoup.connect(Url.URL + url).get();
            if (doc == null){
                return null;
            }else {
                Element data = doc.getElementById("au");
                Elements els = data.getElementsByTag("a");
                Elements img = data.getElementsByTag("img");
                for (int i = 0; i < 8; i++) {
                    mData.add(new TopEight(els.get(i).attr("href"),
                            img.get(i).attr("src")));
                }
                return mData;
            }

        } catch (Exception e) {
            listener.onError();
            return null;
        }

    }



    @Override
    public Flowable<Document> getDetail(final String hrefUrl, final WebResponseListener listener) {


        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(final FlowableEmitter<Document> e) {
                try {
                    e.onNext(Jsoup.connect(hrefUrl).get());
                } catch (IOException e1) {
                    e.onNext(new Document(""));
                }
                e.onComplete();
//                OkGo.<String>get(hrefUrl)
//                        .headers("Content-Type", "text/html; charset=utf-8")
//                        .headers("Accept-Charset","gb2312")
//                        .tag(this)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                Document document = Jsoup.parse(response.body());
//                                e.onNext(document);
//                                e.onComplete();
////                                try {
////                                    String data = URLDecoder.decode(response.body(),"utf-8");
////                                    Document document = Jsoup.parse(data);
////                                    e.onNext(document);
////                                    e.onComplete();
////                                } catch (UnsupportedEncodingException e1) {
////                                    e1.printStackTrace();
////                                }
//
//                            }
//                        });

            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<String[]> getListUrl(final String hrefUrl, final WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<String[]>() {
            @Override
            public void subscribe(final FlowableEmitter<String[]> e) throws Exception {
                OkGo.<String>get(Url.URL+hrefUrl)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Document document = Jsoup.parse(response.body());
                                Element elt = document.getElementById("ccplay");
                                Element els = elt.select("script").first();
                                String temp = els.attr("src");
                                OkGo.<String>get(Url.URL+temp)
                                        .tag(this)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                String[] body = response.body().split("\\$");
                                                e.onNext(body);
                                                e.onComplete();
                                            }
                                        });

                            }
                        });

            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Document> getPlayUrl(final String hrefUrl, WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(final FlowableEmitter<Document> e) throws Exception {
                OkGo.<String>get(hrefUrl)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Document document = Jsoup.parse(response.body());
                                e.onNext(document);
                                e.onComplete();
                            }
                        });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Document> getSearch(final String s, final WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(FlowableEmitter<Document> e) {
                try {
                    String word = new String(s.getBytes(),"utf-8");
                    e.onNext((Document) Jsoup.connect(Url.SEARCH+word).get());
                } catch (Exception e1) {
                    listener.onError();
                    e1.printStackTrace();
                }
                e.onComplete();
            }
        },BackpressureStrategy.BUFFER);
    }
}
