package com.tsdm.angelanime.main.mvp;

import com.tsdm.angelanime.base.BasePresenter;
import com.tsdm.angelanime.base.BaseView;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public interface FragmentContract {
    interface View extends BaseView{

        void getRecentlyDetail(List<RecentlyDetail> recentlyDetails);
    }

    interface Presenter extends BasePresenter<View> {

        void refresh(WebResponseListener listener);
    }
}
