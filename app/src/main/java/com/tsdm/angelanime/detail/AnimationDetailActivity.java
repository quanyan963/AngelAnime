package com.tsdm.angelanime.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.AnimationDetail;
import com.tsdm.angelanime.detail.mvp.AnimationDetailContract;
import com.tsdm.angelanime.detail.mvp.AnimationDetailPresenter;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetailActivity extends MvpBaseActivity<AnimationDetailPresenter>
        implements AnimationDetailContract.View, WebResponseListener {

    @BindView(R.id.sp_player)
    StandardGSYVideoPlayer spPlayer;
    @BindView(R.id.tl_card)
    TabLayout tlCard;
    @BindView(R.id.vp_detail)
    ViewPager vpDetail;
    private String url;
    private String playUrl;
    private ImageView img;//封面
    private AnimationDetail detail;
    private int position = 0;
    private TabAdapter tabAdapter;
    private List<String> titleList;
    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra(HREF_URL);

        presenter.getDetail(url, this);
        initGSYView();
        titleList = new ArrayList<>();
        titleList.add(getString(R.string.introduction));
        titleList.add("简介");
        tabAdapter = new TabAdapter(titleList,viewList);
        tlCard.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpDetail.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpDetail.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlCard));
        tlCard.setTabsFromPagerAdapter(tabAdapter);
    }


    private void initGSYView() {
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);//exo缓存模式，支持m3u8，只支持exo
        spPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public final void onClick(View it) {
                spPlayer.startWindowFullscreen(AnimationDetailActivity.this, false,
                        true);
            }
        });
        spPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void getDetail(AnimationDetail animationDetail) {
        detail = animationDetail;
        presenter.getPlayUrl(detail.getPlayList().get(position), this);
        //spPlayer.setThumbImageView();
    }

    @Override
    public void getPlayUrl(String s) {
        spPlayer.setUp(s, true, detail.getTitle() + detail.getPlayListTitle().get(position));
        spPlayer.startPlayLogic();

    }

    @Override
    public void onError() {

    }

    @Override
    public void onParseError() {

    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }
}
