package com.ztd.yyb.view;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class FileSizeUtil {
	public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
	public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
	public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
	public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

	/**
	 * 获取文件指定文件的指定单位的大小
	 * 
	 * @param //filePath
	 *            文件路径
	 * @param sizeType
	 *            获取大小的类型1为B、2为KB、3为MB、4为GB
	 * @return double值的大小
	 */
	public static double getFileOrFilesSize(File file, int sizeType) {
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("获取文件大小", "获取失败!");
		}
		return FormetFileSize(blockSize, sizeType);
	}

	/**
	 * 调用此方法自动计算指定文件或指定文件夹的大小
	 * 
	 * @param //filePath
	 *            文件路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	public static String getAutoFileOrFilesSize(File file) {
		// File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("获取文件大小", "获取失败!");
		}
		return FormetFileSize(blockSize);
	}

	/**
	 * 获取指定文件大小
	 * 
	 * @param //f
	 * @return
	 * @throws Exception
	 */
	private static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
//			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}

	/**
	 * 获取指定文件夹
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private static long getFileSizes(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + getFileSize(flist[i]);
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	private static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 转换文件大小,指定转换的类型
	 * 
	 * @param fileS
	 * @param sizeType
	 * @return
	 */
	private static double FormetFileSize(long fileS, int sizeType) {
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
		case SIZETYPE_B:
			fileSizeLong = Double.valueOf(df.format((double) fileS));
			break;
		case SIZETYPE_KB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
			break;
		case SIZETYPE_MB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
			break;
		case SIZETYPE_GB:
			fileSizeLong = Double.valueOf(df
					.format((double) fileS / 1073741824));
			break;
		default:
			break;
		}
		return fileSizeLong;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			// file.delete();
		}
	}

	public static int clearCacheFolder(File dir, long numDays) {

		int deletedFiles = 0;

		if (dir != null && dir.isDirectory()) {

			try {

				for (File child : dir.listFiles()) {
//					Log.e("info", "child==========" + child.getName());
					if (child.isDirectory()) {

						deletedFiles += clearCacheFolder(child, numDays);

					}

					if (child.lastModified() < numDays) {

						if (child.delete()) {

							deletedFiles++;

						}

					}

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return deletedFiles;

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

}