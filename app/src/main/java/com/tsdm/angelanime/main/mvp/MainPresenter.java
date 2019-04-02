package com.tsdm.angelanime.main.mvp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.home.HomeFragment;
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

    @Override
    public void onClick(Fragment fragment, View v) {
        if (fragment instanceof HomeFragment){
            view.toSearchActivity(v);
        }else {
            view.toSettingView();
        }
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

    @Override
    public void switchNavView(int id) {
        switch (id){
            case R.id.rb_home:
                view.switchHome();
                break;
            case R.id.rb_classify:
                view.switchClassify();
                break;
        }
    }

    @Override
    public boolean onNavigationSelected(MenuItem item, Context context) {
        switch (item.getItemId()){
            case R.id.cache:
                MyApplication.getImageLoader(context).clearDiskCache();//清除磁盘缓存
                MyApplication.getImageLoader(context).clearMemoryCache();//清除内存缓存
                GSYVideoManager.instance().clearAllDefaultCache(context);//清除video缓存
                view.showView();
                break;
            case R.id.download:
                view.toDownloadView();
        }
        return false;
    }
}
