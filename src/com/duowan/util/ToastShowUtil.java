package com.duowan.util;

import android.content.Context;
import android.widget.Toast;

public class ToastShowUtil {

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 *            提示信息
	 */
	public static void showMsgShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 *            提示信息
	 */
	public static void showMsgLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
