package com.duowan.asynmultipledownload;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
public class DuowanProvider extends BaseContentProvider {

//	public static final String AUTHORITY = "duowandb";
//
//	public static final String CONTENT_AUTHORITY_SLASH = "content://"
//			+ AUTHORITY + "/";

	private static final UriMatcher sUriMatcher;

	private static final int DOWNLOADS = 1;

	private static final int DOWNLOAD_ID = 2;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, "downloads", DOWNLOADS);
		sUriMatcher.addURI(AUTHORITY, "downloads/#", DOWNLOAD_ID);
	}

	/**
	 * 数据库名称
	 */
	public static final String DATABASE_NAME = "duowan.db";

	/**
	 * 数据库版本号
	 */
	public static final int DATABASE_VERSION = 1;

	@Override
	protected String getDatabaseName() {
		return DATABASE_NAME;
	}

	@Override
	protected int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	@Override
	protected void onDatabaseCreate(SQLiteDatabase db) {
		// *****数据库初始化完毕，开始创建表*****//

		// 创建下载表
		db.execSQL(DownloadTable.CREATE_TABLE_SQL);
	}

	@Override
	protected void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		// *****数据库升级逻辑*****//
		if (oldVersion == 1 && newVersion == 2) {
			// 创建搜索历史列表
			// db.execSQL(SearchHistoryTable.CREATE_TABLE_SQL);
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case DOWNLOADS:
			return DownloadTable.CONTENT_TYPE;
		case DOWNLOAD_ID:
			return DownloadTable.ENTRY_CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String orderBy = null;
		String table = null;
		int matcher = sUriMatcher.match(uri);
		switch (matcher) {
		case DOWNLOADS:
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = DownloadTable.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}
			table = DownloadTable.TABLE_NAME;
			break;
		case DOWNLOAD_ID:
			if (TextUtils.isEmpty(selection)) {
				selection = DownloadTable._ID + "=" + ContentUris.parseId(uri);
			} else {
				selection = selection + " AND " + DownloadTable._ID + "="
						+ ContentUris.parseId(uri);
			}
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = DownloadTable.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}
			table = DownloadTable.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri
					+ " matcher" + matcher);
		}
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int matcher = sUriMatcher.match(uri);
		ContentValues initValues = values != null ? new ContentValues(values)
				: new ContentValues();
		SQLiteDatabase db = getWritableDatabase();
		String tableName;
		long rowId;
		switch (matcher) {
		case DOWNLOADS:
			tableName = DownloadTable.TABLE_NAME;
			rowId = db.insert(tableName, null, initValues);
			if (rowId > 0) {
				Uri songUri = ContentUris.withAppendedId(
						DownloadTable.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(songUri, null);
				return songUri;
			}
			break;
		default:
			break;
		}
		throw new IllegalArgumentException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int matcher = sUriMatcher.match(uri);
		SQLiteDatabase db = getWritableDatabase();
		int count = 0;
		switch (matcher) {
		case DOWNLOADS:
			count = db.delete(DownloadTable.TABLE_NAME, selection,
					selectionArgs);
			break;
		case DOWNLOAD_ID:
			count = db.delete(DownloadTable.TABLE_NAME, DownloadTable._ID
					+ "="
					+ ContentUris.parseId(uri)
					+ (TextUtils.isEmpty(selection) ? ""
							: (" AND " + selection)), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri
					+ " Matcher : " + matcher);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int count = 0;
		ContentValues initValues = values == null ? new ContentValues()
				: new ContentValues(values);
		switch (sUriMatcher.match(uri)) {
		case DOWNLOADS:
			count = db.update(DownloadTable.TABLE_NAME, initValues, selection,
					selectionArgs);
			break;
		case DOWNLOAD_ID:
			count = db.update(DownloadTable.TABLE_NAME, initValues,
					DownloadTable._ID
							+ "="
							+ ContentUris.parseId(uri)
							+ (TextUtils.isEmpty(selection) ? ""
									: (" AND " + selection)), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
