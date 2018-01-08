package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanWith.EvaarrayBean;

import java.util.List;

/**
 * Created by  on 2017/5/12.
 */

public class WithRecorAdapter extends RecyclerView.Adapter<WithRecorAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<EvaarrayBean> leavedata;

    public WithRecorAdapter(Context context, List<EvaarrayBean> leavedata) {
        super();

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.leavedata = leavedata;

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


    public class MainViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tran_title, tv_tran_time, tv_amount;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv_tran_title = (TextView) itemView.findViewById(R.id.tv_tran_title);
            tv_tran_time = (TextView) itemView.findViewById(R.id.tv_tran_time);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
        }

        public void bindData(EvaarrayBean item) {

                String ygtitle = item.getYgtitle();

                tv_tran_title.setText(ygtitle);

                tv_tran_time.setText(""+item.getYgbrdtradetime());//交易时间

                String ygbrdtype = item.getYgbrdtype();

                String ygbramount = item.getYgbrdamount();


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
