package com.ztd.yyb.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class TimeUtils {

	
	public static String getMMDDhhmm() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(date);
	}
	
	public static String gethhmmss() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}
	public static String getmmddhhmm() {
		
		
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
//		return sdf.format(date);
		return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		
	}

	public static String getyyyyMMdd(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String getDD(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("DDD");
		return sdf.format(date);
	}
	
	public static String getMMdd(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		return sdf.format(date);
	}

	public static String getMMMMddhhmmss(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getMMMMddhhmm(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}
	
	public static String gethhmmMMMMdd(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}
	
	public static String getMMddHHmm(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(date);
	}
	
	public static long long2Date(String date) {
		try {
			long time = Long.parseLong(date);
			// ʱ���(����1970��ĺ�����)���˴�Ҫ����1000�������
			return time * 1000L;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return 0;
		}

	}

}
