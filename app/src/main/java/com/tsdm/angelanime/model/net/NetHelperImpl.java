package com.tsdm.angelanime.model.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tsdm.angelanime.bean.TopEight;
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

import static android.support.constraint.Constraints.TAG;
import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.tsdm.angelanime.utils.Constants.URL_HOME;

/**
 * Created by Mr.Quan on 2018/11/12.
 */

public class NetHelperImpl implements NetHelper {

    private List<TopEight> mData;
    private WebView mWebView;

    @Inject
    public NetHelperImpl() {
    }

    @SuppressLint("JavascriptInterface")
    private WebView initWebView(Context context, Object script, final WebResponseListener listener){
        if (mWebView == null){
            mWebView = new WebView(context);
            WebSettings settings = mWebView.getSettings();
            // 此方法需要启用JavaScript
            settings.setJavaScriptEnabled(true);

            // 把刚才的接口类注册到名为HTMLOUT的JavaScript接口
            mWebView.addJavascriptInterface(script, "HTMLOUT");

            // 必须在loadUrl之前设置WebViewClient
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    listener.onError();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // 这里可以过滤一下url
                    super.onPageFinished(view, url);
                    view.loadUrl("javascript:window.HTMLOUT.processHTML(document.documentElement.outerHTML);");
                }
            });
        }
        return mWebView;
    }


    @Override
    public void getWebHtml(String s, WebResponseListener listener, Context context,Object script) {
        initWebView(context,script,listener).loadUrl(s);
    }

    @Override
    public void reLoad() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:window.HTMLOUT.processHTML(document.documentElement.outerHTML);");
            }
        });
    }

    @Override
    public void release() {
        mWebView = null;
    }

    @Override
    public List<TopEight> getTopEight(String url, final WebResponseListener listener) {
        mData = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(Url.URL + url).get();
        } catch (Exception e) {
            listener.onError();
        }
        try {
            if (doc != null) {
                Element data = doc.getElementById("au");
                Elements els = data.getElementsByTag("a");
                Elements img = data.getElementsByTag("img");
                for (int i = 0; i < 8; i++) {
                    mData.add(new TopEight(els.get(i).attr("href"),
                            img.get(i).attr("src")));
                }
            }
        }catch (Exception e){
            listener.onParseError();
        }
        return mData;
    }

    @Override
    public Flowable<String[]> getListUrl(final String hrefUrl, final WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<String[]>() {
            @Override
            public void subscribe(final FlowableEmitter<String[]> e) throws Exception {
                try {
                    Document document = Jsoup.connect(Url.URL + hrefUrl).get();

                }catch (Exception e1){

                }
                OkGo.<String>get(Url.URL + hrefUrl)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    Document document = Jsoup.parse(response.body());
                                    Element elt = document.getElementById("ccplay");
                                    Element els = elt.select("script").first();
                                    String temp = els.attr("src");
                                    OkGo.<String>get(Url.URL + temp)
                                            .tag(this)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    try {
                                                        String[] body = response.body().split("\\$");
                                                        e.onNext(body);
                                                        e.onComplete();
                                                    }catch (Exception e){
                                                        listener.onParseError();
                                                    }

                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    listener.onError();
                                                }
                                            });
                                }catch (Exception e){
                                    listener.onParseError();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                listener.onError();
                            }
                        });

            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Document> getPlayUrl(final String hrefUrl, final WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(final FlowableEmitter<Document> e) throws Exception {
                OkGo.<String>get(hrefUrl)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    Document document = Jsoup.parse(response.body());
                                    e.onNext(document);
                                    e.onComplete();
                                }catch (Exception e){
                                    listener.onParseError();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                listener.onError();
                            }
                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Document> getSearch(final int page, final String s, final WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(FlowableEmitter<Document> e) {
                try {
                    String  gb2312 = new String(s.getBytes("iso-8859-1"),"gb2312");
                    String word = new String(gb2312.getBytes("gb2312"),"utf-8");
                    e.onNext(Jsoup.connect(Url.SEARCH + Url.PAGE + page + Url.AND
                            + Url.SEARCH_WORD + word + Url.AND + Url.End).get());
                    e.onComplete();
                } catch (Exception e1) {
                    listener.onError();
                }

            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Document> getHtmlResponse(final String s, final WebResponseListener listener) {
        return Flowable.create(new FlowableOnSubscribe<Document>() {
            @Override
            public void subscribe(FlowableEmitter<Document> e) {
                try {
                    e.onNext(Jsoup.connect(s).get());
                    e.onComplete();
                }catch (IOException e1){
                    listener.onError();
                }
            }
        },BackpressureStrategy.BUFFER);
    }
}
