/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.duowan.download;

import java.io.PrintStream;

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