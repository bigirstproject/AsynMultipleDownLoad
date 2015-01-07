package com.duowan.asynmultipledownload.Interface;

import com.duowan.download.manager.DownloadManager;

/**
 * <p>Description: 需要注册下载界面，需同步当前下载数据的一个回调<／p>
 * @author  ldxs
 * @date  2015年1月6日  下午9:16:05
 */
public interface IDownloadManagerCallBackListener {
	
	 void setCallBackDownloadManagerLitener(DownloadManager downloadManager);

}
