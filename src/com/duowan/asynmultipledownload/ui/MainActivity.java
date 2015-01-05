package com.duowan.asynmultipledownload.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.duowan.asynmultipledownload.DownloadOperator;
import com.duowan.asynmultipledownload.R;
import com.duowan.download.DownloadFile;
import com.duowan.util.FileUtil;
import com.duowan.util.LogCat;

public class MainActivity extends Activity {

	private ListView mListView;
	private DownLoadBaseAdapter mBaseAdapter;
	private ArrayList<DownLoadParcel> mList;

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
		mList = new ArrayList<DownLoadParcel>();
		for (int i = 0; i < DownLoadUrl.url.length; i++) {
			DownLoadParcel parcel = new DownLoadParcel();
			parcel.setId(i + 1);
			parcel.setKey(DownLoadUrl.url[i]);
			parcel.setUrl(DownLoadUrl.url[i]);
			parcel.setFilePath(DownLoadUrl.getFilePath(DownLoadUrl.url[i]));
			mList.add(parcel);
		}
		mBaseAdapter.setData(mList);
		new MyAsyTask().execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(500);
					mBaseAdapter.registerCallback();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBaseAdapter.removeCallback();
		LogCat.d("MainActivity onDestroy() ");
		// AppControler.getInstance().exit();
	}

	class MyAsyTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			List<DownloadFile> downloads = DownloadOperator.getInstance()
					.query(null, null, null);
			if (downloads == null || downloads.size() == 0 || mList == null
					|| mList.size() == 0) {
				return false;
			}
			for (int j = 0; j < mList.size(); j++) {
				String key = mList.get(j).getKey();
				for (int i = downloads.size() - 1; i >= 0; i--) {
					DownloadFile downloadFile = downloads.get(i);
					if (downloadFile.getKey().equals(key)) {
						if (FileUtil.fileIsExist(downloadFile.getFilePath())) {
							mList.get(j).setDownStatus(downloadFile.getState());
							mList.get(j)
									.setProgress(
											(int) ((downloadFile.getHaveRead() * 100) / downloadFile
													.getFileSize()));
						} else {
							mList.get(j).setDownStatus(DownLoadParcel.START);
							mList.get(j).setProgress(0);
						}
						downloads.remove(downloadFile);
						break;
					}
				}
			}
			downloads = null;
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				mBaseAdapter.notifyDataSetChanged();
			}
		}
	}

}
