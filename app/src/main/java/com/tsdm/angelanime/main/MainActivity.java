package com.tsdm.angelanime.main;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.ScheduleDetail;
import com.tsdm.angelanime.classify.ClassifyFragment;
import com.tsdm.angelanime.download.DownloadActivity;
import com.tsdm.angelanime.home.HomeFragment;
import com.tsdm.angelanime.main.mvp.MainContract;
import com.tsdm.angelanime.main.mvp.MainPresenter;
import com.tsdm.angelanime.search.SearchActivity;
import com.tsdm.angelanime.service.DownloadService;
import com.tsdm.angelanime.utils.AlertUtils;
import com.tsdm.angelanime.utils.Utils;

import java.util.List;

import butterknife.BindView;
import cn.icheny.transition.CySharedElementTransition;

import static com.tsdm.angelanime.utils.Constants.NOTIFICATION_ID;
import static com.tsdm.angelanime.utils.Constants.ON_DESTROY;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {//MainFragment.CallBackValue,
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_classify)
    RadioButton rbClassify;
    @BindView(R.id.rg_navigation)
    RadioGroup rgNavigation;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.dl_main)
    DrawerLayout dlMain;

    private List<List<ScheduleDetail>> mScheduleList;

    private Fragment mCurrentFragment;
    private HomeFragment mHomeFragment;
    private ClassifyFragment mClassifyFragment;
    private DownloadService.MyBroadcastReceiver receiver;
    private Intent destroyIntent;

    @Override
    public void init() {
        receiver = new DownloadService.MyBroadcastReceiver();
        IntentFilter destroyFilter = new IntentFilter();
        destroyFilter.addAction(ON_DESTROY);
        registerReceiver(receiver, destroyFilter);
        destroyIntent = new Intent(ON_DESTROY);

        initToolbar();
        setNavigationIcon(false);
        mScheduleList = presenter.getSchedule();
        initListener();
        mCurrentFragment = new ClassifyFragment();
        rgNavigation.check(R.id.rb_home);
    }


    private void initListener() {
        rgNavigation.setOnCheckedChangeListener(this);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return presenter.onNavigationSelected(item,MainActivity.this);
            }
        });
    }

    //ToolbarLeft
    @Override
    public void onLeftClick() {
        AlertUtils.showScheduleDialog(this, mScheduleList);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return onExitActivity(keyCode, event);
    }

    @Override
    public void onDestroy() {
        //MyApplication.getImageLoader(this).clearMemoryCache();
        if (presenter.geDownloadInfo()){
            destroyIntent.putExtra(NOTIFICATION_ID,-1);
        }else {
            destroyIntent.putExtra(NOTIFICATION_ID,0);
        }
        sendBroadcast(destroyIntent);

        try {
            Thread.sleep(500);
            unregisterReceiver(receiver);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        presenter.onClick(mCurrentFragment, view);


    }

    @Override
    public void toSearchActivity(View v) {
        CySharedElementTransition.startActivity(new Intent(this,
                SearchActivity.class), this, v);
    }

    @Override
    public void switchHome() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        switchContent(mCurrentFragment, mHomeFragment);
        rbHome.setCompoundDrawablesWithIntrinsicBounds(null, Utils.changeSVGColor(R.drawable.home
                , R.color.colorAccent, this),
                null, null);
        rbClassify.setCompoundDrawablesWithIntrinsicBounds(null, Utils.changeSVGColor(
                R.drawable.classify, R.color.low_grey, this),
                null, null);
    }

    @Override
    public void switchClassify() {
        if (mClassifyFragment == null) {
            mClassifyFragment = new ClassifyFragment();
        }
        switchContent(mCurrentFragment, mClassifyFragment);
        rbClassify.setCompoundDrawablesWithIntrinsicBounds(null, Utils.changeSVGColor(
                R.drawable.classify, R.color.colorAccent, this),
                null, null);
        rbHome.setCompoundDrawablesWithIntrinsicBounds(null, Utils.changeSVGColor(
                R.drawable.home, R.color.low_grey, this),
                null, null);

    }

    @Override
    public void toSettingView() {
        dlMain.openDrawer(navView);
    }

    @Override
    public void showView() {
        Toast.makeText(this, R.string.delete_complete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toDownloadView() {
        dlMain.closeDrawer(navView);
        startActivity(new Intent(this, DownloadActivity.class));
    }

    private void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            if (mCurrentFragment instanceof ClassifyFragment) {
                setRightImg(true, Utils.changeSVGColor(R.drawable.setting, R.color.white
                        , this), this);
            } else {
                setRightImg(true, Utils.changeSVGColor(R.drawable.search, R.color.white
                        , this), this);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(from).add(R.id.fl_main, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commit();
            }
        }
    }

    //RadioGroup
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        presenter.switchNavView(i);
    }
}
