package com.ztd.yyb.util;


import com.ztd.yyb.net.CookiesManager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by  on 2016/12/28.
 */

public class OkHttp3Utils {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private volatile static OkHttp3Utils mInstance;
    private final String TAG = getClass().getSimpleName();
    private OkHttpClient mOkHttpClient;

    private OkHttp3Utils() {
        super();
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        clientBuilder.cookieJar(new CookiesManager());//自动管理Cookies
        mOkHttpClient = clientBuilder.build();
    }

    public static OkHttp3Utils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttp3Utils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttp3Utils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    private Headers SetHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersBuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersBuilder.build();
        return headers;
    }

    /**
     * post请求参数
     *
     * @param BodyParams
     * @return
     */
    private RequestBody SetPostRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }


    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    private String setGetUrlParams(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams != null) {
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                strParams += "&" + key + "=" + mapParams.get(key);
            }
        }
        return strParams;
    }

    /**
     * 实现post请求
     *
     * @param reqUrl
     * @param headersParams
     * @param params
     */
    public Observable<String> doPost(final String reqUrl, final Map<String, String> headersParams, final Map<String, String> params) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
//                LogUtil.e(TAG, "call:" + Thread.currentThread().getName());
                Request.Builder RequestBuilder = new Request.Builder();
                RequestBuilder.url(reqUrl);// 添加URL地址
                RequestBuilder.post(SetPostRequestBody(params));
                RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
                Request request = RequestBuilder.build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
                        String str = response.body().string();
                        subscriber.onNext(str);
//                        }
                        subscriber.onCompleted();
                        call.cancel();
//                        LogUtil.e(TAG, "onResponse     ==:" + str);
                    }

                    @Override
                    public void onFailure(Call call, IOException exception) {
                        subscriber.onError(exception);
                        call.cancel();
                        LogUtil.e(TAG, "onFailure    exception:" + exception);

                    }
                });
            }
        });

    }

    /**
     * 上传多张图片及参数
     *
     * @param reqUrl  URL地址
     * @param params  参数
     * @param pic_key 上传图片的关键字
     * @param files   图片file集合
     */
    public Observable<String> postMultipart(final String reqUrl, final Map<String, String> params, final String pic_key, final List<File> files) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {

                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                multipartBodyBuilder.setType(MultipartBody.FORM);
                //遍历map中所有参数到builder
                if (params != null) {
                    for (String key : params.keySet()) {
                        multipartBodyBuilder.addFormDataPart(key, params.get(key));
                    }
                }
                //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
                if (files != null) {
                    for (File file : files) {
                        multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                }
                //构建请求体
                RequestBody requestBody = multipartBodyBuilder.build();

                Request.Builder RequestBuilder = new Request.Builder();
                RequestBuilder.url(reqUrl);// 添加URL地址
                RequestBuilder.post(requestBody);
                Request request = RequestBuilder.build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(e);
                        subscriber.onCompleted();
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        subscriber.onNext(str);
                        subscriber.onCompleted();
                        call.cancel();
                    }
                });
            }
        });
    }
    /**
     * 上传多张图片及参数
     *-------------------------------------------------------------------------
     * @param reqUrl  URL地址
     * @param params  参数
     * @param pic_key 上传图片的关键字
     * @param files   图片file集合
     */
    public Observable<String> postMul(final String reqUrl, final Map<String, String> params, final String pic_key, final List<File> files) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {


                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                multipartBodyBuilder.setType(MultipartBody.FORM);
                //遍历map中所有参数到builder
                if (params != null) {
                    for (String key : params.keySet()) {
                        multipartBodyBuilder.addFormDataPart(key, params.get(key));
                    }
                }

                //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
                if (files != null) {
                    for (File file : files) {
                        multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                }
                //构建请求体
                RequestBody requestBody = multipartBodyBuilder.build();

                Request.Builder RequestBuilder = new Request.Builder();
                RequestBuilder.url(reqUrl);// 添加URL地址
                RequestBuilder.post(requestBody);
                Request request = RequestBuilder.build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(e);
                        subscriber.onCompleted();
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        subscriber.onNext(str);
                        subscriber.onCompleted();
                        call.cancel();
                    }
                });




            }
        });
    }


    /**
     * 实现get请求
     *
     * @param reqUrl
     * @param headersParams
     * @param params
     */
    public Observable<String> doGet(final String reqUrl, final Map<String, String> headersParams, final Map<String, String> params) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Request.Builder RequestBuilder = new Request.Builder();
                RequestBuilder.url(reqUrl + setGetUrlParams(params));// 添加URL地址 自行加 ?
                RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
                Request request = RequestBuilder.build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onResponse(final Call call, final Response response) throws IOException {
//                        if (response.isSuccessful()) {
                        String str = response.body().string();
                        subscriber.onNext(str);
//                        }
                        subscriber.onCompleted();
                        call.cancel();
                    }

                    @Override
                    public void onFailure(final Call call, final IOException exception) {
                        subscriber.onError(exception);
                        call.cancel();
                    }
                });
            }
        });

    }

}
