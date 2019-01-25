package com.tsdm.angelanime.main;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.classify.ClassifyFragment;
import com.tsdm.angelanime.home.HomeFragment;
import com.tsdm.angelanime.main.mvp.MainContract;
import com.tsdm.angelanime.main.mvp.MainPresenter;
import com.tsdm.angelanime.search.SearchActivity;
import com.tsdm.angelanime.utils.AlertUtils;
import com.tsdm.angelanime.utils.Utils;

import java.util.List;

import butterknife.BindView;
import cn.icheny.transition.CySharedElementTransition;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {//MainFragment.CallBackValue,
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_classify)
    RadioButton rbClassify;
    @BindView(R.id.rg_navigation)
    RadioGroup rgNavigation;

    private List<List<ScheduleDetail>> mScheduleList;

    private Fragment mCurrentFragment;
    private HomeFragment mHomeFragment;
    private ClassifyFragment mClassifyFragment;
    @Override
    public void init() {
        initToolbar();
        setNavigationIcon(false);

        setRightImg(true, Utils.changeSVGColor(R.drawable.search,R.color.white
                ,this), this);
        mScheduleList = presenter.getSchedule();
        initListener();
        mCurrentFragment = new ClassifyFragment();
        rgNavigation.check(R.id.rb_home);

    }




    private void initListener() {
        rgNavigation.setOnCheckedChangeListener(this);
    }

    //ToolbarLeft
    @Override
    public void onLeftClick() {
        AlertUtils.showScheduleDialog(this, mScheduleList);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return onExitActivity(keyCode, event);
    }

    @Override
    public void onDestroy() {
        //MyApplication.getImageLoader(this).clearMemoryCache();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        presenter.onClick(view);

    }

    @Override
    public void toSearchActivity(View v) {
        CySharedElementTransition.startActivity(new Intent(this,
                SearchActivity.class), this, v);
    }

    @Override
    public void switchHome() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        switchContent(mCurrentFragment, mHomeFragment);
        rbHome.setCompoundDrawablesWithIntrinsicBounds(null,Utils.changeSVGColor(R.drawable.home
                ,R.color.colorAccent,this),
                null,null);
        rbClassify.setCompoundDrawablesWithIntrinsicBounds(null,Utils.changeSVGColor(
                R.drawable.classify,R.color.low_grey,this),
                null,null);
    }

    @Override
    public void switchClassify() {
        if (mClassifyFragment == null) {
            mClassifyFragment = new ClassifyFragment();
        }
        switchContent(mCurrentFragment, mClassifyFragment);
        rbClassify.setCompoundDrawablesWithIntrinsicBounds(null,Utils.changeSVGColor(
                R.drawable.classify,R.color.colorAccent,this),
                null,null);
        rbHome.setCompoundDrawablesWithIntrinsicBounds(null,Utils.changeSVGColor(
                R.drawable.home,R.color.low_grey,this),
                null,null);

    }

    private void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(from).add(R.id.fl_main, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commit();
            }
        }
    }

    //RadioGroup
    @Override
    public void onCheckedChanged(RadioGroup radioGroup,@IdRes int i) {
        presenter.switchNavView(i);
    }
}
