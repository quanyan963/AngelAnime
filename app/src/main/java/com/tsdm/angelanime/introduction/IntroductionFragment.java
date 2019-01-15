package com.tsdm.angelanime.introduction;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
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

import butterknife.BindView;

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

    @Subscribe
    public void onEventFragmentThread(final AnimationDetail detail) {
        if (detail.getRequestStatue() == 0) {
            this.detail = detail;
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//让图片进行内存缓存
                    .cacheOnDisk(true)//让图片进行sdcard缓存
                        /*.showImageForEmptyUri(R.mipmap.ic_empty)//图片地址有误
                        .showImageOnFail(R.mipmap.ic_error)//当图片加载出现错误的时候显示的图片
                        .showImageOnLoading(R.mipmap.loading)//图片正在加载的时候显示的图片*/
                    .build();
            if (detail.getTitleImg().contains(".gif")) {
                //Glide.with(getContext()).load(detail.getTitleImg()).into(ivTitle);
                MyApplication.getImageLoader(getContext()).displayImage(detail.getTitleImg(), ivTitle, options);
            } else {
                //Glide.with(getContext()).load(Url.URL + detail.getTitleImg()).into(ivTitle);
                MyApplication.getImageLoader(getContext()).displayImage(Url.URL + detail.getTitleImg(), ivTitle, options);
            }
            if (detail.getPlayListTitle().size() == 0){
                position = 0;
            }else {
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
                    if (detail.getPlayListTitle().size() != 0){
                        Utils.showBottomPopUp(getActivity(), detail.getPlayListTitle(),
                                IntroductionFragment.this,clFragment,position);
                    }
                }
            });
        } else {
            //显示网络异常界面
        }


    }

    @Override
    public void onPopUpClick(int position) {
        this.position = position;
        listAdapter.setPosition(position);
        ((AnimationDetailActivity) getActivity()).onResultFromFragment(position);
    }
}
