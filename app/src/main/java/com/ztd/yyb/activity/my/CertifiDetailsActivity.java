package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanCertDet.CerdetailarrayBean;
import com.ztd.yyb.bean.beanCertDet.DataCertiDetail;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 家教认证详情，师傅认证详情
 * Created by  on 2017/4/20.
 */

public class CertifiDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_cername)
    TextView tv_cername;
    @BindView(R.id.tv_cernamenum)
    TextView tv_cernamenum;

    @BindView(R.id.image_one)
    ImageView image_one;
    @BindView(R.id.image_two)
    ImageView image_two;
    @BindView(R.id.image_three)
    ImageView image_three;

    @BindView(R.id.image_four)
    ImageView image_four;
    @BindView(R.id.image_five)
    ImageView image_five;
    @BindView(R.id.image_sex)
    ImageView image_sex;


    String type="";
    String ygbmcid="";

    @Override
    protected void initViewsAndEvents() {

        Intent intent = getIntent();
        type = intent.getStringExtra("FLAG");
        ygbmcid = intent.getStringExtra("ygbmcid");

        if(type.equals("2")){
            mTvTitle.setText("师傅认证详情");
        }else  if(type.equals("4")){
            mTvTitle.setText("家教认证详情");
        }

        getData();

    }

    @Override
    protected int getContentViewLayoutID() {

        return R.layout.lay_certifidetail;

    }
    private Map<String, String> mMyCerMap = new HashMap<>();

    private void getData() {

//        String userid = (String) SPUtil.get(USERID, "");

        mMyCerMap.clear();
        mMyCerMap.put("ygbmcid", ygbmcid);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.GETCERTIFIDE_URL, null, mMyCerMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(String s) {//ygbmhand:手持身份证   ygbmfront：正面身份证  ygbmcon：反面身份证

                        dismissLoadingDialog();

//                        身份证：前缀+/idcard/+图片名
//                        证书：前缀+/certificate/+图片名


                        if (!TextUtils.isEmpty(s)) {// int flag = 3;//   0审核中--1认证通过--2认证未通过 --3未认证

                            Gson gson = new Gson();

                            DataCertiDetail dataCertiDetail = gson.fromJson(s, DataCertiDetail.class);

                           if( dataCertiDetail.getData().getSuccess().equals("1")){

                               String ygbmpin = dataCertiDetail.getData().getYgbmpin();
                               String ygbmname = dataCertiDetail.getData().getYgbmname();
                               tv_cername.setText("真实姓名："+ygbmname);
                               tv_cernamenum.setText("身份证号："+ygbmpin);

                               String ygbmcon = dataCertiDetail.getData().getYgbmcon();
                               String ygbmfront = dataCertiDetail.getData().getYgbmfront();
                               String ygbmhand = dataCertiDetail.getData().getYgbmhand();


                               Glide.with(mContext).load(Constants.BASE_URL+"idcard/"+ygbmcon)
                                       .error(R.mipmap.load_init)
                                       .placeholder(R.mipmap.load_init)
                                       .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                                       .into(image_one);

                               Glide.with(mContext).load(Constants.BASE_URL+"idcard/"+ygbmfront)
                                       .error(R.mipmap.load_init)
                                       .placeholder(R.mipmap.load_init)
                                       .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                                       .into(image_two);

                               Glide.with(mContext).load(Constants.BASE_URL+"idcard/"+ygbmhand)
                                       .error(R.mipmap.load_init)
                                       .placeholder(R.mipmap.load_init)
                                       .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                                       .into(image_three);


                               List<CerdetailarrayBean> cerdetailarray = dataCertiDetail.getData().getCerdetailarray();

//                               for (int i = 0; i < cerdetailarray.size(); i++) {
//                                   String ygbmcipath = cerdetailarray.get(i).getYgbmcipath();
//                               }


                               cerde(cerdetailarray);


                           }



                        }

                    }
                });

    }

    private void cerde(List<CerdetailarrayBean> cerdetailarray) {

        if(cerdetailarray.size()==3){
            String ygbmcipath0 = cerdetailarray.get(0).getYgbmcipath();
            String ygbmcipath1 = cerdetailarray.get(1).getYgbmcipath();
            String ygbmcipath2 = cerdetailarray.get(2).getYgbmcipath();

            Glide.with(mContext).load(Constants.BASE_URL+"certificate/"+ygbmcipath0)
                    .error(R.mipmap.load_init)
                    .placeholder(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_four);
            Glide.with(mContext).load(Constants.BASE_URL+"certificate/"+ygbmcipath1)
                    .error(R.mipmap.load_init)
                    .placeholder(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_five);
            Glide.with(mContext).load(Constants.BASE_URL+"certificate/"+ygbmcipath2)
                    .error(R.mipmap.load_init)
                    .placeholder(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_sex);
        }

        if(cerdetailarray.size()==2){
            String ygbmcipath0 = cerdetailarray.get(0).getYgbmcipath();
            String ygbmcipath1 = cerdetailarray.get(1).getYgbmcipath();

            Glide.with(mContext).load(Constants.BASE_URL+"certificate/"+ygbmcipath0)
                    .error(R.mipmap.load_init)
                    .placeholder(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_four);
            Glide.with(mContext).load(Constants.BASE_URL+"certificate/"+ygbmcipath1)
                    .error(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .placeholder(R.mipmap.load_init)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_five);

        }

        if(cerdetailarray.size()==1){

            String ygbmcipath = cerdetailarray.get(0).getYgbmcipath();
            Glide.with(mContext).load(Constants.BASE_URL+"certificate/"+ygbmcipath)
                    .error(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .placeholder(R.mipmap.load_init)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_four);

        }

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
        }
    }

}
