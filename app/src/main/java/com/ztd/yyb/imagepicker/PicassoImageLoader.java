package com.ztd.yyb.imagepicker;

import android.app.Activity;
import android.widget.ImageView;

import com.lzy.imagepicker.loader.ImageLoader;
import com.ztd.yyb.util.PicassoUtil;

import java.io.File;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PicassoImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//        Picasso.with(activity)//
//                .load(new File(path))//
//                .placeholder(R.mipmap.default_image)//
//                .error(R.mipmap.default_image)//
//                .resize(width, height)//
//                .centerInside()
////                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
//                .into(imageView);
        PicassoUtil.getInstance().displayFileImageWithResize(activity,new File(path),imageView,width,height);
    }

    @Override
    public void clearMemoryCache() {
    }
}
