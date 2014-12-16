package com.duowan.asynmultipledownload;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.duowan.download.DownloadFile;
import com.duowan.download.IConfig;
import com.duowan.download.IOperator;
import com.duowan.download.IProgressListener;
import com.duowan.download.manager.DownloadManager;
import com.duowan.download.manager.ParamsWrapper;

/**
 * 下载服务
 * 
 */
public abstract class BaseDownloadService extends BaseService {

	private DownloadManager mDownloadManager;

	public void onCreate() {
		mDownloadManager = new DownloadManager(createOperator(),
				KGLog.isDebug());
		IConfig config = createConfig();
		if (config != null) {
			mDownloadManager.setConfig(config);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDownloadManager.stopAll();
	}

	/**
	 * 创建文件信息保存类
	 * 
	 * @return
	 */
	protected abstract IOperator createOperator();

	/**
	 * 获取下载配置信息
	 * 
	 * @return
	 */
	protected abstract IConfig createConfig();

	private IBinder mBinder = new ServiceStub();

	class ServiceStub extends Binder implements IDownloadService {

		@Override
		public boolean download(String resUrl, String filePath,
				IProgressListener listener) {
			return mDownloadManager.download(resUrl, filePath, listener);
		}

		@Override
		public boolean download(String resUrl, String filePath, int classId,
				IProgressListener listener) {
			return mDownloadManager.download(resUrl, filePath, classId,
					listener);
		}

		@Override
		public boolean download(ParamsWrapper paramsWrapper,
				IProgressListener callback) {
			return mDownloadManager.download(paramsWrapper, callback);
		}

		@Override
		public boolean addToWaittingQueue(ParamsWrapper paramsWrapper) {
			return mDownloadManager.addToWaittingQueue(paramsWrapper);
		}

		@Override
		public void stopDownload(String key) {
			mDownloadManager.stopDownload(key);
		}

		@Override
		public void removeFromWaittingQueue(String key) {
			mDownloadManager.removeFromWaittingQueue(key);
		}

		@Override
		public void removeFromDownloadingSet(String key) {
			mDownloadManager.removeFromDownloadingSet(key);
		}

		@Override
		public boolean isDownloading(String key) {
			return mDownloadManager.isDownloading(key);
		}

		@Override
		public boolean isInWaittingQueue(String key) {
			return mDownloadManager.isInWaittingQueue(key);
		}

		@Override
		public DownloadFile getDownloadFile(String key) {
			return mDownloadManager.getDownloadFile(key);
		}

		@Override
		public int getProgress(String key) {
			return mDownloadManager.getProgress(key);
		}

	}
}
