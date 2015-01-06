package com.duowan.download;

import java.io.FileNotFoundException;
import java.io.InputStream;

class NormalDownloadTask extends AbstractDownloadTask {
	public NormalDownloadTask(FileAccess fileAccess,
			IProgressListener progressListener, FileDownloader downloader,
			IOperator operator, DownloadFile downloadFile) {
		super(fileAccess, progressListener, downloader, operator, downloadFile);
		Logger.debug("task", "∆’Õ®œ¬‘ÿ");
	}

	public void run()
  {
    String resUrl = this.downloadFile.getResUrl();
    if (Logger.isDebug()) {
      Logger.debug("task", "resUrl=" + resUrl);
    }
    label310: while (!(this.stop))
      try {
        KGHttpResponse httpResponse = this.httpConnector.getHttpResponse(resUrl);
        if (httpResponse != null) {
          int responseCode = httpResponse.getResponseCode();
          if ((responseCode == 200) || (responseCode == 206)) {
            long contentLength = -1L;
            if (httpResponse.containsHeader("content_length")) {
              contentLength = ((Long)httpResponse
                .getHeader("content_length")).longValue();
            }

            Logger.debug("task", "contentLength=" + contentLength);
            InputStream input = httpResponse.getInputStream();
            long saveLength = this.fileAccess.saveFile(input, this.mListener);
            if (contentLength != -1L) {
              if (saveLength == contentLength) {
                finish();
                return;
              }

              this.downloadFile.resetHaveRead();
              addFaileCounter();

              break label310; }
            if (saveLength != -1L) {
              finish();
              break label310;
            }
            this.downloadFile.resetHaveRead();
            addFaileCounter();

            break label310;
          }
          this.downloadFile.resetHaveRead();
          addFaileCounter();

          break label310;
        }
        this.downloadFile.resetHaveRead();
        boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
        if (!(isNetworkAvalid)) {
          stopByNetError();
          break label310; }
        addFaileCounter();
      }
      catch (Exception e)
      {
        this.downloadFile.resetHaveRead();

        if (e instanceof FileNotFoundException) {
          stopByFileNotFound();
        } else {
          boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
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

		this.downloadFile.setFileSize(this.downloadFile.getHaveRead());
		this.downloadFile.setState(5);
		this.operator.updateFile(this.downloadFile);
		this.downloadFile.getStatis().setFinishTime(System.currentTimeMillis());
		this.progressListener.onProgressChanged(this.downloadFile, 5);
	}
}