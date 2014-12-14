package com.duowan.asynmultipledownload;

import android.content.Intent;

import com.duowan.asynmultipledownload.DownloadServiceUtil.DownloadServiceToken;

/**
 * ����:Ӧ������/�˳��߼�������
 * 
 */
public final class AppControler {

	private static BaseApplication mApplication;

	private static AppControler mAppControler;

	private DownloadServiceToken mDownloadServiceToken;

	private AppControler(BaseApplication application) {
		mApplication = application;
	}

	public static synchronized AppControler getInstance() {
		if (mAppControler == null) {
			synchronized (AppControler.class) {
				if (mAppControler == null) {
					BaseApplication baseApp = (BaseApplication) BaseApplication
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
				.bindToService(BaseApplication.getInstance());
	}

	/**
	 * �˳�����
	 */
	public void exit() {
		DownloadServiceUtil.unbindFromService(mDownloadServiceToken);
		mApplication
				.stopService(new Intent(mApplication, DownloadService.class));
	}

}
