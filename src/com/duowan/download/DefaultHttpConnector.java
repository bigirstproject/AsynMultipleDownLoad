/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.duowan.download;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Hashtable;

import com.duowan.util.LogCat;

class DefaultHttpConnector implements IHttpConnector {
	private HttpURLConnection httpConn;
	private boolean isCmwap;

	public DefaultHttpConnector() {
		this(false);
	}

	public DefaultHttpConnector(boolean isCmwap) {
		this.isCmwap = isCmwap;
	}

	public KGHttpResponse getHttpResponse(String resUrl) throws Exception {
		this.httpConn = create(resUrl);
		return handleResponse(this.httpConn);
	}

	public KGHttpResponse getHttpResponse(String resUrl, long start)
			throws Exception {
		this.httpConn = create(resUrl);
		this.httpConn.setRequestProperty("RANGE", "bytes=" + start + "-");
		return handleResponse(this.httpConn);
	}

	public KGHttpResponse getHttpResponse(String resUrl, long start, long end)
			throws Exception {
		this.httpConn = create(resUrl);
		this.httpConn.setRequestProperty("RANGE", "bytes=" + start + "-" + end);
		return handleResponse(this.httpConn);
	}

	public long getContentLength(String resUrl) throws Exception {
		this.httpConn = create(resUrl);
		int contentLength = -1;
		int responseCode = this.httpConn.getResponseCode();
		Logger.debug("responseCode", "getContentLength responseCode-->" + responseCode);
		if ((responseCode == 200) || (responseCode == 206)) {
			contentLength = this.httpConn.getContentLength();
			LogCat.d("getContentLength  enter is " + System.currentTimeMillis());
			this.httpConn.disconnect();
			LogCat.d("getContentLength  out  is " + System.currentTimeMillis());
		}
		return contentLength;
	}

	public void close() throws Exception {
		this.httpConn.disconnect();
	}

	private HttpURLConnection create(String resUrl) throws Exception {
		URL url = new URL(resUrl);
		HttpURLConnection httpConn;
		if (this.isCmwap) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"10.0.0.172", 80));
			httpConn = (HttpURLConnection) url.openConnection(proxy);
		} else {
			httpConn = (HttpURLConnection) url.openConnection();
		}
		if (ConfigWrapper.getInstance().getNetType() == NetType.WIFI) {
			httpConn.setConnectTimeout(8000);
			httpConn.setReadTimeout(8000);
		} else {
			httpConn.setConnectTimeout(12000);
			httpConn.setReadTimeout(12000);
		}
		httpConn.setRequestProperty("Connection", "Keep-Alive");
		Hashtable<String, String> requestHeaders = ConfigWrapper.getInstance()
				.getRequestHeaders();
		if (requestHeaders != null) {
			for (String key : requestHeaders.keySet()) {
				httpConn.setRequestProperty(key,
						(String) requestHeaders.get(key));
			}
		}
		return httpConn;
	}

	private KGHttpResponse handleResponse(HttpURLConnection httpConn)
			throws Exception {
		if (httpConn != null) {
			int responseCode = httpConn.getResponseCode();
			Logger.debug("responseCode", "handleResponse responseCode -->" + responseCode);
			InputStream inputStream = httpConn.getInputStream();
			long contentLength = httpConn.getContentLength();
			String contentType = httpConn.getContentType();
			KGHttpResponse kgresp = new KGHttpResponse(responseCode,
					inputStream);
			kgresp.setHeader("content_length", Long.valueOf(contentLength));
			if (contentType != null) {
				kgresp.setHeader("content_type", contentType);
			}
			return kgresp;
		}
		return null;
	}
}