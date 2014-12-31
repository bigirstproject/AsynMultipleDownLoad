package com.duowan.asynmultipledownload.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.duowan.asynmultipledownload.AppControler;
import com.duowan.asynmultipledownload.R;

public class MainActivity extends Activity {

	private ListView mListView;
	private DownLoadBaseAdapter mBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// AppControler.getInstance().init();
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(R.id.listView1);
		mBaseAdapter = new DownLoadBaseAdapter(this);
		mListView.setAdapter(mBaseAdapter);
		addData();
	}

	private void addData() {
		ArrayList<DownLoadParcel> list = new ArrayList<DownLoadParcel>();
		for (int i = 0; i < DownLoadUrl.url.length; i++) {
			DownLoadParcel parcel = new DownLoadParcel();
			parcel.setId(i+1);
			parcel.setUrl(DownLoadUrl.url[i]);
			parcel.setFilePath(DownLoadUrl
					.getFilePath(DownLoadUrl.url[i]));
			list.add(parcel);
		}
		mBaseAdapter.setData(list);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppControler.getInstance().exit();
	}
}
