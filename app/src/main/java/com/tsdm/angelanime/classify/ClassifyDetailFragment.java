package com.tsdm.angelanime.classify;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.SearchList;
import com.tsdm.angelanime.classify.mvp.ClassifyDContract;
import com.tsdm.angelanime.classify.mvp.ClassifyDPresenter;
import com.tsdm.angelanime.search.SearchAdapter;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Quan on 2019/1/12.
 */

public class ClassifyDetailFragment extends MvpBaseFragment<ClassifyDPresenter> implements
        ClassifyDContract.View, WebResponseListener {
    @BindView(R.id.rlv_animation_list)
    RecyclerView rlvAnimationList;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_retry)
    TextView tvRetry;
    @BindView(R.id.rl_net_error)
    RelativeLayout rlNetError;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_error_msg)
    TextView tvErrorMsg;
    @BindView(R.id.rl_data_error)
    RelativeLayout rlDataError;

    private CallBackUrl listener;
    private String url;
    private SearchAdapter adapter;
    private List<SearchList> detail;
    private View loadingView;

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
        loadingView = getActivity().findViewById(R.id.v_loading);
        rlvAnimationList.setHasFixedSize(true);
        rlvAnimationList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rlvAnimationList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.BOTH_SET,
                getActivity().getResources().getDimensionPixelSize(R.dimen.dp_16_x),
                getActivity().getResources().getColor(R.color.light_grey)));
        //rlvAnimationList.setLayoutManager(new LinearLayoutManager(getContext()));
        url = listener.SendMessageValue(this);
        presenter.getClassifyDetail(url, this);
        adapter = new SearchAdapter(getContext());
        rlvAnimationList.setAdapter(adapter);
        initListener();

    }

    private void initListener() {
        rlvAnimationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.onStateChanged(recyclerView, newState, adapter, ClassifyDetailFragment.this);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //向左滑动的时候dx是大于0的，反方向滑动则小于0
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                presenter.onScrolled(dy > 0);
            }
        });
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlNetError.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
                presenter.getClassifyDetail(url, ClassifyDetailFragment.this);
            }
        });
    }

    public void setListener(CallBackUrl listener) {
        this.listener = listener;
    }

    @Override
    public void onError() {
        pbLoading.setVisibility(View.GONE);
        rlNetError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onParseError() {
        pbLoading.setVisibility(View.GONE);
        rlDataError.setVisibility(View.VISIBLE);
        tvErrorMsg.setText(R.string.parse_error);
    }

    @Override
    public void getList(List<SearchList> searchList) {
        if (searchList.size() != 0) {
            loadingView.setVisibility(View.GONE);
            detail = searchList;
            adapter.setList(searchList);
            rlvAnimationList.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            rlDataError.setVisibility(View.VISIBLE);
            tvErrorMsg.setText(R.string.no_data);
        }
    }

    public interface CallBackUrl {
        String SendMessageValue(ClassifyDetailFragment fragment);
    }
}
