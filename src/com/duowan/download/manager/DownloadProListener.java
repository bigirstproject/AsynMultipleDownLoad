package com.duowan.download.manager;

import android.os.Message;

import com.duowan.asynmultipledownload.BaseDownloadService.MyHandle;
import com.duowan.download.DefaultProgressListener;
import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.util.LogCat;

public class DownloadProListener extends DefaultProgressListener {

	private final int DOWNLOADING_STATE = 0x10001;
	protected MyHandle mHandle;

	public DownloadProListener(MyHandle handle) {
		this.mHandle = handle;
	}

	@Override
	public void onProgressChanged(DownloadFile file, int state) {
		super.onProgressChanged(file, state);
		Message msg = new Message();
		msg.arg1 = state;
		msg.obj = file;
		msg.what = DOWNLOADING_STATE;
		if(state == FileDownloader.INTERUPT){
			LogCat.d("onProgressChanged file state is "+System.currentTimeMillis());
		}
		sendMessage(msg);
	}

	@Override
	public void onError(DownloadFile file, int errorType) {
		super.onError(file, errorType);
		Message msg = new Message();
		msg.arg1 = errorType;
		msg.obj = file;
		msg.what = DOWNLOADING_STATE;
		sendMessage(msg);
	}

	protected void sendMessage(Message msg) {
		mHandle.sendMessage(msg);
	}
}