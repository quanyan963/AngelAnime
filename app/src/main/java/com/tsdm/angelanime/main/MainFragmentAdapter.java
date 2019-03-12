package com.tsdm.angelanime.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int position = -1;
        for (int i = 0; i < fragmentList.size(); i++){
            if (fragmentList.get(i) == object){
                position =  i;
                break;
            }
        }
        return position;
    }
}
