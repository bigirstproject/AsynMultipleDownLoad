package com.duowan.download;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.duowan.util.LogCat;

class RangeNotBlockDownloadTask extends AbstractDownloadTask {
	public RangeNotBlockDownloadTask(FileAccess fileAccess,
			IProgressListener progressListener, FileDownloader downloader,
			IOperator operator, DownloadFile downloadFile) {
		super(fileAccess, progressListener, downloader, operator, downloadFile);
		Logger.debug("task", "断点续传但不分块下载");
	}

	public void run() {
		String resUrl = this.downloadFile.getResUrl();
		if (Logger.isDebug()) {
			Logger.debug("task", "resUrl=" + resUrl);
		}

		label490: while (!(this.stop)) {
			try {
				LogCat.d("RangeNotBlockDownloadTask is strat run "
						+ "  :   time is  " + System.currentTimeMillis());
				long start = this.downloadFile.getHaveRead();
				KGHttpResponse httpResponse = null;
				if (start != 0L) {
					httpResponse = this.httpConnector.getHttpResponse(resUrl,
							start);
				} else {
					httpResponse = this.httpConnector.getHttpResponse(resUrl);
				}
				Logger.debug("task", "code = " + httpResponse.getResponseCode());
//				LogCat.d("RangeNotBlockDownloadTask is getResponseCode : time = "
//						+ System.currentTimeMillis());
				if (httpResponse != null) {
					int responseCode = httpResponse.getResponseCode();
					if ((responseCode == 200) || (responseCode == 206)) {

						long contentLength = -1L;

						if (httpResponse.containsHeader("content_length")) {
							contentLength = ((Long) httpResponse
									.getHeader("content_length")).longValue();
						}

						if (Logger.isDebug()) {
							Logger.debug("task", "contentLength="
									+ contentLength);
							String contentType = null;
							if (httpResponse.containsHeader("content_type")) {
								contentType = (String) httpResponse
										.getHeader("content_type");
							}
							Logger.debug("task", "contentType=" + contentType);
						}

						InputStream input = httpResponse.getInputStream();
						long saveLenght = this.fileAccess.saveFile(input,
								start, this.mListener);
						if (saveLenght != -1L) {
							if (contentLength > 0L) {
								if (this.downloadFile.getHaveRead() >= contentLength) {
									finish();
									break label490;
								} else {
									addFaileCounter();
									// LogCat.d("downloadFile.getHaveRead() = "
									// + downloadFile.getHaveRead()
									// + "  : time = "
									// + System.currentTimeMillis());
									// if (Logger.isDebug()) {
									// Logger.debug("error", "数据没下载完毕，继续下载...");
									// }
								}
							}
						} else {
							// LogCat.d("downloadFile.getHaveRead() = "
							// + downloadFile.getHaveRead()
							// + "  : time = "
							// + System.currentTimeMillis());
							addFaileCounter();
						}
					}
				}
			} catch (Exception e) {
				if (e != null && e instanceof FileNotFoundException) {
					stopByFileNotFound();
				} else {
					boolean isNetworkAvalid = ConfigWrapper.getInstance()
							.isNetworkAvalid();
					if (!(isNetworkAvalid)) {
						stopByNetError();
					} else {
						this.httpConnector = createHttpConnector(true);
						addFaileCounter();
					}
				}
			}
		}
	}

	private void finish() {
		stopDownload();
		this.downloadFile.setHaveRead(this.downloadFile.getFileSize());
		this.downloadFile.setState(5);
		this.operator.updateFile(this.downloadFile);
		this.downloadFile.getStatis().setFinishTime(System.currentTimeMillis());
		this.progressListener.onProgressChanged(this.downloadFile, 5);
	}
}