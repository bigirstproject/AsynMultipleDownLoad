package com.duowan.asynmultipledownload.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

/**
 * 描述:服务基类
 * 
 */
public abstract class BaseService extends Service {

	protected UiHandler mUiHandler;

	public void onCreate() {
		super.onCreate();
		mUiHandler = new UiHandler(getMainLooper());
	};

	/**
	 * 处理更新UI任务
	 * 
	 * @param msg
	 */
	protected void handleUiMessage(Message msg) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	/**
	 * 发送UI更新操作
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
