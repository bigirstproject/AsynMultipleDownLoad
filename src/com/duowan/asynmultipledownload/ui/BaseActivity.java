package com.duowan.asynmultipledownload.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * ���� �߱�UI�̸߳���
 * 
 */
public class BaseActivity extends FragmentActivity {

	private Toast mToast;

	protected Handler mUiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handleUiMessage(msg);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * �������UI����
	 * 
	 * @param msg
	 */
	protected void handleUiMessage(Message msg) {
	}

	/**
	 * ����UI���²���
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	/**
	 * ����UI���²���
	 * 
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	/**
	 * ��ʾһ��Toast���͵���Ϣ
	 * 
	 * @param msg
	 *            ��ʾ����Ϣ
	 */
	public void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * ��ʾ{@link Toast}֪ͨ
	 * 
	 * @param strResId
	 *            �ַ�����Դid
	 */
	public void showToast(int strResId) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		}
		mToast.setText(strResId);
		mToast.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	/**
	 * ���������
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (BaseActivity.this.getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * ��������̵���ʾ����
	 */
	protected void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
}
