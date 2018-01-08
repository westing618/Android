package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.bean.beanHome.GzdjlistBean;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.util.SPUtil;

import java.util.List;

/**
 * Created by HH on 2017/7/30.
 */

public class ReleaseDemandAdapter extends RecyclerView.Adapter<ReleaseDemandAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<com.ztd.yyb.bean.beanRlea.GzdjlistBean> leavedata;

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ReleaseDemandAdapter(Context context, List<com.ztd.yyb.bean.beanRlea.GzdjlistBean> leavedata ) {
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
                .inflate(R.layout.attendance_lay_select_class_item, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(leavedata.get(position));

    }


    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_cityname;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv_cityname = (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        public void bindData(com.ztd.yyb.bean.beanRlea.GzdjlistBean item) {

            String classListInfo = item.getYgblcname();
            double ygblcprice = item.getYgblcprice();

            tv_cityname.setText(classListInfo+"(￥"+ygblcprice+"/天)");


        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

}
