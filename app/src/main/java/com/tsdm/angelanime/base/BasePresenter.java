package com.tsdm.angelanime.base;

/**
 * Created by KomoriWu
 *  on 2017/9/18.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}