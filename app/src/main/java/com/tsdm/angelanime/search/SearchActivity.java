package com.tsdm.angelanime.search;

import android.animation.Animator;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.search.mvp.SearchContract;
import com.tsdm.angelanime.search.mvp.SearchPresenter;
import com.tsdm.angelanime.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.icheny.transition.CySharedElementTransition;

/**
 * Created by Mr.Quan on 2018/12/26.
 */

public class SearchActivity extends MvpBaseActivity<SearchPresenter> implements SearchContract.View {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
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
                AlphaAnimation animation = new AlphaAnimation(0f,1f);
                animation.setDuration(250);
                rlSearch.startAnimation(animation);
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

    private void initListener() {
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
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    String key = textView.getText().toString().trim();
                    if(TextUtils.isEmpty(key)){

                        return false;
                    }
                    presenter.search(textView.getText().toString());
                    Utils.showOrHideSoftKeyboard(SearchActivity.this);
                    return true;
                }
                return false;
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
        if (Utils.isSoftShowing(this)){
            Utils.showOrHideSoftKeyboard(this);
        }
        CySharedElementTransition.runExitAnim(this, 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                AlphaAnimation animation = new AlphaAnimation(1f,0f);
                animation.setDuration(250);
                animation.setFillAfter(true);
                rlSearch.startAnimation(animation);
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
    public void onDestroy() {
        super.onDestroy();
    }
}
