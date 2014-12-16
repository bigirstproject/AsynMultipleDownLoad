package com.duowan.download;

class DataBlock {
	public static final int FREE = 0;
	public static final int DOWNLOADING = 1;
	public static final int FINISH = 2;
	private long blockIndex;
	private long start;
	private long end;
	private int state;

	public long getBlockIndex() {
		return this.blockIndex;
	}

	public void setBlockIndex(long blockIndex) {
		this.blockIndex = blockIndex;
	}

	public long getStart() {
		return this.start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return this.end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}
}