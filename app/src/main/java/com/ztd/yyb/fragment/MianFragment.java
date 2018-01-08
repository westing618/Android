package com.ztd.yyb.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.BannerActivity;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.activity.order.LaborDemandDetailsActivity;
import com.ztd.yyb.activity.order.OwnerReleaseDemandActivity;
import com.ztd.yyb.activity.order.ParentsReleaseDemandActivity;
import com.ztd.yyb.activity.order.SchoolNeedsDetailsActivity;
import com.ztd.yyb.activity.order.SearchActivity;
import com.ztd.yyb.adapter.MianFrAdapter;
import com.ztd.yyb.base.BaseLazyFragment;
import com.ztd.yyb.bean.PicarrayBean;
import com.ztd.yyb.bean.beanMainT.MainTBean;
import com.ztd.yyb.bean.beanMainT.OrderJjinfoBean;
import com.ztd.yyb.bean.beanMainT.SmallimageBean;
import com.ztd.yyb.bean.beanMainf.OrderYginfo;
import com.ztd.yyb.bean.beanMainf.Picarray;
import com.ztd.yyb.bean.beanMainf.RootData;
import com.ztd.yyb.bean.beanMainf.Smallimage;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.view.RollHeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.HomeActivity.PAR_KEY_BANNER;
import static com.ztd.yyb.activity.LoginActivity.USERID;
import static com.ztd.yyb.activity.MainNActivity.mViewpager;
//import static com.ztd.yyb.activity.MainActivity.mViewpager;

/**
 * 主页 Fragment
 * Created by  on 2017/3/13.
 */

public class MianFragment extends BaseLazyFragment
//        implements BGARefreshLayout.BGARefreshLayoutDelegate
{

    public static String TYPE = "flag"; //  1 左业主 ，家长   2 右  师傅 老师( 控制 item 布局按钮 立即抢单 显示隐藏)
    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;
//    @BindView(R.id.home_refresh)
//    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.tv_parents)
    TextView mtvParents;
    @BindView(R.id.tv_teacher)
    TextView mtvTteacher;
    @BindView(R.id.tv_tutor_seach)
    TextView mtvTutorseach;

    @BindView(R.id.refresh)
    TwinklingRefreshLayout mRefreshLayout;

    WebView WebView;
    ImageView imaHomeHead;
    Button btnOrder;
    String userid;
    String mytype = "1";
    SmallimageBean smallimagejj;
    PicarrayBean smallimageYg;
    String lcityid;
    private MianFrAdapter mMyAdapter;
    private RollHeaderView banner;
    private Map<String, String> mDemanMap = new HashMap<>();
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据
    //    String link;
    private Boolean type;
    private List<OrderYginfo> mYonggongList = new ArrayList<>();
    private List<OrderJjinfoBean> mTeachList = new ArrayList<>();

    @Override
    protected void initViewsAndEvents() {

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
           }

        inView();

        initData();

        lcityid = (String) SPUtil.get("lcityid", "");

    }

    private void inView() {
        SPUtil.put(MianFragment.TYPE, "1");//默认我是业主 ，我是家教
        type = (Boolean) SPUtil.get(HomeActivity.TYPE, true);//true 用工  flase 学堂
//        mRefreshLayout.setDelegate(this);
//        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getActivity(), true));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mMyAdapter = new MianFrAdapter(getActivity(), mYonggongList, mTeachList, type);
        mRecyclerView.setAdapter(mMyAdapter);
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);

        setRefresh();
    }
    private void setRefresh() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                          mPageNum=1;
////                        getDemanData();
////                        getDemanVoiceData();
                        refreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageNum++;
                        initData();
                        refreshLayout.finishLoadmore();

                    }
                }, 2000);
            }
        });

    }
    @Subscribe
    public void onEventMainThread(DemanEvet degeEvent) {
        String msg = degeEvent.getMsg();
        if (msg.equals("paysucc")) {// 接受付款成功后 ，订单刷新 的通知
            initDatao();
        }
    }

    private void initData() {

        userid = (String) SPUtil.get(USERID, "");
        type = (Boolean) SPUtil.get(HomeActivity.TYPE, true);//true 用工  flase 学堂
        if (mtvParents == null) {
            return;
        }
        if (mtvTteacher == null) {
            return;
        }
        if (type) {
            mytype = "1";
            mtvParents.setText("我是业主");
            mtvTteacher.setText("我是师傅");
        } else {
            mytype = "2";
            mtvParents.setText("我是家长");
            mtvTteacher.setText("我是老师");
        }
        getDemanList();
    }

    private void initDatao() {

        userid = (String) SPUtil.get(USERID, "");
        type = (Boolean) SPUtil.get(HomeActivity.TYPE, true);//true 用工  flase 学堂

        if (type) {
            mytype = "1";
        } else {
            mytype = "2";
        }
        getDemanList();
    }

    private void getDemanList() {//3.1获取需求列表息（分页）   获取需求列表息（20条） getDemandListLimit
        mDemanMap.clear();
        mDemanMap.put("userid", userid);//
        mDemanMap.put("type", mytype);  // type: 用工类型 1 师傅 2家教   pageNumber:页数 pageSize：大小
        String lcityid = (String) SPUtil.get("lcityid", "");
        mDemanMap.put("lcity", lcityid);        // 城市代码
        mDemanMap.put("pageNumber", String.valueOf(mPageNum));
        mDemanMap.put("pageSize", String.valueOf(mPageSize));
//        showLoadingDialog();
//        Log.e("userid","="+userid);
//        Log.e("mytype","="+mytype);
        OkHttp3Utils.getInstance().doPost(Constants.MAIN_DEMAN_LIST_URL, null, mDemanMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(String s) {// 用工 学堂2种 Json  type: 用工类型 1 师傅 2家教

//                        Log.e("onNext","="+s);
//                        Log.e("mPageNum","="+mPageNum);

                        if (!TextUtils.isEmpty(s)) {
                            if (mytype.equals("1")) {
//                                mYonggongList.clear();
                                mMyAdapter.notifyDataSetChanged();
                                getYongGonData(s);
                            } else if (mytype.equals("2")) {
//                                mTeachList.clear();
                                mMyAdapter.notifyDataSetChanged();
                                getTeacherData(s);
                            }
                        } else {
                            ToastUtil.show(mContext, "服务器没有数据");
                        }
                    }
                });
    }

    private void getYongGonData(String s) {
        try {
            Gson gson = new Gson();
            RootData rootData = gson.fromJson(s, RootData.class);
            String hotmap = rootData.getData().getHotmap();
            WebView.loadUrl(Constants.BASE_URL + hotmap + lcityid);
            final List<Picarray> picarray = rootData.getData().getPicarray();
            List<String> imgUrlList = new ArrayList<>();
            for (int i = 0; i < picarray.size(); i++) {
                String imagurl = Constants.BASE_URL + "upload/image/" + picarray.get(i).getUrl();
                imgUrlList.add(imagurl);
            }
            banner.setImgUrlData(imgUrlList);// banner设置图片显示
            banner.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
                @Override
                public void HeaderViewClick(int position) {// kind;// 种类 1 广告 2 用工 3 家教
                    String type = picarray.get(position).getType(); //type： 1 资讯 2 订单 3 外链接
                    String kind = picarray.get(position).getKind();
                    String picid = picarray.get(position).getPicid();
                    String aid = picarray.get(position).getAid();
                    if (type != null) {
                        if (type.equals("3")) {
                            Intent intent = new Intent(getActivity(), BannerActivity.class);
                            String link = picarray.get(position).getLink();
                            intent.putExtra(PAR_KEY_BANNER, "" + link);
                            startActivity(intent);
                        } else if (type.equals("2")) {
                            if (kind != null) {
                                if (kind.equals("2")) {//用工
                                    Intent intent1 = new Intent(getActivity(), LaborDemandDetailsActivity.class);
                                    intent1.putExtra("ygboid", picid);
                                    startActivity(intent1);
                                } else if (kind.equals("3")) {
                                    Intent intent2 = new Intent(getActivity(), SchoolNeedsDetailsActivity.class);
                                    intent2.putExtra("ygboid", picid);
                                    startActivity(intent2);
                                } else if (kind.equals("1")) {
                                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                            }
                        } else if (type.equals("1")) {
                            if (kind != null) {
                                if (kind.equals("1")) {
                                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                } else if (kind.equals("2")) {
                                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }
            });

            Smallimage smallimage = rootData.getData().getSmallimage();
            String url = smallimage.getUrl();
            // 加载图片  小广告图
            Glide.with(mContext).load(Constants.BASE_URL + "upload/image/" + url)
                    .error(R.mipmap.mo_cal)
                    .placeholder(R.mipmap.mo_cal)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imaHomeHead);

            List<OrderYginfo> orderYginfo = rootData.getData().getOrderYginfo();
            if (orderYginfo.size() == 0) {
//                ToastUtil.show(mContext, "没有数据");
            } else {
                mYonggongList.addAll(orderYginfo);
                mMyAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            ToastUtil.show(getActivity(), "数据解析异常");
        }


//        MianBean mianBean = gson.fromJson(s, MianBean.class);

//        if (mianBean.getData().getSuccess().equals("1")) {

        try {

//                String hotmap = mianBean.getData().getHotmap();//热力图
//                Log.e("hotmap",""+hotmap);
//                WebView.loadUrl(Constants.BASE_URL + hotmap + lcityid);
//                final List<PicarrayBeanX> picarray = mianBean.getData().getPicarray();
//                List<String> imgUrlList = new ArrayList<>();
//                Log.e("size",""+imgUrlList.size());
//                for (int i = 0; i < picarray.size(); i++) {
//                    String imagurl = Constants.BASE_URL + "upload/image/" + picarray.get(i).getUrl();
//                    imgUrlList.add(imagurl);
//                }
//
//                banner.setImgUrlData(imgUrlList);// banner设置图片显示
//                banner.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
//                    @Override
//                    public void HeaderViewClick(int position) {// kind;// 种类 1 广告 2 用工 3 家教
//                        String type = picarray.get(position).getType(); //type： 1 资讯 2 订单 3 外链接
//                        String kind = picarray.get(position).getKind();
//                        String picid = picarray.get(position).getPicid();
//                        String aid = picarray.get(position).getAid();
//                        if (type != null) {
//                            if (type.equals("3")) {
//                                Intent intent = new Intent(getActivity(), BannerActivity.class);
//                                String link = picarray.get(position).getLink();
//                                intent.putExtra(PAR_KEY_BANNER, "" + link);
//                                startActivity(intent);
//                            } else if (type.equals("2")) {
//                                if (kind != null) {
//                                    if (kind.equals("2")) {//用工
//                                        Intent intent1 = new Intent(getActivity(), LaborDemandDetailsActivity.class);
//                                        intent1.putExtra("ygboid", picid);
//                                        startActivity(intent1);
//                                    } else if (kind.equals("3")) {
//                                        Intent intent2 = new Intent(getActivity(), SchoolNeedsDetailsActivity.class);
//                                        intent2.putExtra("ygboid", picid);
//                                        startActivity(intent2);
//                                    } else if (kind.equals("1")) {
//                                        String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
//                                        Intent intent = new Intent(getActivity(), BannerActivity.class);
//                                        intent.putExtra("banner", url);
//                                        startActivity(intent);
//                                    }
//                                }
//                            } else if (type.equals("1")) {
//                                if (kind != null) {
//                                    if (kind.equals("1")) {
//                                        String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
//                                        Intent intent = new Intent(getActivity(), BannerActivity.class);
//                                        intent.putExtra("banner", url);
//                                        startActivity(intent);
//                                    } else if (kind.equals("2")) {
//                                        String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
//                                        Intent intent = new Intent(getActivity(), BannerActivity.class);
//                                        intent.putExtra("banner", url);
//                                        startActivity(intent);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                });
//
//                smallimageYg = mianBean.getData().getSmallimage();
////            link = smallimage.getLink();
//                String url = smallimageYg.getUrl();
//                // 加载图片  小广告图
//                Glide.with(mContext).load(Constants.BASE_URL + "upload/image/" + url)
//                        .error(R.mipmap.mo_cal)
//                        .placeholder(R.mipmap.mo_cal)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(imaHomeHead);
//                List<OrderYginfoBean> orderYginfo = mianBean.getData().getOrderYginfo();
//                if (orderYginfo.size() == 0) {
//                    ToastUtil.show(mContext, "没有数据");
//                } else {
//                    mYonggongList.addAll(orderYginfo);
//                    mMyAdapter.notifyDataSetChanged();
//                }

        } catch (Exception e) {
            ToastUtil.show(getActivity(), "数据解析异常");
        }

//        } else {
//            ToastUtil.show(mContext, "获取数据失败");
//        }

    }

    private void getTeacherData(String s) {

        Gson gson = new Gson();

        MainTBean mainTBean = gson.fromJson(s, MainTBean.class);

        if (mainTBean.getData().getSuccess().equals("1")) {

            String hotmap = mainTBean.getData().getHotmap();//热力图

            WebView.loadUrl(Constants.BASE_URL + hotmap + lcityid);

            final List<com.ztd.yyb.bean.beanMainT.PicarrayBean> picarray = mainTBean.getData().getPicarray();

            final List<String> imgUrlList = new ArrayList<>();

            for (int i = 0; i < picarray.size(); i++) {
                String imagurl = Constants.BASE_URL + "upload/image/" + picarray.get(i).getUrl();
                imgUrlList.add(imagurl);
            }

            banner.setImgUrlData(imgUrlList);// banner设置图片显示

            banner.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
                @Override
                public void HeaderViewClick(int position) {

                    //  1 资讯 2 订单 3 外链接

                    String link = picarray.get(position).getLink();

                    String type = picarray.get(position).getType();

                    String kind = picarray.get(position).getKind();

                    String picid = picarray.get(position).getPicid();

                    String aid = picarray.get(position).getAid();



                    if (type != null) {

                        if (type.equals("3")) {

                            Intent intent = new Intent(getActivity(), BannerActivity.class);

                            intent.putExtra(PAR_KEY_BANNER, "" + link);
                            startActivity(intent);

                        } else if (type.equals("2")) {

                            if (kind != null) {

                                if (kind.equals("2")) {//用工

                                    Intent intent1 = new Intent(getActivity(), LaborDemandDetailsActivity.class);
                                    intent1.putExtra("ygboid", picid);
                                    startActivity(intent1);

                                } else if (kind.equals("3")) {//家教

                                    Intent intent2 = new Intent(getActivity(), SchoolNeedsDetailsActivity.class);
                                    intent2.putExtra("ygboid", picid);
                                    startActivity(intent2);

                                } else if (kind.equals("1")) {

                                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);

                                }
                            }


                        } else if (type.equals("1")) {

                            if (kind != null) {

                                if (kind.equals("1")) {

                                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);

                                }
                                if (kind.equals("2")) {
                                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }

                            }


                        }
                    }


                }
            });

            smallimagejj = mainTBean.getData().getSmallimage();

            String url = smallimagejj.getUrl();


            // 加载图片  小广告图
            Glide.with(mContext).load(Constants.BASE_URL + "upload/image/" + url)
                    .error(R.mipmap.mo_cal)
                    .placeholder(R.mipmap.mo_cal)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imaHomeHead);

            List<OrderJjinfoBean> orderJjinfo = mainTBean.getData().getOrderJjinfo();
            if (orderJjinfo.size() == 0) {
//                ToastUtil.show(mContext, "没有数据");
            } else {
                mTeachList.addAll(orderJjinfo);
                mMyAdapter.notifyDataSetChanged();
            }


        } else {
            ToastUtil.show(mContext, "获取数据失败");
        }

    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_tutor_head, view, false);
        mMyAdapter.setHeaderView(header);
        banner = (RollHeaderView) header.findViewById(R.id.banner);
        imaHomeHead = (ImageView) header.findViewById(R.id.image_home_head);
        imaHomeHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mytype.equals("1")) {//用工
                    String aid = smallimageYg.getAid();
                    String kind = smallimageYg.getKind();
                    String link = smallimageYg.getLink();
                    String picid = smallimageYg.getPicid();
                    String type = smallimageYg.getType();
//                    String url = smallimageYg.getUrl();
                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                    if (type != null) {
                        if (type.equals("3")) {
                            Intent intent = new Intent(getActivity(), BannerActivity.class);
                            intent.putExtra(PAR_KEY_BANNER, "" + link);
                            startActivity(intent);
                        } else if (type.equals("2")) {
                            if (kind != null) {
                                if (kind.equals("2")) {//用工
                                    Intent intent1 = new Intent(getActivity(), LaborDemandDetailsActivity.class);
                                    intent1.putExtra("ygboid", picid);
                                    startActivity(intent1);
                                } else if (kind.equals("3")) {//家教
                                    Intent intent2 = new Intent(getActivity(), SchoolNeedsDetailsActivity.class);
                                    intent2.putExtra("ygboid", picid);
                                    startActivity(intent2);
                                } else if (kind.equals("1")) {
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                            }
                        } else if (type.equals("1")) {
                            if (kind != null) {
                                if (kind.equals("1")) {
//                                    String url="http://api.yogobei.com/ad.jsp?aid="+aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                                if (kind.equals("2")) {
//                                    String url="http://api.yogobei.com/ad.jsp?aid="+aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                } else if (mytype.equals("2")) {//家教
                    String aid = smallimagejj.getAid();
                    String kind = smallimagejj.getKind();
                    String link = smallimagejj.getLink();
                    String picid = smallimagejj.getPicid();
                    String type = smallimagejj.getType();
//                    String url = smallimagejj.getUrl();
                    String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                    if (type != null) {
                        if (type.equals("3")) {
                            Intent intent = new Intent(getActivity(), BannerActivity.class);
                            intent.putExtra(PAR_KEY_BANNER, "" + link);
                            startActivity(intent);
                        } else if (type.equals("2")) {
                            if (kind != null) {
                                if (kind.equals("2")) {//用工
                                    Intent intent1 = new Intent(getActivity(), LaborDemandDetailsActivity.class);
                                    intent1.putExtra("ygboid", picid);
                                    startActivity(intent1);
                                } else if (kind.equals("3")) {//家教
                                    Intent intent2 = new Intent(getActivity(), SchoolNeedsDetailsActivity.class);
                                    intent2.putExtra("ygboid", picid);
                                    startActivity(intent2);
                                } else if (kind.equals("1")) {
//                                    String url="http://api.yogobei.com/ad.jsp?aid="+aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                            }
                        } else if (type.equals("1")) {
                            if (kind != null) {
                                if (kind.equals("1")) {
//                                    String url="http://api.yogobei.com/ad.jsp?aid="+aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }
                                if (kind.equals("2")) {
//                                    String url="http://api.yogobei.com/ad.jsp?aid="+aid;
                                    Intent intent = new Intent(getActivity(), BannerActivity.class);
                                    intent.putExtra("banner", url);
                                    startActivity(intent);
                                }

                            }


                        }
                    }
                }


            }
        });

        btnOrder = (Button) header.findViewById(R.id.rob_order);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = btnOrder.getText().toString().toString();

                if (s.equals("我要发布")) {

//                    Log.e("我要发布","我要发布");

                    if (type) {//true 用工  flase 学堂
                        startActivity(new Intent(getActivity(), OwnerReleaseDemandActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), ParentsReleaseDemandActivity.class));
                    }

                } else {//我要抢单

//                    Log.e("我要抢单","我要抢单");

                    SPUtil.put(MianFragment.TYPE, "2");
                    mViewpager.setCurrentItem(1);

                    DemanEvet messageEvent = new DemanEvet();
                    messageEvent.setMsg("Deman");
                    messageEvent.setId("1");
                    EventBus.getDefault().post(messageEvent);

                }

            }
        });

        WebView = (android.webkit.WebView) header.findViewById(R.id.web_map);
        WebView.getSettings().setJavaScriptEnabled(true);//支持javascript

    }

    private void setFooterView(RecyclerView view) {

        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_tutor_footer, view, false);
        mMyAdapter.setFooterView(footer);
        RelativeLayout rela_main = (RelativeLayout) footer.findViewById(R.id.rela_main);
        rela_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewpager.setCurrentItem(1);
                DemanEvet messageEvent = new DemanEvet();
                messageEvent.setMsg("Deman");
                messageEvent.setId("1");
                EventBus.getDefault().post(messageEvent);

            }
        });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_home_tutor;
    }

    @OnClick({R.id.tv_parents, R.id.tv_teacher, R.id.lin_back, R.id.tv_tutor_seach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_parents://左
                mtvParents.setTextColor(getResources().getColor(R.color.themecolor));
                mtvTteacher.setTextColor(getResources().getColor(R.color.color_66));
                btnOrder.setText("我要发布");
                SPUtil.put(MianFragment.TYPE, "1");

                DemanEvet messageEvent = new DemanEvet();
                messageEvent.setMsg("Deman");
                EventBus.getDefault().post(messageEvent);

                mMyAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_teacher://右
                btnOrder.setText("我要听单");
                mtvTteacher.setTextColor(getResources().getColor(R.color.themecolor));
                mtvParents.setTextColor(getResources().getColor(R.color.color_66));
                SPUtil.put(MianFragment.TYPE, "2");
                mMyAdapter.notifyDataSetChanged();

                DemanEvet messageEvent1 = new DemanEvet();
                messageEvent1.setMsg("Deman");
                EventBus.getDefault().post(messageEvent1);

                break;
            case R.id.lin_back:
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;
            case R.id.tv_tutor_seach:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

//    @Override
//    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//
//        mRefreshLayout.endRefreshing();
//    }
//
//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//
//        mRefreshLayout.endLoadingMore();
//
//
//        return true;
//    }
}
