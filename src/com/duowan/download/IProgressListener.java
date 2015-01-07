package com.duowan.download;

public abstract interface IProgressListener {
	public abstract void onProgressChanged(DownloadFile paramDownloadFile,
			int paramInt);

	public abstract void onError(DownloadFile paramDownloadFile, int paramInt);
}