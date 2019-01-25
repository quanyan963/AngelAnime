package com.tsdm.angelanime.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.main.MainActivity;
import com.tsdm.angelanime.start.mvp.StartContract;
import com.tsdm.angelanime.start.mvp.StartPresenter;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import butterknife.BindView;
import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public class StartActivity extends MvpBaseActivity<StartPresenter> implements StartContract.View,
        WebResponseListener,View.OnClickListener {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.sl_shimmer)
    ShimmerLayout slShimmer;

    private boolean isAnimationFinished = false;
    private boolean isLoadingFinished = false;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        presenter.getHtml(this);
        Animation start = new AlphaAnimation(0.0f, 1.0f);
        start.setDuration(1000);
        //保持结束状态
        start.setFillAfter(true);
        slShimmer.startShimmerAnimation();

        start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationFinished = true;
                if (isLoadingFinished) {
                    endAnimation();

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivLogo.startAnimation(start);
        ivLogo.setImageResource(R.mipmap.logo_angel);

        //animationSet.addAnimation(start);
        //animationSet.addAnimation(end);
        //animationSet.setFillAfter(true);
    }

    private void endAnimation() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation end = new AlphaAnimation(1.0f, 0.0f);
                //end.setStartOffset(1500);
                end.setDuration(1000);
                end.setFillAfter(true);

                tvHint.startAnimation(end);
                ivLogo.startAnimation(end);
                end.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(StartActivity.this
                                , MainActivity.class));
                        StartActivity.this.finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void toMain() {
        isLoadingFinished = true;
        if (isAnimationFinished) {
            endAnimation();
        }

    }

    @Override
    public void onError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                slShimmer.stopShimmerAnimation();
                tvHint.setText(R.string.net_error);
                showSnackBar(ivLogo, R.string.network_error,R.string.retry
                        ,StartActivity.this);
            }
        });
    }

    @Override
    public void onParseError() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSnackBar(ivLogo, R.string.parse_error);
            }
        });
    }

    @Override
    public void onClick(View view) {
        tvHint.setText(R.string.init);
        hideSnackBar();
        slShimmer.recomputeViewAttributes(tvHint);
        slShimmer.startShimmerAnimation();
        presenter.getHtml(this);
    }
}
