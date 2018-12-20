package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.model.DataManagerModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter{

    private DataManagerModel mDataManagerModel;
    private RecentlyData mData;
    @Inject

    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public List<TopEight> geTopEight() {
        return mDataManagerModel.getTopEight();
    }

    @Override
    public List<RecentlyDetail> getRecently() {
        List<RecentlyDetail> data = new ArrayList<>();
        mData = mDataManagerModel.getRecently();
        Document document = Jsoup.parse(mData.getElements());
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
}
