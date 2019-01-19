package com.tsdm.angelanime.search;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.search.mvp.SearchContract;
import com.tsdm.angelanime.search.mvp.SearchPresenter;
import com.tsdm.angelanime.utils.HiddenAnimUtils;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.FlowLayoutManager;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.icheny.transition.CySharedElementTransition;

/**
 * Created by Mr.Quan on 2018/12/26.
 */

public class SearchActivity extends MvpBaseActivity<SearchPresenter> implements SearchContract.View,
        WebResponseListener, View.OnClickListener, HistoryAdapter.HistoryClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.rlv_history)
    RecyclerView rlvHistory;
    @BindView(R.id.v_line_bottom)
    View vLineBottom;
    @BindView(R.id.tv_delete_all)
    TextView tvDeleteAll;
    @BindView(R.id.rl_history_list)
    RelativeLayout rlHistoryList;
    @BindView(R.id.rlv_search_list)
    RecyclerView rlvSearchList;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_retry)
    TextView tvRetry;
    @BindView(R.id.rl_net_error)
    RelativeLayout rlNetError;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.rl_data)
    RelativeLayout rlData;
    @BindView(R.id.tv_error_msg)
    TextView tvErrorMsg;
    @BindView(R.id.rl_data_error)
    RelativeLayout rlDataError;

    private List<SearchList> data;
    private List<History> histories;
    private int height;
    private HistoryAdapter historyAdapter;
    private SearchAdapter searchAdapter;
    private View vLoading;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        vLoading = findViewById(R.id.v_loading);
        initHistory();
        initSearch();
        CySharedElementTransition.runEnterAnim(this, 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ivRight.setImageDrawable(Utils.changeSVGColor(R.drawable.search, R.color.low_grey
                        , SearchActivity.this));
                rlSearch.setBackgroundResource(R.drawable.background_search);
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(250);
                rlSearch.startAnimation(animation);
                if (histories.size() != 0) {
                    //getHeight();
                    rlHistoryList.post(new Runnable() {
                        @Override
                        public void run() {
                            height = rlHistoryList.getHeight();
                            showHistory();
                        }
                    });

                }
                setSupportActionBar(toolbar);
                initListener();
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
                Utils.showOrHideSoftKeyboard(SearchActivity.this);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void initSearch() {
        rlvSearchList.setHasFixedSize(true);
        rlvSearchList.setLayoutManager(new GridLayoutManager(this, 2));
        rlvSearchList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.BOTH_SET,
                getResources().getDimensionPixelSize(R.dimen.dp_16_x),
                getResources().getColor(R.color.light_grey)));
        searchAdapter = new SearchAdapter(this);
        rlvSearchList.setAdapter(searchAdapter);
        rlvSearchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.onStateChanged(recyclerView, newState, searchAdapter, SearchActivity.this);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //向左滑动的时候dx是大于0的，反方向滑动则小于0
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                presenter.onScrolled(dy > 0);
            }
        });
    }

    private void initHistory() {
        rlvHistory.setHasFixedSize(true);
        rlvHistory.setLayoutManager(new FlowLayoutManager());
        rlvHistory.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.BOTH_SET,
                getResources().getDimensionPixelSize(R.dimen.dp_8_x),
                getResources().getColor(R.color.white)));
        historyAdapter = new HistoryAdapter(this);
        rlvHistory.setAdapter(historyAdapter);
        histories = presenter.getHistory();
        if (histories.size() == 0) {
            rlHistoryList.setVisibility(View.INVISIBLE);
        } else {
            historyAdapter.setList(histories);
        }
    }

    //获取历史纪录控件高度
    private void getHeight() {
        ViewTreeObserver vto = rlHistoryList.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlHistoryList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (rlHistoryList.getHeight() > 10) {
                    height = rlHistoryList.getHeight();
                }

            }
        });
//        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        rlHistoryList.measure(w, h);
//        height = rlHistoryList.getMeasuredHeight();
    }

    private void initListener() {
        tvDeleteAll.setOnClickListener(this);
        tvRetry.setOnClickListener(this);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.onMenuClick(item);
                return true;
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                presenter.initPage();
                if (data != null) {
                    rlvSearchList.removeAllViews();
                    searchAdapter.removeAll();
                    data = null;
                }

                return presenter.onSearch(textView, i, SearchActivity.this, histories);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (Utils.isSoftShowing(this)) {
            Utils.showOrHideSoftKeyboard(this);
        }
        if (rlData.getVisibility() == View.VISIBLE) {
            //getHeight();
            showHistory();
            presenter.initPage();
            rlvSearchList.removeAllViews();
            searchAdapter.removeAll();
            data = null;
            rlData.setVisibility(View.GONE);

        } else {
            AlphaAnimation animation = new AlphaAnimation(1f, 0f);
            animation.setDuration(250);
            animation.setFillAfter(true);
            rlSearch.startAnimation(animation);
            hideHistory();
            CySharedElementTransition.runExitAnim(this, 300, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void getSearchList(List<SearchList> searchLists) {
        data = searchLists;
        //searchAdapter.setLoadState(searchAdapter.LOADING_COMPLETE);
        if (data.size() != 0) {
            vLoading.setVisibility(View.GONE);
            searchAdapter.setList(searchLists);
            rlvSearchList.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            rlDataError.setVisibility(View.VISIBLE);
            tvErrorMsg.setText(R.string.no_data);

        }
    }

    //隐藏历史纪录
    @Override
    public void hideHistory() {
        //getHeight();
        if (rlHistoryList.getVisibility() == View.VISIBLE)
            HiddenAnimUtils.newInstance(this, rlHistoryList, height).hidOrShow();
    }

    public void showHistory() {
        if (rlHistoryList.getVisibility() == View.GONE
                || rlHistoryList.getVisibility() == View.INVISIBLE)
            HiddenAnimUtils.newInstance(this, rlHistoryList, height).hidOrShow();
    }

    @Override
    public void updateHistory(String value) {
        if (histories == null || histories.size() == 0) {
            histories = new ArrayList<>();
            histories.add(new History(value));
            historyAdapter.setList(histories);
            rlHistoryList.setVisibility(View.VISIBLE);
        } else {
            histories.add(new History(value));
            historyAdapter.addItem(value);
        }
        getHeight();
        showHistory();

    }

    @Override
    public void showSearchView() {
        rlData.setVisibility(View.VISIBLE);
        vLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        rlNetError.setVisibility(View.GONE);
        rlvSearchList.setVisibility(View.GONE);
    }

    @Override
    public void retry() {
        rlNetError.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
        presenter.search(null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //network
    @Override
    public void onError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rlvSearchList.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                rlNetError.setVisibility(View.VISIBLE);
            }
        });
    }

    //network
    @Override
    public void onParseError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbLoading.setVisibility(View.GONE);
                rlDataError.setVisibility(View.VISIBLE);
                tvErrorMsg.setText(R.string.parse_error);
            }
        });
    }

    //itemClick
    @Override
    public void onClick(View view) {
        rlvHistory.removeAllViews();
        histories = null;
        presenter.onViewClick(view);
    }

    @Override
    public void onDeleteClick(History data, boolean isNone) {
        presenter.deleteHistory(data, isNone);
        if (isNone) {
            hideHistory();
        }
    }

    //history
    @Override
    public void onHistoryItemClick(History data) {
        hideHistory();
        showSearchView();
        hidKeyboard();
        presenter.search(data.getHistory(), this);
    }

    private void hidKeyboard() {
        if (Utils.isSoftShowing(this)) {
            Utils.showOrHideSoftKeyboard(this);
        }
    }
}
