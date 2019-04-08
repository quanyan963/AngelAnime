package com.tsdm.angelanime.detail.mvp;

import android.content.Context;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.DownloadUrl;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.bean.event.AnimationDetail;
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

import io.reactivex.functions.Function;

import static com.tsdm.angelanime.utils.Constants.OK;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetailPresenter extends RxPresenter<AnimationDetailContract.View>
        implements AnimationDetailContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private List<String> mListData;
    //    private String mName;
    private boolean hasFinish;

    @Inject
    public AnimationDetailPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
        mListData = new ArrayList<>();
    }

    @Override
    public void getDetail(String s, WebResponseListener listener, Context context, Object script, String name) {
//        mName = name;
        mDataManagerModel.getWebHtml(s, listener, context, script, name);
    }

    @Override
    public void getPlayUrl(int position, final WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getPlayUrl(mListData.get(position), listener)
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) {
                        String[] play = null;
                        String[] url = null;
                        try {
                            Elements elt = document.select("script");
                            String[] tempA = elt.get(elt.size() - 1).toString().split("redirecturl = \\\"");
                            play = tempA[1].split("\\\"");
                            String[] tempB = elt.get(elt.size() - 1).toString().split("main = \\\"");
                            url = tempB[1].split("\\\"");
                        } catch (Exception e) {
                            listener.onParseError();
                        }

                        return play[0] + url[0];
                    }
                })
                .compose(RxUtil.<String>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<String>(view) {

                    @Override
                    public void onNext(String s) {
                        view.getPlayUrl(s);
                    }
                }));
    }

    @Override
    public void getListUrl(String url, WebResponseListener listener, Context context, Object script, String name) {

        mDataManagerModel.getWebHtml(Url.URL + url, listener, context, script, name);
    }

    @Override
    public void getData(String html, WebResponseListener listener) {
        Document document = Jsoup.parse(html);
        try {
            //拼接评论地址用
            Element commentList = document.getElementById("comment_list");
            Elements url = commentList.select("iframe[src]");
            Elements download = document.getElementsByClass("playurl");
            if (url.isEmpty()) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDataManagerModel.reLoad(mName);
//                    }
//                },500);

            } else if (!hasFinish) {
                List<DownloadUrl> baiduUrl = new ArrayList<>();
                List<DownloadUrl> torrentUrl = new ArrayList<>();
                if (!download.isEmpty()) {
                    for (int i = 1; i < download.size(); i++) {
                        if (download.get(i).select("a[href]").get(0)
                                .attr("href").contains("baidu")) {
                            Elements data = download.get(i).select("a[href]");
                            for (int j = 0; j < data.size(); j++) {
                                baiduUrl.add(new DownloadUrl(data.get(j).text(),
                                        data.get(j).attr("href")));
                            }
                        } else {
                            Elements data = download.get(i).select("a[href]");
                            for (int j = 0; j < data.size(); j++) {
                                torrentUrl.add(new DownloadUrl(data.get(j).text(),
                                        data.get(j).attr("href")));
                            }
                        }
                    }
                }
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
                //评论标识号
                String[] videoNum = new String[]{};
                Elements els = document.getElementsByClass("bfdz");
                if (els.size() != 0) {
                    Elements elements = els.get(0).select("a[href]");
                    for (Element element : elements) {
                        playList.add(element.attr("href"));
                        playListTitle.add(element.text());
                        mListData.add(element.attr("href"));
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
                if (e.size() < 2) {
                    e = elt.select("span");
                    if (e.size() != 0) {
                        for (int i = 0; i < e.size(); i++) {
                            if (!e.get(i).text().isEmpty()) {
                                introduction += e.get(i).text() + "\n";
                            } else {
                                introduction += "\n";
                            }
                        }
                    } else {
                        introduction = elt.text();
                    }
                } else {
                    for (int i = 1; i < e.size(); i++) {
                        if (i == 1 && e.get(i).text().length() > 50) {
                            continue;
                        } else {
                            if (!e.get(i).text().isEmpty()) {
                                introduction += e.get(i).text() + "\n";
                            } else {
                                introduction += "\n";
                            }
                        }

                    }
                }

                if (playList.size() != 0) {
                    view.getDetail(new AnimationDetail(title, imgUrl, updateTime, statue, introduction,
                            playList, playListTitle, baiduUrl, torrentUrl,
                            Url.URL + url.get(0).attr("src"), OK));
                } else {
                    view.getDetail(new AnimationDetail(title, imgUrl, updateTime, statue, introduction,
                            Url.URL + url.get(0).attr("src"), OK));
                }
                mDataManagerModel.release();
                hasFinish = true;
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
    public void insertVideoState(VideoState value) {
        mDataManagerModel.insertVideoState(value);
    }

    @Override
    public void getUrl(String html, WebResponseListener listener) {
        view.onComplete();
        Document document = Jsoup.parse(html);
        final Element baseUrl = document.getElementById("cciframe");
        String url = baseUrl.attr("src").split("&")[1].substring(2);

        addSubscribe(mDataManagerModel.getHtmlResponse(url, listener)
                .compose(RxUtil.<Document>rxSchedulerHelper())
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) throws Exception {
                        Elements elements = document.select("script");
                        String[] urls = elements.get(elements.size() - 1).toString()
                                .replace("\r\n\t\t","").split("var ");
                        String baseUrl = urls[3].split("\\\"")[1];
                        String m3u8 = urls[11].split("\\\"")[1].split("\\?")[0];
                        return baseUrl + m3u8;
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(view) {

                    @Override
                    public void onNext(String s) {
                        view.getPlayUrl(s);
                    }
                }));
    }
}
