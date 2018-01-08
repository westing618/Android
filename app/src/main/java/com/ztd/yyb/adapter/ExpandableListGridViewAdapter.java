package com.ztd.yyb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.bean.beanCheck.DataCheck;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.view.CanAddInListViewGridView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ExpandableListGridViewAdapter extends BaseExpandableListAdapter {


    ArrayList<DataCheck> mCheckList;
    ArrayList<ArrayList<DataCheck.DataCheckChild>> mCheckListChild;
    Context context;

    OnGridItemClickListener mOnGridItemClickListener;

    private ExpandableListView elv_collocation;



    public interface OnGridItemClickListener {
        void onGridItemClick(int groupPosition, int childPosition);
    }

    public ExpandableListGridViewAdapter(Context context, ExpandableListView elv_collocation,ArrayList<DataCheck> mCheckList,
                                         ArrayList<ArrayList<DataCheck.DataCheckChild>> mCheckListChild,
                                         OnGridItemClickListener mOnGridItemClickListener) {

        this.mCheckListChild = mCheckListChild;
        this.mCheckList = mCheckList;
        this.context = context;
        this.mOnGridItemClickListener=mOnGridItemClickListener;
        this.elv_collocation=elv_collocation;

    }
    public void setOnItemClickListener(OnGridItemClickListener listener) {
        this.mOnGridItemClickListener = listener;
    }
    //获取当前父item的数据数量
    @Override
    public int getGroupCount() {
        return mCheckList.size();
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        //子列表项的数量
        // return childMapList_list.get(groupPosition).size();
        //子列表项的数量本来是list  多个  现在同样的数据以girdview形式展示  就只有个girdview项 所以返回1
        return 1;
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition) {
        return mCheckList.get(groupPosition);
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCheckListChild.get(groupPosition).get(childPosition);
    }

    //得到父item的ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        //return false;
        return true;
    }

    //设置父item组件
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_section, null);
        }

        TextView tv_title_parent = (TextView) convertView.findViewById(R.id.text_section);

        TextView  tv_title = (TextView) convertView.findViewById(R.id.text_stititle);


        ImageView iv_img_parent_right = (ImageView) convertView.findViewById(R.id.iv_img_parent_right);

        if (isExpanded) {
            iv_img_parent_right.setImageResource(R.mipmap.list_icon_up);
        } else {
            iv_img_parent_right.setImageResource(R.mipmap.list_icon_down);
        }


        String parentName = mCheckList.get(groupPosition).getName();
        String chname = mCheckList.get(groupPosition).getChname();

        tv_title_parent.setText(parentName);
        tv_title.setText(""+chname);

        return convertView;
    }

    //设置子item的组件


    @Override
    public View getChildView(final int groupPosition,final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_layout_girdview, null);
        }

        CanAddInListViewGridView canAddInListViewGridView = (CanAddInListViewGridView)
                convertView.findViewById(R.id.channel_item_child_gridView);

        ArrayList<DataCheck.DataCheckChild> dataCheckChildren = mCheckListChild.get(groupPosition);

//        Log.e("groupPosition","getChildView="+groupPosition);
//        Log.e("childPosition","getChildView="+childPosition);
//        Log.e("isLastChild","getChildView="+isLastChild);

        if(dataCheckChildren.size()==0 && groupPosition==1){
            ToastUtil.show(context,"请先选择年级");
        }

        final GridViewAdapter simpleAdapter = new GridViewAdapter(context, dataCheckChildren);

        canAddInListViewGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                simpleAdapter.changeState(position);  //TODO 实现单选

                elv_collocation.collapseGroup(groupPosition);//手动实现关闭操作
//                elv_collocation.expandGroup(groupPosition); //手动实现展开操作

                ArrayList<DataCheck.DataCheckChild> dataCheckChildren = mCheckListChild.get(groupPosition);
                if(dataCheckChildren != null){

                    String childname = dataCheckChildren.get(position).getChildname();

//                    String childid = dataCheckChildren.get(position).getChildid();

//                        Log.e("1111","childname=="+childname);
//                        Log.e("2222","childid=="+childid);

//                    mCheckList.set(0,new DataCheck("工种", "",childname));
//                    notifyDataSetChanged();

                    if((Boolean) SPUtil.get(HomeActivity.TYPE, true)){

                        if(groupPosition==0){
                            mCheckList.set(0,new DataCheck("工种", "",childname));
                        }else if(groupPosition==1){
                            mCheckList.set(1,new DataCheck("出工人数", "",childname));
                        }else  if(groupPosition==2){
                            mCheckList.set(2,new DataCheck("附近", "",childname));
                        }else if(groupPosition==3){
                            mCheckList.set(3,new DataCheck("用工天数", "",childname));
                        }


                    } else {
                        if(groupPosition==0){
                            mCheckList.set(0,new DataCheck("年级", "",childname));
                        }else if(groupPosition==1){
                            mCheckList.set(1,new DataCheck("科目", "",childname));
                        }else  if(groupPosition==2){
                            mCheckList.set(2,new DataCheck("附近", "",childname));
                        }else if(groupPosition==3){
                            mCheckList.set(3,new DataCheck("上课天数", "",childname));
                        }else  if(groupPosition==4){
                            mCheckList.set(4,new DataCheck("学历", "",childname));
                        }
                    }

                    notifyDataSetChanged();
                }

                if(mOnGridItemClickListener != null){
                    mOnGridItemClickListener.onGridItemClick(groupPosition,position);
                }



            }
        });

        canAddInListViewGridView.setAdapter(simpleAdapter);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // return false;
        return true;
    }
}
