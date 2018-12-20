package com.tsdm.angelanime.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.TableLayout;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.bean.event.ListData;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.main.mvp.MainContract;
import com.tsdm.angelanime.main.mvp.MainPresenter;
import com.tsdm.angelanime.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View, MainFragment.CallBackValue {

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
    @Override
    public void init() {
        //TitleUtils.transparencyBar(this);
        initToolbar();
        detailList = presenter.getRecently();
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
            }
        });
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
}
