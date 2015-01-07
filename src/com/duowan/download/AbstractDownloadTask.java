package com.duowan.download;

import com.duowan.util.LogCat;

abstract class AbstractDownloadTask extends Thread {
	protected IHttpConnector httpConnector;
	protected DownloadFile downloadFile;
	protected FileAccess fileAccess;
	protected IProgressListener progressListener;
	protected FileDownloader downloader;
	protected IOperator operator;
	protected boolean stop;
	protected String tag;
	protected FileAccess.FileSaveProgressListener mListener = new FileAccess.FileSaveProgressListener() {
		public void onProgressChanged(long savedLength) {
			Statistics statis = AbstractDownloadTask.this.downloadFile
					.getStatis();
			statis.addReceivedLen(savedLength);

			if (AbstractDownloadTask.this.downloadFile instanceof BlockedDownloadFile) {
				BlockedDownloadFile blockedDownloadFile = (BlockedDownloadFile) AbstractDownloadTask.this.downloadFile;
				int index = blockedDownloadFile.getBufferedIndex() + 1;
				long bufferedRead = index * blockedDownloadFile.getBlockSize();
				if (bufferedRead >= blockedDownloadFile.getFileSize()) {
					bufferedRead = blockedDownloadFile.getFileSize();
				}
				blockedDownloadFile.setHaveRead(bufferedRead);
			} else {
				AbstractDownloadTask.this.downloadFile.addHaveRead(savedLength);
			}
			if (statis.canNotify())
				AbstractDownloadTask.this.notifyProgress(
						AbstractDownloadTask.this.downloadFile, 3);
		}
	};

	public AbstractDownloadTask(FileAccess fileAccess,
			IProgressListener progressListener, FileDownloader downloader,
			IOperator operator, DownloadFile downloadFile) {
		this.fileAccess = fileAccess;
		this.progressListener = progressListener;
		this.downloader = downloader;
		this.operator = operator;
		this.downloadFile = downloadFile;
		this.httpConnector = createHttpConnector(false);
		this.tag = downloadFile.getKey();
	}

	public void stopDownload() {
		this.stop = true;
		try {
			LogCat.d("stopDownload  enter is " + System.currentTimeMillis());
			this.httpConnector.close();
			LogCat.d("stopDownload  out  is " + System.currentTimeMillis());
		} catch (Exception localException) {
		}
	}

	protected void clearFaileCounter() {
		 this.downloader.resetTryNum();
	}

	protected void addFaileCounter() {
		if (Logger.isDebug()) {
			Logger.debug(getName(), "------жиЪд--------");
		}
		 this.downloader.addTryNum();
	}

	protected void stopByNetError() {
		if (Logger.isDebug()) {
			Logger.debug(getName(), "------stopByNetError--------");
		}
		this.downloader.stopByNetError();
	}

	protected void stopByFileNotFound() {
		if (Logger.isDebug()) {
			Logger.debug(getName(), "------stopByFileNotFound--------");
		}
		this.downloader.stopByFileNotFound();
	}

	protected void notifyProgress(DownloadFile file, int state) {
		synchronized (this.progressListener) {
			if ((!(this.stop)) && (this.progressListener != null)) {
				this.progressListener.onProgressChanged(file, state);
			}
		}
	}

	protected IHttpConnector createHttpConnector(boolean isError) {
		boolean isCmwap = ConfigWrapper.getInstance().isCmwap();
		boolean isReallyCmwap = (isCmwap) && (!(isError));
		return DefaultHttpConnectorFactory.create(isReallyCmwap);
	}

	protected String getMessage(String msg) {
		return getName() + " <-> " + this.downloadFile.getFileName() + ": "
				+ msg;
	}
}