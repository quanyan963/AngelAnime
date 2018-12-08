package com.tsdm.angelanime.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.bean.TopEight;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Mr.Quan on 2018/11/20.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, final ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//让图片进行内存缓存
                .cacheOnDisk(true)//让图片进行sdcard缓存
                        /*.showImageForEmptyUri(R.mipmap.ic_empty)//图片地址有误
                        .showImageOnFail(R.mipmap.ic_error)//当图片加载出现错误的时候显示的图片
                        .showImageOnLoading(R.mipmap.loading)//图片正在加载的时候显示的图片*/
                .build();
        MyApplication.getImageLoader(context).displayImage(((TopEight)path).getImgUrl(),imageView,options);
    }

//    //清除缓存按钮
//    public void clearCache(View view){
//        //清除缓存的方法
//        ImageLoader.getInstance().clearDiskCache();//清除磁盘缓存
//        ImageLoader.getInstance().clearMemoryCache();//清除内存缓存
//        Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
//    }
}
