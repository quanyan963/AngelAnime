package com.tsdm.angelanime.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.VideoState;
import com.tsdm.angelanime.bean.event.AnimationDetail;
import com.tsdm.angelanime.bean.event.Comment;
import com.tsdm.angelanime.comment.CommentFragment;
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
import static com.tsdm.angelanime.utils.Constants.INTRO;
import static com.tsdm.angelanime.utils.Constants.INTRODUCTION;
import static com.tsdm.angelanime.utils.Constants.OK;
import static com.tsdm.angelanime.utils.Constants.PLAY_URL;
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
    private AnimationDetail detail;
    private int position = 0;
    private DetailPagerAdapter detAdapter;
    private List<String> titleList;
    private List<Fragment> viewList;
    private boolean isFirst = true;
    private long playPosition;
    private boolean seekTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeColor = false;
        StatusBarUtils.setWindowStatusBarColor(this,R.color.black);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        spPlayer.setLayoutParams(new ConstraintLayout.LayoutParams(width, (int) (width/16f*9)+2));
        Intent intent = getIntent();
        url = intent.getStringExtra(HREF_URL);
        //position = intent.getIntExtra(POSITION,0);
        initGSYView();
        titleList = new ArrayList<>();
        titleList.add(getString(R.string.introduction));
        titleList.add(getString(R.string.comment));

        viewList = new ArrayList<>();
        viewList.add(new IntroductionFragment());
        viewList.add(new CommentFragment());
        detAdapter = new DetailPagerAdapter(getSupportFragmentManager(),titleList,viewList);
        vpDetail.setAdapter(detAdapter);
        tlCard.setupWithViewPager(vpDetail);
        tlCard.setTabsFromPagerAdapter(detAdapter);
        presenter.getDetail(url, this,this,new MyJavaScriptInterface(),INTRO);
        spPlayer.setRotateViewAuto(true);
        spPlayer.setLockLand(true);
        spPlayer.setSeekRatio(5);
        spPlayer.setNeedShowWifiTip(true);
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

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 在这里处理html源码
            presenter.getData(html,AnimationDetailActivity.this);
        }
    }

    class MyPlayListScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 在这里处理html源码
            presenter.getUrl(html,AnimationDetailActivity.this);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void getDetail(final AnimationDetail animationDetail) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (animationDetail.getRequestStatue() == OK){
                    hideSnackBar();
                    detail = animationDetail;
                    if (isFirst){
                        if (detail.getPlayList() != null){
                            position = detail.getPlayList().size()-1;

                            presenter.getListUrl(detail.getPlayList().get(position),
                                    AnimationDetailActivity.this,
                                    AnimationDetailActivity.this,
                                    new MyPlayListScriptInterface(),PLAY_URL);

                        }else {
                            EventBus.getDefault().post(detail);
                            EventBus.getDefault().post(new Comment(detail.getVideoNum(),OK));
                        }
                    }else {
                        presenter.getListUrl(detail.getPlayList().get(position),
                                AnimationDetailActivity.this,
                                AnimationDetailActivity.this,
                                new MyPlayListScriptInterface(),PLAY_URL);
                    }
                }
            }
        });
        //spPlayer.setThumbImageView();
    }

    @Override
    public void getPlayUrl(String s) {
        spPlayer.setUp(s, true, detail.getTitle()
                + detail.getPlayListTitle().get(position));
        spPlayer.startPlayLogic();
        seekTo = true;
        spPlayer.setGSYVideoProgressListener(new GSYVideoProgressListener() {
            @Override
            public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                if (playPosition != 0){
                    if (seekTo){
                        GSYVideoManager.instance().seekTo(playPosition);
                        spPlayer.setGSYVideoProgressListener(null);
                    }
                    seekTo = false;
                } else {
                    spPlayer.setGSYVideoProgressListener(null);
                }

            }
        });


    }

    @Override
    public void onComplete() {
        if (isFirst){
            EventBus.getDefault().post(detail);
            EventBus.getDefault().post(new Comment(detail.getVideoNum(),OK));
            isFirst = false;
        }
        //presenter.getPlayUrl(position,this);
    }

    @Override
    public void onError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new AnimationDetail(ERROR));
                EventBus.getDefault().post(new Comment(ERROR));
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
                EventBus.getDefault().post(new Comment(ERROR));
                showSnackBar(vpDetail,R.string.parse_error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }else if (tlCard.getSelectedTabPosition() == 1){
            if (!((CommentFragment)detAdapter.getItem(tlCard.getSelectedTabPosition())).hidReply()){
                return;
            }
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
        if (detail != null){
            presenter.insertVideoState(new VideoState(detail.getTitle(),position,
                    GSYVideoManager.instance().getCurrentPosition()));
            GSYVideoManager.instance().getCurrentPosition();
            GSYVideoManager.onPause();
        }
    }

    @Override
    public void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }

    public void onResultFromFragment(int position, Long playPosition){
        if (position == -1){
            GSYVideoManager.onPause();
            startActivity(new Intent(this, IntroductionActivity.class).
                    putExtra(INTRODUCTION,detail.getIntroduction()));
        }else {
            this.playPosition = playPosition;
            if (this.position != position){
                this.position = position;
                GSYVideoManager.releaseAllVideos();
                //onComplete();
                presenter.getListUrl(detail.getPlayList().get(position),
                        AnimationDetailActivity.this,
                        AnimationDetailActivity.this,
                        new MyPlayListScriptInterface(),PLAY_URL);
            }
            //presenter.getPlayUrl(detail.getPlayList().get(position), this);
        }
    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new AnimationDetail(RETRY));
        EventBus.getDefault().post(new Comment(RETRY));
        hideSnackBar();
        presenter.getDetail(url, this,this,new MyJavaScriptInterface(),INTRO);
    }
}
