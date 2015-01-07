package com.duowan.asynmultipledownload.downTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.duowan.asynmultipledownload.Interface.IDownloadManagerCallBackListener;
import com.duowan.asynmultipledownload.Interface.IDownloadService;
import com.duowan.asynmultipledownload.bean.BindBeforeComponent;
import com.duowan.asynmultipledownload.service.DownloadService;
import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.download.IProgressListener;
import com.duowan.download.manager.ParamsWrapper;

/**
 * 下载服务调用工具类
 * 
 */
public class DownloadServiceUtil {

	private static IDownloadService sService;

	private static HashMap<Context, ServiceBinder> sConnectionMap = new HashMap<Context, ServiceBinder>();

	protected static ArrayList<BindBeforeComponent> mCallbacks = new ArrayList<BindBeforeComponent>();

	protected static ArrayList<IDownloadManagerCallBackListener> mDowningCallbacks = new ArrayList<IDownloadManagerCallBackListener>();

	public static class DownloadServiceToken {
		ContextWrapper mWrappedContext;

		DownloadServiceToken(ContextWrapper context) {
			mWrappedContext = context;
		}
	}

	private static class ServiceBinder implements ServiceConnection {

		public void onServiceConnected(ComponentName className, IBinder service) {
			sService = (IDownloadService) service;
			if (mCallbacks != null && mCallbacks.size() > 0) {
				for (int i = 0; i < mCallbacks.size(); i++) {
					BindBeforeComponent component = mCallbacks.remove(i);
					if (component.getListener() != null
							&& component.getDownloadManager() != null) {
						sService.registerCallback(component.getListener(),
								component.getDownloadManager());
					} else if (component.getListener() != null) {
						sService.registerCallback(component.getListener());
					}
				}
				mCallbacks = null;
			}

			if (mDowningCallbacks != null && mDowningCallbacks.size() > 0) {
				for (int i = 0; i < mDowningCallbacks.size(); i++) {
					IDownloadManagerCallBackListener callBackListener = mDowningCallbacks
							.remove(i);
					if (callBackListener != null) {
						sService.registerDowningCallback(callBackListener);
					}
				}
				mDowningCallbacks = null;
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			StopBindService();
		}

	}

	public static void StopBindService() {
		sService = null;
		sConnectionMap.clear();
		mCallbacks.clear();
		mDowningCallbacks.clear();
	}

	public static DownloadServiceToken bindToService(Context context) {
		ContextWrapper cw = new ContextWrapper(context);
		cw.startService(new Intent(cw, DownloadService.class));
		ServiceBinder sb = new ServiceBinder();
		if (cw.bindService((new Intent()).setClass(cw, DownloadService.class),
				sb, Service.BIND_AUTO_CREATE)) {
			sConnectionMap.put(cw, sb);
			return new DownloadServiceToken(cw);
		}
		return null;
	}

	public static void unbindFromService(DownloadServiceToken token) {
		if (token == null) {
			return;
		}
		ContextWrapper cw = token.mWrappedContext;
		ServiceBinder sb = sConnectionMap.remove(cw);
		if (sb == null) {
			return;
		}
		cw.unbindService(sb);
		if (sConnectionMap.isEmpty()) {
			// presumably there is nobody interested in the service at this
			// point,
			// so don't hang on to the ServiceConnection
			sService = null;
		}
	}

	private static boolean checkServiceBinded() {
		boolean isRunning = sService != null;
		return isRunning;
	}

	public static boolean download(String resUrl, String filePath,
			IProgressListener listener) {
		if (checkServiceBinded()) {
			return sService.download(resUrl, filePath, listener);
		}
		return false;
	}

	public static boolean download(String resUrl, String filePath, int classId,
			IProgressListener listener) {
		if (checkServiceBinded()) {
			return sService.download(resUrl, filePath, classId, listener);
		}
		return false;
	}

	public static boolean download(ParamsWrapper paramsWrapper,
			IProgressListener callback) {
		if (checkServiceBinded()) {
			return sService.download(paramsWrapper, callback);
		}
		return false;
	}

	public static void stopDownload(String key) {
		if (checkServiceBinded()) {
			sService.stopDownload(key);
		}
	}

	public static void stopAllDownload() {
		if (checkServiceBinded()) {
			sService.stopAllDownload();
		}
	}

	public static void removeFromDownloadingSet(String key) {
		if (checkServiceBinded()) {
			sService.removeFromDownloadingSet(key);
		}
	}

	public static boolean isDownloading(String key) {
		if (checkServiceBinded()) {
			return sService.isDownloading(key);
		}
		return false;
	}

	public static DownloadFile getDownloadFile(String key) {
		if (checkServiceBinded()) {
			return sService.getDownloadFile(key);
		}
		return null;
	}

	public static int getProgress(String key) {
		if (checkServiceBinded()) {
			return sService.getProgress(key);
		}
		return 0;
	}

	public static void registerCallback(IProgressListener listener) {
		if (checkServiceBinded()) {
			sService.registerCallback(listener);
		} else {
			synchronized (mCallbacks) {
				if (!mCallbacks.contains(listener)) {
					BindBeforeComponent component = new BindBeforeComponent();
					component.setListener(listener);
					mCallbacks.add(component);
				}
			}
		}
	}

	public static void registerCallback(IProgressListener listener,
			IDownloadManagerCallBackListener callBackListener) {
		if (checkServiceBinded()) {
			sService.registerCallback(listener, callBackListener);
		} else {
			synchronized (mCallbacks) {
				if (!mCallbacks.contains(listener)) {
					BindBeforeComponent component = new BindBeforeComponent();
					component.setListener(listener);
					component.setDownloadManager(callBackListener);
					mCallbacks.add(component);
				}
			}
		}
	}

	public static void removeCallback(IProgressListener listener) {
		if (checkServiceBinded()) {
			sService.removeCallback(listener);
		} else {
			synchronized (mCallbacks) {
				BindBeforeComponent component = new BindBeforeComponent();
				component.setListener(listener);
				mCallbacks.remove(component);
			}
		}
	}

	public static void registerDowningCallback(
			IDownloadManagerCallBackListener callBackListener) {
		if (checkServiceBinded()) {
			sService.registerDowningCallback(callBackListener);
		} else {
			synchronized (mDowningCallbacks) {
				if (!mDowningCallbacks.contains(callBackListener)) {
					mDowningCallbacks.add(callBackListener);
				}
			}
		}
	}

	public HashMap<String, FileDownloader> getDownloadingSet() {
		if (checkServiceBinded()) {
			return sService.getDownloadingSet();
		}
		return null;
	}

	public LinkedList<FileDownloader> getWaittingList() {
		if (checkServiceBinded()) {
			return sService.getWaittingList();
		}
		return null;
	}

}
