package com.duowan.util;

import android.util.Log;

public class LogCat {

	private static final String TAG = "KGLog";

	private static boolean isDebug = true;

	/**
	 * 是否处于调试模式
	 * 
	 * @param debug
	 */
	public static boolean isDebug() {
		return isDebug;
	}

	/**
	 * 默认TAG=kugou
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}

	public static void e(String msg) {
		Log.e(TAG, msg);
	}

	/**
	 * 自定义TAG
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	/**
	 * TAG=当前类的名称
	 * 
	 * @param currentClass
	 * @param msg
	 */
	@SuppressWarnings("rawtypes")
	public static void d(Class currentClass, String msg) {
		if (isDebug) {
			Log.d(currentClass.getSimpleName(), msg);
		}
	}

}
