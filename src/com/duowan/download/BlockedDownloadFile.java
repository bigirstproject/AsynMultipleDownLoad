package com.duowan.download;

public class BlockedDownloadFile extends DownloadFile {
	private int blockNum;
	private int blockSize;
	private int bufferedIndex = -1;
	private DataBlock[] dataBlocks;

	public int getBlockNum() {
		return this.blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	public int getBlockSize() {
		return this.blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getBufferedIndex() {
		return this.bufferedIndex;
	}

	public void setBufferedIndex(int bufferedIndex) {
		this.bufferedIndex = bufferedIndex;
	}

	public DataBlock[] getDataBlocks() {
		return this.dataBlocks;
	}

	void splitBlocks() {
		this.blockNum = (int) ((this.fileSize + this.blockSize - 1L) / this.blockSize);
		this.dataBlocks = new DataBlock[this.blockNum];
		for (int i = 0; i < this.blockNum; ++i) {
			this.dataBlocks[i] = new DataBlock();
			this.dataBlocks[i].setBlockIndex(i);
			this.dataBlocks[i].setStart(i * this.blockSize);
			this.dataBlocks[i].setEnd((i + 1) * this.blockSize - 1);
			this.dataBlocks[i].setState(0);
		}
		this.dataBlocks[(this.blockNum - 1)].setEnd(this.fileSize - 1L);
	}

	void splitBlocks(int bufferedIndex) {
		this.blockNum = (int) ((this.fileSize + this.blockSize - 1L) / this.blockSize);
		this.dataBlocks = new DataBlock[this.blockNum];
		for (int i = 0; i < this.blockNum; ++i) {
			this.dataBlocks[i] = new DataBlock();
			this.dataBlocks[i].setBlockIndex(i);
			this.dataBlocks[i].setStart(i * this.blockSize);
			this.dataBlocks[i].setEnd((i + 1) * this.blockSize - 1);
			this.dataBlocks[i].setState((i <= bufferedIndex) ? 2 : 0);
		}
		this.dataBlocks[(this.blockNum - 1)].setEnd(this.fileSize - 1L);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(" blockNum=" + this.blockNum);
		sb.append(" blockSize=" + this.blockSize);
		sb.append(" bufferedIndex=" + this.bufferedIndex);
		return sb.toString();
	}
}