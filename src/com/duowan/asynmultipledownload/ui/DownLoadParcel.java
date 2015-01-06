package com.duowan.asynmultipledownload.ui;

import java.io.Serializable;

public class DownLoadParcel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332115500142060372L;

	public static final int START = 0;
	public static final int PREPAREING = 1;// 准备中
	public static final int READY = 2;// 等待中
	public static final int DOWNING = 3;// 下载中
	public static final int INTERRUPT =4;// 中断
	public static final int CONTINUE = 7;// 继续
	public static final int FINISH  = 5;// 完成（成功）
	public static final int INTERUPTING  = 6;// 暂停中
	
	private int id;
	private String url;
	private String name;
	private String key;
	private String filePath;
	private int progress;
	private int downStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getDownStatus() {
		return downStatus;
	}

	public void setDownStatus(int downStatus) {
		this.downStatus = downStatus;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
