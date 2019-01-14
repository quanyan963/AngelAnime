package com.tsdm.angelanime.home.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.bean.TopEight;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/1/11.
 */

public interface HomeContract {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{
        List<TopEight> geTopEight();

        List<RecentlyDetail> getRecently();

        List<List<ScheduleDetail>> getSchedule();
    }
}
