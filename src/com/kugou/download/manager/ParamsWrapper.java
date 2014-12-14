package com.kugou.download.manager;

public class ParamsWrapper {
	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_AUDIO_FILE = 1;
	private String key;
	private int classId = 0;
	private String resUrl;
	private String fileName;
	private String filePath;
	private long fileSize;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getResUrl() {
		return this.resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int hashCode() {
		return this.classId;
	}

	public boolean equals(Object o) {
		if (o instanceof ParamsWrapper) {
			ParamsWrapper paramsWrapper = (ParamsWrapper) o;
			String inKey = paramsWrapper.getKey();
			boolean result = (inKey != null)
					&& (inKey.equalsIgnoreCase(this.key));
			return result;
		}
		return super.equals(o);
	}
}