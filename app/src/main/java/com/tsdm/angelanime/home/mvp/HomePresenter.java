package com.tsdm.angelanime.home.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.ScheduleDetail;
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
 * Created by Mr.Quan on 2019/1/11.
 */

public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {
    private DataManagerModel mDataManagerModel;
    private RecentlyData mData;

    @Inject
    public HomePresenter(DataManagerModel mDataManagerModel) {
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

    @Override
    public List<List<ScheduleDetail>> getSchedule() {
        List<List<ScheduleDetail>> data = new ArrayList<>();

        if (mData == null){
            mData = mDataManagerModel.getRecently();
        }
        Document document = Jsoup.parse(mData.getSchedule());
        Elements week = document.getElementsByClass("contect_week");
        for (int i = 0; i < 7; i++) {
            List<ScheduleDetail> detail = new ArrayList<>();
            Elements temp = week.get(i).select("a[href]");
            for (int j = 0; j < temp.size(); j++) {
                detail.add(new ScheduleDetail(temp.get(j).attr("href"),
                        temp.get(j).text()));
            }
            data.add(detail);
        }
        return data;
    }
}
