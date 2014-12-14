package com.kugou.download;

import java.util.List;

public class DefaultOperator implements IOperator {
	public long insertFile(DownloadFile file) {
		return 1L;
	}

	public boolean updateFile(DownloadFile file) {
		return false;
	}

	public boolean deleteFile(long id) {
		return false;
	}

	public DownloadFile queryFile(long id) {
		return null;
	}

	public List<DownloadFile> queryFile(String selection, String[] selectionArgs) {
		return null;
	}

	public List<DownloadFile> queryFile(String selection,
			String[] selectionArgs, String orderby) {
		return null;
	}

	public int getCount(String selection, String[] selectionArgs) {
		return 0;
	}
}