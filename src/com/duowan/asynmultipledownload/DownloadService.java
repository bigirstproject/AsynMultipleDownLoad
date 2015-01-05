package com.duowan.asynmultipledownload;

import com.duowan.download.DownloadFile;
import com.duowan.download.IConfig;
import com.duowan.download.IOperator;
import com.duowan.download.IProgressListener;
import com.duowan.util.LogCat;

public class DownloadService extends BaseDownloadService {

	@Override
	protected IOperator createOperator() {
		return new DownloadInfoSaver();
	}

	@Override
	protected IConfig createConfig() {
		return new DownloadConfig();
	}

	@Override
	protected void invokeCallback(DownloadFile file, int state) {
		synchronized (getmCallbacks()) {
			LogCat.d("getmCallbacks().size() = "+getmCallbacks().size());
			if (getmCallbacks() != null && getmCallbacks().size() > 0) {
				for (int i = 0; i < getmCallbacks().size(); i++) {
					IProgressListener listener = (IProgressListener) getmCallbacks()
							.get(i);
					listener.onProgressChanged(file, state);
				}
			}
		}
	}
}
