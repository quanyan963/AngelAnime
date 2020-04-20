package com.tsdm.angelanime.home;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.home.mvp.HomeContract;
import com.tsdm.angelanime.home.mvp.HomePresenter;
import com.tsdm.angelanime.main.MainFragment;
import com.tsdm.angelanime.main.MainFragmentAdapter;
import com.tsdm.angelanime.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

/**
 * Created by Mr.Quan on 2019/1/11.
 */

public class HomeFragment extends MvpBaseFragment<HomePresenter> implements HomeContract.View ,
        MainFragment.CallBackValue{
    @BindView(R.id.bn_top)
    Banner bnTop;
    @BindView(R.id.tab_top)
    TabLayout tabTop;
    @BindView(R.id.vp_main)
    ViewPager vpMain;

    private List<TopEight> mData;
    private List<RecentlyDetail> detailList;
    private String[] titles;
    private List<Fragment> fragments;
    private MainFragmentAdapter pagerAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void init() {
        fragments = new ArrayList<>();
        detailList = presenter.getRecently();
        titles = getResources().getStringArray(R.array.classify);
        for(int i=0;i<titles.length;i++){
            MainFragment fragment = new MainFragment();
            fragment.setListener(this);
            fragments.add(fragment);
            tabTop.addTab(tabTop.newTab());

        }

        tabTop.setupWithViewPager(vpMain,false);
        pagerAdapter = new MainFragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
        vpMain.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
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
                startActivity(new Intent(getContext(),
                        AnimationDetailActivity.class)
                        .putExtra(HREF_URL, mData.get(position).getHrefUrl()));
                //.putExtra(POSITION,0)
            }
        });
    }

    @Override
    public RecentlyDetail SendMessageValue(MainFragment fragment) {
        int position = pagerAdapter.getItemPosition(fragment);
        return detailList.get(position);
    }

    @Override
    public int sendPosition(MainFragment fragment) {
        return pagerAdapter.getItemPosition(fragment);
    }
}
