package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanHome.CitylistBean;
import com.ztd.yyb.util.PingYinUtil;

import java.util.List;

/**
 * 城市选择 适配器
 * Created by  on 2017/3/20.
 */
public class CitySelectionAdapter extends RecyclerView.Adapter<CitySelectionAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<CitylistBean> leavedata;
    OnItemClickListener mOnItemClickListener;

    public CitySelectionAdapter(Context context, List<CitylistBean> leavedata, OnItemClickListener mOnItemClickListener) {
        super();
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.leavedata = leavedata;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return leavedata.size();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater
                .inflate(R.layout.item_cityselection, parent, false);

        return new MainViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(leavedata.get(position));

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_cityname_letter;
        TextView tv_cityname;
        private OnItemClickListener mOnItemClickListener;

        public MainViewHolder(View itemView, OnItemClickListener mOnItemClickListener) {
            super(itemView);

            tv_cityname_letter = (TextView) itemView.findViewById(R.id.tv_cityname_letter);
            tv_cityname = (TextView) itemView.findViewById(R.id.tv_cityname);
            this.mOnItemClickListener = mOnItemClickListener;
            itemView.setOnClickListener(this);

        }

        public void bindData(CitylistBean item) {

            String lcity = item.getLcitymc();

            String firstLetter = new PingYinUtil().getFirstLetter(lcity);//获取 拼音首字母

            tv_cityname.setText(lcity);
            tv_cityname_letter.setText(firstLetter);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


}
