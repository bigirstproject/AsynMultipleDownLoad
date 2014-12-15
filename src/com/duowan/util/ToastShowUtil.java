package com.duowan.util;

import android.content.Context;
import android.widget.Toast;

public class ToastShowUtil {

	/**
	 * ��ʾ��ʾ��Ϣ
	 * 
	 * @param msg
	 *            ��ʾ��Ϣ
	 */
	public static void showMsgShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��ʾ��ʾ��Ϣ
	 * 
	 * @param msg
	 *            ��ʾ��Ϣ
	 */
	public static void showMsgLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
