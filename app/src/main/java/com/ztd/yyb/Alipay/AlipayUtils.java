package com.ztd.yyb.Alipay;

import android.util.Log;

import com.ztd.yyb.util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by  on 2017/4/17.
 */

public class AlipayUtils {

    public static String getPayStr(String goodsName, String goodsDetail, String price,String outTradeNo,String callBackURL){
        String orderStr = getOrderInfo(goodsName,goodsDetail,price,outTradeNo,callBackURL);
        String sign = sign(orderStr);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderStr + "&sign=\"" + sign + "\"&" + getSignType();
        return payInfo;
    }

    private static String getOrderInfo(String goodsName, String goodsDetail, String price,String outTradeNo,String callBackURL) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Constants.ALI_APP_ID + "\"";
        // 签约卖家支付宝账号
//        orderInfo += "&seller_id=" + "\"" + QavsdkApplication.getContext().getString(R.string.alipay_pay_SELLER) + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + goodsName + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + goodsDetail + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + callBackURL + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"15m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        return orderInfo;
    }

    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private static String sign(String content) {

        String sign = SignUtils.sign(content, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB");

        if(sign==null){

            Log.e("111111111","222222222");

            return  "";
        }

        return sign;
    }
}
