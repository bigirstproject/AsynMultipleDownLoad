package com.duowan.asynmultipledownload.operator;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.duowan.asynmultipledownload.application.BaseApplication;
import com.duowan.asynmultipledownload.bean.DownloadTable;
import com.duowan.download.DownloadFile;
import com.duowan.util.LogCat;

/**
 * 下载表操作类
 * 
 */
public class DownloadOperator extends BaseDbOperator<DownloadFile> {

	private static DownloadOperator mDownloadOperator;

	private DownloadOperator(Context context) {
		super(context);
	}

	public static synchronized DownloadOperator getInstance() {
		if (mDownloadOperator == null) {
			Context context = BaseApplication.getInstance();
			if (context == null) {
				throw new IllegalArgumentException("context is null!");
			}
			mDownloadOperator = new DownloadOperator(context);
		}
		LogCat.d("create  database  object : time  = " + System.currentTimeMillis());
		return mDownloadOperator;
	}

	@Override
	public long insert(DownloadFile file) {
		ContentValues cv = new ContentValues();
		cv.put(DownloadTable.FILE_PATH, file.getFilePath());
		cv.put(DownloadTable.FILE_NAME, file.getFileName());
		cv.put(DownloadTable.FILE_SIZE, file.getFileSize());
		cv.put(DownloadTable.MIME_TYPE, file.getMimeType());
		cv.put(DownloadTable.HAVE_READ, file.getHaveRead());
		cv.put(DownloadTable.STATE, file.getState());
		cv.put(DownloadTable.KEY, file.getKey());
		cv.put(DownloadTable.CLASSID, file.getClassId());
		cv.put(DownloadTable.EXT1, file.getExt1());
		cv.put(DownloadTable.EXT2, file.getExt2());
		cv.put(DownloadTable.EXT3, file.getExt3());
		cv.put(DownloadTable.EXT4, file.getExt4());
		cv.put(DownloadTable.EXT5, file.getExt5());
		Uri uri = mContext.getContentResolver().insert(
				DownloadTable.CONTENT_URI, cv);
		if (uri != null) {
			return ContentUris.parseId(uri);
		}
		return -1;
	}

	@Override
	public long update(ContentValues cv, String selection,
			String[] selectionArgs) {
		return mContext.getContentResolver().update(DownloadTable.CONTENT_URI,
				cv, selection, selectionArgs);
	}

	@Override
	public long delete(String selection, String[] selectionArgs) {
		return mContext.getContentResolver().delete(DownloadTable.CONTENT_URI,
				selection, selectionArgs);
	}

	@Override
	public List<DownloadFile> query(String selection, String[] selectionArgs,
			String orderby) {
		return query(selection, selectionArgs, orderby, 0);
	}

	@Override
	public List<DownloadFile> query(String selection, String[] selectionArgs,
			String orderby, int limit) {
		Uri uri = DownloadTable.CONTENT_URI;
		// if (limit > 0) {
		// uri = uri.buildUpon().appendQueryParameter("limit", "" +
		// limit).build();
		// }
		Cursor c = mContext.getContentResolver().query(uri, null, selection,
				selectionArgs, orderby);
		return getDownloadFileFromCursor(c);
	}

	@Override
	public DownloadFile query(String selection, String[] selectionArgs) {
		List<DownloadFile> files = query(selection, selectionArgs, null);
		if (files != null && files.size() > 0) {
			return files.get(0);
		}
		return null;
	}

	@Override
	public int getCount(String selection, String[] selectionArgs) {
		String[] projection = { " count(*) " };
		Cursor c = mContext.getContentResolver().query(
				DownloadTable.CONTENT_URI, projection, selection,
				selectionArgs, DownloadTable.DEFAULT_SORT_ORDER);
		int count = 0;
		if (c != null) {
			if (c.moveToFirst()) {
				count = c.getInt(0);
			}
			c.close();
		}
		return count;
	}

	/**
	 * 获取下载文件信息
	 * 
	 * @param c
	 * @return
	 */
	public static List<DownloadFile> getDownloadFileFromCursor(Cursor c) {
		if (c != null) {
			List<DownloadFile> files = new ArrayList<DownloadFile>(c.getCount());
			DownloadFile file = null;
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				file = new DownloadFile();
				file.setId(c.getLong(c.getColumnIndexOrThrow(DownloadTable._ID)));
				file.setFilePath(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.FILE_PATH)));
				file.setFileName(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.FILE_NAME)));
				file.setFileSize(c.getLong(c
						.getColumnIndexOrThrow(DownloadTable.FILE_SIZE)));
				file.setHaveRead(c.getLong(c
						.getColumnIndexOrThrow(DownloadTable.HAVE_READ)));
				file.setMimeType(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.MIME_TYPE)));
				file.setState(c.getInt(c
						.getColumnIndexOrThrow(DownloadTable.STATE)));
				file.setKey(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.KEY)));
				file.setClassId(c.getInt(c
						.getColumnIndexOrThrow(DownloadTable.CLASSID)));
				file.setExt1(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.EXT1)));
				file.setExt2(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.EXT2)));
				file.setExt3(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.EXT3)));
				file.setExt4(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.EXT4)));
				file.setExt5(c.getString(c
						.getColumnIndexOrThrow(DownloadTable.EXT5)));
				files.add(file);
			}
			c.close();
			return files;
		}
		return null;
	}

	public long update(DownloadFile file, String key) {
		if (key != null) {
			String selection = DownloadTable.KEY + " = ? ";
			String[] selectionArgs = { key };
			ContentValues cv = new ContentValues();
			cv.put(DownloadTable.FILE_PATH, file.getFilePath());
			cv.put(DownloadTable.FILE_NAME, file.getFileName());
			cv.put(DownloadTable.FILE_SIZE, file.getFileSize());
			cv.put(DownloadTable.MIME_TYPE, file.getMimeType());
			cv.put(DownloadTable.HAVE_READ, file.getHaveRead());
			cv.put(DownloadTable.STATE, file.getState());
			cv.put(DownloadTable.CLASSID, file.getClassId());
			cv.put(DownloadTable.EXT1, file.getExt1());
			cv.put(DownloadTable.EXT2, file.getExt2());
			cv.put(DownloadTable.EXT3, file.getExt3());
			cv.put(DownloadTable.EXT4, file.getExt4());
			cv.put(DownloadTable.EXT5, file.getExt5());
			return update(cv, selection, selectionArgs);
		}
		return -1;

	}

	public long update(DownloadFile file, long id) {
		String selection = DownloadTable._ID + " = ? ";
		String[] selectionArgs = { String.valueOf(id) };
		ContentValues cv = new ContentValues();
		cv.put(DownloadTable.FILE_PATH, file.getFilePath());
		cv.put(DownloadTable.FILE_NAME, file.getFileName());
		cv.put(DownloadTable.FILE_SIZE, file.getFileSize());
		cv.put(DownloadTable.MIME_TYPE, file.getMimeType());
		cv.put(DownloadTable.HAVE_READ, file.getHaveRead());
		cv.put(DownloadTable.STATE, file.getState());
		cv.put(DownloadTable.CLASSID, file.getClassId());
		cv.put(DownloadTable.EXT1, file.getExt1());
		cv.put(DownloadTable.EXT2, file.getExt2());
		cv.put(DownloadTable.EXT3, file.getExt3());
		cv.put(DownloadTable.EXT4, file.getExt4());
		cv.put(DownloadTable.EXT5, file.getExt5());
		return update(cv, selection, selectionArgs);
	}

	public long delete(String key) {
		if (key != null) {
			String selection = DownloadTable.KEY + " = ? ";
			String[] selectionArgs = { key };
			return delete(selection, selectionArgs);
		}
		return -1;
	}

	public long delete(long id) {
		String selection = DownloadTable._ID + " = ? ";
		String[] selectionArgs = { String.valueOf(id) };
		return delete(selection, selectionArgs);
	}

	public DownloadFile query(String key) {
		if (key != null) {
			String selection = DownloadTable.KEY + " = ? ";
			String[] selectionArgs = { key };
			return query(selection, selectionArgs);
		}
		return null;

	}

	public DownloadFile query(long id) {
		String selection = DownloadTable._ID + " = ? ";
		String[] selectionArgs = { String.valueOf(id) };
		return query(selection, selectionArgs);
	}

}
