package com.duowan.download;

public class DefaultProgressListener implements IProgressListener {
	private long t1;
	private long t2;

	public void onProgressChanged(DownloadFile file, int state) {
		if (!(Logger.isDebug()))
			return;
		String status;
		switch (state) {
		case 2:
			this.t1 = System.currentTimeMillis();
			status = "准备就绪";
			break;
		case 3:
			status = "正在下载";
			break;
		case 5:
			this.t2 = System.currentTimeMillis();
			status = "下载完成";
			print("下载用时：" + ((this.t1 > 0L) ? this.t2 - this.t1 : -1L));
			break;
		case 4:
			status = "用户中断下载";
			break;
		default:
			status = "未知状态";
		}

		print("当前状态：" + status + " " + file.toString());
	}

	public void onError(DownloadFile file, int errorType) {
		String error;
		switch (errorType) {
		case 10:
			error = "创建文件出错";
			break;
		case 12:
			error = "下载超时";
			break;
		case 13:
			error = "网络中断";
			break;
		case 14:
			error = "文件不存在";
			break;
		case 11:
		default:
			error = "未知错误";
		}

		print("出错原因：" + error + " " + file.toString() + " errorType="
				+ errorType);
	}

	private void print(Object obj) {
		Logger.debug("state", obj.toString());
	}
}