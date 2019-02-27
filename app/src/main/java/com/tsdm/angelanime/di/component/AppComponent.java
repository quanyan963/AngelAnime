package com.tsdm.angelanime.di.component;



import android.content.Context;

import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.di.module.AppModule;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.model.db.DBHelper;
import com.tsdm.angelanime.model.net.NetHelper;
import com.tsdm.angelanime.model.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    MyApplication getContext();

    DataManagerModel getDataManagerModel();

    DBHelper getDbHelper();

    PreferencesHelper getPreferencesHelper();

    NetHelper getNetHelper();
}
