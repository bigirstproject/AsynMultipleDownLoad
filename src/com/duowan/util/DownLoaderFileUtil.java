package com.duowan.util;

public class DownLoaderFileUtil {

	private static final int KB = 1024;
	private static final int MB = 1024 * 1024;
	private static final int GB = 1024 * 1024 * 1024;

	public static String getFileSize(long size) {
		if (size <= 0) {
			return "0KB";
		}
		StringBuffer sb = new StringBuffer();
		int currentSize = 0;
		int remainderSize = 0;
		if (size >= GB) {
			currentSize = (int) (size / GB);
			if (currentSize > 0) {
				sb.append(String.valueOf(currentSize));
				sb.append(".");
			}
			remainderSize = (int) (size % GB);
			currentSize = (int) (remainderSize / MB);
			if (currentSize > 0) {
				sb.append(String.valueOf(currentSize));
			}
			remainderSize = (int) (size % MB);
			currentSize = (int) (remainderSize / KB);
			if (currentSize > 0) {
				sb.append(String.valueOf(currentSize));
			}
			sb.append("GB");
		} else if (size >= MB) {
			currentSize = (int) (size / MB);
			if (currentSize > 0) {
				sb.append(String.valueOf(currentSize));
				sb.append(".");
			}
			remainderSize = (int) (size % MB);
			currentSize = (int) (remainderSize / KB);
			if (currentSize > 0) {
				sb.append(String.valueOf(currentSize));
			}
			sb.append("MB");

		} else if (size >= KB) {
			currentSize = (int) (size / KB);
			if (currentSize > 0) {
				sb.append(String.valueOf(currentSize));
			}
			sb.append("KB");
		} else {
			sb.append(size+"Byte");
		}
		return sb.toString();
	}
}
