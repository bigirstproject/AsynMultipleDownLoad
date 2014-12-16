package com.duowan.asynmultipledownload.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.duowan.asynmultipledownload.AppControler;
import com.duowan.asynmultipledownload.MyHandle;
import com.duowan.asynmultipledownload.R;
import com.duowan.asynmultipledownload.UpdateApp;
import com.duowan.util.ToastShowUtil;

public class MainActivity extends Activity implements OnClickListener {

	private Button mStartDownLoadOne;
	private Button mStopDownLoadOne;

	private Button mStartDownLoadTwo;
	private Button mStopDownLoadTwo;

	private Button mStartDownLoadThree;
	private Button mStopDownLoadThree;

	private TextView mTxtOne;
	private TextView mTxtTwo;
	private TextView mTxtThree;
	private UpdateApp instance;
	private MyHandle mHandle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// AppControler.getInstance().init();
		setContentView(R.layout.activity_main);
		mStartDownLoadOne = (Button) findViewById(R.id.down_load_start_one);
		mStopDownLoadOne = (Button) findViewById(R.id.down_load_stop_one);
		mStartDownLoadTwo = (Button) findViewById(R.id.down_load_start_two);
		mStopDownLoadTwo = (Button) findViewById(R.id.down_load_stop_two);
		mStartDownLoadThree = (Button) findViewById(R.id.down_load_start_three);
		mStopDownLoadThree = (Button) findViewById(R.id.down_load_stop_three);

		mTxtOne = (TextView) findViewById(R.id.down_load_txt_one);
		mTxtTwo = (TextView) findViewById(R.id.down_load_txt_two);
		mTxtThree = (TextView) findViewById(R.id.down_load_txt_three);
		mHandle = new MyHandle(this, mTxtOne, mTxtTwo, mTxtThree);
		mStartDownLoadOne.setOnClickListener(this);
		mStopDownLoadOne.setOnClickListener(this);
		mStartDownLoadTwo.setOnClickListener(this);
		mStopDownLoadTwo.setOnClickListener(this);
		mStartDownLoadThree.setOnClickListener(this);
		mStopDownLoadThree.setOnClickListener(this);
		instance = UpdateApp.getInstance();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.down_load_start_one:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_start));
			new Thread() {
				public void run() {
					instance.startDownload();
				};
			}.start();
			break;
		case R.id.down_load_stop_one:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_stop));
			instance.cancelDownload();
			break;

		case R.id.down_load_start_two:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_start));
			new Thread() {
				public void run() {
					instance.startDownload();
				};
			}.start();
			break;
		case R.id.down_load_stop_two:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_stop));
			instance.cancelDownload();
			break;

		case R.id.down_load_start_three:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_start));
			new Thread() {
				public void run() {
					instance.startDownload();
				};
			}.start();
			break;
		case R.id.down_load_stop_three:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_stop));
			instance.cancelDownload();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppControler.getInstance().exit();
	}
}
