package com.duowan.asynmultipledownload.ui;

import java.io.File;

import android.os.Environment;

import com.duowan.util.FileUtil;

public class DownLoadUrl {

	public static String[] url = new String[] {
			"http://download.yy138.com/download/app/201412/77bb9be0f14be238f94a8699dd091bb9.apk",
			"http://a.myapp.com/o/down/com.yy.android.gamenews",
			"http://down.360safe.com/360kan/360kan_android.apk",
			"http://dl.wandoujia.com/files/third/WanDouJiaSetup_a9.exe",
			"http://dlsw.baidu.com/sw-search-sp/gaosu/2014_12_02_21/bind1/12350/QQ6.6.13172.0_12350_BDdl.exe",
			"http://b.ttdk.cn/softdown/20130814/SogouMall_2.2-release-0718_pid_130910007_20130718162918.apk",
			"http://yinyueshiting.baidu.com/data2/music/122674129/1226741191418709661128.mp3?xcode=e76abb3b918af01f52e1c71f9bd30c08e70c8ba5d04fc31d",
			"http://yinyueshiting.baidu.com/data2/music/121949522/12152327672000128.mp3?xcode=280c7d9ac6471ab1d5ba323bfb9651841185dbd0cbb5163e",
			"http://yinyueshiting.baidu.com/data2/music/124728882/73007331418673661128.mp3?xcode=8845a19c509a3d2831cfb7a7daff9c96bf95b0c3b098821b",
			"http://yinyueshiting.baidu.com/data2/music/7344559/620023111600128.mp3?xcode=8845a19c509a3d284e270de532017153356a13fa219f2b56",
			"http://yinyueshiting.baidu.com/data2/music/123296359/13132725248400128.mp3?xcode=8845a19c509a3d28d5d906d2816a95ae19c56da7f7cc7a9f",
			"http://yinyueshiting.baidu.com/data2/music/124595469/7277793118800128.mp3?xcode=8845a19c509a3d286e55c681882384152ae82a3537173eca",
			"http://yinyueshiting.baidu.com/data2/music/70437083/3133622486400128.mp3?xcode=8845a19c509a3d28fb022efa59f52f194d22d21e963ef64d",
			"http://yinyueshiting.baidu.com/data2/music/123660213/1236391201418655661128.mp3?xcode=35c01e1c67dd604f7a04b0849fe1eb4355f502b5b554ab37",
			"http://yinyueshiting.baidu.com/data2/music/123659004/14945107241200128.mp3?xcode=35c01e1c67dd604f2c25b5a4fe3de9d29747f83dd337b2e9" };

	public static final String BASE_PATH = Environment
			.getExternalStorageDirectory().toString();

	public static final String ROOT_BASE_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ File.separator
			+ "download";

	public static final void createBaseFilePath(String filePath) {
		FileUtil.createFolder(filePath, FileUtil.MODE_COVER);
	}

	public static final String getFilePath(String url) {
		return ROOT_BASE_PATH + "/"
				+ url.substring(url.lastIndexOf("/") + 1, url.length());
	}

}
