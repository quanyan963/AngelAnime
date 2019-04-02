package com.tsdm.angelanime.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsdm.angelanime.di.component.AppComponent;
import com.tsdm.angelanime.di.component.DaggerAppComponent;
import com.tsdm.angelanime.di.module.AppModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Quan on 2018/11/8.
 */

public class MyApplication extends Application {

    private static ImageLoader mImageLoader;
    private static MyApplication sInstance;
    private List<Activity> mActivityList;
    private static AppComponent mAppComponent;
    public static String path = Environment.getExternalStorageDirectory().getPath() + "/tsdm";
    public static String downloadPath = path + "/download";
    private static File file;
    private static ImageLoaderConfiguration config;
    private OkDownload okDownload;

    @Override
    public void onCreate() {
        super.onCreate();
        file = new File(path);
        OkGo.getInstance().init(this);
        if (sInstance == null) {
            sInstance = this;
        }
        mActivityList = new ArrayList<>();
        okDownload = OkDownload.getInstance();
        okDownload.setFolder(downloadPath);
        okDownload.getThreadPool().setCorePoolSize(3);
    }

    public static ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (mImageLoader == null) {
                    config = new ImageLoaderConfiguration.Builder(context)
                            .memoryCacheExtraOptions(200, 200)//配置内存缓存图片的尺寸
                            .memoryCacheSize(2 * 1024 * 1024)//配置内存缓存的大小
                            .threadPoolSize(3)//配置加载图片的线程数
                            .threadPriority(1000)//配置线程的优先级
                            .diskCache(new UnlimitedDiskCache(file))//UnlimitedDiskCache 限制这个图片的缓存路径
                            .diskCacheFileCount(50)//配置sdcard缓存文件的数量
                            .diskCacheFileNameGenerator(new Md5FileNameGenerator())//MD5这种方式生成缓存文件的名字
                            .diskCacheSize(50 * 1024 * 1024)//在sdcard缓存50MB
                            .build();//完成

                    mImageLoader = ImageLoader.getInstance();
                    mImageLoader.init(config);
                }
            }
        }
        return mImageLoader;
    }


    public static MyApplication getInstance() {
        if (sInstance == null) {
            return new MyApplication();
        } else {
            return sInstance;
        }
    }

    public static AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(sInstance))
                    .build();
        }
        return mAppComponent;
    }

    public void addActivity(Activity activity) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity);
        }
    }


    public void removeAllActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
    }
}
