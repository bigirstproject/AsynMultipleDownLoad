/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.duowan.download;

public abstract interface IProgressListener {
	public abstract void onProgressChanged(DownloadFile paramDownloadFile,
			int paramInt);

	public abstract void onError(DownloadFile paramDownloadFile, int paramInt);
}