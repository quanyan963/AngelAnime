package com.tsdm.angelanime.classify.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.ClassifyDetail;
import com.tsdm.angelanime.bean.RecentlyData;
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

public class ClassifyPresenter extends RxPresenter<ClassifyContract.View> implements ClassifyContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private RecentlyData mData;

    @Inject
    public ClassifyPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public List<ClassifyDetail> getClassify() {
        List<ClassifyDetail> data = new ArrayList<>();
        mData = mDataManagerModel.getRecently();
        Document document = Jsoup.parse(mData.getClassify());
        Elements list = document.select("a[href]");
        for (int i = 0; i < list.size(); i++) {
            data.add(new ClassifyDetail(list.get(i).attr("href"),list.get(i).text()));
        }
        return data;
    }
}
