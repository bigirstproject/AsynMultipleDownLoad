package com.duowan.asynmultipledownload.service;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * ���� �߱���̨�̺߳�UI�̸߳���
 * 
 * @author ldxs
 * @date 2015��1��7�� ����10:49:39
 */
public abstract class BaseWorkerService extends BaseService {

	protected HandlerThread mHandlerThread;

	protected BackgroundHandler mBackgroundHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		mHandlerThread = new HandlerThread("BaseWorkerService worker:"
				+ BaseWorkerService.this.getClass().getSimpleName());
		mHandlerThread.start();
		mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBackgroundHandler != null
				&& mBackgroundHandler.getLooper() != null) {
			mBackgroundHandler.getLooper().quit();
		}
	}

	/**
	 * �����̨����
	 */
	protected  void handleBackgroundMessage(Message msg){
		
	};

	/**
	 * ���ͺ�̨����
	 * 
	 * @param msg
	 */
	protected void sendBackgroundMessage(Message msg) {
		if (mBackgroundHandler != null) {

			mBackgroundHandler.sendMessage(msg);
		}
	}

	/**
	 * ���ͺ�̨����
	 * 
	 * @param what
	 */
	protected void sendEmptyBackgroundMessage(int what) {
		if (mBackgroundHandler != null) {

			mBackgroundHandler.sendEmptyMessage(what);
		}
	}

	// ��̨Handler
	@SuppressLint("HandlerLeak")
	public class BackgroundHandler extends Handler {

		BackgroundHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handleBackgroundMessage(msg);
		}
	}

}
