package com.duowan.asynmultipledownload;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.duowan.util.ToastShowUtil;

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
			if (msg != null && msg.arg1 >= 0) {
				ToastShowUtil.showMsgShort(mContext,
						"UPDATE_DOWNLOAD_PROGRESS = " + msg.arg1);
			}
			break;
		default:
			break;
		}
	}
	
	private void sendEmptyHandle(int what) {
		sendEmptyMessage(what);
	}

	private void sendMessages(Message msg) {
		sendMessage(msg);
	}
}
