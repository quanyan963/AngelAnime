package com.tsdm.angelanime.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    public TextView tvTitle;
    public boolean isBack = true;
    public boolean changeColor = true;
    private long mExitTime;
    private MyApplication mApplication;
    Toolbar toolbar;
    public Snackbar snackbar;
    private ImageView ivRight;

    public abstract void init();

    public abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (changeColor)
            com.samluys.statusbar.StatusBarUtils.transparencyBar(this,true);

        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mApplication = MyApplication.getInstance();
        addActivity();
        onCreateView();
        init();
    }

    public void onCreateView() {

    }


    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,StatusBarUtils.getStatusBarHeight(this),0,0);
        if (toolbar != null) {
            tvTitle = (TextView) findViewById(R.id.tv_title);
            ivRight = (ImageView) findViewById(R.id.iv_right);
            setSupportActionBar(toolbar);
            setTitle("");

            toolbar.setOnMenuItemClickListener(onMenuItemClick);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBack){
                        onBackPressed();
                    }else {
                        onLeftClick();
                    }

                }
            });
        }
    }

    public void onLeftClick() {
    }

    public void setNavigationIcon(boolean isBack) {
        this.isBack = isBack;
        if (isBack) {
            toolbar.setNavigationIcon(R.mipmap.back);
        } else {
            toolbar.setNavigationIcon(R.mipmap.schedule);
        }

    }

    public void setRightImg(boolean isShow, @Nullable Drawable drawable, View.OnClickListener listener) {
        if (isShow) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageDrawable(drawable);
            ivRight.setOnClickListener(listener);
        } else {
            ivRight.setVisibility(View.GONE);
        }

    }

    public Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            OnMenuItemClick(menuItem.getItemId());
            return true;
        }
    };

    public void OnMenuItemClick(int itemId) {

    }

    public boolean onExitActivity(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.exit_program_hint,
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                removeAllActivity();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void addActivity() {
        mApplication.addActivity(this);
    }


    public void removeAllActivity() {
        mApplication.removeAllActivity();
    }

    public void showSnackBar(View view, int str) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.gray));
        }
        snackbar.show();
    }

    public void showSnackBar(View view, int str, int btnStr, View.OnClickListener listener) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE).setAction(btnStr,listener);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.gray));
        }
        snackbar.show();
    }

    public void hideSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            snackbar = null;
        }
    }

    @Subscribe
    public void onEventMainThread(String str) {

    }
}
