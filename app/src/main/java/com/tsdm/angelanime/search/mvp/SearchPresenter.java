package com.tsdm.angelanime.search.mvp;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.search.SearchAdapter;
import com.tsdm.angelanime.utils.RxUtil;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Created by Mr.Quan on 2018/12/26.
 */

public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private String searchWord;
    private boolean isSlidingUpward = false;
    private int page = 1;
    private int allPage = 0;

    @Inject
    public SearchPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void onMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_cancel:
                view.back();
                break;
        }
    }

    @Override
    public void search(@Nullable String word, final WebResponseListener listener) {
        if (word != null)
            searchWord = word;
        addSubscribe(mDataManagerModel.getSearch(page, searchWord, listener)
                .map(new Function<Document, List<SearchList>>() {
                    @Override
                    public List<SearchList> apply(Document document) {
                        List<SearchList> data = new ArrayList<>();
                        try{
                            Elements els = document.getElementsByClass("movie-chrList");
                            Elements list = els.select("li");

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
                        }catch (Exception e){
                            listener.onParseError();
                        }
                        return data;
                    }
                }).compose(RxUtil.<List<SearchList>>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<List<SearchList>>(view) {

                    @Override
                    public void onNext(List<SearchList> searchLists) {

                        view.getSearchList(searchLists);
                    }
                }));
    }

    @Override
    public void onStateChanged(RecyclerView recyclerView, int newState, SearchAdapter searchAdapter
            ,WebResponseListener listener) {
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        //当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();

            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == (manager.getItemCount() - 1) && isSlidingUpward) {
                //加载更多
                searchAdapter.setLoadState(searchAdapter.LOADING);
                if (page < allPage) {
                    page += 1;
                    search(null,listener);
                }else {
                    searchAdapter.setLoadState(searchAdapter.LOADING_END);
                }

            }
        }
    }

    @Override
    public void onScrolled(boolean isSlidingUpward) {
        this.isSlidingUpward = isSlidingUpward;
    }

    @Override
    public void initPage() {
        page = 1;
        allPage = 0;
    }

    @Override
    public void insertHistory(String s) {
        mDataManagerModel.insertHistory(new History(s));
    }

    @Override
    public boolean onSearch(TextView textView, int i, Activity activity, List<History> mList) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            String key = textView.getText().toString().trim();
            if (TextUtils.isEmpty(key))
                return false;
            searchWord = textView.getText().toString();
            int count = 0;
            if (mList != null) {
                for (History data : mList) {
                    if (data.getHistory().equals(searchWord)) {
                        continue;
                    } else {
                        count += 1;
                    }
                }
            }
            if (mList == null || count == mList.size()) {
                insertHistory(searchWord);
                view.updateHistory(searchWord);
            }
            view.hideHistory();
            view.showSearchView();
            search(searchWord, (WebResponseListener) activity);
            Utils.showOrHideSoftKeyboard(activity);
            return true;
        }
        return false;
    }

    @Override
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_all:
                mDataManagerModel.deleteAllHistory();
                view.hideHistory();
                break;
            case R.id.tv_retry:
                view.retry();
                break;
        }
    }

    @Override
    public List<History> getHistory() {
        return mDataManagerModel.getHistory();
    }

    @Override
    public void deleteHistory(History data, boolean isNone) {
        if (!isNone) {
            mDataManagerModel.deleteHistory(data);
        } else {
            mDataManagerModel.deleteAllHistory();
        }

    }
}
