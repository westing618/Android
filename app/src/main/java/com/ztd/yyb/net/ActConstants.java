package com.ztd.yyb.net;

/**
 * 接口ACT列表
 * Created by chenjh on 2015/9/23.
 */
public interface ActConstants {
    //获取可用的接口信息
    int INTERFACE_INFO_ACT = 1105;
    //获取升级信息
    int UPDATE_INFO_ACT = 1101;

    //收集用户编码
    int UPLOAD_UID_ACT = 1007;

    //一键登录
    int FAST_LOGIN_ACT = 1001;

    //业务密码登录
    int SERVICE_LOGIN_ACT = 1002;

    //获取短信验证码
    int SMS_CODE_ACT = 1003;
    //短信登录
    int SMS_LOGIN_ACT = 1004;

    //刷新客户端授权码
    int REFRESH_AUTH_CODE_ACT = 1005;

    //副号信息查询
    int QUERY_MAJORNUMBER_ACT = 1201;

    //套餐分钟数查询
    int COST_MAJORNUMBER_ACT = 1301;

    //套餐流量查询
    int FLOW_MAJORNUMBER_ACT = 1302;

    //用户订购流量包
    int FLOW_ORDER_ACT = 1306;

    //套餐预受理查询接口
    int COST_PRE_ORDER_ACT = 1304;

    //产品订购管理
    int COST_ORDER_ACT = 1305;

    //用户账单查询
    int QUERY_BILL_INFO_ACT = 1303;

    //选号信息查询
    int QUERY_PHONE_NUMBER_ACT = 1207;

    //开通副号
    int OPEN_MINOR_NUMBER_ACT = 1208;
    //关闭副号
    int CLOSE_MINOR_NUMBER_ACT = 1209;

    //副号名称设置
    int SET_MINOR_NAME_ACT = 1202;

    //副号开机
    int STARTUP_MINOR_NUMBER_ACT = 1205;
    //副号关机
    int SHUTDOWN_MINOR_NUMBER_ACT = 1204;

    //多号通副号码短信接收（屏蔽）
    int SMS_SETTING_MINOR_NUMBER_ACT = 1203;

    //用户剩余话费、流量、通话分钟数查询
    int QUERY_RESIDUAL_BILL_FLOW_ACT = 1309;

    //上报极光注册ID
    int SEND_JPUSH_REG_ID_ACT = 1501;

    //意见反馈
    int FEEDBACK_ACT = 1102;

    //上传操作信息
    int UPLOAD_LOG_ACT = 1104;

    //用户信息查询
    int USER_INFO_ACT = 1308;

    //套餐详情查询
    int COST_DETAIL_ACT = 1307;
    //获取当前活动信息
    int MARKETING_ACTIVITIES_ACT = 1401;
}
