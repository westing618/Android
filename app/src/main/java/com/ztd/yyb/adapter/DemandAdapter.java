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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.activity.my.PerfectInformationActicity;
import com.ztd.yyb.activity.order.GrabOrderSuccessActivity;
import com.ztd.yyb.activity.order.LaborDemandDetailsActivity;
import com.ztd.yyb.activity.order.SchoolNeedsDetailsActivity;
import com.ztd.yyb.bean.beanDeman.OrderYginfoBean;
import com.ztd.yyb.bean.beanDemanJ.OrderJjinfoBean;
import com.ztd.yyb.bean.beanOrd.OrdSuFull;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.fragment.MianFragment;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;

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
 * Created by  on 2017/3/13.
 */

public class DemandAdapter extends RecyclerView.Adapter<DemandAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<OrderJjinfoBean> mTList;
    List<OrderYginfoBean> mYgList;
    private Map<String, String> mLaborSchoolMap = new HashMap<>();

    public DemandAdapter(Context context, List<OrderJjinfoBean> mTList, List<OrderYginfoBean> mYgList) {
        super();
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mTList = mTList;
        this.mYgList = mYgList;

    }

    @Override
    public int getItemCount() {

        if ((Boolean) SPUtil.get(HomeActivity.TYPE, true)) {

            return mYgList.size();

        } else {
            return mTList.size();
        }

    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater
                .inflate(R.layout.item_demand, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        if ((Boolean) SPUtil.get(HomeActivity.TYPE, true)) {

            holder.bindYgData(mYgList.get(position));

            String ygbdcreatetime = mYgList.get(position).getYgbdcreatetime();

            String totalcount = mYgList.get(position).getTotalcount();

            String ygblcprice = mYgList.get(position).getYgblcprice();

            String ygbdaddprice = mYgList.get(position).getYgbdaddprice();

            holder.tv_detime.setText(ygbdcreatetime);

            holder.tv_deprice.setText("价格￥：" + ygblcprice + "/天");

            holder.tv_detotalcount.setText("(共计：￥" + totalcount + "元)");


            if(ygbdaddprice!=null){
                if(ygbdaddprice.equals("0.00")){
                }else {
                    holder.tv_add_price.setText("+￥"+ygbdaddprice);
                }
            }

            final int ygboid = mYgList.get(position).getYgbdid();

            String type = (String) SPUtil.get(MianFragment.TYPE, "");

            if (type.equals("1")) {

                holder.btn_itemdemand.setVisibility(View.INVISIBLE);

            } else if (type.equals("2")) {

                holder.btn_itemdemand.setVisibility(View.VISIBLE);
            }

            holder.re_demand_ygitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, LaborDemandDetailsActivity.class);
                    intent.putExtra("ygboid", "" + ygboid);
                    context.startActivity(intent);
                }
            });

            final String ygboid1 = mYgList.get(position).getYgboid();

            holder.btn_itemdemand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userid = (String) SPUtil.get(USERID, "");

                    robSchoolOrder(ygboid1, userid, "1");


                }
            });

        } else {

            String ygbdgcreatetime = mTList.get(position).getYgbdgcreatetime();
            String ygbscprice = mTList.get(position).getYgbscprice();
            String totalcount = mTList.get(position).getTotalcount();

            final String ygboid = mTList.get(position).getYgbdgid();

            holder.bindJjData(mTList.get(position));

            holder.tv_detime.setText("" + ygbdgcreatetime);


            holder.tv_deprice.setText("价格：￥" + ygbscprice + "/小时");


            holder.tv_detotalcount.setText("(共计：￥" + totalcount + "元)");

            String type = (String) SPUtil.get(MianFragment.TYPE, "");
            if (type.equals("1")) {
                holder.btn_itemdemand.setVisibility(View.INVISIBLE);
            } else if (type.equals("2")) {
                holder.btn_itemdemand.setVisibility(View.VISIBLE);
            }

            holder.re_demand_jjitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SchoolNeedsDetailsActivity.class);
                    intent.putExtra("ygboid", "" + ygboid);
                    context.startActivity(intent);

                }
            });

            final String ygboid1 = mTList.get(position).getYgboid();

            holder.btn_itemdemand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userid = (String) SPUtil.get(USERID, "");

                    robSchoolOrder(ygboid1, userid, "2");

                }
            });


        }

    }

    private void robSchoolOrder(final String ygbdid, String userid, final String type) {


        mLaborSchoolMap.clear();

        mLaborSchoolMap.put("userid", userid); //抢单用户id
        mLaborSchoolMap.put("orderid", ygbdid); //需求id
        mLaborSchoolMap.put("type", type);   //用工类型 1 师傅 2家教

//        Log.e("orderid","="+ygbdid);

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

                    @Override
                    public void onNext(String s) {//{"data":{"success":"0"},"code":"异常","msg":"失败"}


                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            OrdSuFull ordSuFull = gson.fromJson(s, OrdSuFull.class);

                            if (ordSuFull.getData().getSuccess().equals("1")) {

                                SPUtil.put("putorderid",""+ygbdid);
                                SPUtil.put("puttype",""+type);
                                context.startActivity(new Intent(context, GrabOrderSuccessActivity.class));

                                DemanEvet messageEvent = new DemanEvet();
                                messageEvent.setMsg("paysucc");
                                EventBus.getDefault().post(messageEvent);

                            } else if(ordSuFull.getData().getSuccess().equals("0")){//ygbmcstate：状态：0审核中   1认证通过   2认证未通过   3未认证

                                String ygbmcstate = ordSuFull.getData().getYgbmcstate();
                                String msg = "";

                                if (ygbmcstate.equals("0")) {
                                    msg = "资料审核中请等待";
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
                                                    if(type.equals("1")){
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
                                                }
                                            })
                                            .show();



                                }else {

                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("提示")
                                            .setContentText(ordSuFull.getMsg())
                                            .show();

                                }


                            }

                        }


                    }
                });


    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout re_demand_ygitem;
        RelativeLayout re_demand_jjitem;

        TextView tv_detime, tv_deprice, tv_detotalcount,tv_add_price;
        Button btn_itemdemand;
        //----------------------
        TextView tv_deygname, tv_deygwork, tv_ygdaynum, tv_ygstarttime, tv_ygaddress;

        //----------------------
        TextView tv_declass, tv_desubjects, tv_desex, tv_education, tv_declasstime, tv_declassdays, tv_destarttime, tv_deaddress;

        ImageView image_demayg, image_demaJj;

        ImageView image_type;

        public MainViewHolder(View itemView) {
            super(itemView);

            re_demand_ygitem = (RelativeLayout) itemView.findViewById(R.id.demand_ygitem);
            re_demand_jjitem = (RelativeLayout) itemView.findViewById(R.id.demand_jjitem);


            if ((Boolean) SPUtil.get(HomeActivity.TYPE, true)) {//用工布局  家教布局
                re_demand_ygitem.setVisibility(View.VISIBLE);
                re_demand_jjitem.setVisibility(View.GONE);
            } else {
                re_demand_ygitem.setVisibility(View.GONE);
                re_demand_jjitem.setVisibility(View.VISIBLE);
            }

            //------------------  共用控件
            tv_detime = (TextView) itemView.findViewById(R.id.tv_detime);
            tv_deprice = (TextView) itemView.findViewById(R.id.tv_deprice);
            tv_detotalcount = (TextView) itemView.findViewById(R.id.tv_detotalcount);
            tv_add_price = (TextView) itemView.findViewById(R.id.tv_add_price);


            btn_itemdemand = (Button) itemView.findViewById(R.id.btn_itemdemand);
            //--------------------用工控件

            tv_deygname = (TextView) itemView.findViewById(R.id.tv_deygname);
            tv_deygwork = (TextView) itemView.findViewById(R.id.tv_deygwork);
            tv_ygdaynum = (TextView) itemView.findViewById(R.id.tv_ygdaynum);
            tv_ygstarttime = (TextView) itemView.findViewById(R.id.tv_ygstarttime);
            tv_ygaddress = (TextView) itemView.findViewById(R.id.tv_ygaddress);

            image_type = (ImageView) itemView.findViewById(R.id.image_type);

            image_demayg = (ImageView) itemView.findViewById(R.id.image_demayg);

            //--------------------家教控件

            tv_declass = (TextView) itemView.findViewById(R.id.tv_declass);
            tv_desubjects = (TextView) itemView.findViewById(R.id.tv_desubjects);
            tv_desex = (TextView) itemView.findViewById(R.id.tv_desex);
            tv_education = (TextView) itemView.findViewById(R.id.tv_education);
            tv_declasstime = (TextView) itemView.findViewById(R.id.tv_declasstime);
            tv_declassdays = (TextView) itemView.findViewById(R.id.tv_declassdays);
            tv_destarttime = (TextView) itemView.findViewById(R.id.tv_destarttime);
            tv_deaddress = (TextView) itemView.findViewById(R.id.tv_deaddress);

            image_demaJj = (ImageView) itemView.findViewById(R.id.image_demaJj);

        }

        public void bindYgData(OrderYginfoBean item) {

            String ygbdtype = item.getYgbdtype();
            if (ygbdtype.equals("2")) {
                image_type.setImageResource(R.mipmap.list_icon_qiangxiu);
            }else if(ygbdtype.equals("1")){
                image_type.setImageResource(R.mipmap.list_icon_putong);
            }

            String ygbddays = item.getYgbddays();
            String ygbdtimearrival = item.getYgbdtimearrival();
            String ygblcname = item.getYgblcname();
            String ygbdaddress = item.getYgbdaddress();
            String yglogo = item.getYglogo();

            String ygbdprovince = item.getYgbdprovince();
            String ygbdcity = item.getYgbdcity();
            String ygbdarea = item.getYgbdarea();

            tv_deygwork.setText("工种：" + ygblcname);
            tv_ygdaynum.setText("用工天数：" + ygbddays + "天");
            tv_ygstarttime.setText("到场时间：" + ygbdtimearrival);
            tv_ygaddress.setText("" + ygbdprovince + ygbdcity + ygbdarea + ygbdaddress);

            String test = Constants.BASE_URL + "logo/" + yglogo;

            Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.list_icon_user)
                    .placeholder(R.mipmap.list_icon_user)
                    .transform(new GlideCircleTransform(context)).into(image_demayg);

        }

        public void bindJjData(OrderJjinfoBean item) {

            String ygbscname = item.getYgbscname();
            String ygbdgsex = item.getYgbdgsex();
            String ygbdgdays = item.getYgbdgdays();
            String ygbdgmounttime = item.getYgbdgmounttime();
            String ygbdgmomentname = item.getYgbdgmomentname();
            String ygbeducationname = item.getYgbeducation();
            String ygbdgaddress = item.getYgbdgaddress();
            String ygbtime = item.getYgbtime();
            String yglogo = item.getYglogo();

            String ygbdgprovince = item.getYgbdgprovince();
            String ygbdgcity = item.getYgbdgcity();
            String ygbdgarea = item.getYgbdgarea();

            if (ygbdgsex != null) {
                if (ygbdgsex.equals("1")) {
                    tv_desex.setText("性别要求：女");
                } else {
                    tv_desex.setText("性别要求：男");
                }
            }

            tv_declass.setText("年级：" + ygbdgmomentname);
            tv_desubjects.setText("科目：" + ygbscname);
            tv_education.setText("学历要求：" + ygbeducationname);
            tv_declasstime.setText("上课时长：" + ygbtime);
            tv_declassdays.setText("上课天数：" + ygbdgdays + "天");
            tv_destarttime.setText("开课时间：" + ygbdgmounttime);
            tv_deaddress.setText("" + ygbdgprovince + ygbdgcity + ygbdgarea + ygbdgaddress);


            String test = Constants.BASE_URL + "logo/" + yglogo;


            Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.list_icon_user)
                    .placeholder(R.mipmap.list_icon_user)
                    .transform(new GlideCircleTransform(context)).into(image_demaJj);

        }

    }


}
