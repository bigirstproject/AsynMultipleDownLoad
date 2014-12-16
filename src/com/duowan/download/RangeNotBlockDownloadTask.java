/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.duowan.download;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
		label490: while (!(this.stop))
			try {
				long start = this.downloadFile.getHaveRead();
				KGHttpResponse httpResponse;
				if (start != 0L)
					httpResponse = this.httpConnector.getHttpResponse(resUrl,
							start);
				else {
					httpResponse = this.httpConnector.getHttpResponse(resUrl);
				}
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
						}

						String contentType = null;
						if (httpResponse.containsHeader("content_type")) {
							contentType = (String) httpResponse
									.getHeader("content_type");
						}
						if (Logger.isDebug()) {
							Logger.debug("task", "contentType=" + contentType);
						}

						if ("text/html".equalsIgnoreCase(contentType)) {
							Logger.debug("task", "cmwap net error");
							addFaileCounter();
							this.httpConnector.close();
							this.httpConnector = createHttpConnector(true);
							break label490;
						}

						InputStream input = httpResponse.getInputStream();
						long saveLenght = this.fileAccess.saveFile(input,
								start, this.mListener);

						if (saveLenght != -1L) {
							if (contentLength > 0L) {
								if (this.downloadFile.getHaveRead() >= contentLength) {
									finish();
									return;
								}
								if (Logger.isDebug()) {
									Logger.debug("error", "数据没下载完毕，继续下载...");
								}
								addFaileCounter();

								break label490;
							}
							long haveRead = this.downloadFile.getHaveRead();
							if (haveRead - start > 0L) {
								this.downloadFile.setFileSize(haveRead);
								finish();
								break label490;
							}
							this.downloadFile.setHaveRead(start);
							this.downloader.setTryNumMax();
							this.downloader.addTryNum();
							return;
						}

						addFaileCounter();

						break label490;
					}
					addFaileCounter();

					break label490;
				}
				boolean isNetworkAvalid = ConfigWrapper.getInstance()
						.isNetworkAvalid();
				if (!(isNetworkAvalid)) {
					stopByNetError();
					break label490;
				}
				addFaileCounter();
			} catch (Exception e) {
				e.printStackTrace();
				if (e instanceof FileNotFoundException) {
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

	private void finish() {
		stopDownload();
		this.downloadFile.setHaveRead(this.downloadFile.getFileSize());
		this.downloadFile.setState(5);
		this.operator.updateFile(this.downloadFile);
		this.downloadFile.getStatis().setFinishTime(System.currentTimeMillis());
		this.progressListener.onProgressChanged(this.downloadFile, 5);
	}
}