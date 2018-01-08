package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanCoup.CouponarrayBean;
import com.ztd.yyb.util.SPUtil;

import java.util.List;

/**
 * 我的优惠券  Adapter
 * Created by  on 2017/3/22.
 */

public class MyCouponAdapter extends RecyclerView.Adapter<MyCouponAdapter.MainViewHolder> {

        LayoutInflater inflater;
        Context context;
        List<CouponarrayBean> leavedata;

        public MyCouponAdapter(Context context,List<CouponarrayBean> leavedata) {
            super();
            inflater = LayoutInflater.from(context);
            this.context=context;
            this.leavedata=leavedata;
        }
        @Override
        public int getItemCount() {
            return leavedata.size();
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View view = inflater
                    .inflate(R.layout.item_mycoupon, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.bindData(leavedata.get(position));
        }


        public class MainViewHolder extends RecyclerView.ViewHolder{
            TextView tv_mycoupon,tv_endtime,tv_state,tv_coutitle;

            RelativeLayout re_mycoupon ;

            public MainViewHolder(View itemView) {
                super(itemView);
                 tv_mycoupon = (TextView) itemView.findViewById(R.id.tv_mycoupon);
                tv_endtime = (TextView) itemView.findViewById(R.id.tv_endtime);
                tv_state = (TextView) itemView.findViewById(R.id.tv_state);
                tv_coutitle = (TextView) itemView.findViewById(R.id.tv_coutitle);

                re_mycoupon = (RelativeLayout) itemView.findViewById(R.id.re_mycoupon);


            }

            public void bindData(CouponarrayBean item) {

                int ygbcamount = item.getYgbcamount();
                String ygbcendtime = item.getYgbcvalidendtime();
                tv_endtime.setText("使用截止日期"+ygbcendtime);
                tv_mycoupon.setText("￥"+ygbcamount);


                String state = (String) SPUtil.get("COUPON", "");

                tv_coutitle.setText(""+item.getYgbctitle());

                if(state != null){//state:状态 1:可用 2:已过期 3：已使用
                    if(state.equals("1")){
                        tv_state.setText("可用");
                        re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_usable));
                    }else  if(state.equals("2")){
                        tv_state.setText("已过期");
                        re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_unava));
                    }else  if(state.equals("3")){
                    tv_state.setText("已使用");
                        re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_unava));
                    }
                }


            }

        }


    }
