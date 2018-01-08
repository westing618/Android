package com.ztd.yyb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.activity.LoginActivity;
import com.ztd.yyb.activity.MainActivity;
import com.ztd.yyb.bean.beanHomee.OrderYginfoBean;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.SPUtil;

import java.util.List;

/**
 * Created by  on 2017/3/10.  首页    Adapter
 */

    public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int TYPE_HEADER = 0;  //说明是带有Header的
        public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
        public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

        //获取从Activity中传递过来每个item的数据集合
//        private List<String> mDatas;
        private List<OrderYginfoBean> mjobarrayList ;
        //HeaderView, FooterView
        private View mHeaderView;
        private View mFooterView;
         Context context;
        //构造函数
        public HomeAdapter(Context context,List<OrderYginfoBean> mjobarrayList){
            this.mjobarrayList = mjobarrayList;
            this.context = context;
        }

        //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_yg, parent, false);

        return new ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ListHolder) {

                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了

                String ygbdaddress = mjobarrayList.get(position - 1).getYgbdaddress();//详细地址

                String ygbdkind = mjobarrayList.get(position - 1).getYgblcname();//工种

                String ygbdcreatetime = mjobarrayList.get(position - 1).getYgbdcreatetime();//创建发布时间
                String ygbdtimearrival = mjobarrayList.get(position - 1).getYgbdtimearrival();//施工时间
//                int ygbddays = mjobarrayList.get(position - 1).getYgbddays();//用工天数
                double ygbddays = mjobarrayList.get(position - 1).getYgbddays();
                String ygbdtype = mjobarrayList.get(position - 1).getYgbdtype();//1：普通订单 2：抢修订单
//                String totalcount = mjobarrayList.get(position - 1).getTotalcount();//总金额
                double totalcount = mjobarrayList.get(position - 1).getTotalcount();
                String ygblcprice = mjobarrayList.get(position - 1).getYgblcprice();

//                String ygbdaddprice = mjobarrayList.get(position - 1).getYgbdaddprice();
                double ygbdaddprice = mjobarrayList.get(position - 1).getYgbdaddprice();

                String yglogo = mjobarrayList.get(position - 1).getYglogo();//头像

                String ygbdprovince = mjobarrayList.get(position - 1).getYgbdprovince();
                String ygbdcity = mjobarrayList.get(position - 1).getYgbdcity();
                String ygbdarea = mjobarrayList.get(position - 1).getYgbdarea();

                ((ListHolder) holder).tv_home_address.setText(ygbdprovince+ygbdcity+ygbdarea+ygbdaddress);
                String test= Constants.BASE_URL+"logo/"+yglogo;

                Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.list_icon_user)
                        .placeholder(R.mipmap.list_icon_user)
                        .transform(new GlideCircleTransform(context)).into(((ListHolder) holder).imageView_home);

                ((ListHolder) holder).tv_home_time.setText(ygbdcreatetime);

                ((ListHolder) holder).tv_home_starttime.setText("到场时间："+ ygbdtimearrival);

//                ((ListHolder) holder).tv.setText(ygbdtitle);

                ((ListHolder) holder).tv_home_totalcount.setText("(共计：￥"+totalcount+"元)");

                ((ListHolder) holder).tv_home_price.setText("价格：￥"+ygblcprice+"/天");

                ((ListHolder) holder).tv_home_daynum.setText("用工天数："+ygbddays+"天");


//                if(ygbdaddprice!=null){
                    if(ygbdaddprice==0.0){
                    }else {
                        ((ListHolder) holder).tv_add_price.setText("+￥"+ygbdaddprice);//加价
                    }
//                }


                ((ListHolder) holder).tv_home_work.setText("工种："+ygbdkind);


                    if(ygbdtype != null){
                        if(ygbdtype.equals("1")){
                            ((ListHolder) holder).image_type.setBackground(context.getResources().getDrawable(R.mipmap.list_icon_putong));
                        }else  if(ygbdtype.equals("2")){
                            ((ListHolder) holder).image_type.setBackground(context.getResources().getDrawable(R.mipmap.list_icon_qiangxiu));
                        }

                    }


                ((ListHolder) holder).homeitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SPUtil.put(HomeActivity.TYPE, true);

                        String ISL = (String) SPUtil.get("IS_LOGIN", "");

                        if (ISL.equals("1")) {

                            context.startActivity(new Intent(context, MainActivity.class));

                        } else {

                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    }
                });



                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }

    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder{
        TextView tv,tv_add_price;
        TextView tv_home_time;
        TextView tv_home_work;
        TextView tv_home_daynum;
        TextView tv_home_starttime;
        TextView tv_home_price;
        TextView tv_home_address;
        TextView tv_home_totalcount;
        ImageView image_type;

        ImageView imageView_home;

        LinearLayout homeitem;

        public ListHolder(View itemView) {
            super(itemView);

            //如果是headerview或者是footerview,直接返回

            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }


            tv = (TextView)itemView.findViewById(R.id.tv_title);
            tv_add_price = (TextView)itemView.findViewById(R.id.tv_add_price);

            tv_home_time = (TextView)itemView.findViewById(R.id.tv_home_time);
            tv_home_work = (TextView)itemView.findViewById(R.id.tv_home_work);
            tv_home_daynum = (TextView)itemView.findViewById(R.id.tv_home_daynum);
            tv_home_starttime = (TextView)itemView.findViewById(R.id.tv_home_starttime);
            tv_home_address = (TextView)itemView.findViewById(R.id.tv_home_address);
            image_type = (ImageView)itemView.findViewById(R.id.image_type);

            tv_home_price = (TextView)itemView.findViewById(R.id.tv_home_price);
            tv_home_totalcount = (TextView)itemView.findViewById(R.id.tv_home_totalcount);

            imageView_home= (ImageView)itemView.findViewById(R.id.imageView_home);
            homeitem=(LinearLayout)itemView.findViewById(R.id.homeitem);

        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mjobarrayList.size();
        }else if(mHeaderView == null && mFooterView != null){
            return mjobarrayList.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return mjobarrayList.size() + 1;
        }else {
            return mjobarrayList.size() + 2;
        }
    }
}
