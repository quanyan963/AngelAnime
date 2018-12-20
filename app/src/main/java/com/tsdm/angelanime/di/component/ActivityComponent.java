package com.tsdm.angelanime.di.component;

import android.app.Activity;


import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.di.module.ActivityModule;
import com.tsdm.angelanime.di.scope.ActivityScope;
import com.tsdm.angelanime.introduction.IntroductionActivity;
import com.tsdm.angelanime.main.MainActivity;
import com.tsdm.angelanime.start.StartActivity;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(MainActivity mainActivity);
    void inject(StartActivity startActivity);
    void inject(AnimationDetailActivity detailActivity);
    void inject(IntroductionActivity introductionActivity);
}
