package com.duowan.asynmultipledownload.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

/**
 * ����:�������
 * 
 */
public abstract class BaseService extends Service {

	protected UiHandler mUiHandler;

	public void onCreate() {
		super.onCreate();
		mUiHandler = new UiHandler(getMainLooper());
	};

	/**
	 * �������UI����
	 * 
	 * @param msg
	 */
	protected void handleUiMessage(Message msg) {
	}

	/**
	 * ����UI���²���
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	/**
	 * ����UI���²���
	 * 
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	class UiHandler extends Handler {
		public UiHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handleUiMessage(msg);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
