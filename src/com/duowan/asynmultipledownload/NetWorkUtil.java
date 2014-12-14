package com.duowan.asynmultipledownload;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * ����: ���繤����
 * 
 */
public class NetWorkUtil {

	private NetWorkUtil() {
	}

	/**
	 * ����:��������
	 * 
	 * @author chenys
	 * @since 2013-7-22 ����11:40:42
	 */
	public static class NetworkType {

		public static final String UNKNOWN = "unknown";

		public static final String NET_2G = "2G";

		public static final String NET_3G = "3G";

		public static final String WIFI = "wifi";

		public static final String NET_CMNET = "cmnet";

		public static final String NET_CMWAP = "cmwap";
	}

	/**
	 * ��ǰ�Ƿ��п�������
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		return !(NetworkType.UNKNOWN.endsWith(getNetworkType(context)));
	}

	/**
	 * ��ȡ��ǰ����������
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo == null) {
			return NetworkType.UNKNOWN;
		}
		if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return NetworkType.WIFI;
		}
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int netType = tm.getNetworkType();

		// ��֪3G����
		// NETWORK_TYPE_UMTS 3
		// NETWORK_TYPE_EVDO_0 5
		// NETWORK_TYPE_EVDO_A 6
		// NETWORK_TYPE_HSDPA 8
		// NETWORK_TYPE_HSUPA 9
		// NETWORK_TYPE_HSPA 10
		// NETWORK_TYPE_EVDO_B 12
		// NETWORK_TYPE_LTE 13
		// NETWORK_TYPE_EHRPD 14
		// NETWORK_TYPE_HSPAP 15

		// ��֪2G����
		// NETWORK_TYPE_GPRS 1
		// NETWORK_TYPE_EDGE 2
		// NETWORK_TYPE_CDMA 4
		// NETWORK_TYPE_1xRTT 7
		// NETWORK_TYPE_IDEN 11

		if (netType == TelephonyManager.NETWORK_TYPE_GPRS
				|| netType == TelephonyManager.NETWORK_TYPE_EDGE
				|| netType == TelephonyManager.NETWORK_TYPE_CDMA
				|| netType == TelephonyManager.NETWORK_TYPE_1xRTT
				|| netType == 11) {
			return NetworkType.NET_2G;
		}
		return NetworkType.NET_3G;
	}

	/**
	 * �Ƿ�cmwap
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCmwap(Context context) {
		String currentNetworkType = getNetworkType(context);

		if (NetworkType.NET_2G.equals(currentNetworkType)) {
			try {
				ConnectivityManager connectMgr = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connectMgr != null) {
					NetworkInfo mobNetInfo = connectMgr
							.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					if (mobNetInfo != null && mobNetInfo.isConnected()) {
						if ("cmwap".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
							return true;
						}
					}
				}
			} catch (Exception e) {
			}
		}

		return false;
	}

}
