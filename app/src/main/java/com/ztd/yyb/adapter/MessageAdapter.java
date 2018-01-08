package com.ztd.yyb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.my.SystemessageDetailActivity;
import com.ztd.yyb.bean.beanMess.DataMess;

import java.util.List;

/**
 * 消息适配器
 * Created by  on 2017/3/29.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<DataMess.DataEntity.NoticearrayEntity> leavedata;

    public MessageAdapter(Context context, List<DataMess.DataEntity.NoticearrayEntity> leavedata) {
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
                .inflate(R.layout.item_message, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(leavedata.get(position));
    }


    public class MainViewHolder extends RecyclerView.ViewHolder {

        TextView tv_messconten;
        TextView tv_messtime;
        TextView tv_messtitle;


        RelativeLayout re_itemmess;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv_messconten = (TextView) itemView.findViewById(R.id.tv_messconten);
            tv_messtime = (TextView) itemView.findViewById(R.id.tv_messtime);
            tv_messtitle = (TextView) itemView.findViewById(R.id.tv_messtitle);

            re_itemmess = (RelativeLayout) itemView.findViewById(R.id.re_itemmess);

        }

        public void bindData(DataMess.DataEntity.NoticearrayEntity item) {

            final   String ygbmscontent = item.getYgbmscontent();
            final   String ygbmscreatetime = item.getYgbmscreatetime();
            final   String ygbmstitle = item.getYgbmstitle();

            tv_messconten.setText("" + ygbmscontent);
            tv_messtime.setText("" + ygbmscreatetime);
            tv_messtitle.setText("" + ygbmstitle);

            re_itemmess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, SystemessageDetailActivity.class);
                    intent.putExtra("ygbmscontent",""+ygbmscontent);
                    intent.putExtra("ygbmscreatetime",""+ygbmscreatetime);
                    intent.putExtra("ygbmstitle",""+ygbmstitle);
                    context.startActivity(intent);

                }
            });


        }

    }


}

