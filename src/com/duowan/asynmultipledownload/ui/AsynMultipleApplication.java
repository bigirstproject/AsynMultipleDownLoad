package com.duowan.asynmultipledownload.ui;

import com.duowan.asynmultipledownload.AppControler;

public class AsynMultipleApplication extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		DownLoadUrl.createBaseFilePath(DownLoadUrl.ROOT_BASE_PATH);
		 AppControler.getInstance().init();
	}

}
