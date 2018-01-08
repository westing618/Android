package com.ztd.yyb.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 *
 */

public class PicassoUtil {
    private volatile static PicassoUtil mInstance;
    private final String TAG = getClass().getSimpleName();
    private Picasso mPicasso;
    private OkHttpClient mClient;

    private PicassoUtil() {
        File file = new File(Constants.IMAGE_FILE);
        if (!file.exists()){
            file.mkdirs();
        }
        mClient = new OkHttpClient
                .Builder()
                .cache(new Cache(file, 1000 * 1024))
                .addInterceptor(new CaheInterceptor(BaseApplication.getContext()))
                .addNetworkInterceptor(new CaheInterceptor(BaseApplication.getContext()))
                .build();
        mPicasso = new Picasso.Builder(BaseApplication.getContext())
                .downloader(new ImageDownLoader(mClient))
                .build();
        mPicasso.with(BaseApplication.getContext()).setIndicatorsEnabled(false);//左上角标出颜色，红色为从网络获取，绿色为从内存中获取，蓝色为从硬盘中获取
    }

    public static PicassoUtil getInstance() {
        if (mInstance == null) {
            synchronized (PicassoUtil.class) {
                if (mInstance == null) {
                    mInstance = new PicassoUtil();
                }
            }
        }
        return mInstance;
    }

    public Picasso getPicasso(){
        return mPicasso;
    }

    public void clearCache(Context context, String path) throws IOException {
        FileUtil.getInstance().delete(new File(Constants.IMAGE_FILE));
        mPicasso.with(context).invalidate(Uri.parse(path));
    }

    public void displayImageWithFit(Context context, String url, ImageView imageView) {
        mPicasso.with(context)
                .load(url)
                .fit()
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        LogUtil.e(TAG, "onSuccess");
                    }

                    @Override
                    public void onError() {
                        LogUtil.e(TAG, "onError");
                    }
                });
    }

    public void displayImageWithFitNoCache(Context context, String url, ImageView imageView) {
        mPicasso.with(context)
                .load(url)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        LogUtil.e(TAG, "onSuccess");
                    }

                    @Override
                    public void onError() {
                        LogUtil.e(TAG, "onError");
                    }
                });
    }


    public void displayFileImageWithResize(Context context, File file, ImageView imageView, int width, int height) {
        mPicasso.with(context)
                .load(file)
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)
                .centerInside()
                .into(imageView);
    }
}
