package com.duowan.asynmultipledownload;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.duowan.asynmultipledownload.application.AsynMultipleApplication;
import com.duowan.asynmultipledownload.downTools.DownloadServiceUtil;
import com.duowan.download.DefaultProgressListener;
import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.util.ToastShowUtil;

/**
 * ����: ��������
 * 
 */
public class UpdateApp {

	public static final String url = "http://113.108.88.66/dd.myapp.com/16891/4C54F9B4ED6F0C3F72A4E155A59D5CA3.apk";

	public static final String mFilePath = Environment
			.getExternalStorageDirectory().toString()+"/"
			+ url.substring(url.lastIndexOf("/") + 1, url.length());

	private Context mContext = null;

	private final int UN_CHECKED = 1;

	private final int DOWNLOADING = 4;

	private final int CANCEL_DOWNLOAD = 5;

	private final int ERROR = 6;

	private int mState = UN_CHECKED;

	private final int COMPLETE_DOWNLOAD = 0x10001;

	private final int ERROR_DOWNLOAD = 0x10002;

	private final int INTERUPT_DOWNLOAD = 0x10003;

	private final int UPDATE_DOWNLOAD_PROGRESS = 0x10004;

	private MyHandle mHandle = null;

	private UpdateApp(Context context) {
		this.mContext = context;
		if (mHandle == null) {
			mHandle = new MyHandle();
		}
	}

	private static volatile UpdateApp mInstance = null;

	public static UpdateApp getInstance() {
		if (mInstance == null) {
			synchronized (UpdateApp.class) {
				if (mInstance == null) {
					Context context = AsynMultipleApplication.getInstance()
							.getApplicationContext();
					mInstance = new UpdateApp(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * ��ʼ����
	 */
	public void startDownload() {
		mState = DOWNLOADING;
		DownloadServiceUtil.download(url, mFilePath,
				new AppDownloadProListener());
	}

	/**
	 * ȡ������
	 */
	public void cancelDownload() {
		mState = CANCEL_DOWNLOAD;
		DownloadServiceUtil.stopDownload(url);

	}

	/***************** ������ *****************/

	private class AppDownloadProListener extends DefaultProgressListener {

		private long haveRead = 0;

		private long fileSize = 1;

		@Override
		public void onProgressChanged(DownloadFile file, int state) {
			super.onProgressChanged(file, state);

			if (state == FileDownloader.FINISH) {
				// �������
				mState = CANCEL_DOWNLOAD;
				sendEmptyHandle(COMPLETE_DOWNLOAD);
				return;
			} else if (state == FileDownloader.INTERUPT) {
				mState = CANCEL_DOWNLOAD;
				sendEmptyHandle(INTERUPT_DOWNLOAD);
				return;
			}

			haveRead = file.getHaveRead();
			fileSize = file.getFileSize();
			int downloadPer = (int) ((haveRead * 100) / fileSize);
			Message msg = new Message();
			msg.arg1 = downloadPer;
			msg.what = UPDATE_DOWNLOAD_PROGRESS;
			sendMessage(msg);
		}

		@Override
		public void onError(DownloadFile file, int errorType) {
			super.onError(file, errorType);
			mState = ERROR;
			sendEmptyHandle(ERROR_DOWNLOAD);

		}
	}

	/********************** Handle ���� ********************/
	@SuppressLint("HandlerLeak")
	private class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COMPLETE_DOWNLOAD:
				// �������
				Uri uri = Uri.fromFile(new File(mFilePath));
				ToastShowUtil.showMsgShort(mContext, "COMPLETE_DOWNLOAD");
				break;
			case ERROR_DOWNLOAD:
				// ����ʧ��
				ToastShowUtil.showMsgShort(mContext, "ERROR_DOWNLOAD");
				break;
			case INTERUPT_DOWNLOAD:
				// �ж�����
				ToastShowUtil.showMsgShort(mContext, "INTERUPT_DOWNLOAD");
				break;
			case UPDATE_DOWNLOAD_PROGRESS:
				// ���½���
				if (msg != null && msg.arg1 >= 0) {
					ToastShowUtil.showMsgShort(mContext,
							"UPDATE_DOWNLOAD_PROGRESS = " + msg.arg1);
				}
				break;
			default:
				break;
			}
		}
	}

	private void sendEmptyHandle(int what) {
		mHandle.sendEmptyMessage(what);
	}

	private void sendMessage(Message msg) {
		mHandle.sendMessage(msg);
	}

}
