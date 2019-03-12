package com.tsdm.angelanime.model.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
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
    private WebView initWebView(Context context, Object script, final String name, final WebResponseListener listener){
        mWebView = new WebView(context);
        WebSettings settings = mWebView.getSettings();
        // 此方法需要启用JavaScript
        settings.setJavaScriptEnabled(true);

        // 把刚才的接口类注册到名为HTMLOUT的JavaScript接口
        mWebView.addJavascriptInterface(script, name);

        // 必须在loadUrl之前设置WebViewClient
        mWebView.setWebViewClient(new WebViewClient() {

            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico") ) {
//                    //Log.e(TAG,"favicon.ico 请求错误"+errorResponse.getStatusCode()+errorResponse.getReasonPhrase());
//                } else {
//                    // TODO:  具体可根据返回状态码做相应处理
//                    listener.onError();
//                }

            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                // 这里可以过滤一下url
                super.onPageFinished(view, url);
                Log.e("test", "onPageFinished: "+System.currentTimeMillis()+"+++"+name);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("test", "run: "+System.currentTimeMillis()+"+++"+name);
                        view.loadUrl("javascript:window."+name+".processHTML(document.documentElement.outerHTML);");
                    }
                },600);
            }
        });

        return mWebView;
    }


    @Override
    public void getWebHtml(String s, WebResponseListener listener, Context context,Object script, String name) {
        initWebView(context,script,name,listener).loadUrl(s);
    }

//    @Override
//    public void reLoad(final String name) {
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                if (mWebView != null)
////                    mWebView.loadUrl("javascript:window."+name+".processHTML(document.documentElement.outerHTML);");
////            }
////        });
//    }

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
