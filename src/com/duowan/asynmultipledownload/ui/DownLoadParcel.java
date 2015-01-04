package com.duowan.asynmultipledownload.ui;

import java.io.Serializable;

public class DownLoadParcel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332115500142060372L;

	public static final int START = 0;
	public static final int DOWNING = 1;
	public static final int CONTINUE = 4;
	public static final int INTERRUPT =3;
	public static final int COMPLETE  = 5;
	
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
