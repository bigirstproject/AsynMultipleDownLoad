package com.duowan.download;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.duowan.util.LogCat;

class FileAccess {
	public static final int DEFAULT_BUFFER_SIZE = 16384;
	private IProgressListener progressListener;
	private IBytesListener bytesListener;
	private DownloadFile downloadFile;
	private Statistics statis;
	private int bufferSize;
	private boolean stop;

	FileAccess(DownloadFile downloadFile, IProgressListener listener) {
		this(downloadFile, listener, 16384);
	}

	FileAccess(DownloadFile downloadFile, IProgressListener listener,
			int bufferSize) {
		this.downloadFile = downloadFile;
		this.progressListener = listener;
		this.bufferSize = bufferSize;
		this.statis = downloadFile.getStatis();
	}

	public void setProgressListener(IProgressListener listener) {
		this.progressListener = listener;
	}

	public void setBytesListener(IBytesListener listener) {
		this.bytesListener = listener;
	}

	public void setStop(boolean stop) {
		synchronized (this.progressListener) {
			this.stop = stop;
			this.progressListener = null;
		}
	}

	public long saveFile(InputStream input, FileSaveProgressListener listener) {
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(
					this.downloadFile.getFilePath(), "rw");
		} catch (Exception e) {
			return -1L;
		}
		return saveFile(input, randomAccessFile, listener);
	}

	public long saveFile(InputStream input, long seekTo,
			FileSaveProgressListener listener) {
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(
					this.downloadFile.getFilePath(), "rw");
			randomAccessFile.seek(seekTo);
		} catch (Exception e) {
			return -1L;
		}
		return saveFile(input, randomAccessFile, listener);
	}

	private long saveFile(InputStream input, RandomAccessFile randomAccessFile,
			FileSaveProgressListener listener) {
		try {
			BufferedInputStream bis = new BufferedInputStream(input);
			int read = 0;
			byte[] buffer = new byte[this.bufferSize];
			long haveRead = 0L;
			LogCat.d("saveFile start : stop is "+stop);
			while ((!this.stop)&&(read = bis.read(buffer)) != -1) {
				haveRead += read;
				saveFile(buffer, 0, read, randomAccessFile, listener);
				if (this.stop) {
					LogCat.d("stop savefile ");
					return -1L;
				}
			}
			LogCat.d("saveFile onDestroy() : stop is "+stop);
			return haveRead;
		} catch (Exception e) {
			return -1L;
		} finally {
			try {
				LogCat.d("randomAccessFile onDestroy() ");
				randomAccessFile.close();
			} catch (Exception localException4) {
			}
		}
	}

	public long saveFile(byte[] data, int off, int len,
			FileSaveProgressListener listener) {
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(
					this.downloadFile.getFilePath(), "rw");
		} catch (Exception e) {
			return -1L;
		}
		return saveFile(data, off, len, randomAccessFile, listener);
	}

	public long saveFile(byte[] data, int off, int len, long seekTo,
			FileSaveProgressListener listener) {
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(
					this.downloadFile.getFilePath(), "rw");
			randomAccessFile.seek(seekTo);
		} catch (Exception e) {
			return -1L;
		}
		return saveFile(data, off, len, randomAccessFile, listener);
	}

	private long saveFile(byte[] data, int off, int len,
			RandomAccessFile randomAccessFile, FileSaveProgressListener listener) {
		try {
			randomAccessFile.write(data, off, len);
			if (listener != null) {
				listener.onProgressChanged(len);
			}
			if (this.bytesListener != null) {
				this.bytesListener.onBytesReceived(len);
			}
			return len;
		} catch (Exception e) {
		}
		return -1L;
	}

	static abstract interface FileSaveProgressListener {
		public abstract void onProgressChanged(long paramLong);
	}
}