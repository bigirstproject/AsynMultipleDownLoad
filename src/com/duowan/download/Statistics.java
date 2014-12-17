package com.duowan.download;

public class Statistics {
	private long startTime;
	private long finishTime;
	private long receivedLen;
	private long refreshTime;
	private long downloadLen;
	private int currentSpeed;
	private P2PStatistics p2p;

	void setStartTime(long startTime) {
		this.startTime = startTime;
		this.refreshTime = startTime;
	}

	void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	synchronized void addReceivedLen(long readLen) {
		this.receivedLen += readLen;
	}

	void setDownloadLen(long downloadLen) {
		this.downloadLen = downloadLen;
	}

	P2PStatistics createP2PStatis() {
		this.p2p = new P2PStatistics();
		return this.p2p;
	}

	synchronized boolean canNotify() {
		long duration = System.currentTimeMillis() - this.refreshTime;

		boolean canNotify = duration > ConfigWrapper.getInstance()
				.getRefreshInterval();
		if (canNotify) {
			this.currentSpeed = (int) (this.receivedLen / duration);
			this.refreshTime = System.currentTimeMillis();
			this.receivedLen = 0L;
		}
		return canNotify;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getDownloadTime() {
		long duration = this.finishTime - this.startTime;
		return ((duration < 0L) ? 0L : duration);
	}

	public long getDownloadSize() {
		return this.downloadLen;
	}

	public synchronized int getCurrentSpeed() {
		return this.currentSpeed;
	}

	public int getDownloadSpeed() {
		long duration = this.finishTime - this.startTime;
		int speed = 0;
		if (duration > 0L) {
			speed = (int) (this.downloadLen / duration);
		}
		return speed;
	}

	public P2PStatistics getP2PStatis() {
		return this.p2p;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("downloadSize=" + getDownloadSize());
		sb.append(" currentSpeed=" + getCurrentSpeed());
		sb.append(" downloadTime=" + getDownloadTime());
		sb.append(" downloadSpeed=" + getDownloadSpeed());
		return sb.toString();
	}

	public class P2PStatistics {
		private int avgP2P;
		private int avgP2S;
		private int avgP2SP;
		private int avgSrc;
		private int perValidSrc;
		private int perP2PDown;
		private int perDuplicate;

		void setAvgP2P(int avgP2P) {
			this.avgP2P = avgP2P;
		}

		public int getAvgP2P() {
			return this.avgP2P;
		}

		void setAvgP2S(int avgP2S) {
			this.avgP2S = avgP2S;
		}

		public int getAvgP2S() {
			return this.avgP2S;
		}

		void setAvgP2SP(int avgP2SP) {
			this.avgP2SP = avgP2SP;
		}

		public int getAvgP2SP() {
			return this.avgP2SP;
		}

		void setAvgSrc(int avgSrc) {
			this.avgSrc = avgSrc;
		}

		public int getAvgSrc() {
			return this.avgSrc;
		}

		void setPerValidSrc(int perValidSrc) {
			this.perValidSrc = perValidSrc;
		}

		public int getPerValidSrc() {
			return this.perValidSrc;
		}

		void setPerP2PDown(int perP2PDown) {
			this.perP2PDown = perP2PDown;
		}

		public int getPerP2PDown() {
			return this.perP2PDown;
		}

		void setPerDuplicate(int perDuplicate) {
			this.perDuplicate = perDuplicate;
		}

		public int getPerDuplicate() {
			return this.perDuplicate;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("avgP2P=" + getAvgP2P());
			sb.append(" avgP2S=" + getAvgP2S());
			sb.append(" avgP2SP=" + getAvgP2SP());
			sb.append(" avgSrc=" + getAvgSrc());
			sb.append(" perValidSrc=" + getPerValidSrc());
			sb.append(" perP2PDown=" + getPerP2PDown());
			sb.append(" perDuplicate=" + getPerDuplicate());
			return sb.toString();
		}
	}
}