package com.ztd.yyb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.my.PerfectInformationActicity;
import com.ztd.yyb.activity.order.GrabOrderSuccessActivity;
import com.ztd.yyb.activity.order.LaborDemandDetailsActivity;
import com.ztd.yyb.activity.order.SchoolNeedsDetailsActivity;
import com.ztd.yyb.bean.beanMainT.OrderJjinfoBean;
import com.ztd.yyb.bean.beanMainf.OrderYginfo;
import com.ztd.yyb.bean.beanOrd.OrdSuFull;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.fragment.MianFragment;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * Created by  on 2017/3/15.
 */

public class MianFrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    public List<OrderYginfo> mYonggongList;
    public List<OrderJjinfoBean> mTeachList;
    private View mHeaderView;
    private View mFooterView;
    private Context context;
    private Boolean type;//true 用工  flase 学堂
    private Map<String, String> mLaborSchoolMap = new HashMap<>();

    public MianFrAdapter(Context context, List<OrderYginfo> mYonggongList,
                         List<OrderJjinfoBean> mTeachList, Boolean type) {
        this.mYonggongList = mYonggongList;
        this.mTeachList = mTeachList;
        this.context = context;
        this.type = type;
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL; //2;  说明是不带有header和footer的
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ListHolder(mFooterView);
        }
        View layout = null;

        if (type) { //true 用工  flase 学堂
            layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_yongg, parent, false);
        } else {
            layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_tu, parent, false);
        }
        return new ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {

            if (holder instanceof ListHolder) {//这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了

                if (type) {//true 用工  flase 学堂

                    String ygbdtitle = mYonggongList.get(position - 1).getYgbdtitle();//标题
                    String ygbdaddress = mYonggongList.get(position - 1).getYgbdaddress();//详细地址
                    String ygbdkind = mYonggongList.get(position - 1).getYgblcname();//工种
                    String ygbdcreatetime = mYonggongList.get(position - 1).getYgbdcreatetime();//创建发布时间
                    String ygbdtimearrival = mYonggongList.get(position - 1).getYgbdtimearrival();//施工时间
                    double ygbddays = mYonggongList.get(position - 1).getYgbddays();//用工天数
                    String ygbdtype = mYonggongList.get(position - 1).getYgbdtype();//1：普通订单 2：抢修订单
                    double totalcount = mYonggongList.get(position - 1).getTotalcount();//总金额
                    String ygblcprice = mYonggongList.get(position - 1).getYgblcprice();
                    double addprice = mYonggongList.get(position - 1).getYgbdaddprice();


                    ((ListHolder) holder).tv_home_price.setText("价格：" + ygblcprice + "/天");

                        if (addprice==0.00) {
                            ((ListHolder) holder).tv_add_price.setText("");
                        } else {
                            ((ListHolder) holder).tv_add_price.setText("+￥" + addprice);
                        }


                    String province = mYonggongList.get(position - 1).getYgbdprovince();
                    String city = mYonggongList.get(position - 1).getYgbdcity();
                    String area = mYonggongList.get(position - 1).getYgbdarea();

                    ((ListHolder) holder).tv_home_address.setText(province + city + area + ygbdaddress);

                    String yglogo = mYonggongList.get(position - 1).getYglogo();

                    String test = Constants.BASE_URL + "logo/" + yglogo;


                    Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.list_icon_user)
                            .placeholder(R.mipmap.list_icon_user)
                            .transform(new GlideCircleTransform(context)).into(((ListHolder) holder).imageView_yongg);


                    ((ListHolder) holder).tv_home_daynum.setText("用工天数：" + ygbddays + "天");

                    ((ListHolder) holder).tv_home_time.setText(ygbdcreatetime);
                    ((ListHolder) holder).tv_home_starttime.setText("到场时间：" + ygbdtimearrival);
                    ((ListHolder) holder).tv.setText(ygbdtitle);

                    ((ListHolder) holder).tv_home_totalcount.setText("(共计：￥" + totalcount + "元)");


                    ((ListHolder) holder).tv_home_work.setText("工种：" + ygbdkind);


                    if (ygbdtype != null) {//ygbdtype
                        if (ygbdtype.equals("1")) {
                            ((ListHolder) holder).image_type.setImageResource(R.mipmap.list_icon_putong);
                        } else if (ygbdtype.equals("2")) {
                            ((ListHolder) holder).image_type.setImageResource(R.mipmap.list_icon_qiangxiu);
                        }


                    }
                    final      OrderYginfo orderYginfoBean = mYonggongList.get(position - 1);
//                    final OrderYginfoBean orderYginfoBean = mYonggongList.get(position - 1);

                    final int ygbdid = orderYginfoBean.getYgbdid();
                    final String ygboid = orderYginfoBean.getYgboid();

                    ((ListHolder) holder).lldemand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(context, LaborDemandDetailsActivity.class);
                            intent.putExtra("ygboid", "" + ygbdid);
                            context.startActivity(intent);

                        }
                    });


                    final String type = (String) SPUtil.get(MianFragment.TYPE, "");

                    if (type.equals("1")) {
                        ((ListHolder) holder).btn_itemdemand.setVisibility(View.INVISIBLE);
                    } else if (type.equals("2")) {
                        ((ListHolder) holder).btn_itemdemand.setVisibility(View.VISIBLE);
                    }

                    ((ListHolder) holder).btn_itemdemand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String userid = (String) SPUtil.get(USERID, "");

                            robSchoolOrder(ygboid, userid, "1");

                        }
                    });
//----------------------------------------下面是 家教

                } else {

                    final OrderJjinfoBean orderJjinfoBean = mTeachList.get(position - 1);

                    final String ygbdgid = orderJjinfoBean.getYgbdgid();
                    final String ygboid = orderJjinfoBean.getYgboid();

                    ((ListHolder) holder).llhometutor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(context, SchoolNeedsDetailsActivity.class);
                            intent.putExtra("ygboid", "" + ygbdgid);
                            context.startActivity(intent);

                        }
                    });

                    String totalcount = mTeachList.get(position - 1).getTotalcount();//总金额

                    String ygbdtitle = mTeachList.get(position - 1).getInitiatename();//标题

                    String ygbdgaddress = mTeachList.get(position - 1).getYgbdgaddress();//地址

                    String ygbdgcreatetime = mTeachList.get(position - 1).getYgbdgcreatetime();//创建时间

                    String ygbdgmounttime = mTeachList.get(position - 1).getYgbdgmounttime();// 上岗时间

                    String ygbdgauditing = mTeachList.get(position - 1).getYgbdgauditing();//ygbdgauditing  状态：0 未接 1已接 2取消

                    String ygbeducation = mTeachList.get(position - 1).getYgbeducation();//学历要求

                    String ygbtime = mTeachList.get(position - 1).getYgbtime();//用工时长
                    String ygbdgdays = mTeachList.get(position - 1).getYgbdgdays();//用工天数
                    String ygbsctype = mTeachList.get(position - 1).getYgbdgmomentname();//年级类型  ygbsctype:年级类型：1 小学 2 初中 3
                    String yglogo = mTeachList.get(position - 1).getYglogo();//发布人 头像
                    String ygbscname = mTeachList.get(position - 1).getYgbscname();//科目

                    String ygblcprice = mTeachList.get(position - 1).getYgbscprice();//单价

                    String ygbdgsex = mTeachList.get(position - 1).getYgbdgsex();//性别

                    String ygbdgprovince = mTeachList.get(position - 1).getYgbdgprovince();
                    String ygbdgcity = mTeachList.get(position - 1).getYgbdgcity();
                    String ygbdgarea = mTeachList.get(position - 1).getYgbdgarea();

                    ((ListHolder) holder).tv_home_tutor_address.setText("" + ygbdgprovince + ygbdgcity + ygbdgarea + ygbdgaddress);

                    String test = Constants.BASE_URL + "logo/" + yglogo;


                    Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.list_icon_user)
                            .placeholder(R.mipmap.list_icon_user)
                            .transform(new GlideCircleTransform(context)).into(((ListHolder) holder).image_tutor);


                    ((ListHolder) holder).tv_home_price.setText("价格：￥" + ygblcprice + "/小时");
                    ((ListHolder) holder).tv_home_totalcount.setText("(共计：￥" + totalcount + "元)");
                    ((ListHolder) holder).tv_home_tutor_time.setText("" + ygbdgcreatetime);

                    ((ListHolder) holder).tv_home_tutor_starttime.setText("开课时间：" + ygbdgmounttime);

//                    ((ListHolder) holder).tv_home_tutor_starttime.setText("开课时间：" + ygbdgmounttime);

//                        ((ListHolder) holder).tv_studentname.setText(""+ygbdtitle);
                    ((ListHolder) holder).tv_subjects.setText("科目：" + ygbscname);

                    if (ygbdgsex != null) {
                        if (ygbdgsex.equals("0")) {
                            ((ListHolder) holder).tv_sex.setText("性别要求：" + "女");
                        } else if (ygbdgsex.equals("1")) {
                            ((ListHolder) holder).tv_sex.setText("性别要求：" + "男");
                        }
                    }

                    ((ListHolder) holder).tv_education.setText("学历要求：" + ygbeducation);
                    ((ListHolder) holder).tv_classtime.setText("上课时长：" + ygbtime);
                    ((ListHolder) holder).tv_classdays.setText("上课天数：" + ygbdgdays + "天");

                    ((ListHolder) holder).tv_class.setText("年级：" + ygbsctype);


                    String type = (String) SPUtil.get(MianFragment.TYPE, "");

                    if (type.equals("1")) {
                        ((ListHolder) holder).btn_hometutor.setVisibility(View.INVISIBLE);

                    } else if (type.equals("2")) {
                        ((ListHolder) holder).btn_hometutor.setVisibility(View.VISIBLE);
                    }

                    ((ListHolder) holder).btn_hometutor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String userid = (String) SPUtil.get(USERID, "");

                            robSchoolOrder(ygboid, userid, "2");

//                                Log.e("robSchoolOrder","robSchoolOrder");
//                                Log.e("ygboid","="+ygboid);
                        }
                    });

                }

                return;
            }
            return;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            if (type) {//true 用工  flase 学堂
                return mYonggongList.size();
            } else {
                return mTeachList.size();
            }

        } else if (mHeaderView == null && mFooterView != null) {
            if (type) {//true 用工  flase 学堂
                return mYonggongList.size() + 1;
            } else {
                return mTeachList.size() + 1;
            }
        } else if (mHeaderView != null && mFooterView == null) {
            if (type) {//true 用工  flase 学堂
                return mYonggongList.size() + 1;
            } else {
                return mTeachList.size() + 1;
            }
        } else {
            if (type) {//true 用工  flase 学堂
                return mYonggongList.size() + 2;
            } else {
                return mTeachList.size() + 2;
            }
        }
    }

    private void robSchoolOrder(final String ygbdid, String userid, final String type) {

        mLaborSchoolMap.clear();
        mLaborSchoolMap.put("userid", userid); //抢单用户id
        mLaborSchoolMap.put("orderid", ygbdid); //需求id
        mLaborSchoolMap.put("type", type);   //用工类型 1 师傅 2家教

        OkHttp3Utils.getInstance().doPost(Constants.ROB_ORDER_URL, null, mLaborSchoolMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    // {"data":{"success":"0"},"code":"异常","msg":"失败"}
                    @Override
                    public void onNext(String s) {//{"data":{"orderstate":"0","success":"1"},"msg":"抢单成功"}
                        //{"data":{"ygbmcstate":"3","orderstate":"1","success":"0"},"msg":"未认证或者认证不通过，不可抢单"}
                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            try {

                                OrdSuFull ordSuFull = gson.fromJson(s, OrdSuFull.class);

                                if (ordSuFull.getData().getSuccess().equals("1")) {

                                    SPUtil.put("putorderid", ygbdid);
                                    SPUtil.put("puttype", type);
                                    context.startActivity(new Intent(context, GrabOrderSuccessActivity.class));


                                    DemanEvet messageEvent = new DemanEvet();
                                    messageEvent.setMsg("paysucc");
                                    EventBus.getDefault().post(messageEvent);


                                } else if (ordSuFull.getData().getSuccess().equals("0")) {//ygbmcstate：状态：0审核中   1认证通过   2认证未通过   3未认证

                                    String ygbmcstate = ordSuFull.getData().getYgbmcstate();
                                    String msg = "";

                                    if (ygbmcstate.equals("0")) {

                                        msg = "证书审核中请等待";
                                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(msg)
//                                            .setContentText(orderSu.getMsg())
                                                .show();

                                    } else if (ygbmcstate.equals("3")) {

                                        msg = "资料未认证";

                                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(msg)
                                                .setContentText("马上去认证!")
                                                .setConfirmText("确定!")//确定
                                                .setCancelText("取消!")//取消
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {

                                                        sDialog.dismissWithAnimation();
//                                                    Log.e("确定","确定"); 1 师傅 2家教 ==========  2师傅  4老师

                                                        Intent intent = new Intent(context, PerfectInformationActicity.class);
                                                        if (type.equals("1")) {
                                                            intent.putExtra("FLAG", "2");
                                                        } else {
                                                            intent.putExtra("FLAG", "4");
                                                        }
                                                        context.startActivity(intent);

                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.cancel();
//                                                    Log.e("取消","取消");
                                                    }
                                                })
                                                .show();

                                    } else {

                                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("提示")
                                                .setContentText(ordSuFull.getMsg())
                                                .show();

                                    }

                                }


                            } catch (Exception e) {

                                ToastUtil.show(context, "数据异常");
                            }

                        }

                    }
                });


    }

    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder {

        TextView tv_home_tutor_time, tv_studentname, tv_class, tv_subjects, tv_sex, tv_add_price;
        TextView tv_education, tv_classtime, tv_classdays, tv_home_tutor_starttime, tv_home_tutor_address;
        TextView tv, tv_home_time, tv_home_work, tv_home_daynum, tv_home_starttime, tv_home_price, tv_home_address, tv_home_totalcount;

        ImageView image_type;
        LinearLayout lldemand;
        LinearLayout llhometutor;

        Button btn_itemdemand;
        Button btn_hometutor;

        ImageView imageView_yongg, image_tutor;

        public ListHolder(View itemView) {//如果是headerview或者是footerview,直接返回
            super(itemView);

            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }

            if (type) {//true 用工  flase 学堂

                btn_itemdemand = (Button) itemView.findViewById(R.id.btn_itemdemand);
                lldemand = (LinearLayout) itemView.findViewById(R.id.ll_itemdemand);
                tv = (TextView) itemView.findViewById(R.id.tv_title);
                tv_home_time = (TextView) itemView.findViewById(R.id.tv_home_time);
                tv_add_price = (TextView) itemView.findViewById(R.id.tv_add_price);

                tv_home_work = (TextView) itemView.findViewById(R.id.tv_home_work);
                tv_home_daynum = (TextView) itemView.findViewById(R.id.tv_home_daynum);
                tv_home_starttime = (TextView) itemView.findViewById(R.id.tv_home_starttime);
                tv_home_address = (TextView) itemView.findViewById(R.id.tv_home_address);
                image_type = (ImageView) itemView.findViewById(R.id.image_type);
                tv_home_price = (TextView) itemView.findViewById(R.id.tv_home_price);
                tv_home_totalcount = (TextView) itemView.findViewById(R.id.tv_home_totalcount);

                imageView_yongg = (ImageView) itemView.findViewById(R.id.imageView_yongg);

            } else {

                tv_home_totalcount = (TextView) itemView.findViewById(R.id.tv_home_totalcount);
                tv_home_price = (TextView) itemView.findViewById(R.id.tv_home_price);

                btn_hometutor = (Button) itemView.findViewById(R.id.btn_hometutor);
                llhometutor = (LinearLayout) itemView.findViewById(R.id.ll_itemhometutor);
                tv_home_tutor_time = (TextView) itemView.findViewById(R.id.tv_home_tutor_time);
//                    tv_studentname = (TextView)itemView.findViewById(R.id.tv_studentname);
                tv_class = (TextView) itemView.findViewById(R.id.tv_class);
                tv_subjects = (TextView) itemView.findViewById(R.id.tv_subjects);
                tv_sex = (TextView) itemView.findViewById(R.id.tv_sex);
                tv_education = (TextView) itemView.findViewById(R.id.tv_education);
                tv_classtime = (TextView) itemView.findViewById(R.id.tv_classtime);
                tv_classdays = (TextView) itemView.findViewById(R.id.tv_classdays);
                tv_home_tutor_starttime = (TextView) itemView.findViewById(R.id.tv_home_tutor_starttime);
                tv_home_tutor_address = (TextView) itemView.findViewById(R.id.tv_home_tutor_address);

                image_tutor = (ImageView) itemView.findViewById(R.id.image_tutor);
            }
        }
    }


}
