
package com.duowan.asynmultipledownload;

import com.duowan.download.DownloadFile;
import com.duowan.download.IProgressListener;
import com.duowan.download.manager.ParamsWrapper;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.HashMap;

/**
 * 下载服务调用工具类
 * 
 */
public class DownloadServiceUtil {

    private static IDownloadService sService;

    private static HashMap<Context, ServiceBinder> sConnectionMap = new HashMap<Context, ServiceBinder>();

    public static class DownloadServiceToken {
        ContextWrapper mWrappedContext;

        DownloadServiceToken(ContextWrapper context) {
            mWrappedContext = context;
        }
    }

    private static class ServiceBinder implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder service) {
            sService = (IDownloadService) service;
        }

        public void onServiceDisconnected(ComponentName className) {
            sService = null;
        }
    }

    public static DownloadServiceToken bindToService(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        cw.startService(new Intent(cw, DownloadService.class));
        ServiceBinder sb = new ServiceBinder();
        if (cw.bindService((new Intent()).setClass(cw, DownloadService.class), sb, 0)) {
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

    public static boolean isInitialized() {
        return sService != null;
    }

    /**
     * 下载
     * 
     * @param resUrl
     * @param filePath
     * @param listener
     * @return
     */
    public static boolean download(String resUrl, String filePath, IProgressListener listener) {
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

    public static boolean download(ParamsWrapper paramsWrapper, IProgressListener callback) {
        if (checkServiceBinded()) {
            return sService.download(paramsWrapper, callback);
        }
        return false;
    }

    public static boolean addToWaittingQueue(ParamsWrapper paramsWrapper) {
        if (checkServiceBinded()) {
            return sService.addToWaittingQueue(paramsWrapper);
        }
        return false;
    }

    public static void stopDownload(String key) {
        if (checkServiceBinded()) {
            sService.stopDownload(key);
        }
    }

    public static void removeFromWaittingQueue(String key) {
        if (checkServiceBinded()) {
            sService.removeFromWaittingQueue(key);
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

    public static boolean isInWaittingQueue(String key) {
        if (checkServiceBinded()) {
            return sService.isInWaittingQueue(key);
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

}
