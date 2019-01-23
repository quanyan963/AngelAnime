package com.tsdm.angelanime.classify.mvp;

import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.ClassifyDetail;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.model.DataManagerModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tsdm.angelanime.utils.Constants.AMERICA;
import static com.tsdm.angelanime.utils.Constants.AMERICA_URL;
import static com.tsdm.angelanime.utils.Constants.NEW_ANIM;
import static com.tsdm.angelanime.utils.Constants.NEW_ANIM_URL;

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
        data.add(new ClassifyDetail(NEW_ANIM_URL,NEW_ANIM));
        for (int i = 1; i <= list.size(); i++) {
            data.add(new ClassifyDetail(list.get(i-1).attr("href"),list.get(i-1).text()));
        }
        data.add(new ClassifyDetail(AMERICA_URL,AMERICA));
        return data;
    }
}
