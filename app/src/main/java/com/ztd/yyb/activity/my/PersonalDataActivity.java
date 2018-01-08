package com.ztd.yyb.activity.my;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
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
import com.ztd.yyb.bean.beanHome.SubBean;
import com.ztd.yyb.bean.beanHome.SubBeanX;
import com.ztd.yyb.bean.beanLnfo.DataInfo;
import com.ztd.yyb.bean.beanLnfo.UserinfoBean;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.bean.beanUpData.DataUpin;
import com.ztd.yyb.evenbean.PerfEvent;
import com.ztd.yyb.imagepicker.ImagePickerAdapter;
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
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.ztd.yyb.R.id.edit_detail;
import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;


/**
 * 个人信息
 * Created by  on 2017/3/20.
 */

public class PersonalDataActivity extends BaseActivity implements
        ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1,
            REQUEST_CODE_SELECT = 1001;
    private static final int MSG_LOAD_DATA = 0x0001;
//    private static final int MSG_LOAD_SUCCESS = 0x0002;
//    private static final int MSG_LOAD_FAILED = 0x0003;
    final List<File> files = new ArrayList<>();
    @BindView(R.id.tv_top_right)
    TextView mTvRight;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_shfzh)
    EditText edit_shfzh;
    @BindView(edit_detail)
    EditText edit_detailaddress;
    @BindView(R.id.tv_peraddress)
    TextView tv_peraddress;
    @BindView(R.id.tv_persex)
    TextView tv_persex;
    @BindView(R.id.img_peruser)
    ImageView img_peruser;
    AppConfigBean appConfigBean;
    List<DatadictionaryBean> datadictionary;
    List<AddressTreeBean> addressTree;
    String ygbmsexx;
    String ygbmtel;
    String ygbmpin;
    String ygbmarea;
    String ygbmaddress;
    String ygbmname;
    String mypath;

//    private boolean flag = false;//编辑 /true 保存

    private ImagePickerAdapter adapter;
    private List<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数
    private ParentsAdapter mParentsAdapter;
    private Dialog mSelectHeadteacherDialog; //对话框
    private ArrayList<AddressTreeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<SubBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<SubBean>>> options3Items = new ArrayList<>();
    //    private boolean isLoaded = false;//记录已经有头像图片
    private Map<String, String> mMyinfoMap = new HashMap<>();
    private List<ChangBean> mInfoList = new ArrayList<>();
    private Thread thread;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                FutureTarget<File> future = Glide.with(mContext)
                                        .load(mypath)
                                        .downloadOnly(500, 500);

                                try {

                                    File cacheFile = future.get();
                                    mypath = cacheFile.getAbsolutePath(); //本地保存 头像 图片路径

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        thread.start();
                    }
                    break;

            }
        }
    };

    @Override
    protected void initViewsAndEvents() {

        initMultiImagePicker();
        initSelectHeadteacherDialog();


        edit_phone.setEnabled(false);
//        edit_shfzh.setEnabled(false);
//        edit_detailaddress.setEnabled(false);
//        edit_name.setEnabled(false);

        initJsonData();

    }

    /**
     * 初始化多张图片选择
     */
    private void initMultiImagePicker() {

        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        getUserData();

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");

        Gson gson = new Gson();

        if (!TextUtils.isEmpty(myappconfig)) {
            appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            datadictionary = appConfigBean.getData().getDatadictionary();
            addressTree = appConfigBean.getData().getAddressTree();
        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_personal;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.tv_top_right,
            R.id.img_peruser, R.id.ll_persex,
            R.id.ll_owner_address, R.id.imageView2})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.imageView2:

                String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息
                edit_detailaddress.setText("" + locationDescribe);

                break;

            case R.id.ll_owner_address:
//                if (flag) {
                    ShowPickerView();
//                }
                break;

            case R.id.ll_persex:

//                if (flag) {
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
//                }

                break;

            case R.id.img_peruser:

//                if (flag) {
//                    Log.e("可以修改","可以修改");
                    selImageList.clear();
                    ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                    Intent intent = new Intent(this, ImageGridActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SELECT);
//                }

                break;

            case R.id.tv_top_right:

//                if (flag) {

                    String muygbmsexx = tv_persex.getText().toString();//用户没有重新选择
                    if (muygbmsexx != null) {
                        if (muygbmsexx.equals("男")) {
                            ygbmsexx = "1";
                        } else if (muygbmsexx.equals("女")) {//1:男 0:女
                            ygbmsexx = "0";
                        }
                    }
                    ygbmtel = edit_phone.getText().toString().trim();

                    ygbmpin = edit_shfzh.getText().toString().trim();

                    ygbmaddress = edit_detailaddress.getText().toString().trim();

                    ygbmarea = tv_peraddress.getText().toString().trim();

                    ygbmname = edit_name.getText().toString().trim();

//                    Log.e("4444","="+edit_name.getText().toString().trim());


                    if (ygbmsexx == null) {
                        ToastUtil.show(PersonalDataActivity.this, "请选择性别");
                        return;
                    }

                    if (ygbmname == null || ygbmname.equals("")) {

                        ToastUtil.show(PersonalDataActivity.this, "请填写姓名");
                        return;
                    }

                    if (ygbmpin != null) {
                        if (ygbmpin.length() != 18) {
                            ToastUtil.show(PersonalDataActivity.this, "请输入18位身份证号码");
                            return;
                        }
                    }

                    if (TextUtils.isEmpty(ygbmarea)) {
                        ToastUtil.show(PersonalDataActivity.this, "请选择省市区");
                        return;
                    }
                    if (TextUtils.isEmpty(ygbmaddress)) {
                        ToastUtil.show(PersonalDataActivity.this, "请填写详细地址");
                        return;
                    }


//                    mTvRight.setText("编辑");
////                    flag = false;
//                    edit_phone.setEnabled(false);
//                    edit_shfzh.setEnabled(false);
//                    edit_detailaddress.setEnabled(false);
//                    edit_name.setEnabled(false);


//                } else {

                    mTvRight.setText("保存");
//                    flag = true;
//                    edit_shfzh.setEnabled(true);
//                    edit_detailaddress.setEnabled(true);
//                    edit_name.setEnabled(true);

//                }

//                if (!flag) {

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定修改资料")
//                            .setContentText("Won't be able to recover this file!")
                            .setConfirmText("确定")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    sDialog.dismissWithAnimation();

                                    upData();

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
//
//                }


                break;

        }
    }

    private void upData() {


//        if (!TextUtils.isEmpty(ygbmtel) ) {
//            if (CheckFormatUtils.isMobileNO(ygbmtel) == false) {// 判断手机号码格式
//                ToastUtil.show(this, "手机格式错误");
//            return;
//            }
//        }


        String userid = (String) SPUtil.get(USERID, "");

        Map<String, String> params = new HashMap<>();


        params.put("ygbmid", userid);//用户id

        params.put("ygbmsex", ygbmsexx);//性别：1:男 0:女

        params.put("ygbmname", ygbmname);//姓名

        params.put("ygbmtel", ygbmtel);//手机号

        params.put("ygbmpin", ygbmpin);//身份证

        params.put("ygbmarea", ygbmarea);//省市区

        params.put("ygbmaddress", ygbmaddress);//地址

        params.put("ygbmcid", "");// 证书ID

        params.put("utype", "1");//1 修改个人资料 2 上传证书


//        List<File> files = new ArrayList<>();

        if (selImageList.size() == 0) {

            if (!TextUtils.isEmpty(mypath)) {

                File file = new File(mypath);

                files.add(file);

//                Log.e("图片路径", "mypath=" + mypath);

            }

        }

        showLoadingDialog();

        OkHttp3Utils.getInstance().postMultipart(Constants.UPDATEINFO_URL, params, "logo", files)
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
                               public void onNext(String s) {//{"data":{"success":"0"},"code":"0","msg":"失败"}

//                                   Log.e("889", "JSAJFI=" + s);

                                   dismissLoadingDialog();

                                   if (!TextUtils.isEmpty(s)) {

                                       Gson gson = new Gson();

                                       DataUpin dataUpin = gson.fromJson(s, DataUpin.class);

                                       if (dataUpin.getData().getSuccess().equals("1")) {

                                           ToastUtil.show(PersonalDataActivity.this, "修改成功");

                                           getUserData();

                                           PerfEvent pergeEvent = new PerfEvent();
                                           pergeEvent.setMsg("true");
                                           EventBus.getDefault().post(pergeEvent);


                                       } else {

                                           ToastUtil.show(PersonalDataActivity.this, "修改失败请重试");

                                       }

                                   }
                               }
                           }

                );
    }

    private void getUserData() {

        showLoadingDialog();

        mMyinfoMap.clear();

        String userid = (String) SPUtil.get(USERID, "");
        mMyinfoMap.put("userid", "" + userid);

        OkHttp3Utils.getInstance().doPost(Constants.GRTINFO_URL, null, mMyinfoMap)
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

                            Gson gson = new Gson();
                            DataInfo dataInfo = gson.fromJson(s, DataInfo.class);

                            if (dataInfo.getData().getSuccess().equals("1")) {

                                UserinfoBean userinfo = dataInfo.getData().getUserinfo();

                                String phone = userinfo.getYgbmtel();

                                String ygbmsex = userinfo.getYgbmsex();


                                ygbmtel = phone;

                                edit_phone.setText(phone);

                                String ygbmname = userinfo.getYgbmname();

                                if (ygbmname != null) {
                                    edit_name.setText("" + ygbmname);
                                }


                                if (ygbmsex != null) {
                                    if (ygbmsex.equals("1")) {//性别：1:男 0:女
                                        tv_persex.setText("男");
                                    } else if (ygbmsex.equals("0")) {
                                        tv_persex.setText("女");
                                    }
                                }

                                String ygbmpin = userinfo.getYgbmpin();//身份证
                                if (ygbmpin == null) {
                                    edit_shfzh.setHint("请填写二代身份证号");
                                } else {
                                    edit_shfzh.setText("" + ygbmpin);
                                }
                                String ygmbarea = userinfo.getYgbmarea();//省市区
                                String ygbmaddress = userinfo.getYgbmaddress();//详细地址
                                if (ygmbarea == null) {
                                    tv_peraddress.setHint("点击选择省市区");
                                } else {
                                    tv_peraddress.setText("" + ygmbarea);
                                }
                                if (ygbmaddress == null) {
                                    edit_detailaddress.setHint("请输入详细地址");
                                } else {
                                    edit_detailaddress.setText("" + ygbmaddress);
                                }

                                String ygbmlogo = userinfo.getYgbmlogo();// defaultlogo.png默认 头像

                                if (ygbmlogo == null || ygbmlogo.equals("")) {

                                    mypath = "http://api.yogobei.com/logo/defaultlogo.png";

                                } else {

                                    String test = Constants.BASE_URL + "logo/" + ygbmlogo;

                                    mypath = test;

                                    Glide.with(mContext).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .error(R.mipmap.list_icon_user)
                                            .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                                            .placeholder(R.mipmap.list_icon_user)
                                            .transform(new GlideCircleTransform(mContext)).into(img_peruser);
                                }

                                mHandler.sendEmptyMessage(MSG_LOAD_DATA);

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
        mParentsAdapter = new ParentsAdapter(PersonalDataActivity.this, mInfoList, new ParentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismissSelectHeadteacherDialog();
                String name = mInfoList.get(position).getName();
                String id = mInfoList.get(position).getId();
                tv_persex.setText("" + name);
                ygbmsexx = id;

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
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
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
        if (resultCode == 1004) {
            //添加图片返回
            if (data != null && requestCode == 1001) {

                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                selImageList.addAll(images);

                adapter.setImages(selImageList);

                if (images.size() > 0) {

                    String path = images.get(0).path;

                    setImage();

                    Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.list_icon_user)
                            .placeholder(R.mipmap.list_icon_user)
                            .transform(new GlideCircleTransform(mContext)).into(img_peruser);

                }

            }
        } else if (resultCode == 1005) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);


            }
        }
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

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2).getPickerViewText() +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                tv_peraddress.setText(tx);
                ygbmarea = tx;
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

                            Log.e("onStart", "onStart");
                        }

                        @Override
                        public void onSuccess(File file) {
                            //TODO 压缩成功后调用，返回压缩后的图片文件
                            files.add(file);
                            Log.e("onSuccess", "onSuccess");
                        }

                        @Override
                        public void onError(Throwable e) {
                            //TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩


        }

    }

}
