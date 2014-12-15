package com.duowan.asynmultipledownload;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.duowan.util.ToastShowUtil;

public class MainActivity extends Activity implements OnClickListener {

	Button mStartDownLoad;
	Button mStopDownLoad;
	UpdateApp instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//AppControler.getInstance().init();
		setContentView(R.layout.activity_main);
		mStartDownLoad = (Button) findViewById(R.id.down_load_start);
		mStopDownLoad = (Button) findViewById(R.id.down_load_stop);
		mStartDownLoad.setOnClickListener(this);
		mStopDownLoad.setOnClickListener(this);
		instance = UpdateApp.getInstance();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.down_load_start:
			ToastShowUtil.showMsgShort(this,
					getResources().getString(R.string.down_load_start));
			new Thread() {
				public void run() {
					instance.startDownload();
				};
			}.start();
			break;
		case R.id.down_load_stop:
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
