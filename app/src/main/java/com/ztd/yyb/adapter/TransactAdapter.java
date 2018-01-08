package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanTran.MoneyarrayBeanX;

import java.util.List;

/**
 * 我的钱包 -- 交易记录  Adapter
 * Created by  on 2017/3/13.
 */

    public class TransactAdapter extends RecyclerView.Adapter<TransactAdapter.MainViewHolder> {

        LayoutInflater inflater;
        Context context;
        List<MoneyarrayBeanX> leavedata;
        public TransactAdapter(Context context,List<MoneyarrayBeanX> leavedata) {
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
                    .inflate(R.layout.item_transact, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.bindData(leavedata.get(position));
        }


        public class MainViewHolder extends RecyclerView.ViewHolder{

            TextView tv_tran_title,tv_tran_time,tv_amount;
            public MainViewHolder(View itemView) {
                super(itemView);
                 tv_tran_title = (TextView) itemView.findViewById(R.id.tv_tran_title);
                tv_tran_time = (TextView) itemView.findViewById(R.id.tv_tran_time);
                tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            }

            public void bindData(MoneyarrayBeanX item) {

                String ygtitle = item.getYgtitle();
                tv_tran_title.setText(ygtitle);
                tv_tran_time.setText(""+item.getYgbrdcreatetime());
                String ygbrdtype = item.getYgbrdtype();

                String ygbramount = item.getYgbrdamount();

//                Log.e("11","="+ygbramount);

                if(ygbrdtype != null){//ygbrdtype:交易类型 1 收入 2 支出
                    if(ygbrdtype.equals("1")){
                        tv_amount.setText("+"+ygbramount);
                    }else if(ygbrdtype.equals("2")){
                        tv_amount.setText("-"+ygbramount);
                    }
                }
            }

        }


    }
