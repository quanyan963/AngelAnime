package com.tsdm.angelanime.widget;

import android.content.Context;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.TopEight;
import com.tsdm.angelanime.utils.Utils;
import com.youth.banner.loader.ImageLoaderInterface;

/**
 * Created by Mr.Quan on 2018/11/20.
 */

public class GlideImageLoader implements ImageLoaderInterface<RoundImageView> {
//    @Override
//    public void displayImage(Context context, Object path, final ImageView imageView) {
//        /**
//         注意：
//         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
//         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
//         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
//         切记不要胡乱强转！
//         */
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)//让图片进行内存缓存
//                .cacheOnDisk(true)//让图片进行sdcard缓存
//                        /*.showImageForEmptyUri(R.mipmap.ic_empty)//图片地址有误
//                        .showImageOnFail(R.mipmap.ic_error)//当图片加载出现错误的时候显示的图片
//                        .showImageOnLoading(R.mipmap.loading)//图片正在加载的时候显示的图片*/
//                .build();
//        MyApplication.getImageLoader(context).displayImage(((TopEight)path).getImgUrl(),imageView,options);
//        //Glide.with(context).load(((TopEight)path).getImgUrl()).into(imageView);
//    }

    @Override
    public void displayImage(Context context, Object path, RoundImageView imageView) {
        Utils.displayImage(context,((TopEight)path).getImgUrl(),imageView,Utils
                .getImageOptions(R.mipmap.defult_img,0));
        //MyApplication.getImageLoader(context).displayImage(((TopEight)path).getImgUrl(),imageView,options);
    }

    @Override
    public RoundImageView createImageView(Context context) {
        return new RoundImageView(context);
    }
}
