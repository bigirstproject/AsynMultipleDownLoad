package com.duowan.util;

import android.util.Log;

public class LogCat {

	private static final String TAG = "KGLog";

	private static boolean isDebug = true;

	/**
	 * �Ƿ��ڵ���ģʽ
	 * 
	 * @param debug
	 */
	public static boolean isDebug() {
		return isDebug;
	}

	/**
	 * Ĭ��TAG=kugou
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
	 * �Զ���TAG
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
	 * TAG=��ǰ�������
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