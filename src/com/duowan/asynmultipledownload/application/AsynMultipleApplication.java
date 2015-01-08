package com.duowan.asynmultipledownload.application;

import com.duowan.asynmultipledownload.bean.UrlWrapper;
import com.duowan.asynmultipledownload.downTools.AppControler;

public class AsynMultipleApplication extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		UrlWrapper.createBaseFilePath(UrlWrapper.ROOT_BASE_PATH);
		AppControler.getInstance().init();
	}

}
