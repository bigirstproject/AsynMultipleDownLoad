package com.duowan.util;

import android.util.Log;

public class RunTimeMaxMemory {

	public static int appRunTimeMaxMemory() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		Log.d("TAG", "Max memory is " + maxMemory + "KB");
		return maxMemory;
	}

}
