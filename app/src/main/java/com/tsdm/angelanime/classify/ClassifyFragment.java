package com.tsdm.angelanime.classify;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.ClassifyDetail;
import com.tsdm.angelanime.classify.mvp.ClassifyContract;
import com.tsdm.angelanime.classify.mvp.ClassifyPresenter;
import com.tsdm.angelanime.main.MainFragment;
import com.tsdm.angelanime.main.MainFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Quan on 2019/1/11.
 */

public class ClassifyFragment extends MvpBaseFragment<ClassifyPresenter> implements
        ClassifyContract.View, ClassifyDetailFragment.CallBackUrl {

    @BindView(R.id.tl_classify)
    TabLayout tlClassify;
    @BindView(R.id.vp_classify)
    ViewPager vpClassify;

    private List<ClassifyDetail> mData;
    private List<Fragment> fragments;
    private MainFragmentAdapter pagerAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classify, null);
    }

    @Override
    public void init() {
        fragments = new ArrayList<>();
        mData = presenter.getClassify();

        for(int i=0;i<mData.size();i++){
            ClassifyDetailFragment fragment = new ClassifyDetailFragment();
            fragment.setListener(this);
            fragments.add(fragment);
            tlClassify.addTab(tlClassify.newTab());

        }

        tlClassify.setupWithViewPager(vpClassify,false);
        pagerAdapter = new MainFragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
        vpClassify.setAdapter(pagerAdapter);

        for(int i=0;i<mData.size();i++){
            tlClassify.getTabAt(i).setText(mData.get(i).getTitle());
        }
    }

    @Override
    public String SendMessageValue(ClassifyDetailFragment fragment) {
        int position = pagerAdapter.getItemPosition(fragment);
        return mData.get(position).getUrl();
    }
}
