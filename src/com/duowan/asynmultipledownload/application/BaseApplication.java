package com.duowan.asynmultipledownload.application;

import java.util.HashMap;
import java.util.Hashtable;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * ����:ȫ��Application
 * 
 */
public abstract class BaseApplication extends Application {

	private static BaseApplication mApplication;

	// Ӧ��ȫ�ֱ����洢������
	private static Hashtable<String, Object> mAppParamsHolder;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		mAppParamsHolder = new Hashtable<String, Object>();
	}

	/**
	 * ��ȡApplicationʵ��
	 * 
	 * @return
	 */
	public static BaseApplication getInstance() {
		if (mApplication == null) {
			throw new IllegalStateException("Application is not created.");
		}
		return mApplication;
	}

	/**
	 * �洢ȫ������
	 * 
	 * @param key
	 * @param value
	 */
	public void putValue(String key, Object value) {
		mAppParamsHolder.put(key, value);
	}

	/**
	 * ��ȡȫ������
	 * 
	 * @param key
	 * @return
	 */
	public Object getValue(String key) {
		return mAppParamsHolder.get(key);
	}

	/**
	 * �Ƿ��Ѵ��
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return mAppParamsHolder.containsKey(key);
	}

	/**
	 * ��ȡApp��װ����Ϣ
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	private HashMap<String, Long> categoryTime = new HashMap<String, Long>();

	public Long getCategoryTime(String key) {
		return categoryTime.get(key) == null ? 0 : categoryTime.get(key);
	}

	public void putCategoryTime(String key, long value) {
		categoryTime.put(key, value);
	}

}
