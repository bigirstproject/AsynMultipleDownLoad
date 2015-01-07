package com.duowan.download.manager;

import android.os.Message;

import com.duowan.asynmultipledownload.service.BaseWorkerService.BackgroundHandler;
import com.duowan.download.DefaultProgressListener;
import com.duowan.download.DownloadFile;
import com.duowan.util.LogCat;

public class DownloadProListener extends DefaultProgressListener {

	private final int DOWNLOADING_PROGRESS_STATE = 0x10001;
	private final int DOWNLOADING_ERROR_STATE = 0x10002;
	private BackgroundHandler mBackgroundHandler;

	public DownloadProListener(BackgroundHandler backgroundHandler) {
		this.mBackgroundHandler = backgroundHandler;
	}

	@Override
	public void onProgressChanged(DownloadFile file, int state) {
		super.onProgressChanged(file, state);
		Message msg = new Message();
		msg.arg1 = state;
		msg.obj = file;
		msg.what = DOWNLOADING_PROGRESS_STATE;
		sendMessage(msg);
	}

	@Override
	public void onError(DownloadFile file, int errorType) {
		super.onError(file, errorType);
		Message msg = new Message();
		msg.arg1 = errorType;
		msg.obj = file;
		msg.what = DOWNLOADING_ERROR_STATE;
		sendMessage(msg);
	}

	private void sendMessage(Message msg) {
		LogCat.d("mBackgroundHandler =  " +( mBackgroundHandler == null) + "    time  is " + System.currentTimeMillis());
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendMessage(msg);
		}
	}
}