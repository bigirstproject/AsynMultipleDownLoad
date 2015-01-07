package com.duowan.asynmultipledownload.service;

import com.duowan.asynmultipledownload.contentProvider.DownloadInfoSaver;
import com.duowan.asynmultipledownload.downTools.DownloadConfig;
import com.duowan.download.DownloadFile;
import com.duowan.download.IConfig;
import com.duowan.download.IOperator;
import com.duowan.download.IProgressListener;

public class DownloadService extends BaseDownloadService {
	
	public void onCreate() {
		super.onCreate();
	}

	@Override
	protected IOperator createOperator() {
		return new DownloadInfoSaver();
	}

	@Override
	protected IConfig createConfig() {
		return new DownloadConfig();
	}

	@Override
	protected void invokeCallback(DownloadFile file, int state, int type) {
		synchronized (getmCallbacks()) {
			if (getmCallbacks() != null && getmCallbacks().size() > 0) {
				for (int i = 0; i < getmCallbacks().size(); i++) {
					IProgressListener listener = (IProgressListener) getmCallbacks()
							.get(i);
					if (type == PRORESS_CODE) {
						listener.onProgressChanged(file, state);
					} else if (type == ERROR_CODE) {
						listener.onError(file, state);
					}
				}
			}
		}
	}
}
