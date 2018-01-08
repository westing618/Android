package com.ztd.yyb.activity.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.ParentsAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanHome.AddressTreeBean;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.DatadictionaryBean;
import com.ztd.yyb.bean.beanHome.DmlistBean;
import com.ztd.yyb.bean.beanHome.KmlistBean;
import com.ztd.yyb.bean.beanHome.SubBean;
import com.ztd.yyb.bean.beanHome.SubBeanX;
import com.ztd.yyb.bean.beanHome.SubjectBean;
import com.ztd.yyb.bean.beanPare.DataPare;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.util.CheckFormatUtils;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
 * Created by 家长发布需求 on 2017/3/16.
 */

public class ParentsReleaseDemandActivity extends BaseActivity {

    private static int NUM = 0; // 1;//科目  2;//年级 3;//性别 4;//学历 5;//开课 6;//时长

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_pare_address)
    TextView mTvpareAddress;


    @BindView(R.id.tv_shichang)
    TextView tv_shichang;

    @BindView(R.id.tv_conmetime)
    TextView tv_conmetime;

    @BindView(R.id.tv_parentschool)
    TextView tv_parentschool;

    @BindView(R.id.tv_paresex)
    TextView tv_paresex;

    @BindView(R.id.tv_pare_class)
    TextView tv_pare_class;

    @BindView(R.id.tv_pare_sub)
    TextView tv_pare_sub;

    @BindView(R.id.edit_content)
    EditText edit_content;

    @BindView(R.id.pare_et)
    EditText pare_et;

    @BindView(R.id.pare_etname)
    EditText pare_etname;

    @BindView(R.id.edit_phone)
    EditText edit_phone;

    @BindView(R.id.edit_address_detail)
    EditText edit_address_detail;

    AppConfigBean appConfigBean;
    List<DatadictionaryBean> datadictionary;
    String ygbdgcreatetime; // 创建时间
    String ygbdgaddress; // 详细地址
    String ygbdgmounttime; // 上岗时间
    String ygbdgremark; // 需求说明
    String ygbdgsubject; // 科目
    String ygbdgsex; // 性别

    String ygbdgmoment; // 学历要求
    String ygbdgdays; //
    String ygbtime; //
    String ygbdgtel; //
    String ygbdgcontacts; //
    String ygbeducation; //


    private Dialog mSelectHeadteacherDialog; //对话框
    private ParentsAdapter mParentsAdapter;


    private List<ChangBean> mInfoList = new ArrayList<>();
    private Map<String, String> mAddJiajiaoGongMap = new HashMap<>();

    private ArrayList<AddressTreeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<SubBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<SubBean>>> options3Items = new ArrayList<>();
    List<AddressTreeBean> addressTree;

    private String ygbdgprovince;
    private String ygbdgcity;
    private String ygbdgarea;
    private String straddress;
    //时间选择器
    TimePickerView pvTime;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("发布需求");

        mInfoList.clear();

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");

        Gson gson = new Gson();

        if (!TextUtils.isEmpty(myappconfig)) {
            appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            datadictionary = appConfigBean.getData().getDatadictionary();
            addressTree = appConfigBean.getData().getAddressTree();
        }

        initSelectHeadteacherDialog();
//        initSelectDateDialog();

        initJsonData();

        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                tv_conmetime.setText(getTime(date));
            }
        })
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择开课时间")//标题文字
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .build();
        pvTime.setDate(Calendar.getInstance());


        String phone = (String) SPUtil.get(USERPHONE, "");//用户电话
        String ygbmname = (String) SPUtil.get("ygbmname", "");//用户姓名
        edit_phone.setText(""+phone);
        pare_etname.setText(""+ygbmname);
    }

    private static String getTime(Date date) {
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return  f.format(date);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_parentsreleasedemand;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.ll_pare_subject, R.id.lin_parentclass, R.id.lin_parentsex, R.id.lin_parentschool,
            R.id.lin_parenttime, R.id.ll_pare_address, R.id.btn_Labor_rob_order, R.id.ll_shichang,R.id.imageView2})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_Labor_rob_order:
                addJiajiao();
                break;
            case R.id.lin_back:
                finish();
                break;

            case R.id.imageView2:

                String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息
                edit_address_detail.setText(""+locationDescribe);

                break;

            case R.id.lin_parentclass://年级

                NUM = 2;

//                mInfoList.clear();
//                List<KmlistBean> kmlist = appConfigBean.getData().getKmlist();
//                for (int i = 0; i < kmlist.size(); i++) {
//                    ChangBean chanbean = new ChangBean();
//                    String classname = kmlist.get(i).getClassname();
//                    String classcd = kmlist.get(i).getClasscd();
//                    chanbean.setName(classname);
//                    chanbean.setId(classcd);
//                    mInfoList.add(chanbean);
//                }

                mInfoList.clear();

                for (int i = 0; i < datadictionary.size(); i++) {
                    if ("hznj".equals(datadictionary.get(i).getZldm())) {//孩子年级
                        List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                        for (int ii = 0; ii < dmlist.size(); ii++) {
                            ChangBean chanbean = new ChangBean();
                            String classname = dmlist.get(ii).getDmmc();
                            String classcd = dmlist.get(ii).getDm();
                            chanbean.setName(classname);
                            chanbean.setId(classcd);
                            mInfoList.add(chanbean);
                        }
                    }
                }


                showSelectHeadteacherDialog();


                break;

            case R.id.ll_pare_subject://科目

                String s = tv_pare_class.getText().toString();

                if (s.equals("请选择孩子年级")) {
                    ToastUtil.show(mContext, "请选择孩子年级");
                    return;
                }

                String substring = s.substring(0,2);
//                Log.e("substring","="+substring);

                mInfoList.clear();

                NUM = 1;
                List<KmlistBean> kmlist2 = appConfigBean.getData().getKmlist();
                for (int i = 0; i < kmlist2.size(); i++) {
                    if (substring.equals(kmlist2.get(i).getClassname())) {
                        List<SubjectBean> subject = kmlist2.get(i).getSubject();
                        for (int ii = 0; ii < subject.size(); ii++) {

                            ChangBean chanbean = new ChangBean();

                            String classname = subject.get(ii).getSubjectname();
                            String classcd = subject.get(ii).getSubjectcd();
                            String subjectprice = subject.get(ii).getSubjectprice();

                            chanbean.setPrice(subjectprice);
                            chanbean.setName(classname);
                            chanbean.setId(classcd);

                            mInfoList.add(chanbean);
                        }
                    }
                }

                showSelectHeadteacherDialog();

                break;

            case R.id.lin_parentsex:

                NUM = 3;
                mInfoList.clear();
                for (int i = 0; i < datadictionary.size(); i++) {
                    if ("xb".equals(datadictionary.get(i).getZldm())) {//性别
                        List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                        for (int ii = 0; ii < dmlist.size(); ii++) {
                            ChangBean chanbean = new ChangBean();
                            String classname = dmlist.get(ii).getDmmc();
                            String classcd = dmlist.get(ii).getDm();
                            chanbean.setName(classname);
                            chanbean.setId(classcd);
                            mInfoList.add(chanbean);
                        }
                    }
                }


                showSelectHeadteacherDialog();

                break;
            case R.id.lin_parentschool://学历
                NUM = 4;
                mInfoList.clear();

                for (int i = 0; i < datadictionary.size(); i++) {

                    if ("xl".equals(datadictionary.get(i).getZldm())) {//学历
                        List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                        for (int ii = 0; ii < dmlist.size(); ii++) {
                            ChangBean chanbean = new ChangBean();
                            String dmmc = dmlist.get(ii).getDmmc();
                            String dm = dmlist.get(ii).getDm();
                            chanbean.setName(dmmc);
                            chanbean.setId(dm);
                            mInfoList.add(chanbean);
                        }
                    }
                }

                showSelectHeadteacherDialog();

                break;
            case R.id.lin_parenttime:

                NUM = 5;
                pvTime.show();

                break;

            case R.id.ll_shichang:

                NUM = 6;
                mInfoList.clear();

                for (int i = 0; i < datadictionary.size(); i++) {
                    if ("sksc".equals(datadictionary.get(i).getZldm())) {//上课时长
                        List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                        for (int ii = 0; ii < dmlist.size(); ii++) {

                            ChangBean chanbean = new ChangBean();

                            String classname = dmlist.get(ii).getDmmc();

                            String classcd = dmlist.get(ii).getDm();

                            chanbean.setName(classname);
                            chanbean.setId(classcd);

                            mInfoList.add(chanbean);

                        }
                    }
                }


                showSelectHeadteacherDialog();

                break;
            case R.id.ll_pare_address:

                ShowPickerView();

                break;
        }
    }


    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2).getPickerViewText()+
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                mTvpareAddress.setText(tx);

                ygbdgprovince= options1Items.get(options1).getAddcd();
                ygbdgcity = options2Items.get(options1).get(options2).getAddcd();
                ygbdgarea = options3Items.get(options1).get(options2).get(options3).getAddcd();
                straddress=tx;

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
        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
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
        if(addressTree == null){
            return;
        }

        options1Items.clear();
        options1Items.addAll(addressTree);
//        options1Items = addressTree;

        for (int i=0;i<addressTree.size();i++){//遍历省份
            ArrayList<SubBeanX> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<SubBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<addressTree.get(i).getSub().size(); c++){//遍历该省份的所有城市

                String CityName = addressTree.get(i).getSub().get(c).getAddname();
                String addcd = addressTree.get(i).getSub().get(c).getAddcd();

                SubBeanX sub=new SubBeanX();
                sub.setAddname(CityName);
                sub.setAddcd(addcd);

                CityList.add(sub);//添加城市

                ArrayList<SubBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (addressTree.get(i).getSub().get(c).getAddname() == null||addressTree.get(i).getSub().get(c).getSub().size()==0) {
                    SubBean subb=new SubBean();
                    subb.setAddcd("");
                    subb.setAddname("");
                    City_AreaList.add(subb);
                }else {
                    for (int d=0; d < addressTree.get(i).getSub().get(c).getSub().size(); d++) {//该城市对应地区所有数据
                        SubBean subbb=new SubBean();
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
    private void addJiajiao() {

        String userid = (String) SPUtil.get(USERID, "");


        ygbdgaddress = edit_address_detail.getText().toString().trim();
        ygbdgmounttime = tv_conmetime.getText().toString().trim();
        ygbdgremark = edit_content.getText().toString().trim();



            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String format = dateFormat.format(date);
            ygbdgcreatetime = format;


        ygbdgdays = pare_et.getText().toString().trim();

        ygbdgcontacts = pare_etname.getText().toString().trim();

        ygbdgtel = edit_phone.getText().toString().trim();

        String myclass = tv_pare_class.getText().toString().trim();

        String Address = mTvpareAddress.getText().toString().trim();

        if (TextUtils.isEmpty(Address) || Address.equals("点击选择省市区")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请点击选择省市区");
            return;
        }

        if (TextUtils.isEmpty(ygbdgcreatetime)) {//系统当前时间
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请输入开课时间");
            return;
        }


        if (TextUtils.isEmpty(ygbdgaddress) || ygbdgaddress.equals("点击选择地址")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请输入详细地址");
            return;
        }

        if (TextUtils.isEmpty(myclass) || myclass.equals("请选择孩子年级")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请选择孩子年级");
            return;
        }

        if (TextUtils.isEmpty(ygbdgsubject) || ygbdgsubject.equals("请选择孩子的需求科目")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请选择孩子的需求科目");
            return;
        }
        if (TextUtils.isEmpty(ygbdgsex) || ygbdgsex.equals("请选择性别要求")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请选择性别要求");
            return;
        }
        if (TextUtils.isEmpty(ygbdgmoment) || ygbdgmoment.equals("请选择老师学历要求")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请选择老师学历要求");
            return;
        }
        if (TextUtils.isEmpty(ygbdgmounttime) || ygbdgmounttime.equals("请选择开课时间")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请选择开课时间");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date a = null;// 我自己定义的日期,也是2012/11/25日
        try {
            a = sdf.parse(ygbdgmounttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date b = new Date();// 系统的日期,今天是2012/11/25日
//        Log.e("a.before(b)-->",""+ a.before(b));

        if(a.before(b)){
            ToastUtil.show(this, "请选择合适的开课时间");
            return;
        }

        if (TextUtils.isEmpty(ygbtime) || ygbtime.equals("请选择上课时长")) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请选择上课时长");
            return;
        }
        if (TextUtils.isEmpty(ygbdgdays)) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请输入上课天数");
            return;
        }

        if (TextUtils.isEmpty(ygbdgcontacts)) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请输入联系人");
            return;
        }
        if (TextUtils.isEmpty(ygbdgtel)) {
            ToastUtil.show(ParentsReleaseDemandActivity.this, "请输入联系电话");
            return;
        }
        if (CheckFormatUtils.isMobileNO(ygbdgtel) == false) {// 判断手机号码格式
            ToastUtil.show(this, "手机格式错误");
            return;
        }

//        if (TextUtils.isEmpty(ygbdgremark) || ygbdgremark.equals("请输入您的具体需求，字数在140字内.")) {
//            ToastUtil.show(ParentsReleaseDemandActivity.this, "请输入需求说明");
//            return;
//        }

        mAddJiajiaoGongMap.clear();
        mAddJiajiaoGongMap.put("ygbmid", userid);
        mAddJiajiaoGongMap.put("ygbdgcreatetime", ygbdgcreatetime);//创建时间
        mAddJiajiaoGongMap.put("ygbdgaddress", ygbdgaddress);//详细地址
        mAddJiajiaoGongMap.put("ygbdgmounttime", ygbdgmounttime);//开课时间
        mAddJiajiaoGongMap.put("ygbdgremark", ygbdgremark);//需求说明
        mAddJiajiaoGongMap.put("ygbdgsubject", ygbdgsubject);//科目
        mAddJiajiaoGongMap.put("ygbdgsex", ygbdgsex);//性别要求

        mAddJiajiaoGongMap.put("ygbdgmoment", ygbeducation);//年级
        mAddJiajiaoGongMap.put("ygbeducation", ygbdgmoment);//学历要求

        mAddJiajiaoGongMap.put("ygbdgdays", ygbdgdays);//上课天数
        mAddJiajiaoGongMap.put("ygbtime", ygbtime);//上课时长
        mAddJiajiaoGongMap.put("ygbdgtel", ygbdgtel);//联系电话
        mAddJiajiaoGongMap.put("ygbdgcontacts", ygbdgcontacts);//联系人

        mAddJiajiaoGongMap.put("ygbdgprovince", ygbdgprovince);//省
        mAddJiajiaoGongMap.put("ygbdgcity", ygbdgcity);//市
        mAddJiajiaoGongMap.put("ygbdgarea", ygbdgarea);//区

        String latitude = (String) SPUtil.get("latitude", "");//纬度
        String longitude = (String) SPUtil.get("longitude", "");//经度

        mAddJiajiaoGongMap.put("ygbdglng", longitude);//经度118.1400180000
        mAddJiajiaoGongMap.put("ygbdglat", latitude);//纬度

        mAddJiajiaoGongMap.put("ygbdaddamount", "0");//加价金额

//        mAddJiajiaoGongMap.put("ygbcid", "");//优惠券id
//        mAddJiajiaoGongMap.put("ygbdmode", "3");//付款方式：付款方式：1：支付宝 2：微信 3：余额
//        Log.e("ygbdgprovince",""+ygbdgprovince);//省
//        Log.e("ygbdgcity",""+ygbdgcity);//市
//        Log.e("ygbdgarea",""+ygbdgarea);//区
//        Log.e("ygbdgsubject","="+ygbdgsubject);//科目  ygbdgsubject
//        Log.e("ygbdgmoment","="+ygbeducation);//年级要求
//        Log.e("ygbeducation","="+ygbdgmoment);//学历要求
//        Log.e("ygbdgmounttime","="+ygbdgmounttime);//开课时间
//        Log.e("ygbtime","="+ygbtime);//上课时长

        mAddJiajiaoGongMap.put("straddress", straddress+ygbdgaddress);// :省市区（名称）+详细地址

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.ADDJJ_ORDER_URL, null, mAddJiajiaoGongMap)
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
                    public void onNext(String s) {//
                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            DataPare dataPare = gson.fromJson(s, DataPare.class);

                            if (dataPare.getData().getSuccess().equals("1")) {

                                String orderid = dataPare.getData().getOrderid();
                                double ygbdgamount = dataPare.getData().getYgbdgamount();

                                SPUtil.put("putorderid",""+orderid);
                                SPUtil.put("puttype",""+"2");
                                SPUtil.put("JIAJIA", "1");

                                Intent intent=new Intent(mContext,PaymentActivity.class);
                                intent.putExtra("type","1");
                                intent.putExtra("orderid",""+orderid);
                                intent.putExtra("ygbdgamount",""+ygbdgamount);
                                intent.putExtra("ygbdaddprice",""+"0");//家教没有加价的，默认传0
                                startActivity(intent);

//                                finish();

                            } else if (dataPare.getData().getSuccess().equals("0")) {

                                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(dataPare.getMsg())
                                        .setContentText("请重试")
                                        .show();
                            }
                        }

                        //


                    }
                });

    }

//    /**
//     * 显示选择日期对话框
//     */
//    private void showSelectDateDialog() {
//
//        if (mSelectDateDialog == null) {
////            initSelectDateDialog();
//        }
//        mSelectDateDialog.show();
//    }

//    /**
//     * 初始化选择时间弹出框
//     */
//    private void initSelectDateDialog() {
//        mSelectDateDialog = new Dialog(this, R.style.dialog_bottom_full);
//        mSelectDateDialog.setCanceledOnTouchOutside(true);
//        mSelectDateDialog.setCancelable(true);
//        Window window = mSelectDateDialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.bottom_dialog_animation);
//
//        View view = View.inflate(this, R.layout.attendance_lay_select_date, null);
//
//        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
////        final     TimePicker timePicker = (TimePicker) view.findViewById(R.id.timepicker);
////        timePicker.setIs24HourView(true);
//
//        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mYear = datePicker.getYear();
//                mMonth = datePicker.getMonth() + 1;
//                mDay = datePicker.getDayOfMonth();
//
////                String S1=""+mYear;
////                String S2=""+mMonth;
////                String S3=""+mDay;
////
////                tv_conmetime.setText(""+S1+S2+S3);
//
//
//                int i = new ParentsReleaseDemandActivity.Test().sizeOfInt(mMonth); //判断 月日是几位
//                int y = new ParentsReleaseDemandActivity.Test().sizeOfInt(mDay);
//                String month = "" + i;
//                String day = "" + y;
//
//                if (i == 1) {
//                    month = "0" + mMonth;
//                } else {
//                    month = "" + mMonth;
//                }
//                if (y == 1) {
//                    day = "0" + mDay;
//                } else {
//                    day = "" + mDay;
//                }
//                tv_conmetime.setText(mYear + "" + month + "" + day);
//
//                dismissSelectDateDialog();
//
//            }
//        });
//
//        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismissSelectDateDialog();
//            }
//        });
//        Calendar c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);
//        // 初始化DatePicker组件，初始化时指定监听器
//        datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
//
//            }
//        });
//        window.setContentView(view);
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
//    }

//    /**
//     * 隐藏选择班级对话框
//     */
//    private void dismissSelectDateDialog() {
//        if (mSelectDateDialog != null) {
//            mSelectDateDialog.dismiss();
//        }
//    }

    /**
     * 显示对话框
     */
    private void showSelectHeadteacherDialog() {

        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.show();
            mParentsAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 初始化对话框
     */
    private void initSelectHeadteacherDialog() {

        mSelectHeadteacherDialog = new Dialog(this, R.style.dialog_bottom_full);
        mSelectHeadteacherDialog.setCanceledOnTouchOutside(true);
        mSelectHeadteacherDialog.setCancelable(true);
        Window window = mSelectHeadteacherDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_animation);
        View view = View.inflate(this, R.layout.attendance_lay_select_class, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.select_class_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//使用RecyclerView 默认动画效果
        mParentsAdapter = new ParentsAdapter(ParentsReleaseDemandActivity.this, mInfoList, new ParentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismissSelectHeadteacherDialog();

                String name = mInfoList.get(position).getName();
                String id = mInfoList.get(position).getId();

                Log.e("id",""+id);

                if (NUM == 1) {//科目

                    tv_pare_sub.setText(name);
                    ygbdgsubject=id;

                } else if (NUM == 2) {//年级

                    tv_pare_class.setText(name);
                    ygbeducation=id;

                } else if (NUM == 3) {//性别
                    tv_paresex.setText(name);
                    ygbdgsex = id;
                } else if (NUM == 4) {//学历

                    tv_parentschool.setText(name);
                    ygbdgmoment = id;

                } else if (NUM == 5) {//开课时间

                } else if (NUM == 6) {//时长

                    ygbtime=id;
                    tv_shichang.setText(name);

                }


            }
        });

        recyclerView.setAdapter(mParentsAdapter);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }

    /**
     * 隐藏对话框
     */
    private void dismissSelectHeadteacherDialog() {
        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.dismiss();
        }
    }

//    public class Test {
//        final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
//                99999999, 999999999, Integer.MAX_VALUE};
//
//        int sizeOfInt(int x) {
//            for (int i = 0; ; i++)
//                if (x <= sizeTable[i])
//                    return i + 1;
//        }
//    }
}
