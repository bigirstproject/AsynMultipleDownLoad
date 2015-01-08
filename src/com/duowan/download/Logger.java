package com.duowan.download;

public class Logger {
	private static boolean isDebug = true;

	public static void debug(String tag, String msg) {
		if (isDebug)
			System.out.println(tag + " : " + msg);
	}

	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	public static boolean isDebug() {
		return isDebug;
	}
}