package com.duowan.asynmultipledownload.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.duowan.asynmultipledownload.R;
import com.duowan.asynmultipledownload.Interface.IDownloadManagerCallBackListener;
import com.duowan.asynmultipledownload.bean.DownloaderParcel;
import com.duowan.asynmultipledownload.downTools.DownloadServiceUtil;
import com.duowan.download.FileDownloader;
import com.duowan.download.manager.DownloadManager;
import com.duowan.util.DownLoaderFileUtil;

public class FileDownLoadBaseAdapter extends BaseAdapter {
	public static final int START_STATE = 1;
	public static final int STOP_STATE = 2;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<DownloaderParcel> mDataList;

	public FileDownLoadBaseAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<DownloaderParcel> dataList) {
		this.mDataList = dataList;
		notifyDataInvalidated();
	}

	public ArrayList<DownloaderParcel> getData() {
		return mDataList;
	}

	public synchronized void notifyDataInvalidated() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mDataList != null && mDataList.size() > 0) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDataList != null && mDataList.size() > 0) {
			return mDataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.start = (Button) convertView.findViewById(R.id.down);
			viewHolder.fileSize = (TextView) convertView
					.findViewById(R.id.size);
			viewHolder.currentSpeed = (TextView) convertView
					.findViewById(R.id.rate);
			viewHolder.progress = (TextView) convertView
					.findViewById(R.id.progress);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DownloaderParcel item = (DownloaderParcel) getItem(position);
		viewHolder.start.setOnClickListener(new onItemClick(position, item));
		if (item != null) {
			if (item.getCurrentStatus() == DownloaderParcel.START) {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_start));
			} else if (item.getCurrentStatus() == DownloaderParcel.READY) {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_waiting));
			} else if (item.getCurrentStatus() == DownloaderParcel.DOWNING) {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_downing));
			} else if (item.getCurrentStatus() == DownloaderParcel.INTERUPTING) {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_interrupting));
			} else if (item.getCurrentStatus() == DownloaderParcel.CONTINUE
					|| item.getCurrentStatus() == DownloaderParcel.INTERRUPT) {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_continue));
			} else if (item.getCurrentStatus() == DownloaderParcel.FINISH) {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_complete));
			} else {
				viewHolder.start.setText(mContext.getResources().getString(
						R.string.down_load_start));
			}
		}
		if (item != null && item.getHaveRead() >= 0 && item.getFileSize() >= 0) {
			String havaSize = DownLoaderFileUtil
					.getFileSize(item.getHaveRead());
			String fileSize = DownLoaderFileUtil
					.getFileSize(item.getFileSize());
			viewHolder.fileSize.setText(havaSize + "/" + fileSize);
		} else {
			viewHolder.fileSize.setText("0kb/0kb");
		}
		if (item != null && item.getCurrentSpeed() >= 0) {
			viewHolder.currentSpeed.setText(item.getCurrentSpeed() + "KB");
		} else {
			viewHolder.currentSpeed.setText("0KB");
		}
		if (item != null && item.getProgress() >= 0) {
			viewHolder.progress.setText(item.getProgress() + "%");
		} else {
			viewHolder.progress.setText("0%");
		}
		return convertView;
	}

	public void registerDowningCallback() {
		DownloadServiceUtil
				.registerDowningCallback(new IDownloadManagerCallBackListener() {

					@Override
					public void setCallBackDownloadManagerLitener(
							DownloadManager downloadManager) {
						LinkedList<FileDownloader> waittingList = downloadManager
								.getWaittingList();
						for (int i = 0; i < mDataList.size(); i++) {
							DownloaderParcel downLoadParcel = mDataList.get(i);
							for (int j = 0; j < waittingList.size(); j++) {
								if (waittingList.get(j).getDownloadFile()
										.getResUrl()
										.equals(downLoadParcel.getUrl())) {
									long haveRead = waittingList.get(j)
											.getDownloadFile().getHaveRead();
									long fileSize = waittingList.get(j)
											.getDownloadFile().getFileSize();
									int downloadPer = (int) ((haveRead * 100) / fileSize);
									downLoadParcel.setProgress(downloadPer);
									downLoadParcel
											.setCurrentStatus(DownloaderParcel.READY);
								}
							}
						}
					}
				});
	}

	class onItemClick implements OnClickListener {
		int position;
		DownloaderParcel item;

		public onItemClick(int position, DownloaderParcel item) {
			this.position = position;
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.down:
				if (item != null && !TextUtils.isEmpty(item.getUrl())
						&& !TextUtils.isEmpty(item.getFilePath())) {
					if (item.getCurrentStatus() == DownloaderParcel.START) {
						item.setCurrentStatus(DownloaderParcel.READY);
						DownloadServiceUtil.download(item.getUrl(),
								item.getFilePath(), null);
					} else if (item.getCurrentStatus() == DownloaderParcel.DOWNING
							|| item.getCurrentStatus() == DownloaderParcel.READY) {
						item.setCurrentStatus(DownloaderParcel.INTERUPTING);
						DownloadServiceUtil.stopDownload(item.getUrl());
					} else if (item.getCurrentStatus() == DownloaderParcel.CONTINUE
							|| item.getCurrentStatus() == DownloaderParcel.INTERRUPT) {
						item.setCurrentStatus(DownloaderParcel.READY);
						DownloadServiceUtil.download(item.getUrl(),
								item.getFilePath(), null);
					} else if (item.getCurrentStatus() == DownloaderParcel.FINISH) {
						item.setCurrentStatus(DownloaderParcel.DOWNING);
						DownloadServiceUtil.download(item.getUrl(),
								item.getFilePath(), null);
					}
					notifyDataInvalidated();
				}
				break;
			default:
				break;
			}

		}

	}

	static class ViewHolder {
		Button start;
		TextView fileSize;
		TextView currentSpeed;
		TextView progress;
	}

}
