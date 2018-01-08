package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统消息 详细地址
 * Created by  on 2017/4/28.
 */

public class SystemessageDetailActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_ygbmstitle)
    TextView tv_ygbmstitle;

    @BindView(R.id.tv_ygbmscreatetime)
    TextView tv_ygbmscreatetime;

    @BindView(R.id.tv_ygbmscontent)
    TextView tv_ygbmscontent;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("消息详情");

        String ygbmscontent = getIntent().getStringExtra("ygbmscontent");
        String ygbmscreatetime = getIntent().getStringExtra("ygbmscreatetime");
        String ygbmstitle = getIntent().getStringExtra("ygbmstitle");

        tv_ygbmstitle.setText("" + ygbmstitle);
        tv_ygbmscreatetime.setText("" + ygbmscreatetime+"系统消息");
        tv_ygbmscontent.setText("" + ygbmscontent);

    }

//    private void inData(String ygbmsid) {
//        mSysMessMap.clear();
//        mSysMessMap.put("ygbmsid",""+ygbmsid);
//        OkHttp3Utils.getInstance().doPost(Constants.GETDETAILNOTICE_URL, null, mSysMessMap)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//                    @Override
//                    public void onNext(String s) {
//                        if (!TextUtils.isEmpty(s)) {
//                            Gson gson = new Gson();
////                            DataMess dataMess = gson.fromJson(s, DataMess.class);
////                            if (dataMess.getData().getSuccess().equals("1")) {
////                                List<DataMess.DataEntity.NoticearrayEntity> noticearray
////                                        = dataMess.getData().getNoticearray();
////                                leavedata.addAll(noticearray);
////                                messageadapter.notifyDataSetChanged();
////                            }
//                        }
//                    }
//                });
//    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_systemmessage;
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
