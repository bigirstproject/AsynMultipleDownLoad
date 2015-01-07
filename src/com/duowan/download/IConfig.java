package com.duowan.download;

import java.util.Hashtable;

public abstract interface IConfig {
	public abstract int getBlockSize();

	public abstract int getTaskNum();

	public abstract boolean isRange();

	public abstract boolean isBlock();

	public abstract NetType getNetType();

	public abstract boolean isCmwap();

	public abstract long getRefreshInterval();

	public abstract int getBufferBlockNum();

	public abstract boolean isNetworkAvalid();

	public abstract Hashtable<String, String> getRequestHeaders();

	public abstract boolean is2GNeedToForceBlock();

	public abstract int maxLoadingNums();
}