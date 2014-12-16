/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.duowan.download;

public abstract interface IHttpConnector {
	public abstract KGHttpResponse getHttpResponse(String paramString)
			throws Exception;

	public abstract KGHttpResponse getHttpResponse(String paramString,
			long paramLong) throws Exception;

	public abstract KGHttpResponse getHttpResponse(String paramString,
			long paramLong1, long paramLong2) throws Exception;

	public abstract long getContentLength(String paramString) throws Exception;

	public abstract void close() throws Exception;
}