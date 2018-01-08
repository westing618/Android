package com.ztd.yyb.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.util.SPUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by  on 2017/2/23.
 * 选择班主任适配器
 */

public class ParentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Activity mContext;
    private OnItemClickListener mOnItemClickListener;
    private List<ChangBean> mClassListInfoList = new ArrayList<>();//列表数据

    public ParentsAdapter(Activity context, List<ChangBean> classListInfoList, OnItemClickListener mOnItemClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mClassListInfoList = classListInfoList;
    }

    public List<ChangBean> getClassListInfoList() {
        return mClassListInfoList;
    }

    public void setClassListInfoList(List<ChangBean> mClassListInfoList) {
        this.mClassListInfoList = mClassListInfoList;
    }

    @Override
    public int getItemCount() {
        return mClassListInfoList.size() == 0 ? 1 : mClassListInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_lay_select_class_item, parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ItemViewHolder(view, mOnItemClickListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        setData((ItemViewHolder) holder, position);
    }

    private void setData(ItemViewHolder viewHolder, final int position) {
        if (mClassListInfoList.size() == 0) {
            viewHolder.mTvName.setText("暂无数据");
            viewHolder.mLinLine.setVisibility(View.GONE);
            return;
        }

        String classListInfo = mClassListInfoList.get(position).getName();
        String price = mClassListInfoList.get(position).getPrice();


        if (classListInfo == null) {
            return;
        }


        if(price==null){

            viewHolder.mTvName.setText(classListInfo);

        } else {

            if((Boolean) SPUtil.get(HomeActivity.TYPE, true)){//true 用工  flase 学堂)
                viewHolder.mTvName.setText(classListInfo+"(￥"+price+"/天)");
            }else {
                viewHolder.mTvName.setText(classListInfo+"(￥"+price+"/小时)");
            }



        }



        if (position == getItemCount() - 1) {
            viewHolder.mLinLine.setVisibility(View.GONE);
        } else {
            viewHolder.mLinLine.setVisibility(View.VISIBLE);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //普通item项控件设置
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener mOnItemClickListener;

        private TextView mTvName;
        private View mLinLine;

        public ItemViewHolder(View view, OnItemClickListener mOnItemClickListener) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mLinLine = view.findViewById(R.id.lin_line);

            this.mOnItemClickListener = mOnItemClickListener;
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


}
