package com.duowan.asynmultipledownload;

import android.os.Environment;

public class DownLoadUrlUtil {
	
	public static final String BASE_PATH =  	Environment
	.getExternalStorageDirectory().toString();
	
	public static final String URL_ONE= "http://download.yy138.com/download/app/201412/77bb9be0f14be238f94a8699dd091bb9.apk";
	
	public static final String URL_TWO = "http://a.myapp.com/o/down/com.yy.android.gamenews";
	
	public static final String URL_THREE = "http://dl.wandoujia.com/files/third/WanDouJiaSetup_a9.exe";


	public static final String FILE_PATH_ONE = BASE_PATH+"/"
			+ URL_ONE.substring(URL_ONE.lastIndexOf("/") + 1, URL_ONE.length());
	
	
	public static final String FILE_PATH_TWO = BASE_PATH+"/"
			+ URL_TWO.substring(URL_TWO.lastIndexOf("/") + 1, URL_TWO.length());
	
	
	public static final String FILE_PATH_THREE = BASE_PATH+"/"
			+ URL_THREE.substring(URL_THREE.lastIndexOf("/") + 1, URL_THREE.length());
	
}
