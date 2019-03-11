package com.tsdm.angelanime.di.module;



import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.model.db.DBHelper;
import com.tsdm.angelanime.model.db.DBHelperImpl;
import com.tsdm.angelanime.model.net.NetHelper;
import com.tsdm.angelanime.model.net.NetHelperImpl;
import com.tsdm.angelanime.model.operate.OperateHelper;
import com.tsdm.angelanime.model.operate.OperateHelperImpl;
import com.tsdm.angelanime.model.prefs.PreferencesHelper;
import com.tsdm.angelanime.model.prefs.PreferencesHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by KomoriWu
 * on 2017/9/15.
 */
@Module
public class AppModule {
    private MyApplication myApplication;

    public AppModule(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    @Singleton
    MyApplication provideMyApplication() {
        return myApplication;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(DBHelperImpl dbHelper) {
        return dbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(PreferencesHelperImpl preferencesHelper) {
        return preferencesHelper;
    }

    @Provides
    @Singleton
    NetHelper provideNetHelper(NetHelperImpl netHelper) {
        return netHelper;
    }

    @Provides
    @Singleton
    OperateHelper provideOperateHelper(OperateHelperImpl operateHelper) {
        return operateHelper;
    }

    @Provides
    @Singleton
    DataManagerModel provideDataManagerModel(DBHelperImpl dbHelper,
                                             PreferencesHelperImpl preferencesHelper,
                                             NetHelperImpl netHelper, OperateHelperImpl operateHelper) {
        return new DataManagerModel(dbHelper, preferencesHelper,netHelper,operateHelper);
    }
}
