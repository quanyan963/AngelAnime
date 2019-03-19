package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public class FragmentPresenter extends RxPresenter<FragmentContract.View> implements FragmentContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public FragmentPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void refresh(WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getHtmlResponse(Url.URL+Url.HOME_PAGE,listener)
                .map(new Function<Document, List<RecentlyDetail>>() {
                    @Override
                    public List<RecentlyDetail> apply(Document document) throws Exception {
                        List<RecentlyDetail> data = new ArrayList<>();
                        for(int i=0; i < 5; i++){
                            Element newUp = document.getElementById("tabcontent_"+(10+i));
                            Elements date = newUp.getElementsByClass("date");
                            Elements hrefUrl = newUp.select("a[href]");
                            Elements setNum = newUp.getElementsByClass("setnum");
                            List<String> dates = new ArrayList<>();
                            List<String> hrefUrls = new ArrayList<>();
                            List<String> names = new ArrayList<>();
                            List<String> setNumS = new ArrayList<>();
                            for (int j = 0; j < date.size(); j++){
                                dates.add(date.get(j).text());
                                hrefUrls.add(hrefUrl.get(j).attr("href"));
                                names.add(hrefUrl.get(j).text());
                                setNumS.add(setNum.get(j).text());
                            }
                            data.add(new RecentlyDetail(dates,hrefUrls,names,setNumS));
                        }
                        return data;
                    }
                })
                .compose(RxUtil.<List<RecentlyDetail>>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<List<RecentlyDetail>>(view){

                    @Override
                    public void onNext(List<RecentlyDetail> recentlyDetails) {
                        view.getRecentlyDetail(recentlyDetails);
                    }
                }));
    }
}
