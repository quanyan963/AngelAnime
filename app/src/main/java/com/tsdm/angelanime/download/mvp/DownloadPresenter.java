package com.tsdm.angelanime.download.mvp;

import com.tsdm.angelanime.base.CommonSubscriber;
import com.tsdm.angelanime.base.RxPresenter;
import com.tsdm.angelanime.bean.DownloadStatue;
import com.tsdm.angelanime.bean.FileInformation;
import com.tsdm.angelanime.model.DataManagerModel;
import com.tsdm.angelanime.utils.RxUtil;

import org.reactivestreams.Publisher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mr.Quan on 2019/3/27.
 */

public class DownloadPresenter extends RxPresenter<DownloadContract.View> implements DownloadContract.Presenter {

    private DataManagerModel mDataManagerModel;
    @Inject
    public DownloadPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }


    @Override
    public List<DownloadStatue> geDownloadStatue() {
        return mDataManagerModel.getDownloadStatue();
    }

    @Override
    public void getFileList(final String path) {
        final List<FileInformation> informationList = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<>(files.length);
        Collections.addAll(fileList,files);
        if (files == null) {
            view.getFileList(null);
        }else {
            addSubscribe((Disposable) Observable.fromIterable(fileList)
                    .filter(new Predicate<File>() {
                        @Override
                        public boolean test(File file) throws Exception {
                            return file.getName().contains(".torrent");
                        }
                    }).compose(new ObservableTransformer<File, FileInformation>() {
                        @Override
                        public ObservableSource<FileInformation> apply(Observable<File> upstream) {
                            return upstream.map(new Function<File, FileInformation>() {
                                @Override
                                public FileInformation apply(File file) throws Exception {
                                    return new FileInformation(file.getName(),
                                            new SimpleDateFormat("yyyy-MM-dd HH:mm")
                                                    .format(new Date(file.lastModified())));
                                }
                            }).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                        }
                    }).subscribe(new Consumer<FileInformation>() {
                        @Override
                        public void accept(FileInformation information) throws Exception {
                            informationList.add(information);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            view.getFileList(informationList);
                        }
                    }));
        }
    }
}
