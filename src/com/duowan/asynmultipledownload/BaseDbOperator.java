package com.duowan.asynmultipledownload;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

/**
 * ���ݿ�������
 * 
 */
public abstract class BaseDbOperator<T extends Object> {

	protected Context mContext;

	public BaseDbOperator(Context context) {
		mContext = context;
	}

	/**
	 * ����
	 * 
	 * @param t
	 * @return
	 */
	public abstract long insert(T t);

	/**
	 * ����
	 * 
	 * @param t
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public abstract long update(ContentValues cv, String selection,
			String[] selectionArgs);

	/**
	 * ɾ��
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public abstract long delete(String selection, String[] selectionArgs);

	/**
	 * ��ѯ
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @param orderby
	 * @return
	 */
	public abstract List<T> query(String selection, String[] selectionArgs,
			String orderby);

	/**
	 * ��ѯ
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @param orderby
	 * @param limit
	 * @return
	 */
	public abstract List<T> query(String selection, String[] selectionArgs,
			String orderby, int limit);

	/**
	 * ��ѯ
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public abstract T query(String selection, String[] selectionArgs);

	/**
	 * ��ȡ����
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public abstract int getCount(String selection, String[] selectionArgs);
}
