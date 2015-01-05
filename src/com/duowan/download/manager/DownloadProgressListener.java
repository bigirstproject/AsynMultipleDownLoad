package com.duowan.download.manager;

import com.duowan.download.DefaultProgressListener;
import com.duowan.download.DownloadFile;
import com.duowan.download.IProgressListener;

public class DownloadProgressListener extends DefaultProgressListener {
	private DownloadManager mDownloadManager;
	private IProgressListener mCallback;

	public DownloadProgressListener(DownloadManager manager) {
		this.mDownloadManager = manager;
	}

	public void setCallback(IProgressListener callback) {
		this.mCallback = callback;
	}

	public void onProgressChanged(DownloadFile file, int state) {
		if ((state == 5) || (state == 4)) {
			removeFromDownloadingSet(file);
		}
		if (this.mCallback != null)
			this.mCallback.onProgressChanged(file, state);
	}

	public void onError(DownloadFile file, int errorType) {
		removeFromDownloadingSet(file);
		if (this.mCallback != null)
			this.mCallback.onError(file, errorType);
	}

	private void removeFromDownloadingSet(DownloadFile file) {
		String key = file.getKey();
		this.mDownloadManager.removeFromDownloadingSet(key);
	}
}