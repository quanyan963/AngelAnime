package com.tsdm.angelanime.di.component;

import android.app.Activity;


import com.tsdm.angelanime.di.module.FragmentModule;
import com.tsdm.angelanime.di.scope.FragmentScope;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

//    void inject(MoodLightFragment moodLightFragment);
}
