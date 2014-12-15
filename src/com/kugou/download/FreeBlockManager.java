package com.kugou.download;

final class FreeBlockManager {
	public static synchronized int getFreeBlockIndex(BlockedDownloadFile file) {
		int freeBlockIndex = -1;
		int blockNum = file.getBlockNum();
		int bufferedIndex = file.getBufferedIndex();
		DataBlock[] blocks = file.getDataBlocks();

		boolean isLinked = true;
		for (int i = bufferedIndex + 1; i < blockNum; ++i) {
			int state = blocks[i].getState();
			if (state == 0) {
				isLinked = false;
				freeBlockIndex = i;
				blocks[i].setState(1);
				break;
			}
			if ((state == 2) && (isLinked))
				file.setBufferedIndex(i);
			else if (state == 1) {
				isLinked = false;
			}
		}
		return freeBlockIndex;
	}
}