package com.tsdm.angelanime.classify.mvp;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.ClassifyDetail;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.classify.ClassifyDetailFragment;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.search.SearchAdapter;
import com.tsdm.angelanime.utils.Constants;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Created by Mr.Quan on 2019/1/12.
 */

public class ClassifyDPresenter extends RxPresenter<ClassifyDContract.View> implements ClassifyDContract.Presenter {
    private DataManagerModel mDataManagerModel;
    //用来标记是否正在向上滑动
    private boolean isSlidingUpward = false;
    private int page = 0;
    private int allPage = 0;

    @Inject
    public ClassifyDPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void getClassifyDetail(String url, WebResponseListener listener) {
        addSubscribe(mDataManagerModel.getClassifyDetail(Url.URL+url
                + Constants.INDEX+(page == 0 ? "" : page)+Constants.HTML,listener)
                .map(new Function<Document, List<SearchList>>() {
                    @Override
                    public List<SearchList> apply(Document document) throws Exception {
                        Elements els = document.getElementsByClass("movie-chrList");
                        Elements list = els.select("li");
                        List<SearchList> data = new ArrayList<>();
                        if (list.size() != 0) {

                            allPage = Integer.parseInt(els.select("input[onclick]")
                                    .attr("onclick").split("\\(")[1]
                                    .split(",")[0]);
                            for (int i = 0; i < list.size(); i++) {
                                String hrefUrl = list.get(i).select("a[href]").attr("href");
                                String imgUrl = list.get(i).select("img[alt]").attr("src");
                                String title = list.get(i).select("img[alt]").attr("alt");
                                String statue = list.get(i).select("em").get(0).text();
                                String updateTime = list.get(i).select("em").get(3).text();
                                data.add(new SearchList(imgUrl, title, statue, updateTime, hrefUrl));
                            }
                        }
                        return data;
                    }
                }).compose(RxUtil.<List<SearchList>>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<List<SearchList>>(view){

                    @Override
                    public void onNext(List<SearchList> searchList) {
                        view.getList(searchList);
                    }
                }));
    }

    @Override
    public void onStateChanged(RecyclerView recyclerView, int newState, SearchAdapter adapter
            , WebResponseListener listener) {
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        //当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();

            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == (manager.getItemCount() - 1) && isSlidingUpward) {
                //加载更多
                adapter.setLoadState(adapter.LOADING);
                if (page < allPage) {
                    page += 1;
                    getClassifyDetail(null,listener);
                }else {
                    adapter.setLoadState(adapter.LOADING_END);
                }

            }
        }
    }

    @Override
    public void onScrolled(boolean b) {
        isSlidingUpward = b;
    }
}
