package com.duowan.asynmultipledownload;

public class AsynMultipleApplication extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		 AppControler.getInstance().init();
	}

}
