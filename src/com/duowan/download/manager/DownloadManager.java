package com.duowan.download.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.download.IConfig;
import com.duowan.download.IOperator;
import com.duowan.download.IProgressListener;
import com.duowan.download.Logger;

public class DownloadManager {
	public static final int NOTIFY_PROGRESS = 1;
	public static final int NOTIFY_ERROR = 2;
	private ExecutorService mExecutorDownService;
	private ExecutorService mExecutorStopService;
	private HashMap<String, FileDownloader> mDownloadingSet;
	private Queue<ParamsWrapper> mWaittingQueue;
	private IOperator mOperator;
	private IConfig mConfig;

	public DownloadManager(IOperator operator, boolean isDebug) {
		this.mExecutorDownService = Executors.newCachedThreadPool();
		this.mExecutorStopService = Executors.newCachedThreadPool();
		this.mDownloadingSet = new HashMap<String, FileDownloader>();
		this.mWaittingQueue = new LinkedList<ParamsWrapper>();
		this.mOperator = operator;
		if (this.mOperator == null) {
			throw new IllegalArgumentException("operator can not be null.");
		}
		Logger.setDebug(isDebug);
	}

	public void setConfig(IConfig config) {
		this.mConfig = config;
	}

	public boolean download(String resUrl, String filePath,
			IProgressListener listener) {
		ParamsWrapper paramsWrapper = new ParamsWrapper();
		paramsWrapper.setKey(resUrl);
		paramsWrapper.setClassId(0);
		paramsWrapper.setResUrl(resUrl);
		paramsWrapper.setFilePath(filePath);
		paramsWrapper
				.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
		return download(paramsWrapper, listener);
	}

	public boolean download(String resUrl, String filePath, int classId,
			IProgressListener listener) {
		ParamsWrapper paramsWrapper = new ParamsWrapper();  
		paramsWrapper.setKey(resUrl);
		paramsWrapper.setClassId(classId);
		paramsWrapper.setResUrl(resUrl);
		paramsWrapper.setFilePath(filePath);
		paramsWrapper
				.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
		return download(paramsWrapper, listener);
	}

	public boolean download(ParamsWrapper paramsWrapper,
			IProgressListener callback) {
		FileDownloader downloader = createTask(paramsWrapper);
		String key = paramsWrapper.getKey();
		synchronized (this.mDownloadingSet) {
			if (!(this.mDownloadingSet.containsKey(key))) {
				this.mDownloadingSet.put(key, downloader);
			} else {
				// 如果在下载中，就不再下载
				callback.onProgressChanged(downloader.getDownloadFile(), 1);
				return false;
			}
		}
		DownloadProgressListener listener = new DownloadProgressListener(this);
		if (callback != null) {
			listener.setCallback(callback);
		}
		downloader.setProgressListener(listener);

		this.mExecutorDownService.execute(new DownloadThread(downloader));
		return true;
	}

	public boolean addToWaittingQueue(ParamsWrapper paramsWrapper) {
		synchronized (this.mWaittingQueue) {
			if (!(this.mWaittingQueue.contains(paramsWrapper))) {
				createTask(paramsWrapper);
				return this.mWaittingQueue.offer(paramsWrapper);
			}
			return false;
		}
	}

	public void stopDownload(String key) {
		synchronized (this.mDownloadingSet) {
			FileDownloader downloader = (FileDownloader) this.mDownloadingSet
					.remove(key);
			if (downloader != null)
				mExecutorStopService.execute(new StopDownThread(downloader));
		}
	}

	public void removeFromWaittingQueue(String key) {
		synchronized (this.mWaittingQueue) {
			ParamsWrapper paramsWrapper = new ParamsWrapper();
			paramsWrapper.setKey(key);
			this.mWaittingQueue.remove(paramsWrapper);
		}
	}

	public void removeFromDownloadingSet(String key) {
		synchronized (this.mDownloadingSet) {
			this.mDownloadingSet.remove(key);
		}
	}

	public boolean isDownloading(String key) {
		synchronized (this.mDownloadingSet) {
			return this.mDownloadingSet.containsKey(key);
		}
	}

	public boolean isInWaittingQueue(String key) {
		synchronized (this.mWaittingQueue) {
			ParamsWrapper paramsWrapper = new ParamsWrapper();
			paramsWrapper.setKey(key);
			return this.mWaittingQueue.contains(paramsWrapper);
		}
	}

	public DownloadFile getDownloadFile(String key) {
		FileDownloader downloader = null;
		synchronized (this.mDownloadingSet) {
			downloader = (FileDownloader) this.mDownloadingSet.get(key);
		}
		if (downloader != null) {
			return downloader.getDownloadFile();
		}
		return null;
	}

	public int getProgress(String key) {
		synchronized (this.mDownloadingSet) {
			if (this.mDownloadingSet.containsKey(key)) {
				FileDownloader downloader = (FileDownloader) this.mDownloadingSet
						.get(key);
				DownloadFile file = downloader.getDownloadFile();
				long haveRead = file.getHaveRead();
				long fileSize = file.getFileSize();
				if (fileSize > 0L) {
					int progress = (int) ((float) haveRead / (float) fileSize * 100.0F);
					if (progress < 0) {
						progress = 0;
					}
					if (progress > 100) {
						progress = 100;
					}
					return progress;
				}
			}
		}
		return 0;
	}

	public void stopAll() {
		new Thread(new Runnable() {
			public void run() {
				synchronized (DownloadManager.this.mWaittingQueue) {
					DownloadManager.this.mWaittingQueue.clear();
				}

				synchronized (DownloadManager.this.mDownloadingSet) {
					if (DownloadManager.this.mDownloadingSet != null)
						try {
							for (String key : DownloadManager.this.mDownloadingSet
									.keySet()) {
								((FileDownloader) DownloadManager.this.mDownloadingSet
										.get(key)).stop();
							}

							DownloadManager.this.mDownloadingSet.clear();
						} catch (Exception localException) {
						}
				}
			}
		}).start();
	}

	private FileDownloader createTask(ParamsWrapper paramsWrapper) {
		int classId = paramsWrapper.getClassId();
		String key = paramsWrapper.getKey();
		String resUrl = paramsWrapper.getResUrl();
		String filePath = paramsWrapper.getFilePath();
		String fileName = paramsWrapper.getFileName();
		long fileSize = paramsWrapper.getFileSize();

		FileDownloader downloader = new FileDownloader(resUrl, filePath,
				fileName, fileSize, key);
		downloader.setOperator(this.mOperator);
		if (this.mConfig != null) {
			downloader.setConfig(this.mConfig);
		}

		downloader.prepare();

		DownloadFile file = downloader.getDownloadFile();

		file.setClassId(classId);
		return downloader;
	}

	private class DownloadThread implements Runnable {
		private FileDownloader mDownloader;

		DownloadThread(FileDownloader paramFileDownloader) {
			this.mDownloader = paramFileDownloader;
		}

		public void run() {
			this.mDownloader.startTask();
		}
	}

	private class StopDownThread implements Runnable {
		private FileDownloader mStopDown;

		StopDownThread(FileDownloader paramFileDownloader) {
			this.mStopDown = paramFileDownloader;
		}

		public void run() {
			this.mStopDown.stop();
		}
	}
}