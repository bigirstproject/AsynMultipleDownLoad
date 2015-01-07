package com.duowan.asynmultipledownload.Interface;

import java.util.HashMap;
import java.util.LinkedList;

import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.download.IProgressListener;
import com.duowan.download.manager.ParamsWrapper;

/**
 * 后台服务接口
 * 
 */
public interface IDownloadService {

	boolean download(String resUrl, String filePath, IProgressListener listener);

	boolean download(String resUrl, String filePath, int classId,
			IProgressListener listener);

	boolean download(ParamsWrapper paramsWrapper, IProgressListener callback);

	void stopDownload(String key);

	void stopAllDownload();

	void removeFromDownloadingSet(String key);

	boolean isDownloading(String key);

	DownloadFile getDownloadFile(String key);

	int getProgress(String key);

	void registerCallback(IProgressListener listener);

	void registerCallback(IProgressListener listener,
			IDownloadManagerCallBackListener downloadManager);

	void removeCallback(IProgressListener listener);

	HashMap<String, FileDownloader> getDownloadingSet();

	LinkedList<FileDownloader> getWaittingList();

	void registerDowningCallback(
			IDownloadManagerCallBackListener callBackListener);

}
