/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.kugou.download;

class DefaultHttpConnectorFactory {
	public static IHttpConnector create(boolean isCmwap) {
		return new DefaultHttpConnector(isCmwap);
	}
}