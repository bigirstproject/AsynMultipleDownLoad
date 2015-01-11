package com.duowan.asynmultipledownload.downTools;

import com.duowan.asynmultipledownload.application.AsynMultipleApplication;
import com.duowan.asynmultipledownload.downTools.DownloadServiceUtil.DownloadServiceToken;

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
	 * ��������ʱ����ʼ������
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
		// mApplication
		// .stopService(new Intent(mApplication, DownloadService.class));
		// mAppControler = null;
		// mApplication =null;
		// mDownloadServiceToken =null;
	}

}