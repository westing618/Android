package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanInvoice.ReceiptinfoBean;

import java.util.List;

/**
 * 发票 Adapter
 * Created by  on 2017/3/28.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MainViewHolder> {

        LayoutInflater inflater;
        Context context;
        List<ReceiptinfoBean> leavedata;

        public InvoiceAdapter(Context context,List<ReceiptinfoBean> leavedata) {
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
        public InvoiceAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View view = inflater
                    .inflate(R.layout.item_invoice, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.bindData(leavedata.get(position));
        }


        public class MainViewHolder extends RecyclerView.ViewHolder{
            TextView tv_myintime,tv_myinstate,tv_invphone,tv_invo_address,tv_inname,tv_home_nem,tv_invo_money,tv_num;

            public MainViewHolder(View itemView) {
                super(itemView);
                tv_myintime = (TextView) itemView.findViewById(R.id.tv_myintime);
                tv_myinstate = (TextView) itemView.findViewById(R.id.tv_myinstate);
                tv_invphone = (TextView) itemView.findViewById(R.id.tv_invphone);
                tv_invo_address = (TextView) itemView.findViewById(R.id.tv_invo_address);

                tv_inname = (TextView) itemView.findViewById(R.id.tv_inname);
                tv_home_nem = (TextView) itemView.findViewById(R.id.tv_home_nem);
                tv_invo_money = (TextView) itemView.findViewById(R.id.tv_invo_money);

                tv_num = (TextView) itemView.findViewById(R.id.tv_num);


            }

            public void bindData(ReceiptinfoBean item) {//ygbrstate:状态：0 申请中 1 审批通过 2审批不通过

                String ygbrtel = item.getYgbrtel();

                tv_invphone.setText("手机号："+ygbrtel);

                tv_invo_address.setText(""+item.getYgbrprovince()+item.getYgbrarea()+item.getYgbraddress());

                tv_myintime.setText(item.getYgbrcreatetime());

                tv_invo_money.setText(""+item.getYgbramount());

                tv_num.setText("发票税号："+item.getYgbrnumber());

                tv_inname.setText("发票抬头："+item.getYgbrtitle());

                String ygbraddressee = item.getYgbrcontact();

                tv_home_nem.setText("联系人："+ygbraddressee);

                String ygbrstate = item.getYgbrstate();

                if(ygbrstate != null){
                    if(ygbrstate.equals("0")){
                        tv_myinstate.setText("申请中");
                    }else if(ygbrstate.equals("1")){
                        tv_myinstate.setText("审批通过");
                    }else if(ygbrstate.equals("2")){
                        tv_myinstate.setText("审批不通过");
                    }
                }



            }

        }


    }
