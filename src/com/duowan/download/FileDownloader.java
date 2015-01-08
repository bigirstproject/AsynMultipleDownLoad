package com.duowan.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.duowan.util.LogCat;

public class FileDownloader {
	public static final int PREPAREING = 1;// 准备中
	public static final int READY = 2;// 等待中
	public static final int DOWNLOADING = 3;// 下载中
	public static final int INTERUPT = 4;// 中断（暂停）
	public static final int FINISH = 5;// 完成（成功）
	public static final int INTERUPTING = 6;// 暂停中
	public static final int CREATE_FILE_ERROR = 10;
	public static final int GET_FILE_SIZE_ERROR = 11;
	public static final int TIMEOUT_ERROR = 12;
	public static final int NETWORK_ERROR = 13;
	public static final int FILE_NOT_FOUND = 14;
	private int tryMaxNum;
	private int tryNum;
	private FileAccess fileAccess;
	private IOperator operator;
	private IProgressListener progressListener;
	private IConfig config;
	private ConfigWrapper configWrapper;
	private String resUrl;
	private String filePath;
	private String fileName;
	private long fileSize;
	private String key;
	private DownloadFile downloadFile;
	private List<AbstractDownloadTask> tasks;
	private boolean isStop;
	private int MAX_TASK_NUM = 3;
	private int mThreadCounter;

	public FileDownloader(String resUrl, String filePath) {
		this(resUrl, filePath, new File(filePath).getName());
	}

	public FileDownloader(String resUrl, String filePath, String fileName) {
		this(resUrl, filePath, fileName, 0L, resUrl);
	}

	public FileDownloader(String resUrl, String filePath, long fileSize) {
		this(resUrl, filePath, new File(filePath).getName(), fileSize, resUrl);
	}

	public FileDownloader(String resUrl, String filePath, String fileName,
			long fileSize, String key) {
		this.tryMaxNum = 3;

		this.fileSize = -1L;

		this.tasks = new ArrayList<AbstractDownloadTask>();

		this.MAX_TASK_NUM = 3;

		this.resUrl = resUrl;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.key = key;
		this.config = new DefaultConfig();
		this.configWrapper = ConfigWrapper.getInstance();
		this.progressListener = new DefaultProgressListener();
		this.operator = new DefaultOperator();
		loadConfig();
	}

	public void setOperator(IOperator operator) {
		this.operator = operator;
	}

	public void setProgressListener(IProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	public IProgressListener getProgressListener() {
		return progressListener;
	}

	public void setConfig(IConfig config) {
		this.config = config;
		loadConfig();
	}

	public IConfig getConfig() {
		return this.config;
	}

	public void setBytesListener(IBytesListener listener) {
		this.fileAccess.setBytesListener(listener);
	}

	public DownloadFile getDownloadFile() {
		return this.downloadFile;
	}

	public void prepare() {
		if (this.configWrapper.isBlock()) {
			this.downloadFile = new BlockedDownloadFile();
		} else
			this.downloadFile = new DownloadFile();

		this.downloadFile.setResUrl(this.resUrl);
		this.downloadFile.setFilePath(this.filePath);
		this.downloadFile.setFileName(this.fileName);
		this.downloadFile.setMimeType(getMimeType(this.filePath));
		this.downloadFile.setFileSize(this.fileSize);
		this.downloadFile.setState(2);
		this.downloadFile.setKey(this.key);
		this.fileAccess = new FileAccess(this.downloadFile,
				this.progressListener);
		this.fileAccess.setProgressListener(this.progressListener);

		String selection = "key = ? ";
		String[] selectionArgs = { this.key };

		List<DownloadFile> files = this.operator.queryFile(selection,
				selectionArgs);
		if ((files != null) && (files.size() > 0)) {
			DownloadFile tempFile = (DownloadFile) files.get(0);
			File file = new File(tempFile.getFilePath());
			if (file.exists()) {
				this.downloadFile.setId(tempFile.getId());
				this.downloadFile.setHaveRead(tempFile.getHaveRead());
				this.downloadFile.setState(tempFile.getState());
				this.downloadFile.setFilePath(tempFile.getFilePath());
				this.downloadFile.setFileSize(tempFile.getFileSize());
				this.filePath = tempFile.getFilePath();
			} else {
				this.downloadFile.setId(tempFile.getId());
				this.downloadFile.setHaveRead(0);
				this.downloadFile.setState(0);
				this.downloadFile.setFilePath(tempFile.getFilePath());
				this.downloadFile.setFileSize(tempFile.getFileSize());
				this.filePath = tempFile.getFilePath();
				this.operator.updateFile(downloadFile);
			}
		} else {
			long id = this.operator.insertFile(this.downloadFile);
			this.downloadFile.setId(id);
		}

	}

	public void startTask() {
		if (isStop()) {
			return;
		}

		this.fileSize = this.downloadFile.getFileSize();
		this.filePath = this.downloadFile.getFilePath();

		if (this.fileSize <= 0L) {
			LogCat.d("getContentLength  in is  time = " + System.currentTimeMillis());
			this.fileSize = getContentLength(this.resUrl);
			Logger.debug("fileSize", "getFileSize=" + this.fileSize);
			if (this.fileSize > 0L) {
				this.downloadFile.setFileSize(this.fileSize);
			}
			LogCat.d("getContentLength  out  is time = " + System.currentTimeMillis());
		}

		File file = new File(this.filePath);
		if (file.exists()) {
			if (this.downloadFile.getState() == 5) {
				this.progressListener.onProgressChanged(this.downloadFile, 5);
				return;
			}
		} else {
			this.downloadFile.setHaveRead(0L);
			this.downloadFile.setState(0);
			LogCat.d("createFile  in  is time = " + System.currentTimeMillis());
			boolean createFile = createFile(this.downloadFile);
			LogCat.d("createFile  out  is time = " + System.currentTimeMillis());
			if (!(createFile)) {
				this.progressListener.onError(this.downloadFile, 10);
				return;
			}
		}
		LogCat.d("startTask  loading  is A : time  = " + System.currentTimeMillis());
		boolean isRange = this.configWrapper.isRange();

		boolean isBlock = this.configWrapper.isBlock();

		int blockSize = this.configWrapper.getBlockSize();

		if (isStop()) {
			return;
		}
		if (isRange) {
			if (isBlock) {
				if (this.fileSize <= 0L) {
					this.progressListener.onError(this.downloadFile, 11);
					return;
				}

				BlockedDownloadFile blockedDownloadFile = (BlockedDownloadFile) this.downloadFile;
				long haveRead = blockedDownloadFile.getHaveRead();
				int bufferedIndex = (int) (haveRead / blockSize - 1L);
				Logger.debug("bufferedIndex", "bufferedIndex----->"
						+ bufferedIndex);
				blockedDownloadFile.setBufferedIndex(bufferedIndex);
				blockedDownloadFile.setBlockSize(Math.min(blockSize,
						(int) this.fileSize));
				blockedDownloadFile.splitBlocks(bufferedIndex);
			}
		} else {
			this.downloadFile.setHaveRead(0L);
		}
		LogCat.d("startTask  loading  is B : time  = " + System.currentTimeMillis());
		
		this.operator.updateFile(this.downloadFile);
		
		LogCat.d("updateFile  dabase ： time  = " + System.currentTimeMillis());
		
		Statistics statis = this.downloadFile.getStatis();

		statis.setStartTime(System.currentTimeMillis());

		statis.setDownloadLen(this.downloadFile.getFileSize()
				- this.downloadFile.getHaveRead());

		this.downloadFile.setState(2);
		this.progressListener.onProgressChanged(this.downloadFile, 2);

		int taskNum = 1;
		if ((isRange) && (isBlock)) {
			taskNum = this.configWrapper.getTaskNum();
		}
		Logger.debug("test", "isRange = " + isRange + "     isBlock = "
				+ isBlock + "   taskNum = " + taskNum);
		LogCat.d("startTask  loading  is C : time  = " + System.currentTimeMillis());
		startDownloadTasks(taskNum);
	}

	public void stop() {
		stopDownload(4);
	}

	void stopByTimeOut() {
		stopDownload(12);
	}

	void stopByFileNotFound() {
		stopDownload(14);
	}

	private synchronized void stopDownload(int stopReason) {
		if (!(isStop()))
			this.isStop = true;
		else {
			return;
		}
		int size = this.tasks.size();
		LogCat.d("stopDownload size is " + size + "   "
				+ System.currentTimeMillis());
		for (int i = 0; i < size; ++i) {
			((AbstractDownloadTask) this.tasks.get(i)).stopDownload();
		}
		this.fileAccess.setStop(true);
		LogCat.d("stopDownload file state is B " + System.currentTimeMillis());
		Logger.debug("stop", "---------" + this.fileName
				+ " stopDownload----------");
		if (this.downloadFile.getState() != 5) {
			this.downloadFile.setState(4);
		}

		this.operator.updateFile(this.downloadFile);
		if (stopReason == 4) {
			this.progressListener.onProgressChanged(this.downloadFile, 4);
		} else if (stopReason == 12) {
			this.progressListener.onError(this.downloadFile, 12);
		} else if (stopReason == 13) {
			this.progressListener.onError(this.downloadFile, 13);
		} else if (stopReason == 14) {
			this.progressListener.onError(this.downloadFile, 14);
		}
		this.tasks.clear();
	}

	synchronized void resetTryNum() {
		this.tryNum = 0;
	}

	synchronized void addTryNum() {
		this.tryNum += 1;
		LogCat.d("tryNum = "+tryNum
				+ "  :   tryMaxNum =  " +tryMaxNum +"  :  time  = "+ System.currentTimeMillis());
		if (this.tryNum >= this.tryMaxNum)
			stopByTimeOut();
	}

	synchronized void setTryNumMax() {
		this.tryNum = this.tryMaxNum;
	}

	synchronized void stopByNetError() {
		stopDownload(13);
	}

	private boolean createFile(DownloadFile downloadFile) {
		try {
			String filePath = downloadFile.getFilePath();
			long fileSize = downloadFile.getFileSize();
			File file = new File(filePath);
			File parent = file.getParentFile();
			LogCat.d("createFile parent  in : time = " + System.currentTimeMillis());
			if (!(parent.exists())) {
				parent.mkdirs();
			}
			LogCat.d("createFile parent  out : time = " + System.currentTimeMillis());
			
			if (fileSize > 0L) {
				LogCat.d("createFile RandomAccessFile  in : time = " + System.currentTimeMillis());
				RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
				LogCat.d("fileSize = "+fileSize +"   createFile RandomAccessFile  length  in: time = " + System.currentTimeMillis());
				raf.setLength(fileSize);
				LogCat.d("createFile RandomAccessFile  close  in: time = " + System.currentTimeMillis());
				raf.close();
				LogCat.d("createFile RandomAccessFile  out : time = " + System.currentTimeMillis());
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	private synchronized void startDownloadTasks(int taskNum) {
		if (isStop()) {
			return;
		}
		this.downloadFile.setState(3);
		this.operator.updateFile(this.downloadFile);
		this.progressListener.onProgressChanged(this.downloadFile, 3);
		LogCat.d("startTask  loading  is D : time  = " + System.currentTimeMillis());
		for (int i = 0; (i < taskNum) && (!(isStop())); ++i)
			startDownloadTask();
	}

	private void startDownloadTask() {
		boolean isRange = this.configWrapper.isRange();
		boolean isBlock = this.configWrapper.isBlock();
		if (!(isStop())) {
			AbstractDownloadTask task = createDownloadTask(isRange, isBlock);
			this.tasks.add(task);

			LogCat.d("startTask  loading  is E : time  = " + System.currentTimeMillis());
			
			task.start();
			LogCat.d("AbstractDownloadTask  is start ： time  = " + System.currentTimeMillis());
		}
	}

	public synchronized void speedUp() {
		while ((this.tasks.size() < 3) && (!(isStop()))) {
			startDownloadTask();
			Logger.debug("speed", "speedUp-->");
		}
	}

	public synchronized void speedDown() {
		while (this.tasks.size() > 1)
			removeDownloadTask();
	}

	private void loadConfig() {
		this.configWrapper.setConfig(this.config);
		this.configWrapper.setBlockSize(this.config.getBlockSize());
		this.configWrapper.setTaskNum(this.config.getTaskNum());
		this.configWrapper.setNetType(this.config.getNetType());
		this.configWrapper.setRange(this.config.isRange());
		this.configWrapper.setCmwap(this.config.isCmwap());
		this.configWrapper.setRefreshInterval(this.config.getRefreshInterval());
		this.configWrapper.setBufferBlockNum(this.config.getBufferBlockNum());
		this.configWrapper.setRequestHeaders(this.config.getRequestHeaders());
		this.configWrapper.setNeedToForceBlock(this.config
				.is2GNeedToForceBlock());
		if (this.configWrapper.isRange()) {
			this.configWrapper.setBlock(this.config.isBlock());
		} else
			this.configWrapper.setBlock(false);

		if ((!(this.config.is2GNeedToForceBlock()))
				|| (this.configWrapper.getNetType() != NetType.G2))
			return;
		this.configWrapper.setRange(true);
		this.configWrapper.setBlock(true);
	}

	private AbstractDownloadTask createDownloadTask(boolean isRange,
			boolean isBlock) {
		AbstractDownloadTask downloadTask = null;
		if (isRange) {
			if (isBlock)
				downloadTask = new RangeBlockDownloadTask(this.fileAccess,
						this.progressListener, this, this.operator,
						this.downloadFile);
			else
				downloadTask = new RangeNotBlockDownloadTask(this.fileAccess,
						this.progressListener, this, this.operator,
						this.downloadFile);
		} else {
			downloadTask = new NormalDownloadTask(this.fileAccess,
					this.progressListener, this, this.operator,
					this.downloadFile);
		}
		downloadTask.setName("download" + (++this.mThreadCounter));
		return downloadTask;
	}

	private void removeDownloadTask() {
		int size = this.tasks.size();
		if (size > 0) {
			AbstractDownloadTask task = (AbstractDownloadTask) this.tasks
					.remove(size - 1);
			task.stopDownload();
			this.mThreadCounter -= 1;
			Logger.debug("speed", "speedDown-->");
		}
	}

	private long getContentLength(String resUrl) {
		boolean isCmwap = this.configWrapper.isCmwap();
		int counter = 0;
		long contentLength = -2147483648L;
		while (counter++ < 2) {
			try {
				IHttpConnector httpConn = DefaultHttpConnectorFactory
						.create(isCmwap);
				contentLength = httpConn.getContentLength(resUrl);
				if (contentLength > 0) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return contentLength;
	}

	private String getMimeType(String filePath) {
		if ((filePath != null) && (filePath.length() > 0)
				&& (filePath.lastIndexOf(".") != -1)) {
			return filePath.substring(filePath.lastIndexOf(".") + 1);
		}

		return "UNKNOWN";
	}

	private synchronized boolean isStop() {
		return this.isStop;
	}

	public int hashCode() {
		return ((int) System.currentTimeMillis() % 8);
	}

	public boolean equals(Object obj) {
		if (obj instanceof FileDownloader) {
			FileDownloader downloader = (FileDownloader) obj;
			return ((downloader.key != null) && (downloader.key
					.equalsIgnoreCase(this.key)));
		}
		return super.equals(obj);
	}
}