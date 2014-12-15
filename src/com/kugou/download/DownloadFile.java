package com.kugou.download;

public class DownloadFile implements IFileColumns {
	protected long id;
	protected String resUrl;
	protected String fileName;
	protected String filePath;
	protected long haveRead;
	protected long fileSize;
	protected String mimeType;
	protected int state;
	protected String key;
	protected int classId;
	protected String ext1;
	protected String ext2;
	protected String ext3;
	protected String ext4;
	protected String ext5;
	protected Statistics statis;

	public DownloadFile() {
		this.statis = new Statistics();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

	public synchronized long getHaveRead() {
		return this.haveRead;
	}

	synchronized void addHaveRead(long read) {
		this.haveRead += read;
	}

	synchronized void resetHaveRead() {
		this.haveRead = 0L;
	}

	synchronized void reduceHaveRead(long read) {
		this.haveRead -= read;
	}

	public synchronized void setHaveRead(long haveRead) {
		this.haveRead = haveRead;
	}

	public long getFileSize() {
		return ((this.fileSize != 0L) ? this.fileSize : -1L);
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

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

	public String getExt1() {
		return this.ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return this.ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return this.ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public Statistics getStatis() {
		return this.statis;
	}

	public void setStatis(Statistics statis) {
		this.statis = statis;
	}

	public int hashCode() {
		return (int) this.id;
	}

	public boolean equals(Object obj) {
		if (obj instanceof DownloadFile) {
			DownloadFile file = (DownloadFile) obj;
			return file.getKey().equalsIgnoreCase(this.key);
		}
		return super.equals(obj);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" DownloadFile-->");

		sb.append(" id=" + this.id);

		sb.append(" fileName=" + this.fileName);
		sb.append(" filePath=" + this.filePath);
		sb.append(" haveRead=" + this.haveRead);
		sb.append(" fileSize=" + this.fileSize);
		sb.append(" mimeType=" + this.mimeType);
		sb.append(" classId=" + this.classId);
		sb.append(" state=" + this.state);
		sb.append(" ext1=" + this.ext1);
		sb.append(" ext2=" + this.ext2);
		sb.append(" ext3=" + this.ext3);
		sb.append(" statis=\"" + this.statis.toString() + "\"");
		return sb.toString();
	}
}