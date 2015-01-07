package com.duowan.download;

import com.duowan.util.LogCat;

public class Logger {
	private static boolean isDebug = true;

	public static void debug(String tag, String msg) {
		if (isDebug)
			LogCat.d("test", tag + " : " + msg);
			System.out.println(tag + " : " + msg);
	}

	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	public static boolean isDebug() {
		return isDebug;
	}
}