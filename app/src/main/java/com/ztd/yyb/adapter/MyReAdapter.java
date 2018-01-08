package com.ztd.yyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ztd.yyb.R;
import com.ztd.yyb.bean.beanMyReleJj.OrderJjinfoBean;
import com.ztd.yyb.bean.beanMyReleYg.OrderYginfoBean;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.SPUtil;

import java.util.List;

import static com.ztd.yyb.activity.my.MyReleaseActivity.REORDERTYPE;

/**
 * 我的发布适配器
 * Created by  on 2017/3/30.
 */
public class MyReAdapter extends RecyclerView.Adapter<MyReAdapter.MainViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<OrderYginfoBean> mYginfoList;
    List<OrderJjinfoBean> mJjinfoList;

    OnItemClickListener mOnItemClickListener;

    public MyReAdapter(Context context, List<OrderYginfoBean> mYginfoList,
                       List<OrderJjinfoBean> mJjinfoList,
                       OnItemClickListener mOnItemClickListener) {

        super();

        inflater = LayoutInflater.from(context);

        this.context = context;

        this.mYginfoList = mYginfoList;
        this.mJjinfoList = mJjinfoList;

        this.mOnItemClickListener = mOnItemClickListener;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {

        String flag = (String) SPUtil.get(REORDERTYPE, "");
        if (flag.equals("1")) {
            return mYginfoList.size();
        } else {
            return mJjinfoList.size();
        }

    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = inflater
                .inflate(R.layout.item_myrelease, parent, false);

        MainViewHolder vh = new MainViewHolder(view, mOnItemClickListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {


        String flag = (String) SPUtil.get(REORDERTYPE, "");

        if (flag.equals("1")) {//  return mYginfoList.size();

            holder.bindYgData(mYginfoList.get(position));

        } else {//   return mJjinfoList.size();

            holder.bindJjData(mJjinfoList.get(position));

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_myreleatime, tv_myrelstate, tv_myrework, tv_myrestarttime, tv_myreaddress;

        TextView tv_myreone, tv_myretwo, tv_myrethree;

        ImageView imagemyre_type,imageView;

        private OnItemClickListener mOnItemClickListener;

        public MainViewHolder(View itemView, OnItemClickListener mOnItemClickListener) {
            super(itemView);

            this.mOnItemClickListener = mOnItemClickListener;

            itemView.setOnClickListener(this);

            tv_myreleatime = (TextView) itemView.findViewById(R.id.tv_myreleatime);
            tv_myrelstate = (TextView) itemView.findViewById(R.id.tv_myrelstate);
            tv_myrework = (TextView) itemView.findViewById(R.id.tv_myrework);
            tv_myrestarttime = (TextView) itemView.findViewById(R.id.tv_myrestarttime);
            tv_myreaddress = (TextView) itemView.findViewById(R.id.tv_myreaddress);

            imagemyre_type = (ImageView) itemView.findViewById(R.id.imagemyre_type);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);


            tv_myreone = (TextView) itemView.findViewById(R.id.tv_myreone);
            tv_myretwo = (TextView) itemView.findViewById(R.id.tv_myretwo);
            tv_myrethree = (TextView) itemView.findViewById(R.id.tv_myrethree);

            tv_myreone.setOnClickListener(this);
            tv_myretwo.setOnClickListener(this);
            tv_myrethree.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }

        public void bindYgData(OrderYginfoBean itemdatayg) {

            String ygbdtype = itemdatayg.getYgbdtype();//1：普通订单 2：抢

            if (ygbdtype != null) {
                if (ygbdtype.equals("1")) {
                    imagemyre_type.setImageResource((R.mipmap.list_icon_putong));
                } else {
                    imagemyre_type.setImageResource((R.mipmap.list_icon_qiangxiu));
                }
            }

            String ygbdaddress = itemdatayg.getYgbdaddress();
            String ygbprovince = itemdatayg.getYgbprovince();
            String ygbcity = itemdatayg.getYgbcity();
            String ygbarea = itemdatayg.getYgbarea();

            tv_myreaddress.setText(ygbprovince + ygbcity + ygbarea + ygbdaddress);

            String ygbocreatetime = itemdatayg.getYgbocreatetime();

            tv_myreleatime.setText(ygbocreatetime);

            String ygbdtimearrival = itemdatayg.getYgbdtimearrival();

            tv_myrestarttime.setText("到场时间：" + ygbdtimearrival);

            String ygblcname = itemdatayg.getYgblcname();

            tv_myrework.setText("工种：" + ygblcname);


            String yglogo = itemdatayg.getYglogo();
            String test = Constants.BASE_URL + "logo/" + yglogo;


            Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.list_icon_user)
                    .placeholder(R.mipmap.list_icon_user)
                    .transform(new GlideCircleTransform(context)).into(imageView);



            String ygbdauditing = itemdatayg.getYgbdauditing(); //0 未审核 1 审核通过 2 审核未通过---------------------1
            String ygbdstate = itemdatayg.getYgbdstate();       //  0 未接 1已接 2取消-----------------------------2
            String ygbotype = itemdatayg.getYgbotype();//0 进行中 1 已完成 2 已取消  3：施工进行中 4:待业主确认 9：所有进行中---------------3
            String ygboastate = itemdatayg.getYgboastate(); //0 未确认 1 已确认
            String ygbeid = itemdatayg.getYgbeid();

//            Log.e("ygbdauditing","="+ygbdauditing);//0
//            Log.e("ygbdstate","="+ygbdstate);//1
//            Log.e("ygbotype","="+ygbotype);//1
//            Log.e("ygboastate","="+ygboastate);//0
//            Log.e("ygbeid","="+ygbeid);//


            setmystate(ygbdauditing, ygbdstate, ygbotype, ygboastate);


            if (ygbotype.equals("2")) {

                tv_myretwo.setText("查看详情");
                tv_myretwo.setVisibility(View.VISIBLE);
                tv_myrethree.setVisibility(View.GONE);
                tv_myreone.setVisibility(View.GONE);

            } else {

                if (ygbdauditing.equals("0")) {

                    tv_myreone.setText("取消订单");
                    tv_myretwo.setText("查看详情");
                    tv_myrethree.setVisibility(View.GONE);

                    tv_myretwo.setVisibility(View.VISIBLE);
                    tv_myreone.setVisibility(View.VISIBLE);

                } else if (ygbdauditing.equals("1")) {

                    if (ygbdstate.equals("0")) {

                        tv_myreone.setText("取消订单");
                        tv_myretwo.setText("查看详情");
                        tv_myrethree.setText("加价");

                        tv_myreone.setVisibility(View.VISIBLE);
                        tv_myretwo.setVisibility(View.VISIBLE);
                        tv_myrethree.setVisibility(View.VISIBLE);

                    } else {

                        if (ygbotype.equals("0") || ygbotype.equals("3")) {

                            tv_myretwo.setText("查看详情");
                            tv_myrethree.setText("施工完成");

                            tv_myreone.setVisibility(View.GONE);

                            tv_myretwo.setVisibility(View.VISIBLE);
                            tv_myrethree.setVisibility(View.VISIBLE);

                        } else {

                            if (ygboastate.equals("0")) {

                                tv_myreone.setVisibility(View.GONE);
                                tv_myretwo.setText("查看详情");
                                tv_myrethree.setText("同意支付");

                                tv_myretwo.setVisibility(View.VISIBLE);
                                tv_myrethree.setVisibility(View.VISIBLE);

                            } else {

                                if (ygbotype.equals("1")) {


                                    if (ygbeid.equals("")) {

                                        tv_myreone.setVisibility(View.GONE);

                                        tv_myretwo.setText("查看详情");
                                        tv_myrethree.setText(" 去评价 ");

                                        tv_myretwo.setVisibility(View.VISIBLE);
                                        tv_myrethree.setVisibility(View.VISIBLE);

                                    } else {

                                        tv_myretwo.setText("查看详情");
                                        tv_myretwo.setVisibility(View.VISIBLE);
                                        tv_myreone.setVisibility(View.GONE);
                                        tv_myrethree.setVisibility(View.GONE);


                                    }


                                }
                            }
                        }
                    }
                } else {

                    tv_myreone.setText("取消订单");
                    tv_myretwo.setText("查看详情");
                    tv_myrethree.setVisibility(View.GONE);

                    tv_myreone.setVisibility(View.VISIBLE);
                    tv_myretwo.setVisibility(View.VISIBLE);


                }
            }


        }


        public void bindJjData(OrderJjinfoBean itemdatajj) {

            imagemyre_type.setVisibility(View.GONE);

            String ygbprovince = itemdatajj.getYgbprovince();
            String ygbcity = itemdatajj.getYgbcity();
            String ygbarea = itemdatajj.getYgbarea();

            String ygbscname = itemdatajj.getYgbscname();
            tv_myrework.setText("科目：" + ygbscname);
            String ygbocreatetime = itemdatajj.getYgbocreatetime();
            tv_myreleatime.setText(ygbocreatetime);
            String ygbdgmounttime = itemdatajj.getYgbdgmounttime();
            tv_myrestarttime.setText("开课时间：" + ygbdgmounttime);

            String ygbdgaddress = itemdatajj.getYgbdgaddress();

            tv_myreaddress.setText(ygbprovince + ygbcity + ygbarea + ygbdgaddress);

            String yglogo = itemdatajj.getYglogo();
            String test = Constants.BASE_URL + "logo/" + yglogo;


            Glide.with(context).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.list_icon_user)
                    .placeholder(R.mipmap.list_icon_user)
                    .transform(new GlideCircleTransform(context)).into(imageView);



            String ygbdauditing = itemdatajj.getYgbdgauditing(); //0 未审核 1 审核通过 2 审核未通过---------------------1
            String ygbdstate = itemdatajj.getYgbdgstate();       //  0 未接 1已接 2取消-----------------------------2
            String ygbotype = itemdatajj.getYgbotype();//0 进行中 1 已完成 2 已取消  3：施工进行中 4:待业主确认 9：所有进行中---------------3
            String ygboastate = itemdatajj.getYgboastate(); //0 未确认 1 已确认
            String ygbeid = itemdatajj.getYgbeid();

            setmystate(ygbdauditing, ygbdstate, ygbotype, ygboastate);

//                Log.e("ygbdauditing","="+ygbdauditing);
//                Log.e("ygbdstate","="+ygbdstate);
//                Log.e("ygbotype","="+ygbotype);
//                Log.e("ygboastate","="+ygboastate);

//                Log.e("ygbeid","="+ygbeid);

            if (ygbotype.equals("2")) {

                tv_myreone.setText("查看详情");

                tv_myrethree.setVisibility(View.GONE);

                tv_myretwo.setVisibility(View.GONE);
                tv_myreone.setVisibility(View.VISIBLE);

            } else {

                if (ygbdauditing.equals("0")) {

                    tv_myreone.setText("取消订单");
                    tv_myretwo.setText("查看详情");
                    tv_myrethree.setVisibility(View.GONE);

                    tv_myreone.setVisibility(View.VISIBLE);
                    tv_myretwo.setVisibility(View.VISIBLE);

                } else if (ygbdauditing.equals("1")) {

                    if (ygbdstate.equals("0")) {

                        tv_myreone.setText("取消订单");
                        tv_myretwo.setText("查看详情");
                        tv_myrethree.setVisibility(View.GONE);

                        tv_myreone.setVisibility(View.VISIBLE);
                        tv_myretwo.setVisibility(View.VISIBLE);

                    } else {

                        if (ygbotype.equals("0")|| ygbotype.equals("3")) {

                            tv_myreone.setVisibility(View.GONE);
                            tv_myretwo.setText("查看详情");
                            tv_myrethree.setText("结束课程");
                            tv_myretwo.setVisibility(View.VISIBLE);
                            tv_myrethree.setVisibility(View.VISIBLE);

                        } else {

                            if (ygboastate.equals("0")) {

                                tv_myreone.setVisibility(View.GONE);

                                tv_myretwo.setText("查看详情");
                                tv_myrethree.setText("同意支付");

                                tv_myretwo.setVisibility(View.VISIBLE);
                                tv_myrethree.setVisibility(View.VISIBLE);

                            } else {

                                if (ygbotype.equals("1")) {

                                    tv_myretwo.setText("查看详情");

                                    tv_myretwo.setVisibility(View.VISIBLE);

                                    tv_myreone.setVisibility(View.GONE);

                                    tv_myrethree.setVisibility(View.GONE);


                                    if (ygbeid == null || ygbeid.equals("")) {

                                        tv_myreone.setVisibility(View.GONE);

                                        tv_myretwo.setText("查看详情");

                                        tv_myrethree.setText("去评价");

                                        tv_myretwo.setVisibility(View.VISIBLE);

                                        tv_myrethree.setVisibility(View.VISIBLE);

                                    }


                                }
                            }
                        }
                    }
                } else {

                    tv_myreone.setText("取消订单");
                    tv_myretwo.setText("查看详情");

                    tv_myrethree.setVisibility(View.GONE);

                    tv_myreone.setVisibility(View.VISIBLE);
                    tv_myretwo.setVisibility(View.VISIBLE);

                }
            }
        }

        //右上角状态
        private void setmystate(String ygbdauditing, String ygbdstate, String ygbotype, String ygboastate) {

            if (ygbotype.equals("2")) {

                tv_myrelstate.setText("已取消");

            } else {

                if (ygbdauditing.equals("0")) {

                    tv_myrelstate.setText("审核中");

                } else if (ygbdauditing.equals("1")) {

                    if (ygbdstate.equals("0")) {

                        tv_myrelstate.setText("未接单");

                    } else {

                        if (ygbotype.equals("0") || ygbotype.equals("3")) {

                            tv_myrelstate.setText("进行中");

                        } else {

                            if (ygboastate.equals("0")) {

                                tv_myrelstate.setText("进行中");

                            } else {

                                if (ygbotype.equals("1")) {

                                    tv_myrelstate.setText("已完成");

                                }
                            }
                        }
                    }
                } else {
                    tv_myrelstate.setText("审核未通过");
                }

            }

        }

    }


}
