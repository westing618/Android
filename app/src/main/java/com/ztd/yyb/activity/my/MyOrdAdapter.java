package com.ztd.yyb.activity.my;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanMyOrderBean.OrderJjinfoBean;
import com.ztd.yyb.bean.beanOrder.OrderYginfoBean;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.SPUtil;

import java.util.List;

import static com.ztd.yyb.activity.my.MyOrderTwoActivity.ORDERTYPETWO;

/**
 * Created by  on 2017/3/21.
 */
public class MyOrdAdapter extends RecyclerView.Adapter<MyOrdAdapter.MainViewHolder> {

    LayoutInflater inflater;

    Context context;

    List<OrderYginfoBean> LeaveYgdata;

    List<OrderJjinfoBean> LeavedJjata;

    OnItemClickListener mOnItemClickListener;

    public MyOrdAdapter(Context context, List<OrderYginfoBean> LeaveYgdata,
                        List<OrderJjinfoBean> LeavedJjata, OnItemClickListener mOnItemClickListener) {
        super();
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.LeaveYgdata = LeaveYgdata;
        this.LeavedJjata = LeavedJjata;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {

        String flag = (String) SPUtil.get(ORDERTYPETWO, "");
        if (flag.equals("1")) {
            return LeaveYgdata.size();
        } else {
            return LeavedJjata.size();
        }

    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater
                .inflate(R.layout.item_myorder, parent, false);
        MainViewHolder vh = new MainViewHolder(view, mOnItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        String flag = (String) SPUtil.get(ORDERTYPETWO, "");

        if (flag.equals("1")) {
            holder.bindYgData(LeaveYgdata.get(position));
            holder.itemView.setTag(position);
        } else {
            holder.bindJjData(LeavedJjata.get(position));
            holder.itemView.setTag(position);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout re_itemYg;
        TextView tv_ygtitle, tv_yghomework, tv_yghomestarttime, tv_yghome_address, tv_cartime;

        ImageView image_type;
        TextView myorder_one, myorder_two, myorder_three;
        TextView order_state;
       ImageView image_headd;
        private OnItemClickListener mOnItemClickListener;

        public MainViewHolder(View itemView, OnItemClickListener mOnItemClickListener) {
            super(itemView);

            this.mOnItemClickListener = mOnItemClickListener;
            itemView.setOnClickListener(this);

            image_headd = (ImageView) itemView.findViewById(R.id.imageView);

            image_type = (ImageView) itemView.findViewById(R.id.image_type);
            re_itemYg = (RelativeLayout) itemView.findViewById(R.id.re_itemYg);

            myorder_one = (TextView) itemView.findViewById(R.id.myorder_one);
            myorder_two = (TextView) itemView.findViewById(R.id.myorder_two);
            myorder_three = (TextView) itemView.findViewById(R.id.myorder_three);

            myorder_one.setOnClickListener(this);
            myorder_two.setOnClickListener(this);
            myorder_three.setOnClickListener(this);

            order_state = (TextView) itemView.findViewById(R.id.order_state);
            tv_ygtitle = (TextView) itemView.findViewById(R.id.tv_ygtitle);
            tv_cartime = (TextView) itemView.findViewById(R.id.tv_cartime);
            tv_yghomework = (TextView) itemView.findViewById(R.id.tv_yghomework);
            tv_yghomestarttime = (TextView) itemView.findViewById(R.id.tv_yghomestarttime);
            tv_yghome_address = (TextView) itemView.findViewById(R.id.tv_yghome_address);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }

        public void bindYgData(OrderYginfoBean Ygitem) {

            String ygbprovince = Ygitem.getYgbprovince();
            String ygbcity = Ygitem.getYgbcity();
            String ygbarea = Ygitem.getYgbarea();
            String ygbdaddress = Ygitem.getYgbdaddress();
            tv_yghome_address.setText(ygbprovince + ygbcity + ygbarea + ygbdaddress);
            String yglogo = Ygitem.getYglogo();
            String test = Constants.BASE_URL + "logo/" + yglogo;


            Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.list_icon_user)
                    .placeholder(R.mipmap.list_icon_user)
                    .transform(new GlideCircleTransform(context)).into(image_headd);

            tv_yghomework.setText("工种：" + Ygitem.getYgblcname());

            tv_cartime.setText(Ygitem.getYgbocreatetime());

            tv_yghomestarttime.setText("到场时间：" + Ygitem.getYgbdtimearrival());

            String ygbdtype = Ygitem.getYgbdtype();
            if (ygbdtype != null) {
                if (ygbdtype.equals("1")) {
                    image_type.setBackground(context.getResources().getDrawable(R.mipmap.list_icon_putong));//默认普通订单
                } else if (ygbdtype.equals("2")) {
                    image_type.setBackground(context.getResources().getDrawable(R.mipmap.list_icon_qiangxiu));
                }
            }

            String ygbotype = Ygitem.getYgbotype();
            String ygboastate = Ygitem.getYgboastate();

//            Log.e("ygbotype","="+ygbotype);
//            Log.e("ygboastate","="+ygboastate);

            if (ygbotype.equals("0")) {

//                Log.e("111111111111111111","1111111111111111");

                order_state.setText("进行中");

                myorder_one.setText("取消订单");
                myorder_two.setText("查看详情");
                myorder_three.setText("开始出工");
                myorder_one.setVisibility(View.VISIBLE);
                myorder_two.setVisibility(View.VISIBLE);
                myorder_three.setVisibility(View.VISIBLE);

                re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));

            } else if (ygbotype.equals("3")) {
                order_state.setText("进行中");

                myorder_two.setText("查看详情");
                myorder_three.setText("施工完成");
                myorder_two.setVisibility(View.VISIBLE);
                myorder_three.setVisibility(View.VISIBLE);
                myorder_one.setVisibility(View.GONE);

                re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));


//                Log.e("2222222222222","222222222222222");
            } else if (ygbotype.equals("2")) {

                order_state.setText("已取消");
                myorder_two.setText("查看详情");
                re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorA));
                myorder_two.setVisibility(View.VISIBLE);
                myorder_one.setVisibility(View.GONE);
                myorder_three.setVisibility(View.GONE);

//                Log.e("333333333333333","3333333333333333");
            } else {

                if (ygboastate.equals("0")) {
                    re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));
                    order_state.setText("进行中");
                    myorder_two.setText("查看详情");
                    myorder_three.setText("等待业主确认");
                    myorder_one.setVisibility(View.GONE);
                    myorder_two.setVisibility(View.VISIBLE);
                    myorder_three.setVisibility(View.VISIBLE);

//                    Log.e("44444444444444","4444444444444444");
                } else {

                    if (ygbotype.equals("1")) {
                        re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorA));
                        order_state.setText("已完成");
                        myorder_two.setText("查看详情");
                        myorder_two.setVisibility(View.VISIBLE);
                        myorder_one.setVisibility(View.GONE);
                        myorder_three.setVisibility(View.GONE);
                    } else {
//                        Log.e("55555555555555","5555555555555555");
                        myorder_one.setVisibility(View.GONE);
                        myorder_two.setVisibility(View.GONE);
                        myorder_three.setVisibility(View.GONE);
                    }
                }

            }

        }

        public void bindJjData(OrderJjinfoBean Jjitem) {
            image_type.setVisibility(View.GONE);
            String ygbprovince = Jjitem.getYgbprovince();
            String ygbcity = Jjitem.getYgbcity();
            String ygbarea = Jjitem.getYgbarea();
            String ygbdgaddress = Jjitem.getYgbdgaddress();

            String ygbocreatetime = Jjitem.getYgbocreatetime();

            tv_cartime.setText(""+ygbocreatetime);

            tv_yghome_address.setText(ygbprovince + ygbcity + ygbarea + ygbdgaddress);

            String yglogo = Jjitem.getYglogo();

            String test = Constants.BASE_URL + "logo/" + yglogo;

            Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.list_icon_user)
                    .placeholder(R.mipmap.list_icon_user)
                    .transform(new GlideCircleTransform(context)).into(image_headd);

            tv_yghomework.setText("科目：" + Jjitem.getYgbscname());
            tv_yghomestarttime.setText("开课时间：" + Jjitem.getYgbdgmounttime());
//            String ygbdtype = Jjitem.getYgbdtype();
            String ygbotype = Jjitem.getYgbotype();
            String ygboastate = Jjitem.getYgboastate();

            if (ygbotype.equals("0")) {
                order_state.setText("进行中");
                myorder_one.setText("取消订单");
                myorder_two.setText("查看详情");
                myorder_three.setText("开始上课");
                myorder_one.setVisibility(View.VISIBLE);
                myorder_two.setVisibility(View.VISIBLE);
                myorder_three.setVisibility(View.VISIBLE);
                re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));
            } else if (ygbotype.equals("3")) {
                order_state.setText("进行中");
                myorder_one.setVisibility(View.GONE);
                myorder_two.setText("查看详情");
                myorder_three.setText("课程结束");
                myorder_two.setVisibility(View.VISIBLE);
                myorder_three.setVisibility(View.VISIBLE);
                re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));
            } else if (ygbotype.equals("2")) {
                order_state.setText("已取消");
                myorder_two.setText("查看详情");

                myorder_one.setVisibility(View.GONE);
                myorder_three.setVisibility(View.GONE);
                myorder_two.setVisibility(View.VISIBLE);

                re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));
            } else {
                if (ygboastate.equals("0")) {
                    order_state.setText("进行中");
                    myorder_two.setText("查看详情");
                    myorder_three.setText("等待家长确认");
                    myorder_one.setVisibility(View.GONE);
                    myorder_two.setVisibility(View.VISIBLE);
                    myorder_three.setVisibility(View.VISIBLE);
                    re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));
                } else {
                    if (ygbotype.equals("1")) {

                        order_state.setText("已完成");
                        myorder_two.setText("查看详情");

                        myorder_two.setVisibility(View.VISIBLE);
                        myorder_one.setVisibility(View.GONE);
                        myorder_three.setVisibility(View.GONE);
                        re_itemYg.setBackgroundColor(context.getResources().getColor(R.color.colorAcce));

                    }

                }

            }


        }


    }


}
