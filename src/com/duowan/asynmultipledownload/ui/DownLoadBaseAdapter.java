package com.duowan.asynmultipledownload.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.duowan.asynmultipledownload.DownloadServiceUtil;
import com.duowan.asynmultipledownload.R;
import com.duowan.util.LogCat;
import com.duowan.util.ToastShowUtil;

public class DownLoadBaseAdapter extends BaseAdapter {
	public static final int START_STATE = 1;
	public static final int STOP_STATE = 2;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<DownLoadParcel> mList;

	public DownLoadBaseAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<DownLoadParcel> list) {
		this.mList = list;
		notifyDataInvalidated();
	}

	public synchronized void notifyDataInvalidated() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mList != null && mList.size() > 0) {
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && mList.size() > 0) {
			return mList.get(position);
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
			viewHolder.start = (Button) convertView.findViewById(R.id.start);
			viewHolder.stop = (Button) convertView.findViewById(R.id.stop);
			viewHolder.progress = (TextView) convertView
					.findViewById(R.id.progress);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DownLoadParcel item = (DownLoadParcel) getItem(position);
		if (item != null && item.getProgress() > 0) {
			viewHolder.progress.setText(item.getProgress() + "%");
			// viewHolder.progress.setText(String.format(mContext.getResources().getString(
			// R.string.down_load_progress),item.getProgress()));
			if(item.isDownStatus()){
				viewHolder.start.setText(mContext.getResources().getString(R.string.down_load_stop));
			}else{
				viewHolder.start.setText(mContext.getResources().getString(R.string.down_load_start));
			}
		} else {
			viewHolder.progress.setText("0%");
		}
		viewHolder.start.setOnClickListener(new onItemClick(position, item));
		
		return convertView;
	}

	class onItemClick implements OnClickListener {
		int position;
		DownLoadParcel item;

		public onItemClick(int position, DownLoadParcel item) {
			this.position = position;
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.start:
				if (item != null && !TextUtils.isEmpty(item.getUrl())
						&& !TextUtils.isEmpty(item.getFilePath())) {
					if (!item.isDownStatus()) {
						ToastShowUtil.showMsgShort(
								mContext,
								mContext.getResources().getString(
										R.string.down_load_start));
						item.setDownStatus(true);
						notifyDataInvalidated();
						DownloadServiceUtil.download(item.getUrl(), item
								.getFilePath(), new AppDownloadProListener(
								new MyHandle(mContext)));
					} else {
						ToastShowUtil.showMsgShort(mContext, mContext.getResources()
								.getString(R.string.down_load_stop));
						item.setDownStatus(false);
						notifyDataInvalidated();
						DownloadServiceUtil.stopDownload(item.getUrl());
					}
				} else {
					ToastShowUtil.showMsgShort(mContext, "item = null");
				}
				break;
			case R.id.stop:
				ToastShowUtil.showMsgShort(mContext, mContext.getResources()
						.getString(R.string.down_load_stop));
				DownloadServiceUtil.stopDownload(item.getUrl());
				break;
			default:
				break;
			}

		}

	}

	public class MyHandle extends Handler {

		private Context mContext;

		private final int UN_CHECKED = 1;

		private final int DOWNLOADING = 4;

		private final int CANCEL_DOWNLOAD = 5;

		private final int ERROR = 6;

		private int mState = UN_CHECKED;

		private final int COMPLETE_DOWNLOAD = 0x10001;

		private final int ERROR_DOWNLOAD = 0x10002;

		private final int INTERUPT_DOWNLOAD = 0x10003;

		private final int UPDATE_DOWNLOAD_PROGRESS = 0x10004;

		public MyHandle(Context context) {
			this.mContext = context;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COMPLETE_DOWNLOAD:
				// 完成下载
				ToastShowUtil.showMsgShort(mContext, "COMPLETE_DOWNLOAD");
				if (msg != null && msg.arg1 >= 0 && msg.obj instanceof String) {
					String resUrl = (String) msg.obj;
					LogCat.d("test", "COMPLETE_DOWNLOAD = " + msg.arg1);
					for (int i = 0; i < mList.size(); i++) {
						DownLoadParcel downLoadParcel = mList.get(i);
						if (resUrl.equals(downLoadParcel.getUrl())) {
							downLoadParcel.setProgress(100);
							notifyDataInvalidated();
							break;
						}
					}
				}
				break;
			case ERROR_DOWNLOAD:
				// 下载失败
				ToastShowUtil.showMsgShort(mContext, "ERROR_DOWNLOAD");
				break;
			case INTERUPT_DOWNLOAD:
				// 中断下载
				ToastShowUtil.showMsgShort(mContext, "INTERUPT_DOWNLOAD");
				break;
			case UPDATE_DOWNLOAD_PROGRESS:
				// 更新进度
				if (msg != null && msg.arg1 >= 0 && msg.obj instanceof String) {
					String resUrl = (String) msg.obj;
					Log.d("test", resUrl + " = " + msg.arg1);
					for (int i = 0; i < mList.size(); i++) {
						DownLoadParcel downLoadParcel = mList.get(i);
						if (resUrl.equals(downLoadParcel.getUrl())) {
							downLoadParcel.setProgress(msg.arg1);
							notifyDataInvalidated();
							break;
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

	static class ViewHolder {
		Button start;
		Button stop;
		TextView progress;
	}

}
