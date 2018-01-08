package com.ztd.yyb.activity.my;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.yanzhenjie.nohttp.BasicBinary;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.ParentsAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.BeanR.RenZhen;
import com.ztd.yyb.bean.beanHome.AddressTreeBean;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.DatadictionaryBean;
import com.ztd.yyb.bean.beanHome.DmlistBean;
import com.ztd.yyb.bean.beanHome.SubBean;
import com.ztd.yyb.bean.beanHome.SubBeanX;
import com.ztd.yyb.bean.beanMyCerti.CerarrayBean;
import com.ztd.yyb.bean.beanMyCerti.DataCertifi;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.evenbean.PerfEvent;
import com.ztd.yyb.imagepicker.ImagePickerAdapter;
import com.ztd.yyb.imagepickerr.ImagePickerAdaptertwo;
import com.ztd.yyb.net.HttpListener;
import com.ztd.yyb.net.HttpResponseListener;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
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
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 认证完善资料
 * Created by  on 2017/3/20.
 */

public class PerfectInformationActicity extends BaseActivity implements
        ImagePickerAdapter.OnRecyclerViewItemClickListener, ImagePickerAdaptertwo.OnRecyclerViewItemClickListenertwo {

    public static final int IMAGE_ITEM_ADD = -1, REQUEST_CODE_SELECT = 1001;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.ll_owner_xueli)
    LinearLayout ll_owner_xueli;

    @BindView(R.id.iconthree)
    ImageView iconthree;

    @BindView(R.id.iconone)
    ImageView iconone;

    @BindView(R.id.icontwo)
    ImageView icontwo;

    @BindView(R.id.tv_persex)
    TextView tv_persex;

    @BindView(R.id.tv_worktimexueli)
    TextView tv_worktimexueli;

    @BindView(R.id.tv_owner_address)
    TextView tv_owner_address;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.tv_pername)
    EditText tv_pername;

    @BindView(R.id.tv_work)
    EditText tv_work;

    @BindView(R.id.tv_worktime)
    TextView tv_worktime;

    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_jobtime)
    TextView tv_jobtime;
    @BindView(R.id.tv_jobaddress)
    TextView tv_jobaddress;

    @BindView(R.id.edit_shfzh)
    EditText edit_shfzh;


    @BindView(R.id.img_user)
    ImageView img_user;


    String type = "";
    String ygbmjob = "";//职业
    String ygbmname = "";//姓名
    String ygbmjobage = "";//工龄
    String ygbmarea = "";//省市区
    String ygbmaddress = "";//详细地址
    String ygbmpin = "";//身份证号
    String ygbmsexx;
    String ygbmeducation;
    String logo;
    String ygbmcid = "";

    AppConfigBean appConfigBean;
    List<AddressTreeBean> addressTree;
    List<DatadictionaryBean> datadictionary;
    RecyclerView shfzh_recyclerview;

    private ImagePickerAdapter adapter;
    private ImagePickerAdaptertwo adaptertwo;

    private List<ImageItem> selImageList;      // 资料
    private List<ImageItem> selImageListtwo;   // 身份证

    private int maxImgCount = 3;               // 允许选择图片最大数

    private int num;

//    private Map<String, String> mMyinfoMap = new HashMap<>();
    private ArrayList<AddressTreeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<SubBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<SubBean>>> options3Items = new ArrayList<>();
    private List<ChangBean> mInfoList = new ArrayList<>();
    private ParentsAdapter mParentsAdapter;
    private Dialog mSelectHeadteacherDialog; //对话框

//    private boolean flag = false;//判断 之前是否有提交过身份证

    private Map<String, String> mMyCerMap = new HashMap<>();
    /**
     * 请求队列。
     */
    private RequestQueue mQueue;

    @Override
    protected void initViewsAndEvents() {

        initWidget();

        // 初始化请求队列，传入的参数是请求并发值。
        mQueue = NoHttp.newRequestQueue(1);

        Intent intent = getIntent();

        type = intent.getStringExtra("FLAG");

        if (type.equals("2")) {
            mTvTitle.setText("师傅资料认证");
            ll_owner_xueli.setVisibility(View.GONE);
        } else if (type.equals("4")) {

            ll_owner_xueli.setVisibility(View.VISIBLE);

            mTvTitle.setText("家教资料认证");
            tv_job.setText("目前职业");
            tv_jobtime.setText("在岗工龄");
            tv_jobaddress.setText("现住地址");
        }

//        getDataIn();

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");

        Gson gson = new Gson();

        if (!TextUtils.isEmpty(myappconfig)) {
            appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
//            gzdjlist = appConfigBean.getData().getGzdjlist();
            datadictionary = appConfigBean.getData().getDatadictionary();
            addressTree = appConfigBean.getData().getAddressTree();
        }

        initSelectHeadteacherDialog();

        initJsonData();

        getData();//获取证书编号ygbmcid

    }

    private void getData() {

        String userid = (String) SPUtil.get(USERID, "");
        mMyCerMap.clear();
        mMyCerMap.put("userid", userid);
        showLoadingDialog();
        OkHttp3Utils.getInstance().doPost(Constants.GETCERTIFI_URL, null, mMyCerMap)
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
//                        ygbmcstate：状态：0审核中    1认证通过    2认证未通过  3未认证
//                        ygbmctype   类型：1：业主   2：师傅      3：家长   4家教

                        if (!TextUtils.isEmpty(s)) {// int flag = 3;//   0审核中--1认证通过--2认证未通过 --3未认证

                            Gson gson = new Gson();

                            DataCertifi dataCertifi = gson.fromJson(s, DataCertifi.class);

                            if (dataCertifi.getData().getSuccess().equals("1")) {

                                List<CerarrayBean> cerarray = dataCertifi.getData().getCerarray();

                                for (int i = 0; i < cerarray.size(); i++) {

                                    String ygbmctype = cerarray.get(i).getYgbmctype();

                                    String ygbmci = cerarray.get(i).getYgbmcid();

                                    if(type.equals("2")){//师傅

                                        if (ygbmctype.equals("2")) {
                                            ygbmcid = ygbmci;
                                        }

                                    }else {

                                        if (ygbmctype.equals("4")) {
                                            ygbmcid = ygbmci;
                                        }

                                    }

                                }

                            }

                        }

                    }
                });

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
        return R.layout.lay_perfectinformation;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    private void initWidget() {


        selImageList = new ArrayList<>();
        selImageListtwo = new ArrayList<>();


        selImageListtwo.clear();
        selImageList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        shfzh_recyclerview = (RecyclerView) findViewById(R.id.shfzh_recyclerview);

        adaptertwo = new ImagePickerAdaptertwo(this, selImageListtwo, maxImgCount);
        adaptertwo.setOnItemClickListener(this);
        shfzh_recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        shfzh_recyclerview.setHasFixedSize(true);
        shfzh_recyclerview.setAdapter(adaptertwo);


    }

    @OnClick({R.id.lin_back, R.id.btn_Labor_rob_order, R.id.img_user,
            R.id.ll_owner_address, R.id.ll_owner_classsex, R.id.imageView2,R.id.ll_owner_school,R.id.ll_owner_xueli})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.ll_owner_xueli:
                num=3;
                mInfoList.clear();
                for (int i = 0; i < datadictionary.size(); i++) {
                    if ("xl".equals(datadictionary.get(i).getZldm())) {//学历
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
            case R.id.ll_owner_school:
                num=1;
                mInfoList.clear();
                for (int i = 0; i < datadictionary.size(); i++) {
                    if ("gl".equals(datadictionary.get(i).getZldm())) {//工龄
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

            case R.id.imageView2:

                String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息
                editText.setText("" + locationDescribe);

                break;

            case R.id.ll_owner_classsex:
                num=2;

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

            case R.id.ll_owner_address:

                ShowPickerView();

                break;


            case R.id.btn_Labor_rob_order:

//                robData();

                ygbmname = tv_pername.getText().toString().trim();

                ygbmjob = tv_work.getText().toString().trim();

//                ygbmjobage = tv_worktime.getText().toString().trim();

                ygbmarea = tv_owner_address.getText().toString().toString();

                ygbmaddress = editText.getText().toString().trim();

                ygbmpin = edit_shfzh.getText().toString().trim();

//        ygbmsexx=tv_persex.getText().toString().trim();//不能传中文

//                ygbmeducation = tv_worktimexueli.getText().toString().trim();

                if (TextUtils.isEmpty(ygbmname)) {
                    ToastUtil.show(this, "请填写姓名");
                    return;
                }

                if (ygbmpin.length() != 18) {
                    ToastUtil.show(this, "请输入18位身份证号码");
                    return;
                }

                if (TextUtils.isEmpty(ygbmsexx)) {
                    ToastUtil.show(this, "请选择性别");
                    return;
                }

                if (TextUtils.isEmpty(ygbmjob)) {
                    ToastUtil.show(this, "请填写职业");
                    return;
                }

                if (TextUtils.isEmpty(ygbmjobage)) {
                    ToastUtil.show(this, "请选择工龄");
                    return;
                }

                if (type.equals("4")) {
                    if (TextUtils.isEmpty(ygbmeducation)) {
                        ToastUtil.show(this, "请选择学历");
                        return;
                    }
                }

                if (TextUtils.isEmpty(ygbmarea)) {
                    ToastUtil.show(this, "请选择省市区");
                    return;
                }

                if (TextUtils.isEmpty(ygbmaddress)) {
                    ToastUtil.show(this, "请输入详细地址");
                    return;
                }

//                if (!flag) {//之前有提交过身份证的，不用再要求再次提交
                    if (selImageListtwo.size() == 0 || selImageListtwo.size() < 3) {
                        ToastUtil.show(mContext, "请上传三张有效身份证正反面手持照片");
                        return;
                    }
//                }


                uploadMultiListFile();

                break;
            case R.id.img_user:

                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra("FLAG", "3");
                startActivityForResult(intent, REQUEST_CODE_SELECT);


                break;

        }
    }

    /**
     * 显示对话框
     */
    private void showSelectHeadteacherDialog() {

        if (mSelectHeadteacherDialog == null) {

            initSelectHeadteacherDialog();

        } else {
            mSelectHeadteacherDialog.show();
        }
        mParentsAdapter.notifyDataSetChanged();
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
        mParentsAdapter = new ParentsAdapter(PerfectInformationActicity.this, mInfoList, new ParentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismissSelectHeadteacherDialog();

                String name = mInfoList.get(position).getName();
                String id = mInfoList.get(position).getId();

                if(num==1){//工龄
                    tv_worktime.setText(name);
                    ygbmjobage=id;
                }else  if(num==2){//性别
                    tv_persex.setText(name);
                    ygbmsexx = id;
                }else  if(num==3){
                    ygbmeducation=id;
                    tv_worktimexueli.setText(name);
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

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2).getPickerViewText() +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();

                tv_owner_address.setText(tx);

//                ygbdgprovince= options1Items.get(options1).getAddcd();
//                ygbdgcity = options2Items.get(options1).get(options2).getAddcd();
//                ygbdgarea = options3Items.get(options1).get(options2).get(options3).getAddcd();

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

    String imageGrid;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e("requestCode","="+requestCode);
//        Log.e("resultCode","="+resultCode);

        if (resultCode == 1004) {
            //添加图片返回
            if (data != null && requestCode == 1001) {

                 imageGrid = data.getStringExtra("ImageGrid");

//                Log.e("imageGrid","="+imageGrid);

                if (imageGrid == null) {
                    imageGrid = (String) SPUtil.get("ImageGrid", "");
                }


                if (imageGrid.equals("1")) {

                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    selImageListtwo.addAll(images);
                    adaptertwo.setImages(selImageListtwo);//身份证

                    setImage1();

//                    Log.e("添加图片返回身份证","添加图片返回身份证");

                } else if (imageGrid.equals("2")) {


                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);//证书

                    setImage();


//                    Log.e("添加图片返回证书","添加图片返回证书");

                } else if (imageGrid.equals("3")) {

//                    Log.e("添加图片返头像","添加图片返头像");

                    ArrayList<ImageItem> images =
                            (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                    String path = images.get(0).path;

                    logo = path;

                    Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.list_icon_user)
                            .placeholder(R.mipmap.list_icon_user)
                            .transform(new GlideCircleTransform(mContext)).into(img_user);


                }

            }


        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {

            String imageGrid = data.getStringExtra("ImageGrid");
            if (imageGrid == null) {

                return;
            }

            if (imageGrid.equals("1")) {

                if (data != null && requestCode == REQUEST_CODE_PREVIEW) {

                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                    selImageListtwo.clear();
                    selImageListtwo.addAll(images);
                    adaptertwo.setImages(selImageListtwo);

                }


            } else if (imageGrid.equals("2")) {

                //预览图片返回
                if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }

            } else if (imageGrid.equals("3")) {

                ArrayList<ImageItem> images =
                        (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                String path = images.get(0).path;


                Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.list_icon_user)
                        .placeholder(R.mipmap.list_icon_user)
                        .transform(new GlideCircleTransform(mContext)).into(img_user);

            }

        }


    }


    final List<File> files = new ArrayList<>();//TODO 证书压缩

    private void setImage() {

        for (int i = 0; i < selImageList.size(); i++) {

            File file = new File(selImageList.get(i).path);

            Luban.get(this)
                    .load(file)                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {
                            //TODO 压缩开始前调用，可以在方法内启动 loading UI
//                            Log.e("onStart","onStart");
                        }
                        @Override
                        public void onSuccess(File file) {
                            //TODO 压缩成功后调用，返回压缩后的图片文件
                            files.add(file);
//                            Log.e("onSuccess","onSuccess");
                        }
                        @Override
                        public void onError(Throwable e) {
                            //TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩


        }

    }

    List<File> file1 = new ArrayList<>();//三个不同的key 传3个不同的

    private void setImage1() {

        for (int i = 0; i < selImageListtwo.size(); i++) {

        if(selImageListtwo.size() != 3){
            return;
        }

            File file = new File(selImageListtwo.get(i).path);

            Luban.get(this)
                    .load(file)                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {
                            //TODO 压缩开始前调用，可以在方法内启动 loading UI
//                            Log.e("onStart","onStart");
                        }
                        @Override
                        public void onSuccess(File file) {
                            //TODO 压缩成功后调用，返回压缩后的图片文件
                            file1.add(file);
//                            Log.e("onSuccess","onSuccess");
                        }
                        @Override
                        public void onError(Throwable e) {
                            //TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩


        }

    }





    @Override
    public void onItemClick(View view, int position) {


        switch (position) {

            case IMAGE_ITEM_ADD:

//                打开选择,本次允许选择的数量

                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra("FLAG", "2");
                startActivityForResult(intent, REQUEST_CODE_SELECT);

                break;
            default:

                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra("FLAG", "2");
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);

                break;
        }

    }

    @Override
    public void onItemClicktwo(View view, int position) {


        switch (position) {

            case IMAGE_ITEM_ADD:
//                打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageListtwo.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra("FLAG", "1");
                startActivityForResult(intent, REQUEST_CODE_SELECT);

                break;

            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adaptertwo.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra("FLAG", "1");
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }


    }

    /**
     * 一个key上传多个文件，上传文件list。
     */
    private void uploadMultiListFile() {

        Request<String> request = NoHttp.createStringRequest(Constants.UPDATEINFO_URL, RequestMethod.POST);

        // 添加普通参数。
        String userid = (String) SPUtil.get(USERID, "");

        request.add("ygbmid", userid);//用户id
        request.add("ygbmname", ygbmname);//姓名
        request.add("ygbmjob", ygbmjob);//职业
        request.add("ygbmjobage", ygbmjobage);//工龄
        request.add("type", type);//证书类型  2：师傅             4家教
        request.add("ygbmaddress", ygbmaddress);//地址
        request.add("ygbmarea", ygbmarea);//省市区
        request.add("ygbmpin", ygbmpin);//身份证号
        request.add("ygbmsex", ygbmsexx);//性别：1:男 0:女
        request.add("ygbmcid", ygbmcid);// 证书ID
        request.add("utype", "2");// 2 上传证书           1 修改个人资料

        if("4".equals(type)){
            request.add("ygbmeducation", ygbmeducation);// 证书ID
        }


        if (logo != null) {

            BasicBinary binary1 = new FileBinary(new File(logo));
            request.add("logo", binary1);
        }


        if (selImageListtwo.size() == 3) {
//
            BasicBinary binary1 = new FileBinary(file1.get(0));
//            binaries1.add(binary1);
            request.add("appendfixz", binary1);

            BasicBinary binary2 = new FileBinary(file1.get(1));
//            binaries2.add(binary2);
            request.add("appendfixf", binary2);

            BasicBinary binary3 = new FileBinary(file1.get(2));
//            binaries3.add(binary3);
            request.add("appendfixh", binary3);

        }

//      List<File> files = new ArrayList<>();//TODO 证书图片压缩

        List<Binary> binaries = new ArrayList<>();

//        for (int i = 0; i < selImageList.size(); i++) {
//            File file = new File(selImageList.get(i).path);
//            BasicBinary binary = new FileBinary(file);
//            binaries.add(binary);
//        }

        for (int i = 0; i < files.size(); i++) {
            BasicBinary binary = new FileBinary(files.get(i));
            binaries.add(binary);
        }


        request.add("appendfix", binaries);




        showLoadingDialog();

//        String contentType = request.getContentType();
//        String cacheKey = request.getCacheKey();
//        String s = request.toString();
//        Log.e("request","="+s);
//        Log.e("cacheKey","="+cacheKey);



        request(0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadingDialog();

                String s = response.get();

                Log.e("onSucceed", "=" + s);

                if (!TextUtils.isEmpty(s)) {

                    Gson gson = new Gson();

                    RenZhen renZhen = gson.fromJson(s, RenZhen.class);

                    if (renZhen.getData().getSuccess().equals("1")) {

                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("提交成功请等待审核!")
//                                .setContentText("Won't be able to recover this file!")
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        PerfEvent messageEvent=new PerfEvent();
                                        messageEvent.setMsg("true");
                                        EventBus.getDefault().post(messageEvent);
                                        finish();
                                    }
                                })
                                .show();


                    } else {

                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("提交失败，请重试!")
//                                .setContentText("Won't be able to recover this file!")
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();

                    }

                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadingDialog();
//                String s = response.get();
//                Log.e("onFailed", "=" + s);

            }
        }, false, true);


    }


    /**
     * 发起请求。
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean
            isLoading) {

//        request.setCancelSign(object);

        if(mQueue == null){
            return;
        }

        mQueue.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));

    }

}
