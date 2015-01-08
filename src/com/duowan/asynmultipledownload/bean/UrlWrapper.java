package com.duowan.asynmultipledownload.bean;

import java.io.File;

import android.os.Environment;

import com.duowan.util.FileUtil;

public class UrlWrapper {

	public static String[] url = new String[] {
			"http://download.yy138.com/download/app/201412/77bb9be0f14be238f94a8699dd091bb9.apk",
			"http://d1.apk8.com:8020/wangyou_m/diyidixue.apk",
			"http://down.360safe.com/360kan/360kan_android.apk",
			"http://d1.apk8.com:8020/youxi_m/shenmiaotaowang2.apk",
			"http://d1.apk8.com:8020/game_m/baolijuedoushi.apk",
			"http://b.ttdk.cn/softdown/20130814/SogouMall_2.2-release-0718_pid_130910007_20130718162918.apk",
			"http://d1.apk8.com:8020/wangyou_m/shikonglieren.apk",
			"http://d1.apk8.com:8020/wangyou_m/yingzhiren.apk",
			"http://d.apk8.com:8020/CPA/%E8%81%8A%E6%96%8B3.0.0-a015tt.apk",
			"http://d1.apk8.com:8020/wangyou_m/dixiachengchuanshuo.apk",
			"http://d1.apk8.com:8020/youxi_m/xiongchumo.apk",
			"http://d1.apk8.com:8020/game_m/tiantiankupao.apk",
			"http://d1.apk8.com:8020/game_m/tiaoyuedexiaowugui.apk",
			"http://d2.apk8.com:8020/soft/jingdongshangchengguanwangban.apk",
			"http://d2.apk8.com:8020/soft/gaodeditu.apk",
			"http://d2.apk8.com:8020/soft/aicheditu.apk",
			"http://d2.apk8.com:8020/soft/momo.apk" };

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
