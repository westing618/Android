package com.ztd.yyb.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 曾wt on 2016/1/27.
 */
public class CommonUtil {

    /**
     * return if str is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    public static String dateDiff(String startTime, String endTime,
                                  String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nn = 1000 * 24 * 60 * 60 * 30 * 12;// 一年的毫秒数
        long ny = 1000 * 24 * 60 * 60 * 30;// 一月的毫秒数
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            long year = diff / nd / 30 / 12;// 计算差几年
            long month = diff / nd / 30;// 计算差几个月
            long day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            if (year == 0) {
                if (month == 0) {
                    if (day == 0) {
                        if (hour == 0) {
                            return "刚刚";
                        }
                        return hour + "小时前";
                    } else {

                        return day + "天前";
                    }

                } else {
                    return month + "个月之前";
                }
            } else {
                return year + "年前";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isPWD(String mobiles){
        Pattern p = Pattern.compile("[0-9A-Za-z_]{8,20}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
    /**
     * 判断是否是中文
     *
     * @param name
     * @return
     * @auth Aaron
     */
    public static boolean isChineseUseName(String name) {
        String regEx = "[\\u4E00-\\u9FA5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     *
     * @param //version1
     * @param //version2
     * @return if version1 > version2, return 1, if equal, return 0, else return
     *         -1
     */
    public static boolean VersionComparison(String versionServer, String versionLocal) {
        String version1 = versionServer;
        String version2 = versionLocal;
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");

        int index1 = 0;
        int index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
//            ZozoLog.i(TAG," ===== number1 ====" + Arrays.toString(number1));
            int[] number2 = getValue(version2, index2);
//            ZozoLog.i(TAG," ===== number2 ====" + Arrays.toString(number2));

            if (number1[0] < number2[0]){
//                ZozoLog.i(TAG," ===== number1[0] ====" + number1[0]);
//                ZozoLog.i(TAG," ===== number2[0] ====" + number2[0]);
                return false;
            }
            else if (number1[0] > number2[0]){
//                ZozoLog.i(TAG," ===== number1[0] ====" + number1[0]);
//                ZozoLog.i(TAG," ===== number2[0] ====" + number2[0]);
                return true;
            }
            else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return false;
        if (index1 < version1.length())
            return true;
        else
            return false;
    }

    /**
     *
     * @param version
     * @param index
     *            the starting point
     * @return the number between two dots, and the index of the dot
     */
    public static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }
    public static int dipToPx(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }



}
