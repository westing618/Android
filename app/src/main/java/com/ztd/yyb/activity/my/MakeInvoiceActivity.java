package com.ztd.yyb.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.UpData;
import com.ztd.yyb.bean.beanHome.AddressTreeBean;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.DatadictionaryBean;
import com.ztd.yyb.bean.beanHome.SubBean;
import com.ztd.yyb.bean.beanHome.SubBeanX;
import com.ztd.yyb.bean.beanInvoice.Datainvoice;
import com.ztd.yyb.evenbean.InvoiceEvent;
import com.ztd.yyb.util.CheckFormatUtils;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;
import static com.ztd.yyb.activity.LoginActivity.USERPHONE;


/**
 * 我的发布 开发票
 * Created by  on 2017/3/13.
 */

public class MakeInvoiceActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_invonum)
    TextView tv_invonum;

    @BindView(R.id.tv_makeaddress)
    TextView tv_makeaddress;

    @BindView(R.id.et_invoicename)
    EditText et_invoicename;

    @BindView(R.id.et_invoicenum)
    EditText et_invoicenum;

    @BindView(R.id.et_invoicemoney)
    EditText et_invoicemoney;

    @BindView(R.id.et_inpername)
    EditText et_inpername;

    @BindView(R.id.et_inphone)
    EditText et_inphone;

    @BindView(R.id.editText_address)
    EditText editText_address;

    @BindView(R.id.et_connect)
    TextView et_connect;



//    String sumamount;

    String ygbmarea;//区
    String ygbrprovince;//省
    String ygbrcity;//市

    private Map<String, String> mInvoiceMap = new HashMap<>();

    private ArrayList<AddressTreeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<SubBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<SubBean>>> options3Items = new ArrayList<>();

    AppConfigBean appConfigBean;
    List<DatadictionaryBean> datadictionary;
    List<AddressTreeBean> addressTree;


    private Map<String, String> mMytrMap = new HashMap<>();

    private int mPageNum = 1;//页码
    private int mPageSize = 10;//一次获取几条数据

    double sumamount;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("开票");


        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");

        Gson gson = new Gson();

        if (!TextUtils.isEmpty(myappconfig)) {

            appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            datadictionary = appConfigBean.getData().getDatadictionary();
            addressTree = appConfigBean.getData().getAddressTree();

            et_connect.setText(""+appConfigBean.getData().getReceiptContent());
        }


        initJsonData();

        String phone = (String) SPUtil.get(USERPHONE, "");//用户电话
        et_inphone.setText(""+phone);

        getData();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_makelnvoice;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    private void getData() {

        mMytrMap.clear();

        String  userid = (String) SPUtil.get(USERID, "");
        mMytrMap.put("userid",""+userid);
        mMytrMap.put("pageNumber", String.valueOf(mPageNum));
        mMytrMap.put("pageSize", String.valueOf(mPageSize));

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.RECEIOLIST_URL, null, mMytrMap)
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
                    public void onNext(String s) {
                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson=new Gson();

                            Datainvoice datainvoice = gson.fromJson(s, Datainvoice.class);

                            if(datainvoice.getData().getSuccess().equals("1")){

                                sumamount = datainvoice.getData().getSumamount();//可开票金额

                                tv_invonum.setText("(可开票金额￥"+sumamount+")");

                            }

                        }
                    }
                });

    }


    @OnClick({R.id.lin_back,R.id.btn_makevo,R.id.ll_makeaddress,R.id.imageView2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;

            case R.id.btn_makevo:

                postMake();

                break;
            case R.id.imageView2:

                String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息

                editText_address.setText(""+locationDescribe);

                break;
            case R.id.ll_makeaddress:

                ShowPickerView();

                break;
        }
    }

    private void postMake() {

        String ygbrname = et_invoicename.getText().toString().trim();
        String ygbrnamenum = et_invoicenum.getText().toString().trim();

        String ygbramount = et_invoicemoney.getText().toString().trim();
        String ygbrcontact = et_inpername.getText().toString().trim();
        String ygbrtel = et_inphone.getText().toString().trim();
        String ygbraddress = editText_address.getText().toString().trim();
        String trim = tv_makeaddress.getText().toString().trim();

        if(TextUtils.isEmpty(ygbrname)){
            ToastUtil.show(mContext,"请输入发票抬头");
            return;
        }
        if(TextUtils.isEmpty(ygbrnamenum)){
            ToastUtil.show(mContext,"请输入发票税号");
            return;
        }

        if(TextUtils.isEmpty(ygbramount)){
            ToastUtil.show(mContext,"请输入发票金额");
            return;
        }

        double amount=Double.parseDouble(ygbramount);//用户输入

        BigDecimal bd = new BigDecimal(amount);
        BigDecimal setScale = bd.setScale(2, bd.ROUND_DOWN);//保留２位小数

        if(sumamount < amount){
            ToastUtil.show(mContext,"开票金额不足");
            return;
        }

        if(TextUtils.isEmpty(ygbrcontact)){
            ToastUtil.show(mContext,"请输入发票联系人");
            return;
        }
        if(TextUtils.isEmpty(ygbrtel)){
            ToastUtil.show(mContext,"请输入联系电话");
            return;
        }

        if (CheckFormatUtils.isMobileNO(ygbrtel) == false) {// 判断手机号码格式
            ToastUtil.show(this,"手机格式错误");
            return;
        }

        if(TextUtils.isEmpty(trim)){
            ToastUtil.show(mContext,"请点击选择省市区");
            return;
        }

        if(TextUtils.isEmpty(ygbraddress)){
            ToastUtil.show(mContext,"请输入详细地址");
            return;
        }


        String  userid = (String) SPUtil.get(USERID, "");
        mInvoiceMap.clear();

        mInvoiceMap.put("ygbrtype","");//类别（可以不传）
        mInvoiceMap.put("ygbrname",ygbrname);//抬头
        mInvoiceMap.put("ygbrnumber",ygbrnamenum);//税号
        mInvoiceMap.put("ygbrcontent",""+et_connect.getText().toString());//内容
        mInvoiceMap.put("ygbramount",""+setScale);//金额
        mInvoiceMap.put("ygbrcontact",ygbrcontact);//联系人
        mInvoiceMap.put("ygbrtel",ygbrtel);//联系电话
        mInvoiceMap.put("ygbmid",""+userid);//用户ID

        mInvoiceMap.put("ygbrprovince",ygbrprovince);//省
        mInvoiceMap.put("ygbrcity",ygbrcity);//市
        mInvoiceMap.put("ygbrarea",ygbmarea);//区

        mInvoiceMap.put("ygbraddress",ygbraddress);//详细地址

        showLoadingDialog();
        OkHttp3Utils.getInstance().doPost(Constants.ADDRECEIPT_URL, null, mInvoiceMap)

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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"code":"0","msg":"开票成功"}
                        dismissLoadingDialog();

                        if(!TextUtils.isEmpty(s)){
                            Gson gson=new Gson();
                            UpData upData = gson.fromJson(s, UpData.class);
                            if(upData.getData().getSuccess().equals("1")){
                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(upData.getMsg())
//                                .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                InvoiceEvent invoiceEvent=new InvoiceEvent();
                                                invoiceEvent.setMsg("MakeInvoice");
                                                EventBus.getDefault().post(invoiceEvent);

                                                finish();
                                            }
                                        })
                                        .show();
                            } else {

                                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(upData.getMsg())
//                                .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
//                                                finish();
                                            }
                                        })
                                        .show();

                            }

                        }


                    }
                });

    }
    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2).getPickerViewText() +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();

                tv_makeaddress.setText(tx);

                ygbrprovince= options1Items.get(options1).getAddcd();
                ygbrcity = options2Items.get(options1).get(options2).getAddcd();
                ygbmarea = options3Items.get(options1).get(options2).get(options3).getAddcd();

            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();

    }

    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
//        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据
//        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items.clear();
        options1Items.addAll(addressTree);
//        options1Items = addressTree;

        for (int i = 0; i < addressTree.size(); i++) {//遍历省份
            ArrayList<SubBeanX> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<SubBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < addressTree.get(i).getSub().size(); c++) {//遍历该省份的所有城市

                String CityName = addressTree.get(i).getSub().get(c).getAddname();
                String addcd = addressTree.get(i).getSub().get(c).getAddcd();

                SubBeanX sub = new SubBeanX();
                sub.setAddname(CityName);
                sub.setAddcd(addcd);

                CityList.add(sub);//添加城市

                ArrayList<SubBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (addressTree.get(i).getSub().get(c).getAddname() == null || addressTree.get(i).getSub().get(c).getSub().size() == 0) {
                    SubBean subb = new SubBean();
                    subb.setAddcd("");
                    subb.setAddname("");
                    City_AreaList.add(subb);
                } else {
                    for (int d = 0; d < addressTree.get(i).getSub().get(c).getSub().size(); d++) {//该城市对应地区所有数据
                        SubBean subbb = new SubBean();
                        String AreaName = addressTree.get(i).getSub().get(c).getSub().get(d).getAddname();
                        String addcd1 = addressTree.get(i).getSub().get(c).getSub().get(d).getAddcd();
                        subbb.setAddname(AreaName);
                        subbb.setAddcd(addcd1);
                        City_AreaList.add(subbb);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

//        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


}
