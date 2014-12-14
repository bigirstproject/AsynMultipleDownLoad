package com.duowan.asynmultipledownload;

import android.util.Log;

/**
 * ��־����
 * 
 */
public class KGLog {

	private static final String TAG = "voice";

	private static boolean isDebug = true;

	/**
	 * �Ƿ��ڵ���ģʽ
	 * 
	 * @param debug
	 */
	public static boolean isDebug() {
		return isDebug;
	}

	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void e(String msg) {
		if (isDebug) {
			Log.e(TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

}