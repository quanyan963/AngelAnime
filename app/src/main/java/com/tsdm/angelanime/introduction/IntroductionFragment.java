package com.tsdm.angelanime.introduction;

import android.os.Bundle;
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
import com.tsdm.angelanime.widget.RoundImageView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class IntroductionFragment extends MvpBaseFragment<IntroductionPresenter> implements IntroductionContract.View {
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

    private AnimationDetail detail;
    private IntroductionAdapter listAdapter;

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
        listAdapter = new IntroductionAdapter(getContext());
        rlvPlayList.setAdapter(listAdapter);
        initListener();
    }

    private void initListener() {
        listAdapter.setOnClickListener(new IntroductionAdapter.MusicClickListener() {
            @Override
            public void onItemClick(int position) {
                ((AnimationDetailActivity)getActivity()).onResultFromFragment(position);
            }
        });
        rlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AnimationDetailActivity)getActivity()).onResultFromFragment(-1);
            }
        });
    }

    @Subscribe
    public void onEventFragmentThread(AnimationDetail detail) {
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
                MyApplication.getImageLoader(getContext()).displayImage(detail.getTitleImg(), ivTitle, options);
            } else {
                MyApplication.getImageLoader(getContext()).displayImage(Url.URL + detail.getTitleImg(), ivTitle, options);
            }

            tvName.setText(detail.getTitle());
            tvUpdateTime.setText(detail.getUpdateTime());
            tvIntroduction.setText(detail.getIntroduction());
            tvListStatue.setText(detail.getStatue());

            listAdapter.addList(detail.getPlayListTitle());
        } else {
            //显示网络异常界面
        }
    }
}
