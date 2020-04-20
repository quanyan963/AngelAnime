package com.tsdm.angelanime.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;


import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.di.component.DaggerFragmentComponent;
import com.tsdm.angelanime.di.component.FragmentComponent;
import com.tsdm.angelanime.di.module.FragmentModule;

import javax.inject.Inject;

/**
 * Created by KomoriWu
 *  on 2017/9/18.
 */

public abstract class MvpBaseFragment<T extends BasePresenter> extends BaseFragment
        implements BaseView {
    @Inject
    public T presenter;

    protected abstract void initInject();

    public FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    private FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        if (presenter != null) {
            presenter.attachView(this);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }

}
