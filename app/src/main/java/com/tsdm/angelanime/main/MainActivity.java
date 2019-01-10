package com.tsdm.angelanime.main;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.calendar.CalendarActivity;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.main.mvp.MainContract;
import com.tsdm.angelanime.main.mvp.MainPresenter;
import com.tsdm.angelanime.search.SearchActivity;
import com.tsdm.angelanime.utils.AlertUtils;
import com.tsdm.angelanime.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.icheny.transition.CySharedElementTransition;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View,
        MainFragment.CallBackValue, View.OnClickListener {

    @BindView(R.id.bn_top)
    Banner bnTop;
    @BindView(R.id.tab_top)
    TabLayout tabTop;
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    private List<TopEight> mData;
    private List<RecentlyDetail> detailList;
    private String[] titles;
    private List<Fragment> fragments = new ArrayList<>();
    private MainFragmentAdapter pagerAdapter;
    private List<List<ScheduleDetail>> mScheduleList;
    @Override
    public void init() {
        //TitleUtils.transparencyBar(this);
        initToolbar();
        setNavigationIcon(false);
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources()
                ,R.drawable.search,getTheme());
        vectorDrawableCompat.setTint(getResources().getColor(R.color.white));
        setRightImg(true,vectorDrawableCompat,this);
        detailList = presenter.getRecently();
        mScheduleList = presenter.getSchedule();
        titles = getResources().getStringArray(R.array.classify);
        for(int i=0;i<titles.length;i++){
            fragments.add(new MainFragment());
            tabTop.addTab(tabTop.newTab());
        }

        tabTop.setupWithViewPager(vpMain,false);
        pagerAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(pagerAdapter);

        for(int i=0;i<titles.length;i++){
            tabTop.getTabAt(i).setText(titles[i]);
        }

        initListener();
        mData = presenter.geTopEight();
        bnTop.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        bnTop.setImageLoader(new GlideImageLoader());
        bnTop.setImages(mData);
        bnTop.setDelayTime(3000);
        bnTop.setIndicatorGravity(BannerConfig.CENTER);

        bnTop.start();

    }



    private void initListener() {
        bnTop.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(MainActivity.this,
                        AnimationDetailActivity.class)
                        .putExtra(HREF_URL, mData.get(position).getHrefUrl()));
                //.putExtra(POSITION,0)
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        bnTop.startAutoPlay();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        bnTop.stopAutoPlay();
//    }

    @Override
    public void onLeftClick() {
        AlertUtils.showScheduleDialog(this,mScheduleList);
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
    public RecentlyDetail SendMessageValue(MainFragment fragment) {
        int position = pagerAdapter.getItemPosition(fragment);
        return detailList.get(position);
    }

    @Override
    public void onDestroy() {
        MyApplication.getImageLoader(this).clearMemoryCache();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        presenter.onClick(view);

    }

    @Override
    public void toSearchActivity(View v) {
        CySharedElementTransition.startActivity(new Intent(this,
                SearchActivity.class),this,v);
    }
}
