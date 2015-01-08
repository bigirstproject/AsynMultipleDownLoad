package com.duowan.asynmultipledownload.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.duowan.asynmultipledownload.R;
import com.duowan.asynmultipledownload.bean.DownloaderParcel;
import com.duowan.asynmultipledownload.bean.UrlWrapper;
import com.duowan.asynmultipledownload.downTools.AppControler;
import com.duowan.asynmultipledownload.downTools.DownloadServiceUtil;
import com.duowan.asynmultipledownload.operator.DownloadOperator;
import com.duowan.download.DownloadFile;
import com.duowan.download.FileDownloader;
import com.duowan.util.FileUtil;
import com.duowan.util.LogCat;

public class MainActivity extends BaseWorkerActivity {

	private final int UPDATE_DOWNLOAD_PROGRESS = 0x10001;
	private final int ERROR_DOWNLOAD = 0x10002;
	private final int REQUEST_DATABASE_CODE = 0x10003;
	private final int RESPONSE_REFRESH_CODE = 0x10004;

	private ListView mListView;
	private ProgressBar progressBar;
	private FileDownLoadBaseAdapter mBaseAdapter;
	private ArrayList<DownloaderParcel> mList;
	private FileDownloadProCallBackListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppControler.getInstance().init();
		setContentView(R.layout.activity_main);
		listener = new FileDownloadProCallBackListener(mBackgroundHandler);
		DownloadServiceUtil.registerCallback(listener);
		mListView = (ListView) findViewById(R.id.listView1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		mBaseAdapter = new FileDownLoadBaseAdapter(this);
		mListView.setAdapter(mBaseAdapter);
		initData();
		showViewLoading();
		sendEmptyBackgroundMessage(REQUEST_DATABASE_CODE);
	}

	private void initData() {
		mList = new ArrayList<DownloaderParcel>();
		for (int i = 0; i < UrlWrapper.url.length; i++) {
			DownloaderParcel parcel = new DownloaderParcel();
			parcel.setId(i + 1);
			parcel.setKey(UrlWrapper.url[i]);
			parcel.setUrl(UrlWrapper.url[i]);
			parcel.setFilePath(UrlWrapper.getFilePath(UrlWrapper.url[i]));
			mList.add(parcel);
		}
		mBaseAdapter.setData(mList);
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		ArrayList<DownloaderParcel> dataList = mBaseAdapter.getData();
		switch (msg.what) { 
		case REQUEST_DATABASE_CODE:
			// 从本地数据库中获取数据
			List<DownloadFile> downloads = DownloadOperator.getInstance()
					.query(null, null, null);
			if (downloads == null || downloads.size() == 0 || dataList == null
					|| dataList.size() == 0) {
				sendEmptyUiMessage(RESPONSE_REFRESH_CODE);
				return;
			}
			for (int j = 0; j < dataList.size(); j++) {
				DownloaderParcel parcelItem = dataList.get(j);
				String key = parcelItem.getKey();
				for (int i = downloads.size() - 1; i >= 0; i--) {
					DownloadFile downloadFile = downloads.get(i);
					if (downloadFile.getKey().equals(key)) {
						if (FileUtil.fileIsExist(downloadFile.getFilePath())) {
							int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
									.getFileSize());
							parcelItem.setProgress(progress);
							parcelItem.setHaveRead(downloadFile.getHaveRead());
							parcelItem.setFileSize(downloadFile.getFileSize());
							parcelItem.setCurrentSpeed(0);
							parcelItem
									.setCurrentStatus(downloadFile.getState());
						} else {
							if(downloadFile.getHaveRead() > 0){
								parcelItem.setCurrentStatus(DownloaderParcel.START);
								parcelItem.setProgress(0);
								// 更新数据库
								downloadFile.setHaveRead(0);
								downloadFile.setFileSize(0);
								downloadFile.setState(0);
								DownloadOperator.getInstance().update(downloadFile,
										downloadFile.getKey());
							}else{
								int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
										.getFileSize());
								parcelItem.setProgress(progress);
								parcelItem.setHaveRead(downloadFile.getHaveRead());
								parcelItem.setFileSize(downloadFile.getFileSize());
								parcelItem.setCurrentSpeed(0);
								parcelItem
										.setCurrentStatus(downloadFile.getState());
							}
						}
						downloads.remove(downloadFile);
						break;
					}
				}
			}
			downloads = null;
			mBaseAdapter.registerDowningCallback();
			sendEmptyUiMessage(RESPONSE_REFRESH_CODE);
			break;
		case UPDATE_DOWNLOAD_PROGRESS:
			if (msg != null && msg.arg1 >= 0 && msg.obj instanceof DownloadFile
					&& dataList != null) {
				DownloadFile downloadFile = (DownloadFile) msg.obj;
				// LogCat.d("test", "downloadFile = " +
				// downloadFile.toString());
				String key = downloadFile.getKey();
				for (int i = 0; i < dataList.size(); i++) {
					DownloaderParcel downLoadParcel = dataList.get(i);
					if (key.equals(downLoadParcel.getKey())) {
						if (msg.arg1 == FileDownloader.READY) {
							int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
									.getFileSize());
							downLoadParcel.setProgress(progress);
							downLoadParcel.setHaveRead(downloadFile
									.getHaveRead());
							downLoadParcel.setFileSize(downloadFile
									.getFileSize());
							downLoadParcel.setCurrentSpeed(0);
							downLoadParcel
									.setCurrentStatus(DownloaderParcel.READY);
						} else if (msg.arg1 == FileDownloader.DOWNLOADING) {
							int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
									.getFileSize());
							downLoadParcel.setProgress(progress);
							downLoadParcel.setHaveRead(downloadFile
									.getHaveRead());
							downLoadParcel.setFileSize(downloadFile
									.getFileSize());
							downLoadParcel.setCurrentSpeed(downloadFile
									.getStatis().getCurrentSpeed());
							downLoadParcel
									.setCurrentStatus(DownloaderParcel.DOWNING);
						} else if (msg.arg1 == FileDownloader.INTERUPTING) {
							int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
									.getFileSize());
							downLoadParcel.setProgress(progress);
							downLoadParcel.setHaveRead(downloadFile
									.getHaveRead());
							downLoadParcel.setFileSize(downloadFile
									.getFileSize());
							downLoadParcel.setCurrentSpeed(0);
							downLoadParcel
									.setCurrentStatus(DownloaderParcel.INTERUPTING);
						} else if (msg.arg1 == FileDownloader.INTERUPT) {
							int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
									.getFileSize());
							downLoadParcel.setProgress(progress);
							downLoadParcel.setHaveRead(downloadFile
									.getHaveRead());
							downLoadParcel.setFileSize(downloadFile
									.getFileSize());
							downLoadParcel.setCurrentSpeed(0);
							downLoadParcel
									.setCurrentStatus(DownloaderParcel.INTERRUPT);
						} else if (msg.arg1 == FileDownloader.FINISH) {
							downLoadParcel.setProgress(100);
							downLoadParcel.setHaveRead(downloadFile
									.getHaveRead());
							downLoadParcel.setFileSize(downloadFile
									.getFileSize());
							downLoadParcel.setCurrentSpeed(0);
							downLoadParcel
									.setCurrentStatus(DownloaderParcel.FINISH);
						}
						break;
					}
				}
				sendEmptyUiMessage(RESPONSE_REFRESH_CODE);
			}
			break;
		case ERROR_DOWNLOAD:
			if (msg != null && msg.arg1 >= 0 && msg.obj instanceof DownloadFile
					&& dataList != null) {
				DownloadFile downloadFile = (DownloadFile) msg.obj;
				String key = downloadFile.getKey();
				for (int i = 0; i < dataList.size(); i++) {
					DownloaderParcel downLoadParcel = dataList.get(i);
					if (key.equals(downLoadParcel.getKey())) {
						if (msg.arg1 == FileDownloader.CREATE_FILE_ERROR
								|| msg.arg1 == FileDownloader.GET_FILE_SIZE_ERROR
								|| msg.arg1 == FileDownloader.TIMEOUT_ERROR
								|| msg.arg1 == FileDownloader.NETWORK_ERROR
								|| msg.arg1 == FileDownloader.FILE_NOT_FOUND) {
							int progress = (int) ((downloadFile.getHaveRead() * 100) / downloadFile
									.getFileSize());
							downLoadParcel.setProgress(progress);
							downLoadParcel
									.setCurrentStatus(DownloaderParcel.INTERRUPT);
						}
						break;
					}
				}
				sendEmptyUiMessage(RESPONSE_REFRESH_CODE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void handleUiMessage(Message msg) {
		switch (msg.what) {
		case RESPONSE_REFRESH_CODE:
			mBaseAdapter.notifyDataSetChanged();
			showViewSuccess();
			break;

		default:
			break;
		}
	}

	private void showViewSuccess() {
		mListView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	private void showViewLoading() {
		mListView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void showViewFailure() {
		mListView.setVisibility(View.GONE);
		progressBar.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DownloadServiceUtil.unRegisterCallback(listener);
		LogCat.d("MainActivity onDestroy() ");
		// AppControler.getInstance().exit();
	}

}
