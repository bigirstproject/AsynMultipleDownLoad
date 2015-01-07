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
			status = "׼������";
			break;
		case 3:
			status = "��������";
			break;
		case 5:
			this.t2 = System.currentTimeMillis();
			status = "�������";
			print("������ʱ��" + ((this.t1 > 0L) ? this.t2 - this.t1 : -1L));
			break;
		case 4:
			status = "�û��ж�����";
			break;
		default:
			status = "δ֪״̬";
		}

		print("��ǰ״̬��" + status + " " + file.toString());
	}

	public void onError(DownloadFile file, int errorType) {
		String error;
		switch (errorType) {
		case 10:
			error = "�����ļ�����";
			break;
		case 12:
			error = "���س�ʱ";
			break;
		case 13:
			error = "�����ж�";
			break;
		case 14:
			error = "�ļ�������";
			break;
		case 11:
		default:
			error = "δ֪����";
		}

		print("����ԭ��" + error + " " + file.toString() + " errorType="
				+ errorType);
	}

	private void print(Object obj) {
		Logger.debug("state", obj.toString());
	}
}