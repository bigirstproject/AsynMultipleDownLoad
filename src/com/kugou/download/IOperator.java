package com.kugou.download;

import java.util.List;

public abstract interface IOperator {
	public abstract long insertFile(DownloadFile paramDownloadFile);

	public abstract boolean updateFile(DownloadFile paramDownloadFile);

	public abstract boolean deleteFile(long paramLong);

	public abstract DownloadFile queryFile(long paramLong);

	public abstract List<DownloadFile> queryFile(String paramString,
			String[] paramArrayOfString);

	public abstract List<DownloadFile> queryFile(String paramString1,
			String[] paramArrayOfString, String paramString2);

	public abstract int getCount(String paramString, String[] paramArrayOfString);
}