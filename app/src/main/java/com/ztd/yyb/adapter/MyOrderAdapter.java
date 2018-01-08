package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztd.yyb.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  on 2017/3/13.
 */

    public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MainViewHolder> {

        LayoutInflater inflater;
        Context context;
        List<String> leavedata;
        public MyOrderAdapter(Context context,List<String> leavedata) {
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
                    .inflate(R.layout.item_myorder, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.bindData(leavedata.get(position));
//            holder.bindData();


        }


        public class MainViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.re_item)    RelativeLayout re_item;
            @BindView(R.id.tv_ordertime)
            TextView tv_ordertime;



            public MainViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bindData(String item) {
                tv_ordertime.setText(item);
            }

//            public void bindData() {
//                re_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        context.startActivity(new Intent(context, MyOrderActivityDetails.class));
//                    }
//                });
//            }

        }


    }
