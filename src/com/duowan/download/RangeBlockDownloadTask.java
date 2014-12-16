package com.duowan.download;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class RangeBlockDownloadTask extends AbstractDownloadTask {
	private BlockedDownloadFile blockedDownloadFile;
	private byte[] buffer;
	private byte[] temp = new byte[8192];
	private int blockSize;
	private final int ERROR_DATA_LENGTH = 1;

	private final int ERROR_RESPONSE_CODE = 2;

	private final int ERROR_NO_RESPONSE = 3;

	private final int ERROR_EXCEPTION = 4;

	private final int ERROR_SAVE_FILE = 5;

	public RangeBlockDownloadTask(FileAccess fileAccess,
			IProgressListener progressListener, FileDownloader downloader,
			IOperator operator, DownloadFile downloadFile) {
		super(fileAccess, progressListener, downloader, operator, downloadFile);
		this.blockedDownloadFile = ((BlockedDownloadFile) downloadFile);
		this.blockSize = ConfigWrapper.getInstance().getBlockSize();
		Logger.debug("task", getMessage("断点续传并分块下载"));
	}

	public void run() {
		String resUrl = this.blockedDownloadFile.getResUrl();
		if (Logger.isDebug()) {
			Logger.debug(this.tag,
					getMessage("开始下载" + this.blockedDownloadFile.getFileName()));
		}
		label721: label988: while (!(this.stop)) {
			int freeBlockIndex = FreeBlockManager
					.getFreeBlockIndex(this.blockedDownloadFile);
			int bufferedIndex = this.blockedDownloadFile.getBufferedIndex();
			if (freeBlockIndex == -1) {
				if (bufferedIndex + 1 == this.blockedDownloadFile.getBlockNum()) {
					stopDownload();
					synchronized (this.downloadFile) {
						if (this.blockedDownloadFile.getState() != 5) {
							this.blockedDownloadFile
									.setHaveRead(this.blockedDownloadFile
											.getFileSize());
							this.blockedDownloadFile.setState(5);
							this.operator.updateFile(this.blockedDownloadFile);
							this.downloadFile.getStatis().setFinishTime(
									System.currentTimeMillis());
							this.progressListener.onProgressChanged(
									this.blockedDownloadFile, 5);
						}
					}
				}
				Logger.debug(this.tag,
						getMessage(this.blockedDownloadFile.getFileName()
								+ " 下载完毕."));
				break;
			}
			DataBlock dataBlock = this.blockedDownloadFile.getDataBlocks()[freeBlockIndex];
			int bufferBlockNum = ConfigWrapper.getInstance()
					.getBufferBlockNum();
			bufferBlockNum = Math.max(bufferBlockNum, ConfigWrapper
					.getInstance().getTaskNum());
	
			if ((bufferBlockNum != -1)
					&& (freeBlockIndex - (bufferedIndex + 1) >= bufferBlockNum)) {
				dataBlock.setState(0);
				 try {
						sleep(500L);
					} catch (Exception localException1) {
					}
			} else {
				long start = dataBlock.getStart();
				long end = dataBlock.getEnd();
				long reqLength = end - start + 1L;
				if (Logger.isDebug()) {
					Logger.debug(this.tag, getMessage("---->begin"));
					Logger.debug(this.tag, getMessage("reqUrl=" + resUrl));
					Logger.debug(this.tag, getMessage("freeBlockIndex="
							+ freeBlockIndex));
					Logger.debug(this.tag, getMessage("start=" + start
							+ " end=" + end));
					Logger.debug(this.tag, getMessage("reqLength=" + reqLength));
				}
				if ((this.buffer == null) || (this.buffer.length != reqLength))
					this.buffer = new byte[(int) reqLength];
				try {
					if (Logger.isDebug()) {
						Logger.debug(this.tag, getMessage("--reading..."));
					}
					KGHttpResponse httpResponse = this.httpConnector
							.getHttpResponse(resUrl, start, end);
					if (Logger.isDebug()) {
						Logger.debug(this.tag, getMessage("--done"));
					}
					if (httpResponse != null) {
						int responseCode = httpResponse.getResponseCode();
						if ((responseCode == 200) || (responseCode == 206)) {
							if (httpResponse.containsHeader("content_length")) {
								long contentLength = ((Long) httpResponse
										.getHeader("content_length"))
										.longValue();

								Logger.debug(this.tag,
										getMessage("content-length="
												+ contentLength));
								if (contentLength == reqLength)
									faile(dataBlock, 1);
								this.downloader.setTryNumMax();
								break label721;
							}
							if (httpResponse.containsHeader("content_type")) {
								String contentType = (String) httpResponse
										.getHeader("content_type");
								Logger.debug(this.tag,
										getMessage("content-type="
												+ contentType));

								if ("image/jpeg".equalsIgnoreCase(contentType)) {
									this.downloader.setTryNumMax();
									faile(dataBlock, 1);
									break label721;
								}
							}

							InputStream input = httpResponse.getInputStream();
							byte[] data = readData(input, reqLength);
							if ((data != null) && (data.length == reqLength)) {
								long saveLength = this.fileAccess.saveFile(
										data, 0, data.length, start,
										this.mListener);
								if (saveLength != -1L) {
									if (Logger.isDebug()) {
										Logger.debug(this.tag, getMessage("第"
												+ freeBlockIndex + "块数据下载完毕"));
									}
									dataBlock.setState(2);
									clearFaileCounter();
									break label988;
								}
								faile(dataBlock, 5);

								break label988;
							}
							if (Logger.isDebug()) {
								Logger.debug(this.tag,
										getMessage("error: reqLength="
												+ reqLength + " receiveLength="
												+ data.length));
							}
							faile(dataBlock, 1);

							break label988;
						}
						faile(dataBlock, 2);

						break label988;
					}
					faile(dataBlock, 3);
				} catch (Exception e) {
					Logger.debug(this.tag,
							getMessage("error: " + e.getMessage()));

					if (e instanceof FileNotFoundException) {
						stopByFileNotFound();
					} else {
						this.httpConnector = createHttpConnector(true);
						faile(dataBlock, 4);
					}
				}
			}
		}
//		if (Logger.isDebug())
//			label995: Logger.debug(this.tag, getMessage("------线程结束--------"));
	}

	private byte[] readData(InputStream input, long reqLength) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(input);
		int read = 0;
		int haveRead = 0;
		while ((read = bis.read(this.temp)) != -1) {
			if (haveRead + read <= this.buffer.length) {
				System.arraycopy(this.temp, 0, this.buffer, haveRead, read);
				haveRead += read;
			} else {
				input.close();
				input = null;
				bis.close();
				bis = null;
				return null;
			}
		}
		input.close();
		input = null;
		bis.close();
		bis = null;

		if (haveRead == reqLength) {
			return this.buffer;
		}
		return null;
	}

	private void faile(DataBlock dataBlock, int errorType) {
		String error = "unknown";
		switch (errorType) {
		case 1:
			error = "块大小不对";
			break;
		case 2:
			error = "响应码错误（非200,206）";
			break;
		case 3:
			error = "服务器没响应";
			break;
		case 4:
			error = "发生异常";
			break;
		case 5:
			error = "保存文件出错";
		}

		if (Logger.isDebug()) {
			Logger.debug(this.tag, getMessage("error=" + error));
		}
		dataBlock.setState(0);

		boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
		if (!(isNetworkAvalid))
			stopByNetError();
		else
			addFaileCounter();
	}
}