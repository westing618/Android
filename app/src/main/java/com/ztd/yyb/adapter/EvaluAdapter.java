package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanSuess.ChangBean;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  on 2017/4/7.
 */

public class EvaluAdapter extends RecyclerView.Adapter<EvaluAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<ChangBean> leavedata;

    //TODO 实现单选
    private int lastPosition = -1;   //lastPosition 记录上一次选中的图片位置，-1表示未选中
    private Vector<Boolean> vector = new Vector<Boolean>();// 定义一个向量作为选中与否容器


    OnEvaluItemClickListener onEvaluItemClickListener;
    public interface OnEvaluItemClickListener {
        void onEvaluItemClick(int position);
    }
    public void setOnEvaluClickListener(OnEvaluItemClickListener  listener) {
        this.onEvaluItemClickListener = listener;
    }

    public EvaluAdapter(Context context, List<ChangBean> leavedata, OnEvaluItemClickListener onEvaluItemClickListener) {

        super();
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.leavedata = leavedata;
        this.onEvaluItemClickListener=onEvaluItemClickListener;

        for (int i = 0; i < leavedata.size(); i++) {
            vector.add(false);
        }

    }

    @Override
    public int getItemCount() {
        return leavedata.size();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater
                .inflate(R.layout.list_item_button, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {


        if (vector.size() != 0) {

                if (vector.elementAt(position) == true) {
                    holder.btn_eva.setBackground(context.getResources().getDrawable(R.drawable.bg_repaiup));
                    holder.btn_eva.setTextColor(context.getResources().getColor(R.color.color_white));
                } else {
                    holder.btn_eva.setBackground(context.getResources().getDrawable(R.drawable.bg_repai));
                    holder.btn_eva.setTextColor(context.getResources().getColor(R.color.color_11));
                }


        }

        holder.btn_eva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEvaluItemClickListener!=null){
                    onEvaluItemClickListener.onEvaluItemClick(position);
                    changeState( position);
                }
            }
        });
        holder.bindData(leavedata.get(position).getName());
    }

    /**
     * 修改选中时的状态
     *
     * @param position
     */
    public void changeState(int position) {
        if (lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btn_eva)
        TextView btn_eva;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(String item) {
            btn_eva.setText(item);
        }

    }

}
