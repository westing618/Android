package com.ztd.yyb.activity.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.ParentsAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanHome.AddressTreeBean;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.DatadictionaryBean;
import com.ztd.yyb.bean.beanHome.DmlistBean;
import com.ztd.yyb.bean.beanHome.GzdjlistBean;
import com.ztd.yyb.bean.beanHome.SubBean;
import com.ztd.yyb.bean.beanHome.SubBeanX;
import com.ztd.yyb.bean.beanPare.DataPare;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.imagepicker.ImagePickerAdapter;
import com.ztd.yyb.util.CheckFormatUtils;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
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
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;
import static com.ztd.yyb.activity.LoginActivity.USERPHONE;

/**
 * Created by 业主发布需求
 * on 2017/3/16.
 */

public class OwnerReleaseDemandActivity extends BaseActivity implements
        View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1, REQUEST_CODE_SELECT = 1001;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static int NUM = 0; // 1;//工种  2;//加价金额
    final List<File> files = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_shuru)
    LinearLayout ll_shuru;
    @BindView(R.id.ll_shuruprice)
    LinearLayout ll_shuruprice;
    @BindView(R.id.order_regular)
    TextView order_regular;//普通
    @BindView(R.id.order_repair)
    TextView order_repair;//抢修
    @BindView(R.id.tv_owner_address)
    TextView mTvtownerAddress;
    @BindView(R.id.tv_owner_class)
    TextView tv_owner_class;
    @BindView(R.id.tv_owner_subject)
    TextView tv_owner_subject;
    @BindView(R.id.tv_owner_addprice)
    TextView tv_owner_addprice;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.edit_num)
    EditText edit_num;
    @BindView(R.id.edit_days)
    EditText edit_days;
    @BindView(R.id.edit_perple)
    EditText edit_perple;
    @BindView(R.id.edit_phone)
    EditText edit_phone;

    @BindView(R.id.edit_content)
    EditText edit_content;

    @BindView(R.id.lay_shuru)
    View lay_shuru;

    @BindView(R.id.et_shurukind)
    EditText et_shurukind;
    @BindView(R.id.edit_kindprice)
    EditText edit_kindprice;

    @BindView(R.id.lay_shuruprice)
    View lay_shuruprice;

    //    ArrayList<Bitmap> list = new ArrayList(3);
    ArrayList<String> arryList = new ArrayList(3);
    AppConfigBean appConfigBean;
    List<GzdjlistBean> gzdjlist;
    List<DatadictionaryBean> datadictionary;
    //时间选择器
    TimePickerView pvTime;
    List<AddressTreeBean> addressTree;

    private Dialog mSelectHeadteacherDialog;//对话框
    private ParentsAdapter mParentsAdapter;      //
    private String picPath;
    private String ygbdtype = "1";//类型 1普通 2抢修
    private Map<String, String> mAddYongGongMap = new HashMap<>();
    private List<ChangBean> mInfoList = new ArrayList<>();
    private String ygbdaddress;
    private String ygbdkind;
    private String straddress;

    private String newkind="";
    private String newprice="";

    private String ygbdtimearrival;
    private String ygbdworkers;
    private String ygbddays;
    private String ygbdcontacts;
    private String ygbdtel;
    private String ygbdremark;
    private String ygbdgprovince;
    private String ygbdgcity;
    private String ygbdgarea;
    private String ygbdaddprice = "0";//加价金额
    private ImagePickerAdapter adapter;
    private List<ImageItem> selImageList;      //当前选择的所有图片
    private ArrayList<AddressTreeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<SubBeanX>> options2Items = new ArrayList<>();

    boolean adtype=true;//用户选择 工种类型替他的时候，做特殊   (普通true 和其他false)

    private ArrayList<ArrayList<ArrayList<SubBean>>> options3Items = new ArrayList<>();

    @Override
    protected void initViewsAndEvents() {
//        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mTvTitle.setText("发布需求");


        inData();
        initSelectHeadteacherDialog();
        initJsonData();
        setTimePicker();
        setImagePicker();

        EventBus.getDefault().register(this);

    }
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(ChangBean message){
        String name = message.getName();
        String price = message.getPrice();
        String id = message.getId();

        tv_owner_subject.setText(name+"(￥"+price+"/天)");
        ygbdkind = id;//工种

        if (ygbdkind.equals("37")) {//当用户选择其他的时候，显示输入
            adtype=false;
            ll_shuru.setVisibility(View.VISIBLE);
            ll_shuruprice.setVisibility(View.VISIBLE);
            lay_shuru.setVisibility(View.VISIBLE);
            lay_shuruprice.setVisibility(View.VISIBLE);
        } else {
            adtype=true;
            ll_shuru.setVisibility(View.GONE);
            ll_shuruprice.setVisibility(View.GONE);
            lay_shuru.setVisibility(View.GONE);
            lay_shuruprice.setVisibility(View.GONE);
        }

    }

    private void inData() {

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(myappconfig)) {
            appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            gzdjlist = appConfigBean.getData().getGzdjlist();
            datadictionary = appConfigBean.getData().getDatadictionary();
            addressTree = appConfigBean.getData().getAddressTree();
        }


        String phone = (String) SPUtil.get(USERPHONE, "");//用户电话
        String ygbmname = (String) SPUtil.get("ygbmname", "");//用户姓名

        edit_phone.setText("" + phone);
        edit_perple.setText("" + ygbmname);


    }

    private void setImagePicker() {

        selImageList = new ArrayList<>();
        selImageList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new ImagePickerAdapter(this, selImageList, 3);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private void setTimePicker() {
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_owner_class.setText(getTime(date));
            }
        })
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择开工时间")//标题文字
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .build();
        pvTime.setDate(Calendar.getInstance());

    }

    private String getTime(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(date);
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
        if (addressTree == null) {
            return;
        }

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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_ownerreleasedemand;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.ll_owner_address, R.id.ll_owner_subject,
            R.id.ll_owner_class, R.id.btn_Labor_rob_order, R.id.tvcalan,
            R.id.order_regular, R.id.order_repair, R.id.ll_owner_addprice, R.id.imageView2})//
    public void onClick(View view) {

//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.list_btn_add);

        switch (view.getId()) {

            case R.id.order_regular:

                order_regular.setTextColor(getResources().getColor(R.color.color_white));
                order_repair.setTextColor(getResources().getColor(R.color.color_66));

                order_regular.setBackground(getResources().getDrawable(R.drawable.bg_common));
                order_repair.setBackground(getResources().getDrawable(R.drawable.bg_repair));

                ygbdtype = "1";

                break;
            case R.id.order_repair:

                order_regular.setTextColor(getResources().getColor(R.color.color_66));
                order_repair.setTextColor(getResources().getColor(R.color.color_white));

                order_regular.setBackground(getResources().getDrawable(R.drawable.bg_repair));
                order_repair.setBackground(getResources().getDrawable(R.drawable.bg_common));

                ygbdtype = "2";

                break;

            case R.id.btn_Labor_rob_order:

                addYongGongtwo();

                break;
            case R.id.lin_back:
                finish();
                break;
            case R.id.imageView2:

                String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息
                edit_address.setText("" + locationDescribe);

                break;

            case R.id.ll_owner_address:

                ShowPickerView();

                break;

            case R.id.ll_owner_subject:

//                NUM = 1;
//
//                mInfoList.clear();
//
//                mParentsAdapter.notifyDataSetChanged();
//
//                for (int i = 0; i < gzdjlist.size(); i++) {
//
//                    ChangBean chanbean = new ChangBean();
//                    String classname = gzdjlist.get(i).getYgblcname();
//                    String classcd = gzdjlist.get(i).getYgblcid();
//                    String ygblcprice = gzdjlist.get(i).getYgblcprice();
//                    chanbean.setPrice(ygblcprice);
//                    chanbean.setName(classname);
//                    chanbean.setId(classcd);
//                    mInfoList.add(chanbean);
//
//                }
//
//                showSelectHeadteacherDialog();
                Intent intent=new Intent(this,ReleaseDemandActivity.class);
                intent.putExtra("flag","1");
                startActivity(intent);


                break;
            case R.id.ll_owner_addprice://加价金额

                NUM = 2;

                mInfoList.clear();
                mParentsAdapter.notifyDataSetChanged();

                for (int i = 0; i < datadictionary.size(); i++) {

                    if ("jjje".equals(datadictionary.get(i).getZldm())) {//上课时长

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

            case R.id.ll_owner_class://开工时间


                pvTime.show();

                break;


        }
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2).getPickerViewText() +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                mTvtownerAddress.setText(tx);

                ygbdgprovince = options1Items.get(options1).getAddcd();
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
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void addYongGongtwo() {

        String userid = (String) SPUtil.get(USERID, "");
        ygbdaddress = edit_address.getText().toString().trim();
        String trim = mTvtownerAddress.getText().toString().trim();
//        ygbdkind = tv_owner_subject.getText().toString().trim();

        ygbdtimearrival = tv_owner_class.getText().toString().trim();

        ygbdworkers = edit_num.getText().toString().trim();

        ygbddays = edit_days.getText().toString().trim();

        ygbdcontacts = edit_perple.getText().toString().trim();

        ygbdtel = edit_phone.getText().toString().trim();

        ygbdremark = edit_content.getText().toString().trim();

        newkind=  et_shurukind.getText().toString().trim();

        newprice= edit_kindprice.getText().toString().trim();


        if (TextUtils.isEmpty(trim) || trim.equals("点击选择省市区")) {
            ToastUtil.show(this, "点击选择省市区");
            return;
        }
        if (TextUtils.isEmpty(ygbdaddress) || ygbdaddress.equals("请输入详细地址")) {
            ToastUtil.show(this, "请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(ygbdkind) || ygbdkind.equals("点击选择工种")) {
            ToastUtil.show(this, "请点击选择工种");
            return;
        }

        if(!adtype){
            if (TextUtils.isEmpty(newkind) ) {
                ToastUtil.show(this, "您已经选择其他工种请输入工种名称");
                return;
            }
            if (TextUtils.isEmpty(newprice) ) {
                ToastUtil.show(this, "您已经选择其他工种请输入工种价格");
                return;
            }
        }

        if (TextUtils.isEmpty(ygbdtimearrival) || ygbdtimearrival.equals("点击选择时间")) {
            ToastUtil.show(this, "请选择时间");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date a = null;// 我自己定义的日期,也是2012/11/25日
        try {
            a = sdf.parse(ygbdtimearrival);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date b = new Date();// 系统的日期,今天是2012/11/25日
//        Log.e("a.before(b)-->",""+ a.before(b));
        if(a.before(b)){
            ToastUtil.show(this, "请选择合适的开工时间");
            return;
        }

        if (TextUtils.isEmpty(ygbdworkers)) {
            ToastUtil.show(this, "请输入出工人数");
            return;
        }

        if (TextUtils.isEmpty(ygbddays)) {
            ToastUtil.show(this, "请输入用工天数");
            return;
        }
        if (TextUtils.isEmpty(ygbdcontacts)) {
            ToastUtil.show(this, "请输入联系人");
            return;
        }
        if (TextUtils.isEmpty(ygbdtel)) {
            ToastUtil.show(this, "请输入手机号");
            return;
        }

        if (CheckFormatUtils.isMobileNO(ygbdtel) == false) {// 判断手机号码格式
            ToastUtil.show(this, "手机格式错误");
            return;
        }

//        if (TextUtils.isEmpty(ygbdremark)) {
//            ToastUtil.show(this, "请输入需求说明");
//            return;
//        }


        Map<String, String> params = new HashMap<>();

        params.put("ygbdtype", ygbdtype); //需求类型：1：普通订单 2：抢修订单
        params.put("ygbmid", userid);//用户id
        params.put("ygbdaddress", ygbdaddress);//详细地址

        params.put("ygbdtimearrival", ygbdtimearrival);//施工时间
        params.put("ygbdremark", ygbdremark);//需求说明
        params.put("ygbdworkers", ygbdworkers);//出工人数
        params.put("ygbddays", ygbddays);//用工天数
        params.put("ygbdtel", ygbdtel);//联系电话
        params.put("ygbdcontacts", ygbdcontacts);//联系人

        params.put("ygbdprovince", ygbdgprovince);//省
        params.put("ygbdcity", ygbdgcity);//市
        params.put("ygbdarea", ygbdgarea);//区

        params.put("ygbdaddprice", ygbdaddprice);//加价金额


        String latitude = (String) SPUtil.get("latitude", "");//纬度
        String longitude = (String) SPUtil.get("longitude", "");//经度

        params.put("ygbdlng", longitude);// 经度118.1400180000
        params.put("ygbdlat", latitude); // 纬度24.4960660000
        params.put("ygbdkind", ygbdkind);//工种


        params.put("straddress", straddress+ygbdaddress);// :省市区（名称）+详细地址



        if(adtype){
            params.put("addtype", "0");//0 普通
            params.put("newkind", "");//用户输入的工种
            params.put("newprice", "");//用户输入的价格
        }else {
            params.put("addtype", "1");// 1  其他
            params.put("newkind", newkind);//用户输入的工种
            params.put("newprice", newprice);//用户输入的价格
        }

        showLoadingDialog();

        OkHttp3Utils.getInstance().postMultipart(Constants.ADDYG_ORDER_URL, params, "appendfix", files)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {

                        dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(String s) {

                        Log.e("onNext", "=" + s);

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {
//
                            Gson gson = new Gson();

                            DataPare dataPare = gson.fromJson(s, DataPare.class);

                            if (dataPare.getData().getSuccess().equals("1")) {

                                String orderid = dataPare.getData().getOrderid();

                                double ygbdgamount = dataPare.getData().getYgbdamount();

                                SPUtil.put("putorderid", "" + orderid);
                                SPUtil.put("puttype", "" + "1");

                                SPUtil.put("JIAJIA", "1");

                                Intent intent = new Intent(mContext, PaymentActivity.class);
                                intent.putExtra("type", "0");
                                intent.putExtra("orderid", "" + orderid);
                                intent.putExtra("ygbdgamount", "" + ygbdgamount);
//                                intent.putExtra("ygbdaddprice", "" + ygbdaddprice);

                                startActivity(intent);

                                //                        finish();

                            } else if (dataPare.getData().getSuccess().equals("0")) {

                                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(dataPare.getMsg())
                                        .setContentText("请重试")
                                        .show();

                            }

                        }


                    }
                });


    }

    /**
     * 显示对话框
     */
    private void showSelectHeadteacherDialog() {
        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.show();
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
        mParentsAdapter = new ParentsAdapter(OwnerReleaseDemandActivity.this, mInfoList,
                new ParentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                dismissSelectHeadteacherDialog();

                if (NUM == 1) {

                    String name = mInfoList.get(position).getName();

                    String price = mInfoList.get(position).getPrice();

                    tv_owner_subject.setText(name+"(￥"+price+"/天)");

                    ygbdkind = mInfoList.get(position).getId();//工种

                    if (ygbdkind.equals("37")) {//当用户选择其他的时候，显示输入

                        adtype=false;

                        ll_shuru.setVisibility(View.VISIBLE);
                        ll_shuruprice.setVisibility(View.VISIBLE);
                        lay_shuru.setVisibility(View.VISIBLE);
                                lay_shuruprice.setVisibility(View.VISIBLE);
                    } else {

                        adtype=true;
                        ll_shuru.setVisibility(View.GONE);
                        ll_shuruprice.setVisibility(View.GONE);
                        lay_shuru.setVisibility(View.GONE);
                        lay_shuruprice.setVisibility(View.GONE);
                    }

                } else if (NUM == 2) {

                    tv_owner_addprice.setText(mInfoList.get(position).getName());
                    ygbdaddprice = mInfoList.get(position).getName();//加价金额

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

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(3 - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {

                ArrayList<ImageItem> images = (ArrayList<ImageItem>)
                        data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                selImageList.addAll(images);

                setImage();

                adapter.setImages(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        }

    }

    private void setImage() {


//        final List<File> files = new ArrayList<>();

        for (int i = 0; i < selImageList.size(); i++) {

            File file = new File(selImageList.get(i).path);

            Luban.get(this)
                    .load(file)                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {
                            //TODO 压缩开始前调用，可以在方法内启动 loading UI

//                            Log.e("onStart", "onStart");
                        }

                        @Override
                        public void onSuccess(File file) {
                            //TODO 压缩成功后调用，返回压缩后的图片文件
                            files.add(file);
//                            Log.e("onSuccess", "onSuccess");
                        }

                        @Override
                        public void onError(Throwable e) {
                            //TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩


        }

    }


}
