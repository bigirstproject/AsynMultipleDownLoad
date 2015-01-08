package com.duowan.asynmultipledownload.bean;

import java.io.Serializable;

public class DownloaderParcel implements Serializable {

	private static final long serialVersionUID = -3332115500142060372L;

	public static final int START = 0;
	public static final int PREPAREING = 1;// 准备中
	public static final int READY = 2;// 等待中
	public static final int DOWNING = 3;// 下载中
	public static final int INTERRUPT = 4;// 中断
	public static final int FINISH = 5;// 完成（成功）
	public static final int INTERUPTING = 6;// 暂停中
	public static final int CONTINUE = 7;// 继续

	private int id;
	private String url;
	private String name;
	private String key;
	private String filePath;
	private int progress;
	private int currentStatus;
	private long fileSize;
	private long haveRead;
	private int currentSpeed;

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

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long size) {
		this.fileSize = size;
	}

	public long getHaveRead() {
		return haveRead;
	}

	public void setHaveRead(long haveRead) {
		this.haveRead = haveRead;
	}

	public int getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

}
