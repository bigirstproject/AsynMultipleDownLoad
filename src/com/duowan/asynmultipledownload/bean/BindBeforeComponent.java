package com.duowan.asynmultipledownload.bean;

import com.duowan.asynmultipledownload.Interface.IDownloadManagerCallBackListener;
import com.duowan.download.IProgressListener;

public class BindBeforeComponent {
	private IProgressListener listener;

	private IDownloadManagerCallBackListener downloadManager;

	public IProgressListener getListener() {
		return listener;
	}

	public void setListener(IProgressListener listener) {
		this.listener = listener;
	}

	public IDownloadManagerCallBackListener getDownloadManager() {
		return downloadManager;
	}

	public void setDownloadManager(IDownloadManagerCallBackListener downloadManager) {
		this.downloadManager = downloadManager;
	}

}
