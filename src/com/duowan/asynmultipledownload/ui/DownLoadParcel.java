package com.duowan.asynmultipledownload.ui;

import java.io.Serializable;

public class DownLoadParcel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332115500142060372L;

	private int id;
	private String url;
	private String name;
	private String filePath;
	private int progress;
	private boolean downStatus;

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

	public boolean isDownStatus() {
		return downStatus;
	}

	public void setDownStatus(boolean downStatus) {
		this.downStatus = downStatus;
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

}
