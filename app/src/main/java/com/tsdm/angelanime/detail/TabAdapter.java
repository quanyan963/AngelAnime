package com.tsdm.angelanime.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Quan on 2018/12/7.
 */

public class TabAdapter extends PagerAdapter {
    private List<String> titleList;
    private List<View> viewList;

    public TabAdapter(List<String> titleList, List<View> viewList) {
        this.titleList = titleList;
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList == null? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
