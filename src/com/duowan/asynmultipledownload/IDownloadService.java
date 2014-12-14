package com.duowan.asynmultipledownload;

import com.kugou.download.DownloadFile;
import com.kugou.download.IProgressListener;
import com.kugou.download.manager.ParamsWrapper;

/**
 * 后台服务接口
 * 
 */
public interface IDownloadService {

	boolean download(String resUrl, String filePath, IProgressListener listener);

	boolean download(String resUrl, String filePath, int classId,
			IProgressListener listener);

	boolean download(ParamsWrapper paramsWrapper, IProgressListener callback);

	boolean addToWaittingQueue(ParamsWrapper paramsWrapper);

	void stopDownload(String key);

	void removeFromWaittingQueue(String key);

	void removeFromDownloadingSet(String key);

	boolean isDownloading(String key);

	boolean isInWaittingQueue(String key);

	DownloadFile getDownloadFile(String key);

	int getProgress(String key);

}
