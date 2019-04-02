package com.tsdm.angelanime.main.mvp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.bean.TopEight;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/11/10.
 */

public interface MainContract {
    interface View extends BaseView {

        void toSearchActivity(android.view.View v);

        void switchHome();

        void switchClassify();

        void toSettingView();

        void showView();

        void toDownloadView();
    }

    interface Presenter extends BasePresenter<View> {

        List<TopEight> geTopEight();

        List<RecentlyDetail> getRecently();

        void onClick(Fragment fragment, android.view.View view);

        List<List<ScheduleDetail>> getSchedule();

        void switchNavView(int id);

        boolean onNavigationSelected(MenuItem item, Context context);
    }
}
