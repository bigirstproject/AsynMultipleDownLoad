/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.kugou.download;

import java.util.Hashtable;

final class ConfigWrapper
{
  private int blockSize;
  private int taskNum;
  private boolean isRange;
  private boolean isBlock;
  private NetType netType;
  private boolean cmwap;
  private long refreshInterval;
  private int bufferBlockNum;
  private static ConfigWrapper instance;
  private IConfig config;
  private Hashtable<String, String> requestHeaders;
  private boolean is2GNeedToForceBlock;

  public static synchronized ConfigWrapper getInstance()
  {
    if (instance == null) {
      instance = new ConfigWrapper();
    }
    return instance;
  }

  public int getBlockSize() {
    return this.blockSize;
  }

  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }

  public int getTaskNum() {
    return this.taskNum;
  }

  public void setTaskNum(int taskNum) {
    this.taskNum = taskNum;
  }

  public boolean isRange() {
    return this.isRange;
  }

  public void setRange(boolean isRange) {
    this.isRange = isRange;
  }

  public boolean isBlock() {
    return this.isBlock;
  }

  public void setBlock(boolean isBlock) {
    this.isBlock = isBlock;
  }

  public NetType getNetType() {
    return this.netType;
  }

  public void setNetType(NetType netType) {
    this.netType = netType;
  }

  public boolean isCmwap() {
    return this.cmwap;
  }

  public void setCmwap(boolean cmwap) {
    this.cmwap = cmwap;
  }

  public void setRefreshInterval(long refreshInterval) {
    this.refreshInterval = refreshInterval;
  }

  public long getRefreshInterval() {
    return this.refreshInterval;
  }

  public int getBufferBlockNum() {
    return this.bufferBlockNum;
  }

  public void setBufferBlockNum(int bufferBlockNum) {
    this.bufferBlockNum = bufferBlockNum;
  }

  public boolean isNetworkAvalid() {
    if (this.config != null) {
      return this.config.isNetworkAvalid();
    }
    return true;
  }

  public void setConfig(IConfig config)
  {
    this.config = config;
  }

  public Hashtable<String, String> getRequestHeaders() {
    return this.requestHeaders;
  }

  public void setRequestHeaders(Hashtable<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  public void setNeedToForceBlock(boolean is2GNeedToForceBlock) {
    this.is2GNeedToForceBlock = is2GNeedToForceBlock;
  }

  public boolean is2GNeedToForceBlock() {
    return this.is2GNeedToForceBlock;
  }
}