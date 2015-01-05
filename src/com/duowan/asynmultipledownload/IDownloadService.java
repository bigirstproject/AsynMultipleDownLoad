package com.duowan.asynmultipledownload;

import com.duowan.download.DownloadFile;
import com.duowan.download.IProgressListener;
import com.duowan.download.manager.ParamsWrapper;

/**
 * ��̨����ӿ�
 * 
 */
public interface IDownloadService {

	boolean download(String resUrl, String filePath, IProgressListener listener);

	boolean download(String resUrl, String filePath, int classId,
			IProgressListener listener);

	boolean download(ParamsWrapper paramsWrapper, IProgressListener callback);

	boolean addToWaittingQueue(ParamsWrapper paramsWrapper);

	void stopDownload(String key);

	void stopAllDownload();

	void removeFromWaittingQueue(String key);

	void removeFromDownloadingSet(String key);

	boolean isDownloading(String key);

	boolean isInWaittingQueue(String key);

	DownloadFile getDownloadFile(String key);

	int getProgress(String key);

	void registerCallback(IProgressListener listener);

	void removeCallback(IProgressListener listener);

}
