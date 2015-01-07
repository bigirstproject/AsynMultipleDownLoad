package com.duowan.asynmultipledownload.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;

import com.duowan.asynmultipledownload.Interface.IDownloadManagerCallBackListener;
import com.duowan.asynmultipledownload.Interface.IDownloadService;
import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.download.IConfig;
import com.duowan.download.IOperator;
import com.duowan.download.IProgressListener;
import com.duowan.download.manager.DownloadManager;
import com.duowan.download.manager.DownloadProListener;
import com.duowan.download.manager.ParamsWrapper;
import com.duowan.util.LogCat;



/**
 * 下载服务
 * 
 */
public abstract class BaseDownloadService extends BaseWorkerService {

	private static final int DOWNLOADING_PROGRESS_STATE = 0x10001;
	private static final int DOWNLOADING_ERROR_STATE = 0x10002;
	protected static final int PRORESS_CODE = 1;
	protected static final int ERROR_CODE = 2;
	protected ArrayList<IProgressListener> mCallbacks;
	protected DownloadManager mDownloadManager;

	public void onCreate() {
		super.onCreate();
		mCallbacks = new ArrayList<IProgressListener>();
		mDownloadManager = new DownloadManager(createOperator(),
				LogCat.isDebug());
		IConfig config = createConfig();
		if (config != null) {
			mDownloadManager.setConfig(config);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	protected ArrayList<IProgressListener> getmCallbacks() {
		return mCallbacks;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogCat.d("BaseDownloadService is  onDestroy() : Callbacks size is"
				+ mCallbacks.size());
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

	/**
	 * 遍历注册的监听器
	 * 
	 */
	protected abstract void invokeCallback(DownloadFile file, int state,
			int type);

	@Override
	protected void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case DOWNLOADING_PROGRESS_STATE:
			if (msg != null && msg.obj instanceof DownloadFile) {
				DownloadFile file = (DownloadFile) msg.obj;
				invokeCallback(file, msg.arg1, PRORESS_CODE);
				if (!(msg.arg1 == FileDownloader.PREPAREING
						|| msg.arg1 == FileDownloader.READY || msg.arg1 == FileDownloader.DOWNLOADING)) {
					mDownloadManager.downNextWaittingQueueWrapper();
				}
			}
		case DOWNLOADING_ERROR_STATE:
			if (msg != null && msg.obj instanceof DownloadFile) {
				DownloadFile file = (DownloadFile) msg.obj;
				invokeCallback(file, msg.arg1, ERROR_CODE);
				if (!(msg.arg1 == FileDownloader.PREPAREING
						|| msg.arg1 == FileDownloader.READY || msg.arg1 == FileDownloader.DOWNLOADING)) {
					mDownloadManager.downNextWaittingQueueWrapper();
				}
			}
		default:
			break;
		}

	}

	private IBinder mBinder = new ServiceStub();

   class ServiceStub extends Binder implements IDownloadService {

		@Override
		public boolean download(String resUrl, String filePath,
				IProgressListener listener) {
			if (listener != null) {
				return mDownloadManager.download(resUrl, filePath, listener);
			} else {
				return mDownloadManager.download(resUrl, filePath,
						new DownloadProListener(mBackgroundHandler));
			}
		}

		@Override
		public boolean download(String resUrl, String filePath, int classId,
				IProgressListener listener) {
			if (listener != null) {
				return mDownloadManager.download(resUrl, filePath, classId,
						listener);
			} else {
				return mDownloadManager.download(resUrl, filePath, classId,
						new DownloadProListener(mBackgroundHandler));
			}
		}

		@Override
		public boolean download(ParamsWrapper paramsWrapper,
				IProgressListener listener) {
			if (listener != null) {
				return mDownloadManager.download(paramsWrapper, listener);
			} else {
				return mDownloadManager.download(paramsWrapper,
						new DownloadProListener(mBackgroundHandler));
			}
		}

		@Override
		public void stopDownload(String key) {
			mDownloadManager.stopDownload(key);
		}

		@Override
		public void stopAllDownload() {
			mDownloadManager.stopAll();
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
		public DownloadFile getDownloadFile(String key) {
			return mDownloadManager.getDownloadFile(key);
		}

		@Override
		public int getProgress(String key) {
			return mDownloadManager.getProgress(key);
		}

		@Override
		public void registerCallback(IProgressListener listener) {
			synchronized (mCallbacks) {
				if (!mCallbacks.contains(listener)) {
					mCallbacks.add(listener);
				}
			}
		}

		@Override
		public void registerCallback(IProgressListener listener,
				IDownloadManagerCallBackListener callBackListener) {
			synchronized (mCallbacks) {
				if (!mCallbacks.contains(listener)) {
					mCallbacks.add(listener);
				}
			}
			callBackListener
					.setCallBackDownloadManagerLitener(mDownloadManager);
		}

		@Override
		public void removeCallback(IProgressListener listener) {
			synchronized (mCallbacks) {
				mCallbacks.remove(listener);
			}
		}

		@Override
		public HashMap<String, FileDownloader> getDownloadingSet() {
			return mDownloadManager.getDownloadingSet();
		}

		@Override
		public LinkedList<FileDownloader> getWaittingList() {
			return mDownloadManager.getWaittingList();
		}

		@Override
		public void registerDowningCallback(
				IDownloadManagerCallBackListener callBackListener) {
			callBackListener
					.setCallBackDownloadManagerLitener(mDownloadManager);
		}

	}
}
