package com.duowan.asynmultipledownload.ui;

import android.os.Message;

import com.duowan.asynmultipledownload.ui.DownLoadBaseAdapter.ProgressHandle;
import com.duowan.download.DefaultProgressListener;
import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;

public class AppDownloadProListener extends DefaultProgressListener {

	private long haveRead = 0;

	private long fileSize = 1;

	private final int UN_CHECKED = 1;

	private final int DOWNLOADING = 4;

	private final int CANCEL_DOWNLOAD = 5;

	private final int ERROR = 6;

	private int mState = UN_CHECKED;

	private final int COMPLETE_DOWNLOAD = 0x10001;

	private final int ERROR_DOWNLOAD = 0x10002;

	private final int INTERUPT_DOWNLOAD = 0x10003;
	
	private final int UPDATE_DOWNLOAD_PROGRESS = 0x10004;

	private ProgressHandle mHandle = null;

	public AppDownloadProListener(ProgressHandle handle) {
		this.mHandle = handle;
	}

	@Override
	public void onProgressChanged(DownloadFile file, int state) {
		super.onProgressChanged(file, state);

		if (state == FileDownloader.FINISH) {
			// œ¬‘ÿÕÍ≥…
			mState = CANCEL_DOWNLOAD;
			Message msg = new Message();
			msg.arg1 = 100;
			msg.obj = file.getResUrl();
			msg.what = COMPLETE_DOWNLOAD;
			sendMessage(msg);
		} else if (state == FileDownloader.INTERUPT) {
			
			mState = CANCEL_DOWNLOAD;
			haveRead = file.getHaveRead();
			fileSize = file.getFileSize();
			int downloadPer = (int) ((haveRead * 100) / fileSize);
			Message msg = new Message();
			msg.arg1 = downloadPer;
			msg.obj = file.getResUrl();
			msg.what = INTERUPT_DOWNLOAD;
			sendMessage(msg);
		}else if (state == FileDownloader.DOWNLOADING) {
			mState = DOWNLOADING;
			haveRead = file.getHaveRead();
			fileSize = file.getFileSize();
			int downloadPer = (int) ((haveRead * 100) / fileSize);
			Message msg = new Message();
			msg.arg1 = downloadPer;
			msg.obj = file.getResUrl();
			msg.what = UPDATE_DOWNLOAD_PROGRESS;
			sendMessage(msg);
		}
		
	}

	@Override
	public void onError(DownloadFile file, int errorType) {
		super.onError(file, errorType);
		mState = ERROR;
		haveRead = file.getHaveRead();
		fileSize = file.getFileSize();
		int downloadPer = (int) ((haveRead * 100) / fileSize);
		Message msg = new Message();
		msg.arg1 = downloadPer;
		msg.obj = file.getResUrl();
		msg.what = ERROR_DOWNLOAD;
		sendMessage(msg);
	}

	private void sendMessage(Message msg) {
		mHandle.sendMessage(msg);
	}
}
