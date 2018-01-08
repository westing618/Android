package com.ztd.yyb.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 *
 */
public class CheckFormatUtils {
	/**
	 * 检查是否为电子邮件地址
	 *
	 * @param line
	 * @return
	 */
	public static boolean checkEmail(String line) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(line);
		return matcher.matches();
	}

	/**
	 * 检查是否为手机号码
	 *
	 * @param phoneNumStr
	 * @return
	 */
	public static boolean checkPhone(String phoneNumStr) {
		try {
			Long.parseLong(phoneNumStr);
			if (phoneNumStr.length() == 11
					&& (phoneNumStr.startsWith("13")
					|| phoneNumStr.startsWith("14")
					|| phoneNumStr.startsWith("17")
					|| phoneNumStr.startsWith("15")
					|| phoneNumStr.startsWith("18")
					|| phoneNumStr.startsWith("400") || phoneNumStr
					.startsWith("100")))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查密码是否符合规范
	 *
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password) {
		if (password == null || password.length() == 0 || password.length() < 6) {
			return false;
		}
		return true;
	}

	/**
	 * 校验str只能是数字,英文字母和中文
	 *
	 * @param str
	 * @return
	 */
	public static boolean isValidTagAndAlias(String str) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 检验account 匹配帐号是否合法
	 *
	 * @param account
	 * @return
	 */

	public static boolean checkAccount(String account) {
		Pattern p = Pattern.compile("^\\w+$");
		Matcher m = p.matcher(account);
		return m.matches();
	}

	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";

		String expression2 = "^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";

		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);

		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static boolean isMobile(String mobiles){

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}
}
