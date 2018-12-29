package com.tsdm.angelanime.search;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
//    @BindView(R.id.iv_more)
//    ImageView ivMore;
//    @BindView(R.id.rl_top)
//    RelativeLayout rlTop;
//    @BindView(R.id.v_line_top)
//    View vLineTop;
    @BindView(R.id.rlv_history)
    RecyclerView rlvHistory;
    @BindView(R.id.v_line_bottom)
    View vLineBottom;
    @BindView(R.id.tv_delete_all)
    TextView tvDeleteAll;
    @BindView(R.id.rl_history_list)
    RelativeLayout rlHistoryList;
//    @BindView(R.id.rl_history)
//    RelativeLayout rlHistory;
    @BindView(R.id.rlv_search_list)
    RecyclerView rlvSearchList;

    private List<SearchList> data;
    private List<History> histories;
    private int height;
    private HistoryAdapter historyAdapter;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        rlvHistory.setHasFixedSize(true);
        rlvHistory.setLayoutManager(new FlowLayoutManager());
        rlvHistory.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.BOTH_SET,
                getResources().getDimensionPixelSize(R.dimen.dp_8_x),
                getResources().getColor(R.color.white)));
        historyAdapter = new HistoryAdapter(this);
        rlvHistory.setAdapter(historyAdapter);
        histories = presenter.getHistory();
        if (histories.size() == 0){
            rlHistoryList.setVisibility(View.INVISIBLE);
        }else {
            historyAdapter.setList(histories);
        }
        CySharedElementTransition.runEnterAnim(this, 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources()
//                        ,R.drawable.search,getTheme());
//                vectorDrawableCompat.setTint(getResources().getColor(R.color.low_grey));
//                ivRight.setImageDrawable(vectorDrawableCompat);
                rlSearch.setBackgroundResource(R.drawable.background_search);
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(250);
                rlSearch.startAnimation(animation);
                if (histories.size() != 0){
                    //getHeight();
                    rlHistoryList.post(new Runnable() {
                        @Override
                        public void run() {
                            height = rlHistoryList.getHeight();
                            HiddenAnimUtils.newInstance(SearchActivity.this,rlHistoryList,height).hidOrShow();
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

//    //获取历史纪录控件高度
//    private void getHeight() {
//        ViewTreeObserver vto = rlHistoryList.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                rlHistoryList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                height = rlHistoryList.getHeight();
//                HiddenAnimUtils.newInstance(SearchActivity.this,rlHistoryList,height).hidOrShow();
//            }
//        });
////        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
////        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
////        rlHistoryList.measure(w, h);
////        height = rlHistoryList.getMeasuredHeight();
//    }

    private void initListener() {
        tvDeleteAll.setOnClickListener(this);
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
                return presenter.onSearch(textView,i,SearchActivity.this);
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
        AlphaAnimation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(250);
        animation.setFillAfter(true);
        rlSearch.startAnimation(animation);
        if (rlHistoryList.getVisibility() == View.VISIBLE){
            HiddenAnimUtils.newInstance(this,rlHistoryList,height).hidOrShow();
        }
        CySharedElementTransition.runExitAnim(this, 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {


                //((View)toolbar.getMenu().getItem(0)).startAnimation(animation);
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
        if (data.size() != 0) {

        } else {

        }
    }

    //隐藏历史纪录
    @Override
    public void hideHistory() {
        //getHeight();
        HiddenAnimUtils.newInstance(this,rlHistoryList,height).hidOrShow();
        rlvHistory.removeAllViews();
        histories = null;
    }

    @Override
    public void updateHistory(String value) {
        if (histories == null || histories.size() == 0){
            histories = new ArrayList<>();
            histories.add(new History(value));
            historyAdapter.setList(histories);
        }else {
            //histories.add(new History(value));
            historyAdapter.addItem(value);
        }

        if (rlHistoryList.getVisibility() == View.GONE){
            //getHeight();
            HiddenAnimUtils.newInstance(this,rlHistoryList,height).hidOrShow();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onParseError() {

    }

    @Override
    public void onClick(View view) {
        presenter.onViewClick(view);
    }

    @Override
    public void onDeleteClick(History data, boolean isNone) {
        presenter.deleteHistory(data);
        if (isNone){
            HiddenAnimUtils.newInstance(this,rlHistoryList,height).hidOrShow();
        }
    }

    //history
    @Override
    public void onHistoryItemClick(History data) {
        presenter.search(data.getHistory(),this);
        hidKeyboard();
    }

    private void hidKeyboard(){
        if (Utils.isSoftShowing(this)) {
            Utils.showOrHideSoftKeyboard(this);
        }
    }
}
