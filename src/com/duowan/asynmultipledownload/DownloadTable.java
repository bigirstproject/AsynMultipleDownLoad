package com.duowan.asynmultipledownload;

import android.net.Uri;

/**
 * 描述:下载表结构
 * 
 */
public class DownloadTable implements AppBaseColumns {

	/**
	 * 文件名
	 */
	public static final String FILE_NAME = "fileName";

	/**
	 * 文件路径
	 */
	public static final String FILE_PATH = "filePath";

	/**
	 * 已下载大小
	 */
	public static final String HAVE_READ = "haveRead";

	/**
	 * 文件大小
	 */
	public static final String FILE_SIZE = "fileSize";

	/**
	 * 文件类型
	 */
	public static final String MIME_TYPE = "mimeType";

	/**
	 * 下载状态
	 */
	public static final String STATE = "state";

	/**
	 * 文件唯一标识符
	 */
	public static final String KEY = "key";

	/**
	 * 分类ID
	 */
	public static final String CLASSID = "classid";

	/**
	 * 扩展字段1
	 */
	public static final String EXT1 = "ext1";

	/**
	 * 扩展字段2
	 */
	public static final String EXT2 = "ext2";

	/**
	 * 扩展字段3
	 */
	public static final String EXT3 = "ext3";

	/**
	 * 扩展字段4
	 */
	public static final String EXT4 = "ext4";

	/**
	 * 扩展字段5
	 */
	public static final String EXT5 = "ext5";

	public static final Uri CONTENT_URI = Uri
			.parse(BaseContentProvider.CONTENT_AUTHORITY_SLASH + "downloads");

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/duowandb.download";

	public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/duowandb.download";

	/**
	 * 默认的排序字段
	 */
	public static final String DEFAULT_SORT_ORDER = _ID;

	/**
	 * 表名称
	 */
	public static final String TABLE_NAME = "Download";

	/**
	 * 创建语句
	 */
	public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + DownloadTable._ID + " INTEGER PRIMARY KEY,"
			+ DownloadTable.KEY + " TEXT," + DownloadTable.FILE_PATH + " TEXT,"
			+ DownloadTable.FILE_NAME + " TEXT," + DownloadTable.FILE_SIZE
			+ " INTEGER," + DownloadTable.HAVE_READ + " INTEGER,"
			+ DownloadTable.MIME_TYPE + " TEXT," + DownloadTable.STATE
			+ " INTEGER," + DownloadTable.CLASSID + " INTEGER,"
			+ DownloadTable.EXT1 + " TEXT," + DownloadTable.EXT2 + " TEXT,"
			+ DownloadTable.EXT3 + " TEXT," + DownloadTable.EXT4 + " TEXT,"
			+ DownloadTable.EXT5 + " TEXT," + DownloadTable.CREATE_AT
			+ " INTEGER," + DownloadTable.MODIFIED_AT + " INTEGER);";
}
