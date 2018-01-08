package com.ztd.yyb.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanCoup.CouponarrayBean;
import com.ztd.yyb.evenbean.CouEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by  on 2017/5/8.
 */

public class ChooseCouponAdapter extends RecyclerView.Adapter<ChooseCouponAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Activity context;
    List<CouponarrayBean> leavedata;

    public ChooseCouponAdapter(Activity context,List<CouponarrayBean> leavedata) {
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
        RelativeLayout re_mycoupon;

        public MainViewHolder(View itemView) {

            super(itemView);

            tv_mycoupon = (TextView) itemView.findViewById(R.id.tv_mycoupon);
            re_mycoupon = (RelativeLayout) itemView.findViewById(R.id.re_mycoupon);

            tv_endtime = (TextView) itemView.findViewById(R.id.tv_endtime);
            tv_state = (TextView) itemView.findViewById(R.id.tv_state);
            tv_coutitle = (TextView) itemView.findViewById(R.id.tv_coutitle);

        }

        public void bindData(CouponarrayBean item) {

            final   int ygbcamount = item.getYgbcamount();

            String ygbcendtime = item.getYgbcvalidendtime();

            final    String ygbctitle = item.getYgbctitle();

            tv_endtime.setText("使用截止日期"+ygbcendtime);

            tv_mycoupon.setText("￥"+ygbcamount);

//            String state = item.getState();

            tv_coutitle.setText(""+ ygbctitle);
            tv_state.setText(""+"可用");
            re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_usable));

//            if(state != null){//state:状态 1:可用 2:已过期 3：已使用
//                if(state.equals("1")){
//                    tv_state.setText(""+"可用");
//                    re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_usable));
//                }else  if(state.equals("2")){
//                    tv_state.setText(""+"已过期");
//                    re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_unava));
//                }else  if(state.equals("3")){
//                    tv_state.setText(""+"已使用");
//                    re_mycoupon.setBackground(context.getResources().getDrawable(R.mipmap.bg_img_unava));
//                }
//            }

            final String ygbcid = item.getYgbcid();


            re_mycoupon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

//                    Log.e("OnClickListener","OnClickListener");

                    CouEvent couEvent = new CouEvent();
                    couEvent.setId(ygbcid);
                    couEvent.setMsg(ygbctitle);
                    couEvent.setProce(""+ygbcamount);
                    couEvent.setFlag(true);//使用优惠券
                    EventBus.getDefault().post(couEvent);

                    context.finish();


                }
            });



        }

    }


}

