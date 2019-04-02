package com.tsdm.angelanime.download;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.base.MvpBaseActivity;
import com.tsdm.angelanime.bean.FileInformation;
import com.tsdm.angelanime.download.mvp.DownloadContract;
import com.tsdm.angelanime.download.mvp.DownloadPresenter;
import com.tsdm.angelanime.service.MyServiceConn;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.tsdm.angelanime.utils.Constants.ACTIVITY_SEND;
import static com.tsdm.angelanime.utils.Constants.NOTIFICATION_ID;
import static com.lzy.okgo.model.Progress.WAITING;
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
    private MyServiceConn.MyBroadcastReceiver receiver;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        receiver = new MyServiceConn.MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ON_PAUSE);
        registerReceiver(receiver, filter);
        final Intent pauseIntent = new Intent(ON_PAUSE);
        pauseIntent.setAction(ON_PAUSE);
        fileName = getFilesAllName(MyApplication.downloadPath);
        Intent intent = getIntent();
        int notificationId = intent.getIntExtra(NOTIFICATION_ID,-1);
        initToolbar();
        setNavigationIcon(true);
        rlvDownload.setHasFixedSize(true);
        rlvDownload.setLayoutManager(new LinearLayoutManager(this));
        downloadAdapter = new DownloadAdapter(this);
        rlvDownload.setAdapter(downloadAdapter);
        if (fileName != null)
            downloadAdapter.setData(fileName);
        if (downList.size() != 0){
            for (int i = 0; i < downList.size(); i++) {
                if (downList.get(i) == notificationId){
                    downloadAdapter.updateData(downList.size() - i,
                            new FileInformation("",WAITING,0,notificationId));
                    break;
                }else if (i == downList.size() - 1){
                    downList.add(notificationId);
                    downloadAdapter.setDownloading(new FileInformation("",WAITING,0,notificationId));
                }
            }
        }else {
            downList.add(notificationId);
            downloadAdapter.setDownloading(new FileInformation("",WAITING,0,notificationId));
        }
        downloadAdapter.setListener(new DownloadAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(int id) {
                pauseIntent.putExtra(NOTIFICATION_ID,id);
                sendBroadcast(pauseIntent);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_download;
    }

    public static List<FileInformation> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            return null;
        }
        List<FileInformation> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            if (files[i].getName().contains(".torrent")){
                s.add(new FileInformation(files[i].getName(), new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date(files[i].lastModified()))));
            }
        }
        return s;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread (FileInformation information){
        if (downList.size() != 0){
            for (int i = 0; i < downList.size(); i++) {
                if (downList.get(i) != null && downList.get(i) == information.getId()){
                    downloadAdapter.updateData(downList.size() - i - 1, information);
                    break;
                }else if (i == downList.size() - 1){
                    downList.add(information.getId());
                    downloadAdapter.setDownloading(information);
                }
            }
        }else {
            downList.add(information.getId());
            downloadAdapter.setDownloading(information);
        }
    }
}
