package com.tsdm.angelanime.introduction;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.event.AnimationDetail;
import com.tsdm.angelanime.detail.AnimationDetailActivity;
import com.tsdm.angelanime.introduction.mvp.IntroductionContract;
import com.tsdm.angelanime.introduction.mvp.IntroductionPresenter;
import com.tsdm.angelanime.utils.Url;
import com.tsdm.angelanime.utils.Utils;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.RoundImageView;
import com.tsdm.angelanime.widget.listener.PopUpListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.supercharge.shimmerlayout.ShimmerLayout;

import static com.tsdm.angelanime.utils.Constants.OK;
import static com.tsdm.angelanime.utils.Constants.RETRY;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class IntroductionFragment extends MvpBaseFragment<IntroductionPresenter> implements
        IntroductionContract.View, PopUpListener {
    @BindView(R.id.iv_title)
    RoundImageView ivTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    @BindView(R.id.rlv_play_list)
    RecyclerView rlvPlayList;
    @BindView(R.id.tv_list_statue)
    TextView tvListStatue;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.cl_fragment)
    ConstraintLayout clFragment;
    @BindView(R.id.sl_img)
    ShimmerLayout slImg;
    @BindView(R.id.sl_name)
    ShimmerLayout slName;
    @BindView(R.id.sl_time)
    ShimmerLayout slTime;
    @BindView(R.id.sl_introduction)
    ShimmerLayout slIntroduction;

    private AnimationDetail detail;
    private IntroductionAdapter listAdapter;
    private int position;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduction, null);
    }

    @Override
    public void init() {
        startShimmer();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rlvPlayList.setHasFixedSize(true);
        rlvPlayList.setLayoutManager(manager);
        rlvPlayList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST,
                getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8_x),
                getActivity().getResources().getColor(R.color.light_grey)));
        listAdapter = new IntroductionAdapter(getContext());
        rlvPlayList.setAdapter(listAdapter);
        initListener();

    }

    private void initListener() {
        listAdapter.setOnClickListener(new IntroductionAdapter.MusicClickListener() {
            @Override
            public void onItemClick(int position) {
                IntroductionFragment.this.position = position;
                ((AnimationDetailActivity) getActivity()).onResultFromFragment(position);
                listAdapter.setPosition(position);
            }
        });
        rlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AnimationDetailActivity) getActivity()).onResultFromFragment(-1);
            }
        });
    }

    private void startShimmer(){
        slImg.startShimmerAnimation();
        slName.startShimmerAnimation();
        slTime.startShimmerAnimation();
        slIntroduction.startShimmerAnimation();
    }

    private void stopShimmer(){
        slImg.stopShimmerAnimation();
        slName.stopShimmerAnimation();
        slTime.stopShimmerAnimation();
        slIntroduction.stopShimmerAnimation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFragmentThread(final AnimationDetail detail) {

        if (detail.getRequestStatue() == OK) {
            stopShimmer();

            ivTitle.setBackgroundResource(0);
            tvName.setBackgroundResource(0);
            tvUpdateTime.setBackgroundResource(0);
            tvIntroduction.setBackgroundResource(0);
            this.detail = detail;
            if (detail.getTitleImg().contains(".gif")) {
                Utils.displayImage(getContext(), detail.getTitleImg(), ivTitle, Utils
                        .getImageOptions(R.mipmap.defult_img, 0));
            } else {

                Utils.displayImage(getContext(), Url.URL + detail.getTitleImg(), ivTitle, Utils
                        .getImageOptions(R.mipmap.defult_img, 0));
            }
            if (detail.getPlayListTitle().size() == 0) {
                position = 0;
            } else {
                position = detail.getPlayListTitle().size() - 1;
                listAdapter.setPosition(position);
                listAdapter.addList(detail.getPlayListTitle());
                rlvPlayList.scrollToPosition(position);
            }
            tvName.setText(detail.getTitle());
            tvUpdateTime.setText(detail.getUpdateTime());
            tvIntroduction.setText(detail.getIntroduction());
            tvListStatue.setText(detail.getStatue());

            rlSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detail.getPlayListTitle().size() != 0) {
                        Utils.showBottomPopUp(getActivity(), detail.getPlayListTitle(),
                                IntroductionFragment.this, clFragment, position);
                    }
                }
            });
        }else if (detail.getRequestStatue() == RETRY){
            startShimmer();
        }else {
            stopShimmer();
        }

    }

    @Override
    public void onPopUpClick(int position) {
        this.position = position;
        listAdapter.setPosition(position);
        ((AnimationDetailActivity) getActivity()).onResultFromFragment(position);
    }
}
