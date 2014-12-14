package com.duowan.asynmultipledownload;

import java.util.Hashtable;

import android.content.Context;

import com.duowan.asynmultipledownload.NetWorkUtil.NetworkType;
import com.kugou.download.DefaultConfig;
import com.kugou.download.NetType;

/**
 * 下载配置
 * 
 */
public class DownloadConfig extends DefaultConfig {

	@Override
	public NetType getNetType() {
		Context context = BaseApplication.getInstance();
		String networkType = NetWorkUtil.getNetworkType(context);
		if (NetworkType.WIFI.equals(networkType)) {
			return NetType.WIFI;
		} else if (NetworkType.NET_3G.equals(networkType)) {
			return NetType.G3;
		} else if (NetworkType.NET_2G.equals(networkType)) {
			return NetType.G2;
		} else {
			return NetType.UNKNOWN;
		}
	}

	@Override
	public int getBlockSize() {
		if (is2GNet()) {
			// 2G下一次请求8K
			return 8 * 1024;
		}
		return 32 * 1024;
	}

	@Override
	public int getTaskNum() {
		return 2;
	}

	@Override
	public boolean isCmwap() {
		Context context = BaseApplication.getInstance();
		return NetWorkUtil.isCmwap(context);
	}

	@Override
	public long getRefreshInterval() {
		return 500;
	}

	@Override
	public boolean isRange() {
		return true;
	}

	@Override
	public boolean isBlock() {
		return false;
	}

	@Override
	public int getBufferBlockNum() {
		return 2;
	}

	@Override
	public boolean isNetworkAvalid() {
		Context context = BaseApplication.getInstance();
		return NetWorkUtil.isNetworkAvailable(context);
	}

	private boolean is2GNet() {
		return getNetType() == NetType.G2;
	}

	@Override
	public Hashtable<String, String> getRequestHeaders() {
		Hashtable<String, String> headers = new Hashtable<String, String>();
		// headers.put("User-Agent", KGHttpClient.getUserAgent() +
		// "/downloader_"
		// + Build.VSERSION_NAME);
		// User user = UserManager.getUser();
		// if (user.getSessionId() != null && user.getUserId() != 0) {
		// headers.put("X-Session-Key", user.getSessionId());
		// headers.put("X-User-Key", String.valueOf(user.getUserId()));
		// }
		// headers.put("X-API-Key",
		// StringUtil.getAppVersionKey(CommunityApplication.getInstance()));
		return headers;
	}

	@Override
	public boolean is2GNeedToForceBlock() {
		return false;
	}

}
