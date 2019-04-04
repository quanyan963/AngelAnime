package com.tsdm.angelanime.introduction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.base.MvpBaseFragment;
import com.tsdm.angelanime.bean.VideoState;
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
import io.supercharge.shimmerlayout.ShimmerLayout;

import static com.tsdm.angelanime.utils.Constants.OK;
import static com.tsdm.angelanime.utils.Constants.RETRY;

/**
 * Created by Mr.Quan on 2018/12/10.
 */

public class IntroductionFragment extends MvpBaseFragment<IntroductionPresenter> implements
        IntroductionContract.View, PopUpListener, LinkAdapter.DownloadClickListener {
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
    @BindView(R.id.rlv_baidu)
    RecyclerView rlvBaidu;
    @BindView(R.id.rlv_torrent)
    RecyclerView rlvTorrent;
    @BindView(R.id.rl_baidu)
    RelativeLayout rlBaidu;
    @BindView(R.id.rl_torrent)
    RelativeLayout rlTorrent;

    private AnimationDetail detail;
    private IntroductionAdapter listAdapter;
    private LinkAdapter baiDuAdapter;
    private LinkAdapter torrentAdapter;
    private int position;
    private VideoState mVideoState;

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
        presenter.bind(getContext());

        mVideoState = presenter.geVideoState();
        startShimmer();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //播放列表
        rlvPlayList.setHasFixedSize(true);
        rlvPlayList.setLayoutManager(manager);
        rlvPlayList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration
                .VERTICAL_LIST, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8_x)
                , getActivity().getResources().getColor(R.color.light_grey)));
        listAdapter = new IntroductionAdapter(getContext());
        rlvPlayList.setAdapter(listAdapter);
        //百度云
        LinearLayoutManager baiduManager = new LinearLayoutManager(getContext());
        baiduManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rlvBaidu.setHasFixedSize(true);
        rlvBaidu.setLayoutManager(baiduManager);
        rlvBaidu.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration
                .VERTICAL_LIST, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8_x)
                , getActivity().getResources().getColor(R.color.light_grey)));
        baiDuAdapter = new LinkAdapter(getContext());
        rlvBaidu.setAdapter(baiDuAdapter);
        //种子下载
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rlvTorrent.setHasFixedSize(true);
        rlvTorrent.setLayoutManager(gridLayoutManager);
        rlvTorrent.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration
                .BOTH_SET, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8_x)
                , getActivity().getResources().getColor(R.color.light_grey)));
        torrentAdapter = new LinkAdapter(getContext());
        rlvTorrent.setAdapter(torrentAdapter);
        initListener();
    }

    private void initListener() {
        listAdapter.setOnClickListener(new IntroductionAdapter.MusicClickListener() {
            @Override
            public void onItemClick(int position) {
                IntroductionFragment.this.position = position;
                ((AnimationDetailActivity) getActivity()).onResultFromFragment(position, 0l);
                listAdapter.setPosition(position);
            }
        });
        rlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null)
                    ((AnimationDetailActivity) getActivity()).onResultFromFragment(-1, 0l);
            }
        });

        baiDuAdapter.setOnClickListener(this);
        torrentAdapter.setOnClickListener(this);
    }

    private void startShimmer() {
        slImg.startShimmerAnimation();
        slName.startShimmerAnimation();
        slTime.startShimmerAnimation();
        slIntroduction.startShimmerAnimation();
    }

    private void stopShimmer() {
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
            if (detail.getBaiduUrls().size() != 0) {
                rlBaidu.setVisibility(View.VISIBLE);
                baiDuAdapter.addList(detail.getBaiduUrls());
            } else {
                rlBaidu.setVisibility(View.GONE);
            }
            if (detail.getTorrentUrls().size() != 0 && !detail.getTorrentUrls().get(0).getUrl().contains("magnet")) {
                rlTorrent.setVisibility(View.VISIBLE);
                torrentAdapter.addList(detail.getTorrentUrls());
            } else {
                rlTorrent.setVisibility(View.GONE);
            }
            if (detail.getTitleImg().contains(".gif")) {
                Utils.displayImage(getContext(), detail.getTitleImg(), ivTitle, Utils
                        .getImageOptions(R.mipmap.defult_img, 0));
            } else {

                Utils.displayImage(getContext(), Url.URL + detail.getTitleImg(), ivTitle, Utils
                        .getImageOptions(R.mipmap.defult_img, 0));
            }
            if (detail.getPlayListTitle() != null) {
                if (detail.getPlayListTitle().size() == 0) {
                    position = 0;
                } else {
                    listAdapter.addList(detail.getPlayListTitle());
                    if (!mVideoState.getTitle().equals("")) {
                        if (detail.getTitle().equals(mVideoState.getTitle())) {
                            position = mVideoState.getListPosition();
                            listAdapter.setPosition(position);
                            ((AnimationDetailActivity) getActivity()).
                                    onResultFromFragment(position, mVideoState.getVideoPosition());
                        } else {
                            position = detail.getPlayListTitle().size() - 1;
                            listAdapter.setPosition(position);
                        }
                        rlvPlayList.scrollToPosition(position);
                    }
                }
            }

            tvName.setText(detail.getTitle());
            tvUpdateTime.setText(detail.getUpdateTime());
            tvIntroduction.setText(detail.getIntroduction());
            tvListStatue.setText(detail.getStatue());


            rlSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detail.getPlayListTitle() != null) {
                        Utils.showBottomPopUp(getActivity(), detail.getPlayListTitle(),
                                IntroductionFragment.this, clFragment, position);
                    }
                }
            });
        } else if (detail.getRequestStatue() == RETRY) {
            startShimmer();
        } else {
            stopShimmer();
        }

    }

    @Override
    public void onPopUpClick(int position) {
        this.position = position;
        listAdapter.setPosition(position);
        ((AnimationDetailActivity) getActivity()).onResultFromFragment(position, 0l);
    }

    @Override
    public void onItemClick(String url) {
        if (url.contains("baidu")){
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else {
            presenter.download(url);
        }

    }

    @Override
    public void onDestroy() {
        presenter.unBind();
        super.onDestroy();
    }
}
