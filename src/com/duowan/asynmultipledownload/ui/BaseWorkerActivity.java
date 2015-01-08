
package com.duowan.asynmultipledownload.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * ���� �߱���̨�̺߳�UI�̸߳���
 * 
 */
public abstract class BaseWorkerActivity extends BaseActivity {

    protected HandlerThread mHandlerThread;

    protected BackgroundHandler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
    }

    @Override
	protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit();
        }
    }

    /**
     * �����̨����
     */
    protected abstract void handleBackgroundMessage(Message msg);

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
