package com.ztd.yyb.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanEvalu.EvaluatearrayBean;

import java.util.List;

/**
 * 我的评价 Adapter
 * Created by  on 2017/3/22.
 */

    public class MyEvaluAdapter extends RecyclerView.Adapter<MyEvaluAdapter.MainViewHolder> {

        LayoutInflater inflater;
        Context context;
        List<EvaluatearrayBean> leavedata;

        public MyEvaluAdapter(Context context,List<EvaluatearrayBean> leavedata) {
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
                    .inflate(R.layout.item_evalu, parent, false);

            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.bindData(leavedata.get(position));
        }


        public class MainViewHolder extends RecyclerView.ViewHolder{

            TextView tv_evtitle,tv_evname,tv_ev_address,tv_evconnect;
            RatingBar evratingBar;
            public MainViewHolder(View itemView) {
                super(itemView);
                 tv_evtitle = (TextView) itemView.findViewById(R.id.tv_evtitle);
                tv_evname = (TextView) itemView.findViewById(R.id.tv_evname);
                tv_ev_address = (TextView) itemView.findViewById(R.id.tv_ev_address);
                tv_evconnect = (TextView) itemView.findViewById(R.id.tv_evconnect);
                evratingBar = (RatingBar) itemView.findViewById(R.id.evratingBar);

            }

            public void bindData(EvaluatearrayBean item) {

                String ygbname = item.getYgbname();

                tv_evname.setText(ygbname+":");

                tv_evconnect.setText(item.getYgbeevaluatemc());
                tv_ev_address.setText(""+item.getYgbecreatetime());

                int ygbestar = item.getYgbestar();

                evratingBar.setRating(ygbestar);

//                tv_evtitle.setText(":"+item.getYgbdtitle());



            }

        }


    }
