package com.tsdm.angelanime.model.db;



import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.bean.History;
import com.tsdm.angelanime.bean.RecentlyData;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.bean.dao.DaoMaster;
import com.tsdm.angelanime.bean.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class DBHelperImpl implements DBHelper {
    private static final String DB_NAME = "tsdm.db";
    private DaoSession mDaoSession;

    @Inject
    public DBHelperImpl() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(MyApplication.
                getInstance(), DB_NAME);
        Database db = openHelper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }


    @Override
    public void insertTopEight(List<TopEight> value) {
        mDaoSession.getTopEightDao().deleteAll();
        mDaoSession.getTopEightDao().insertInTx(value);
    }

    @Override
    public List<TopEight> getTopEight() {
        return mDaoSession.getTopEightDao().loadAll();
    }

    @Override
    public void insertRecently(RecentlyData data) {
        mDaoSession.getRecentlyDataDao().deleteAll();
        mDaoSession.getRecentlyDataDao().insert(data);
    }

    @Override
    public RecentlyData getRecently() {
        return mDaoSession.getRecentlyDataDao().loadAll().get(0);
    }

    @Override
    public void insertHistory(History data) {
        mDaoSession.getHistoryDao().insert(data);
    }

    @Override
    public List<History> getHistory() {
        return mDaoSession.getHistoryDao().loadAll();
    }

    @Override
    public void deleteAllHistory() {
        mDaoSession.getHistoryDao().deleteAll();

    }

    @Override
    public void deleteHistory(int position) {
        History history = mDaoSession.getHistoryDao().loadAll().get(position);
        mDaoSession.getHistoryDao().delete(history);
    }
}
