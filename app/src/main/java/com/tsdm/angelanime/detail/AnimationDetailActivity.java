package com.tsdm.angelanime.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.event.AnimationDetail;
import com.tsdm.angelanime.detail.mvp.AnimationDetailContract;
import com.tsdm.angelanime.detail.mvp.AnimationDetailPresenter;
import com.tsdm.angelanime.introduction.IntroductionActivity;
import com.tsdm.angelanime.introduction.IntroductionFragment;
import com.tsdm.angelanime.utils.StatusBarUtils;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

import static com.tsdm.angelanime.utils.Constants.ERROR;
import static com.tsdm.angelanime.utils.Constants.HREF_URL;
import static com.tsdm.angelanime.utils.Constants.INTRODUCTION;
import static com.tsdm.angelanime.utils.Constants.POSITION;
import static com.tsdm.angelanime.utils.Constants.RETRY;

/**
 * Created by Mr.Quan on 2018/11/21.
 */

public class AnimationDetailActivity extends MvpBaseActivity<AnimationDetailPresenter>
        implements AnimationDetailContract.View, WebResponseListener,View.OnClickListener {

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
    private DetailPagerAdapter detAdapter;
    private List<String> titleList;
    private List<Fragment> viewList;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View
                .OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                switch (i){
                    case 0:
                        if (spPlayer.isIfCurrentIsFullscreen()){
                            getWindow().getDecorView().setSystemUiVisibility(View
                                    .SYSTEM_UI_FLAG_FULLSCREEN);
                        }else {
                            com.samluys.statusbar.StatusBarUtils.transparencyBar(
                                    AnimationDetailActivity.this,true);
                            getWindow().getDecorView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getWindow().getDecorView().setSystemUiVisibility(View
                                            .SYSTEM_UI_FLAG_FULLSCREEN);
                                }
                            },2000);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra(HREF_URL);
        //position = intent.getIntExtra(POSITION,0);
        initGSYView();
        titleList = new ArrayList<>();
        titleList.add(getString(R.string.introduction));
        titleList.add(getString(R.string.comment));

        viewList = new ArrayList<>();
        viewList.add(new IntroductionFragment());
        viewList.add(new IntroductionFragment());
        detAdapter = new DetailPagerAdapter(getSupportFragmentManager(),titleList,viewList);
        vpDetail.setAdapter(detAdapter);
        tlCard.setupWithViewPager(vpDetail);
        tlCard.setTabsFromPagerAdapter(detAdapter);
        presenter.getDetail(url, this);
        spPlayer.setRotateViewAuto(true);
        spPlayer.setLockLand(true);
        spPlayer.setNeedShowWifiTip(true);
        spPlayer.setSeekRatio(5);
        spPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (view.getY() < 20){
                            return false;
                        }else {
                            return true;
                        }
                    default:
                        return true;
                }
            }
        });
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
        if (isFirst){
            if (detail.getPlayList().size() != 0){
                position = detail.getPlayList().size()-1;
                presenter.getListUrl(detail.getPlayList().get(position), this);
            }else {
                EventBus.getDefault().post(detail);
            }
        }else {
            presenter.getListUrl(detail.getPlayList().get(position), this);
        }
        //spPlayer.setThumbImageView();
    }

    @Override
    public void getPlayUrl(String s) {
        spPlayer.setUp(s, true, detail.getTitle()
                + detail.getPlayListTitle().get(position));
        spPlayer.startPlayLogic();

    }

    @Override
    public void onComplete() {
        if (isFirst){
            EventBus.getDefault().post(detail);
            isFirst = false;
        }
        presenter.getPlayUrl(position,this);
    }

    @Override
    public void onError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new AnimationDetail(ERROR));
                showSnackBar(vpDetail,R.string.network_error,R.string.retry
                        ,AnimationDetailActivity.this);
            }
        });
    }

    @Override
    public void onParseError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new AnimationDetail(ERROR));
                showSnackBar(vpDetail,R.string.parse_error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }

    public void onResultFromFragment(int position){
        if (position == -1){
            GSYVideoManager.onPause();
            startActivity(new Intent(this, IntroductionActivity.class).
                    putExtra(INTRODUCTION,detail.getIntroduction()));
        }else {
            if (this.position != position){
                this.position = position;
                GSYVideoManager.releaseAllVideos();
                onComplete();
            }
            //presenter.getPlayUrl(detail.getPlayList().get(position), this);
        }
    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new AnimationDetail(RETRY));
        hideSnackBar();
        presenter.getDetail(url, this);
    }
}
