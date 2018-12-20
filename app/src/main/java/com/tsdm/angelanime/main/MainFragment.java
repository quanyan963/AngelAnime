package com.tsdm.angelanime.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.main.mvp.FragmentContract;
import com.tsdm.angelanime.main.mvp.FragmentPresenter;
import com.tsdm.angelanime.widget.DividerItemDecoration;

import butterknife.BindView;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public class MainFragment extends MvpBaseFragment<FragmentPresenter> implements FragmentContract.View {
    @BindView(R.id.rlv_animation_list)
    RecyclerView rlvAnimationList;

    private FRecycleAdapter adapter;
    private CallBackValue callBackValue;
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBackValue = (CallBackValue) context;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void init() {
        rlvAnimationList.setHasFixedSize(true);
        rlvAnimationList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rlvAnimationList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.BOTH_SET,
                getActivity().getResources().getDimensionPixelSize(R.dimen.dp_16_x),
                getActivity().getResources().getColor(R.color.white)));
        //rlvAnimationList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FRecycleAdapter(callBackValue.SendMessageValue(this),getContext());
        rlvAnimationList.setAdapter(adapter);

    }

    public interface CallBackValue{
        RecentlyDetail SendMessageValue(MainFragment fragment);
    }
}
