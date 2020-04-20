package com.tsdm.angelanime.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.RecentlyDetail;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.main.mvp.FragmentContract;
import com.tsdm.angelanime.main.mvp.FragmentPresenter;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.listener.WebResponseListener;

import java.util.List;

import butterknife.BindView;

import static com.tsdm.angelanime.utils.Constants.HREF_URL;

/**
 * Created by Mr.Quan on 2018/12/19.
 */

public class MainFragment extends MvpBaseFragment<FragmentPresenter> implements FragmentContract.View,
        FRecycleAdapter.onRecycleItemClick, WebResponseListener {
    @BindView(R.id.rlv_animation_list)
    RecyclerView rlvAnimationList;
    @BindView(R.id.srl_fresh)
    SwipeRefreshLayout srlFresh;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.iv_error)
    ImageView ivError;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
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

    private FRecycleAdapter adapter;
    private CallBackValue callBackValue;
    private RecentlyDetail detail;
    private View vLoading;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        callBackValue = (CallBackValue) context;
//    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        vLoading = view.findViewById(R.id.fv_loading);
        return view;
    }

    @Override
    public void init() {
        srlFresh.setColorSchemeColors(getContext().getResources().getColor(R.color.colorAccent));
        vLoading.setVisibility(View.GONE);
        rlvAnimationList.setVisibility(View.VISIBLE);
        rlvAnimationList.setHasFixedSize(true);
        rlvAnimationList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rlvAnimationList.addItemDecoration(new DividerItemDecoration(getActivity()
                , DividerItemDecoration.BOTH_SET, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.dp_16_x), getActivity()
                .getResources().getColor(R.color.light_grey)));
        //rlvAnimationList.setLayoutManager(new LinearLayoutManager(getContext()));
        detail = callBackValue.SendMessageValue(this);
        adapter = new FRecycleAdapter(detail, getContext());
        rlvAnimationList.setAdapter(adapter);
        adapter.setOnItemClick(this);
        srlFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh(MainFragment.this);
            }
        });
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbLoading.setVisibility(View.VISIBLE);
                rlNetError.setVisibility(View.GONE);
                presenter.refresh(MainFragment.this);
            }
        });
    }

    public void setListener(CallBackValue callBackValue) {
        this.callBackValue = callBackValue;
    }

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(getActivity(), AnimationDetailActivity.class).
                putExtra(HREF_URL, Url.URL + detail.getUrl().get(position)));
        //.putExtra(POSITION,(detail.getUrl().size()-1))
    }

    @Override
    public void onError() {
        srlFresh.setRefreshing(false);
        Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onParseError() {
        srlFresh.setRefreshing(false);
        Toast.makeText(getContext(), R.string.parse_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getRecentlyDetail(List<RecentlyDetail> recentlyDetails) {
        adapter.setData(recentlyDetails.get(callBackValue.sendPosition(this)));
        srlFresh.setRefreshing(false);
    }

    public interface CallBackValue {
        RecentlyDetail SendMessageValue(MainFragment fragment);

        int sendPosition(MainFragment fragment);
    }
}
