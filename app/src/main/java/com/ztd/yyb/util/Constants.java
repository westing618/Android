package com.ztd.yyb.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/3/4.
 */
public interface Constants {

//    String BASE_URL = "http://192.168.0.10:8080/ygb/"; //测试地址 用工贝

   String BASE_URL="http://api.yogobei.com/";//  地址 用工贝

    String LOGIN_MESSAGE_URL = BASE_URL + "memberAppController.do?sendLoginMessage";//验证码
    String CODEMESSAGE_URL = BASE_URL + "memberAppController.do?codeMessage";//1.3获取登录验证码 （前提已登录）
    String CODECHECK_URL = BASE_URL + "memberAppController.do?codeCheck";//1.4校验验证码（前提已登录）
    String LOGIN_URL = BASE_URL + "memberAppController.do?loginCheck";//登录
    String MAIN_BANNER_URL = BASE_URL + "memberAppController.do?findInfo";//一级首页
    String MAIN_DEMAN_LIST_URL = BASE_URL + "memberAppController.do?findSecond";//二级首页
    String ROB_ORDER_URL = BASE_URL + "jobController.do?joborder";// 3.3 用工  家教抢单
    String APP_CONFIG_URL = BASE_URL + "systemController.do?getAppConfig";// 0.获取系统参数数据字典
    String WEATHER_CONFIG_URL = BASE_URL + "systemController.do?getWeather";// 获取天气
    String MY_ORDER_URL = BASE_URL + "memberAppController.do?getmyorder";// 我的订单

    String ADDYG_ORDER_URL = BASE_URL + "jobController.do?addygorder";//  3.7 新增用工需求订单
    String GETLABOURCOST = BASE_URL + "systemController.do?getLabourCost";//  按城市获取工种类型

    String ADDJJ_ORDER_URL = BASE_URL + "jobController.do?addjjorder";//  3.9 新增家教需求订单

    String MY_MONET_URL = BASE_URL + "memberAppController.do?getmymoney";//     5.7我的钱包

    String MONEYHISTORY_URL = BASE_URL + "memberAppController.do?getMoneyHistory";//     5.11 提现记录

    String MY_PAYHOSTORY_URL = BASE_URL + "memberAppController.do?getpayhostory";//     5.8我的钱包-交易记录
    String RANKHISTORY_URL = BASE_URL + "evaluateController.do?getevaluatelist";//     8.2查看评价列表
    String COUPONLIST_URL = BASE_URL + "memberAppController.do?getcouponlist";//     5.14我的优惠券

    String GRTINFO_URL = BASE_URL + "memberAppController.do?getInfo";//     5.1.获取个人信息

    String RECEIOLIST_URL = BASE_URL + "memberAppController.do?getreceiptlist";//    5.6发票信息列表

    String GETPUBLISHLIST_URL = BASE_URL + "memberAppController.do?getpublishList";//    5.3 我的发布

    String UPDATEINFO_URL = BASE_URL + "memberAppController.do?updateInfo";//    5.15修改个人信息  3.4 填写用户信息（完善资料）

    String DEMANDLIST_URL = BASE_URL + "jobController.do?getDemandList";//    //3.1获取需求列表息（分页）
    String DEMANDLISTLIMINT_URL = BASE_URL + "jobController.do?getDemandListLimit";//    //获取需求列表息（20条）

    String GETCERTIFI_URL = BASE_URL + "memberAppController.do?getcertificateList";//    //5.20 我的认证

    String GETCERTIFIDE_URL = BASE_URL + "memberAppController.do?getcertificateDetail";//    //5.21 认证详情

    String UPDATEPAYPW_URL = BASE_URL + "memberAppController.do?updatepaypw";//    //5.19修改支付密码

    String BINDBANDCARD_URL = BASE_URL + "memberAppController.do?bindBandCard";//    //5.9绑定银行卡

    String GETBANKMONEY_URL = BASE_URL + "memberAppController.do?getBankMoney";//                //5.10 提现

    String GETBANKMONEYFINISH_URL = BASE_URL + "memberAppController.do?getBankMoneyFinish";//    //5.10 提现完成

    String ADDRECEIPT_URL = BASE_URL + "memberAppController.do?addreceipt";//    //5.5 开票

    String ADDEVALUATE_URL = BASE_URL + "evaluateController.do?addevaluate";//    //8.1添加评价

    String CANCEL_ORDER_URL = BASE_URL + "jobController.do?cancelorder";//   3.5取消订单

    String CANCEL_ORDERDEMAND_URL = BASE_URL + "jobController.do?canceldemand";//   3.5取消需求单

    String STARTORDER_URL = BASE_URL + "jobController.do?startorder";//    3.6 开始施工/   家教  开始上课
    String FINISHORDER_URL = BASE_URL + "jobController.do?finishorder";//    3.6施工/     家教 完成
    String PAYHORDERORDER_URL = BASE_URL + "jobController.do?payhorder";//    3.8 订单确认付款


    String GETSYSTEMNOTICE_URL = BASE_URL + "noticeController.do?getSystemNotice";//    6.1 系统消息
    String GETDETAILNOTICE_URL = BASE_URL + "noticeController.do?getDetailNotice";//    6.2系统消息详情


    String FINDORDER_URL = BASE_URL + "jobController.do?findorder";//    3.3需求列表筛选

    String GETDEMANDDETAIL_URL = BASE_URL + "jobController.do?getDemandDetail";//    3.2获取需求订单详情

    String MYORDERDETAIL_URL = BASE_URL + "memberAppController.do?getmyorderDetail";//   5.3 订单详情

    String MYPUBLISHDETAIL_URL = BASE_URL + "memberAppController.do?getmypublishDetail";//   5.4 发布详情

   String WXPAY_URL = BASE_URL + "wxPayController.do?topay";//     9.2微信支付
   String ALIPAY_URL = BASE_URL + "aliPayController/sign?";//     9.4获取支付宝签名 aliPayController/sign?
   String GETALIPAY_URL = BASE_URL + "aliPayController/getSign.do";//    9.7支付宝获取签名和参数

   String NOTIFY_URL = BASE_URL + "aliPayController/notify.do";//     支付宝异步通知返回结果

   String PAYCONTR_URL = BASE_URL + "payController.do?balancePay";//   9.3余额支付

   String APPINTRODUCE_URL = BASE_URL + "systemController.do?getAppIntroduce";//   9.1 应用简介

   String ADDFEEDBACK_URL = BASE_URL + "feedbackAppController.do?addfeedback";//   7.1 添加用户反馈信息


    public static final String APP_DIR = Environment
            .getExternalStorageDirectory() + "/zyygb/";
    public static final String IMG_DIR = APP_DIR + "images/";
    //文件路径
    String FILES = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "zyygb" + File.separator;
    //图片缓存路径
    String IMAGE_FILE = FILES + "image" + File.separator;
    String DATA_FILES = FILES + "data"
            + File.separator;


    String PREFERENCE_NAME = "HRK";
    int SOCKET_TIMEOUT = 30000;//接口超时
    String AES_KEY = "abcdef1234567890";
    String DES_KEY = "ucJwldG5";
    String RESPONSE_IS_NULL = "response_is_null";
    String VOLLEY_ERROR_CODE = "volley_error_code";
    int PAGESIZE = 5;
    int PAGESIZE1 = 10;
//    String FILES = Environment
//            .getExternalStorageDirectory().toString()
//            + File.separator
//            + "Hrk" + File.separator;

    String WENXIN_APP_ID = "wx27de7d2ab7c7bff2";//微信ID
    String ALI_APP_ID = "2017040606569032";//支付宝ID
//    String RSA_PRIVATE = "2017040606569032";//支付宝

    String WENXIN_APP_SECRET = "b1a626388b5295faa6e1c4e963f05691";

    String SINA_APP_KEY = "505031379";
    String SINA_APP_SECRET = "b45f210c3b9fa5d20d79dcb447bc000e";
    String SINA_REDIRECT_URL = "http://sns.whalecloud.com";

    String QQ_APP_ID = "1106012576";
    String QQ_APP_KEY = "fFSBKrH9noSt9t7u";
    String URL_LOGINOUT = BASE_URL + "cm/test/member_InfoAction.do?action=logout";
    String APP_ID = "wx1a1f64fcdf4c85a8";
    String API_KEY = "E3747008F40F622F899371CE7088391F";
    String MCH_ID = "1444972102";
    String ALIAPP_ID = "2017022405856241";
    String URL_PAYWC = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";//微信支付端口

    String URL_PAYALI = "http://gmy.xzlpq.com:81/index/Alipay/alipayRequestPara";//支付宝端口

    String URL_ORDERPAY = BASE_URL + "WxOrderPay/wxpayRequestPara";//付款




    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";

}
