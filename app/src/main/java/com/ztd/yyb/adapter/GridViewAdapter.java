package com.ztd.yyb.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanCheck.DataCheck;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class GridViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataCheck.DataCheckChild> dataCheckChildren;
    private LayoutInflater mInflater;
    LinearLayout.LayoutParams params;
    //TODO 实现单选
    private int lastPosition = -1;   //lastPosition 记录上一次选中的图片位置，-1表示未选中
    private Vector<Boolean> vector = new Vector<Boolean>();// 定义一个向量作为选中与否容器

    public GridViewAdapter(Context context, ArrayList<DataCheck.DataCheckChild> dataCheckChildren) {
        this.context = context;
        this.dataCheckChildren = dataCheckChildren;
        mInflater = LayoutInflater.from(context);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        for (int i = 0; i < dataCheckChildren.size(); i++) {
            vector.add(false);
        }

    }

    @Override
    public int getCount() {
        return dataCheckChildren.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ItemViewTag viewTag;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.layout_item, null);
            viewTag = new ItemViewTag((TextView) convertView.findViewById(R.id.text_item));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }

        if (vector.elementAt(position) == true) {
            viewTag.mName.setBackground(context.getResources().getDrawable(R.drawable.bg_repaiup));
            viewTag.mName.setTextColor(context.getResources().getColor(R.color.color_white));
        } else {
            viewTag.mName.setBackground(context.getResources().getDrawable(R.drawable.bg_repai));
            viewTag.mName.setTextColor(context.getResources().getColor(R.color.color_11));
        }

        // set name
        viewTag.mName.setText(dataCheckChildren.get(position).getChildname());
        return convertView;
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

    class ItemViewTag {

        protected TextView mName;

        public ItemViewTag(TextView name) {
            this.mName = name;
        }
    }
}
