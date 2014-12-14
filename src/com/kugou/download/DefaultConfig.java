/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.kugou.download;

import java.util.Hashtable;

public class DefaultConfig implements IConfig {
	private final int DEFAULT_BLOCK_SIZE = 102400;

	private final int DEFAULT_DOWNLOADTASK_NUM = 2;

	private final int DEFAULT_REFRESH_INTERVAL = 300;

	public int getBlockSize() {
		return 102400;
	}

	public int getTaskNum() {
		return 2;
	}

	public boolean isRange() {
		return true;
	}

	public boolean isBlock() {
		return false;
	}

	public NetType getNetType() {
		return NetType.WIFI;
	}

	public boolean isCmwap() {
		return false;
	}

	public long getRefreshInterval() {
		return 300L;
	}

	public int getBufferBlockNum() {
		return getTaskNum();
	}

	public boolean isNetworkAvalid() {
		return true;
	}

	public Hashtable<String, String> getRequestHeaders() {
		return null;
	}

	public boolean is2GNeedToForceBlock() {
		return true;
	}
}