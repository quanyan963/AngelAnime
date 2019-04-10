package com.tsdm.angelanime.download;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.DownloadStatue;
import com.tsdm.angelanime.bean.FileInformation;
import com.tsdm.angelanime.download.mvp.DownloadContract;
import com.tsdm.angelanime.download.mvp.DownloadPresenter;
import com.tsdm.angelanime.service.DownloadService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.lzy.okgo.model.Progress.FINISH;
import static com.lzy.okgo.model.Progress.NONE;
import static com.tsdm.angelanime.utils.Constants.NOTIFICATION_ID;
import static com.tsdm.angelanime.utils.Constants.ON_CANCEL;
import static com.tsdm.angelanime.utils.Constants.ON_PAUSE;

/**
 * Created by Mr.Quan on 2019/3/27.
 */

public class DownloadActivity extends MvpBaseActivity<DownloadPresenter> implements DownloadContract.View {
    @BindView(R.id.rlv_download)
    RecyclerView rlvDownload;
    private DownloadAdapter downloadAdapter;
    private List<FileInformation> fileName;
    private List<Integer> downList = new ArrayList<>();
    private DownloadService.MyBroadcastReceiver receiver;
    private List<DownloadStatue> statueList = new ArrayList<>();

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        statueList = presenter.geDownloadStatue();
        rlvDownload.setHasFixedSize(true);
        rlvDownload.setLayoutManager(new LinearLayoutManager(this));
        downloadAdapter = new DownloadAdapter(this);
        rlvDownload.setAdapter(downloadAdapter);
        fileName = getFilesAllName(MyApplication.downloadPath);
        if (fileName != null)
            downloadAdapter.setData(fileName);
        if (statueList.size() != 0) {
            for (int i = 0; i < statueList.size(); i++) {
                downList.add(statueList.get(i).getNotifyId());
                downloadAdapter.setDownloading(new FileInformation("",
                        statueList.get(i).getStatue(), 0, statueList.get(i).getNotifyId()));
            }
        }


        initToolbar();
        setNavigationIcon(true);

        receiver = new DownloadService.MyBroadcastReceiver();
        IntentFilter pauseFilter = new IntentFilter();
        pauseFilter.addAction(ON_PAUSE);
        registerReceiver(receiver, pauseFilter);
        final Intent pauseIntent = new Intent(ON_PAUSE);
        pauseIntent.setAction(ON_PAUSE);

        IntentFilter cancelFilter = new IntentFilter();
        cancelFilter.addAction(ON_CANCEL);
        registerReceiver(receiver, cancelFilter);
        final Intent cancelIntent = new Intent(ON_CANCEL);
        cancelIntent.setAction(ON_CANCEL);
        downloadAdapter.setListener(new DownloadAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(int id) {
                pauseIntent.putExtra(NOTIFICATION_ID, id);
                sendBroadcast(pauseIntent);
            }

            @Override
            public void onViewDelete(int position, int id) {
                if (position < downList.size()){
                    cancelIntent.putExtra(NOTIFICATION_ID, id);
                    sendBroadcast(cancelIntent);

                }else {
                    String path = MyApplication.downloadPath +"/"+ fileName.get(position).getFileName();
                    File file = new File(path);
                    if (file.exists()){
                        file.delete();
                        downloadAdapter.deleteData(position);
                    }

                }

            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_download;
    }

    public static List<FileInformation> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        List<FileInformation> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(".torrent")) {
                s.add(new FileInformation(files[i].getName(), new SimpleDateFormat("yyyy-MM-dd HH:mm")
                        .format(new Date(files[i].lastModified()))));
            }
        }
        return s;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FileInformation information) {
        if (downList.size() != 0) {
            for (int i = 0; i < downList.size(); i++) {
                if (downList.get(i) != null && downList.get(i) == information.getId()) {
                    if (information.getState() == NONE) {
                        downloadAdapter.deleteData(downList.size() - i - 1);
                        for (int j = 0; j < downList.size(); j++) {
                            if (downList.get(i) == information.getId()){
                                downList.remove(i);
                                break;
                            }
                        }
                    } else if (information.getState() == FINISH) {
                        downloadAdapter.insertComplete(downList.size() - i - 1,
                                downList.size() - 1, information);
                        downList.remove(i);
                    } else {
                        downloadAdapter.updateData(downList.size() - i - 1, information);
                    }
                    break;
                } else if (i == downList.size() - 1) {
                    downList.add(information.getId());
                    downloadAdapter.setDownloading(information);
                    break;
                }
            }
        } else {
            downList.add(information.getId());
            downloadAdapter.setDownloading(information);
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
