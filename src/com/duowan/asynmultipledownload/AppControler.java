package com.duowan.asynmultipledownload;

import android.content.Intent;

import com.duowan.asynmultipledownload.DownloadServiceUtil.DownloadServiceToken;
import com.duowan.asynmultipledownload.ui.AsynMultipleApplication;

/**
 * 描述:应用启动/退出逻辑处理类
 * 
 */
public final class AppControler {

	private static AsynMultipleApplication mApplication;

	private static AppControler mAppControler;

	private DownloadServiceToken mDownloadServiceToken;

	private AppControler(AsynMultipleApplication application) {
		mApplication = application;
	}

	public static synchronized AppControler getInstance() {
		if (mAppControler == null) {
			synchronized (AppControler.class) {
				if (mAppControler == null) {
					AsynMultipleApplication baseApp = (AsynMultipleApplication) AsynMultipleApplication
							.getInstance();
					mAppControler = new AppControler(baseApp);
				}
			}
		}
		return mAppControler;
	}

	/**
	 * 启动软件时做初始化工作
	 */
	public void init() {
		mDownloadServiceToken = DownloadServiceUtil
				.bindToService(AsynMultipleApplication.getInstance());
	}

	/**
	 * 退出服务
	 */
	public void exit() {
		DownloadServiceUtil.unbindFromService(mDownloadServiceToken);
		mApplication
				.stopService(new Intent(mApplication, DownloadService.class));
		mAppControler = null;
		mApplication =null;
		mDownloadServiceToken =null;
	}

}
