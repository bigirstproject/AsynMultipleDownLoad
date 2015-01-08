package com.duowan.asynmultipledownload.ui;

import android.os.Handler;
import android.os.Message;

import com.duowan.download.DefaultProgressListener;
import com.duowan.download.DownloadFile;

public class FileDownloadProCallBackListener extends DefaultProgressListener {

	private Handler mProbarHandle;
	private final int UPDATE_DOWNLOAD_PROGRESS = 0x10001;
	private final int ERROR_DOWNLOAD = 0x10002;

	public FileDownloadProCallBackListener(Handler handle) {
		this.mProbarHandle = handle;
	}

	@Override
	public void onProgressChanged(DownloadFile file, int state) {
		super.onProgressChanged(file, state);
		Message msg = new Message();
		msg.arg1 = state;
		msg.obj = file;
		msg.what = UPDATE_DOWNLOAD_PROGRESS;
		sendMessage(msg);
	}

	@Override
	public void onError(DownloadFile file, int errorType) {
		Message msg = new Message();
		msg.arg1 = errorType;
		msg.obj = file;
		msg.what = ERROR_DOWNLOAD;
		sendMessage(msg);
	}

	private void sendMessage(Message msg) {
		if (mProbarHandle != null) {
			mProbarHandle.sendMessage(msg);
		}
	}
}
