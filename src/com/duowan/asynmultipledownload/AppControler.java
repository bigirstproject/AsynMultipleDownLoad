package com.duowan.asynmultipledownload;

import android.content.Intent;

import com.duowan.asynmultipledownload.DownloadServiceUtil.DownloadServiceToken;
import com.duowan.asynmultipledownload.ui.AsynMultipleApplication;

/**
 * ����:Ӧ������/�˳��߼�������
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
	 * �������ʱ����ʼ������
	 */
	public void init() {
		mDownloadServiceToken = DownloadServiceUtil
				.bindToService(AsynMultipleApplication.getInstance());
	}

	/**
	 * �˳�����
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
