package com.tsdm.angelanime.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.main.mvp.MainContract;
import com.tsdm.angelanime.main.mvp.MainPresenter;
import com.tsdm.angelanime.utils.TitleUtils;
import com.tsdm.angelanime.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.bn_top)
    Banner bnTop;
    private List<TopEight> mData;

    @Override
    public void init() {
        TitleUtils.transparencyBar(this);
        initToolbar();
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
                        .putExtra(HREF_URL,mData.get(position).getHrefUrl()));
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
        return onExitActivity(keyCode,event);
    }
}
