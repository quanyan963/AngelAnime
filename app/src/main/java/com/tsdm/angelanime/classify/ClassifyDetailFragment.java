package com.tsdm.angelanime.classify;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.classify.mvp.ClassifyDContract;
import com.tsdm.angelanime.classify.mvp.ClassifyDPresenter;
import com.tsdm.angelanime.main.MainActivity;
import com.tsdm.angelanime.search.SearchAdapter;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Mr.Quan on 2019/1/12.
 */

public class ClassifyDetailFragment extends MvpBaseFragment<ClassifyDPresenter> implements
        ClassifyDContract.View,WebResponseListener {
    @BindView(R.id.rlv_animation_list)
    RecyclerView rlvAnimationList;

    private CallBackUrl listener;
    private String url;
    private SearchAdapter adapter;
    private List<SearchList> detail;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
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
                getActivity().getResources().getColor(R.color.light_grey)));
        //rlvAnimationList.setLayoutManager(new LinearLayoutManager(getContext()));
        url = listener.SendMessageValue(this);
        presenter.getClassifyDetail(url,this);
        adapter = new SearchAdapter(getContext());
        rlvAnimationList.setAdapter(adapter);
        rlvAnimationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.onStateChanged(recyclerView,newState,adapter,ClassifyDetailFragment.this);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //向左滑动的时候dx是大于0的，反方向滑动则小于0
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                presenter.onScrolled(dy > 0);
            }
        });
    }

    public void setListener(CallBackUrl listener){
        this.listener = listener;
    }

    @Override
    public void onError() {

    }

    @Override
    public void onParseError() {

    }

    @Override
    public void getList(List<SearchList> searchList) {
        if (searchList.size() != 0) {
            detail = searchList;
            adapter.setList(searchList);
        } else {
            ((MainActivity)getActivity()).showSnackBar(rlvAnimationList,R.string.not_found);
        }
    }

    public interface CallBackUrl{
        String SendMessageValue(ClassifyDetailFragment fragment);
    }
}
