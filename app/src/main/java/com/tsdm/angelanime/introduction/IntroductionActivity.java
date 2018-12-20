package com.tsdm.angelanime.introduction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.introduction.mvp.IntroductionAContract;
import com.tsdm.angelanime.introduction.mvp.IntroductionAPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tsdm.angelanime.utils.Constants.INTRODUCTION;

/**
 * Created by Mr.Quan on 2018/12/14.
 */

public class IntroductionActivity extends MvpBaseActivity<IntroductionAPresenter> implements IntroductionAContract.View {
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;

    private Intent intent;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        tvTitle.setText(R.string.introduction);
        setNavigationIcon(true);
        intent = getIntent();
        String introduction = intent.getStringExtra(INTRODUCTION);
        tvIntroduction.setText(introduction);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_introduction;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
