package com.duowan.download.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.download.IConfig;
import com.duowan.download.IOperator;
import com.duowan.download.IProgressListener;
import com.duowan.download.Logger;
import com.duowan.util.LogCat;

public class DownloadManager {
	public static final int NOTIFY_PROGRESS = 1;
	public static final int NOTIFY_ERROR = 2;
	private ExecutorService mExecutorDownService;
	private ExecutorService mExecutorStopService;
	private HashMap<String, FileDownloader> mDownloadingSet;
	private LinkedList<FileDownloader> mWaittingList;
	private IOperator mOperator;
	private IConfig mConfig;

	public DownloadManager(IOperator operator, boolean isDebug) {
		this.mExecutorDownService = Executors.newCachedThreadPool();
		this.mExecutorStopService = Executors.newCachedThreadPool();
		this.mDownloadingSet = new HashMap<String, FileDownloader>();
		this.mWaittingList = new LinkedList<FileDownloader>();
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
		DownloadProgressListener listener = new DownloadProgressListener(this);
		if (callback != null) {
			listener.setCallback(callback);
		}
		downloader.setProgressListener(listener);
		addToWaittingQueue(downloader);
		downNextWaittingQueueWrapper();
		return false;
	}

	public boolean downNextWaittingQueueWrapper() {
		LogCat.d("downNextWaittingQueueWrapper  mWaittingList.size() is "
				+ mWaittingList.size());
		synchronized (this.mWaittingList) {
			if (mWaittingList != null && mWaittingList.size() > 0
					&& mConfig != null
					&& mConfig.maxLoadingNums() > mDownloadingSet.size()) {
				FileDownloader downloader = mWaittingList.remove(0);
				String key = downloader.getDownloadFile().getKey();
				synchronized (this.mDownloadingSet) {
					if (!(this.mDownloadingSet.containsKey(key))) {
						this.mDownloadingSet.put(key, downloader);
						this.mExecutorDownService
						.execute(new DownloadThread(downloader));
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean addToWaittingQueue(FileDownloader downloader) {
		synchronized (this.mWaittingList) {
			if (!(this.mWaittingList.contains(downloader))) {
				return this.mWaittingList.add(downloader);
			}
			return false;
		}
	}

	public boolean isInWaittingQueue(String key) {
		synchronized (this.mWaittingList) {
			ParamsWrapper paramsWrapper = new ParamsWrapper();
			paramsWrapper.setKey(key);
			FileDownloader downloader = createTask(paramsWrapper);
			return this.mWaittingList.contains(downloader);
		}
	}

	// public void removeFromWaittingList(String key) {
	// synchronized (this.mWaittingList) {
	// ParamsWrapper paramsWrapper = new ParamsWrapper();
	// paramsWrapper.setKey(key);
	// FileDownloader downloader = createTask(paramsWrapper);
	// this.mWaittingList.remove(downloader);
	// }
	// }

	public void removeFromWaittingListAndMessage(String key) {
		for (int i = 0; i < mWaittingList.size(); i++) {
			if (mWaittingList.get(i).getDownloadFile().getKey().equals(key)) {
				synchronized (this.mWaittingList) {
					IProgressListener listener = mWaittingList.get(i)
							.getProgressListener();
					mWaittingList.get(i).getDownloadFile()
							.setState(FileDownloader.INTERUPT);
					listener.onProgressChanged(mWaittingList.get(i)
							.getDownloadFile(), FileDownloader.INTERUPT);
					mWaittingList.remove(i);
					break;
				}
			}
		}
	}
	
	public void sendWaittingListAndMessage(String key) {
		for (int i = 0; i < mWaittingList.size(); i++) {
			if (mWaittingList.get(i).getDownloadFile().getKey().equals(key)) {
				synchronized (this.mWaittingList) {
					IProgressListener listener = mWaittingList.get(i)
							.getProgressListener();
					mWaittingList.get(i).getDownloadFile()
							.setState(FileDownloader.INTERUPT);
					listener.onProgressChanged(mWaittingList.get(i)
							.getDownloadFile(), FileDownloader.INTERUPT);
					break;
				}
			}
		}
	}

	public void stopDownload(String key) {
		if (isInWaittingQueue(key)) {
			removeFromWaittingListAndMessage(key);
		} else {
			FileDownloader downloader = (FileDownloader) this.mDownloadingSet
					.remove(key);
			if (downloader != null) {
				mExecutorStopService.execute(new StopDownThread(downloader));
			}
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
				synchronized (DownloadManager.this.mWaittingList) {
					DownloadManager.this.mWaittingList.clear();
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