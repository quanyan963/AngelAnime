package com.tsdm.angelanime.di.component;

import android.app.Activity;


import com.tsdm.angelanime.classify.ClassifyDetailFragment;
import com.tsdm.angelanime.classify.ClassifyFragment;
import com.tsdm.angelanime.comment.CommentFragment;
import com.tsdm.angelanime.di.module.FragmentModule;
import com.tsdm.angelanime.di.scope.FragmentScope;
import com.tsdm.angelanime.home.HomeFragment;
import com.tsdm.angelanime.introduction.IntroductionFragment;
import com.tsdm.angelanime.main.MainFragment;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    void inject(IntroductionFragment introductionFragment);
    void inject(CommentFragment commentFragment);
    void inject(MainFragment mainFragment);
    void inject(HomeFragment homeFragment);
    void inject(ClassifyFragment classifyFragment);
    void inject(ClassifyDetailFragment detailFragment);
}
