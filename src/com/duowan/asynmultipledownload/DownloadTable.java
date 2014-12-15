package com.duowan.asynmultipledownload;

import android.net.Uri;

/**
 * ����:���ر�ṹ
 * 
 */
public class DownloadTable implements AppBaseColumns {

	/**
	 * �ļ���
	 */
	public static final String FILE_NAME = "fileName";

	/**
	 * �ļ�·��
	 */
	public static final String FILE_PATH = "filePath";

	/**
	 * �����ش�С
	 */
	public static final String HAVE_READ = "haveRead";

	/**
	 * �ļ���С
	 */
	public static final String FILE_SIZE = "fileSize";

	/**
	 * �ļ�����
	 */
	public static final String MIME_TYPE = "mimeType";

	/**
	 * ����״̬
	 */
	public static final String STATE = "state";

	/**
	 * �ļ�Ψһ��ʶ��
	 */
	public static final String KEY = "key";

	/**
	 * ����ID
	 */
	public static final String CLASSID = "classid";

	/**
	 * ��չ�ֶ�1
	 */
	public static final String EXT1 = "ext1";

	/**
	 * ��չ�ֶ�2
	 */
	public static final String EXT2 = "ext2";

	/**
	 * ��չ�ֶ�3
	 */
	public static final String EXT3 = "ext3";

	/**
	 * ��չ�ֶ�4
	 */
	public static final String EXT4 = "ext4";

	/**
	 * ��չ�ֶ�5
	 */
	public static final String EXT5 = "ext5";

	public static final Uri CONTENT_URI = Uri
			.parse(BaseContentProvider.CONTENT_AUTHORITY_SLASH + "downloads");

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/duowandb.download";

	public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/duowandb.download";

	/**
	 * Ĭ�ϵ������ֶ�
	 */
	public static final String DEFAULT_SORT_ORDER = _ID;

	/**
	 * ������
	 */
	public static final String TABLE_NAME = "Download";

	/**
	 * �������
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
