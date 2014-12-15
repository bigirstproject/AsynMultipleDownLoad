package com.duowan.asynmultipledownload;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button mStartDownLoad;
	Button mStopDownLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mStartDownLoad = (Button) findViewById(R.id.down_load_start);
		mStopDownLoad = (Button) findViewById(R.id.down_load_stop);
		mStartDownLoad.setOnClickListener(this);
		mStopDownLoad.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.down_load_start:

			break;
		case R.id.down_load_stop:

			break;
		default:
			break;
		}
	}

}
