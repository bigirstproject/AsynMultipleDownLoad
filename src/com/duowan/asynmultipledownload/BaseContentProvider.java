
package com.duowan.asynmultipledownload;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ����:����ContentProvider��ʵ�������ݿⴴ������
 * 
 */
public abstract class BaseContentProvider extends ContentProvider {

    public static String AUTHORITY = "duowandb";

    public static String CONTENT_AUTHORITY_SLASH = "content://" + AUTHORITY + "/";

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return mDatabaseHelper != null;
    }

    /**
     * ��ȡ�ɶ����ݿ�
     * 
     * @return
     */
    protected SQLiteDatabase getReadableDatabase() {
        return mDatabaseHelper.getReadableDatabase();
    }

    /**
     * ��ȡ�ɶ�д���ݿ�
     * 
     * @return
     */
    protected SQLiteDatabase getWritableDatabase() {
        return mDatabaseHelper.getWritableDatabase();
    }

    /**
     * �������ݿ�����
     * 
     * @return
     */
    protected abstract String getDatabaseName();

    /**
     * �������ݿ�汾��
     * 
     * @return
     */
    protected abstract int getDatabaseVersion();

    /**
     * ���ݿⴴ���ɹ�������������ص���ȥ�������ݱ�
     */
    protected abstract void onDatabaseCreate(SQLiteDatabase db);

    /**
     * ���ݿ�����
     * 
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    protected abstract void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BaseContentProvider.this.getDatabaseName(), null, getDatabaseVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            onDatabaseCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onDatabaseUpgrade(db, oldVersion, newVersion);
        }

        // 4.0����ϵͳ�����ݿ�Ӹ߽�����ʱ����ǿ���׳��쳣��ͨ����д������������Խ������
        @SuppressLint("Override")
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // ��Ȼû���õ�����Ҫ����������
        }

    }

}
