/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.kugou.download;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class KGHttpResponse
{
  private int responseCode;
  private InputStream inputStream;
  private Map<String, Object> headers;
  public static final String CONTENT_LENGTH = "content_length";
  public static final String CONTENT_TYPE = "content_type";

  public int getResponseCode()
  {
    return this.responseCode;
  }

  public KGHttpResponse(int responseCode, InputStream inputStream) {
    this.responseCode = responseCode;
    this.inputStream = inputStream;
    this.headers = new HashMap();
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public InputStream getInputStream() {
    return this.inputStream;
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public Object getHeader(String name) {
    return this.headers.get(name);
  }

  public void setHeader(String name, Object value) {
    this.headers.put(name, value);
  }

  public boolean containsHeader(String name) {
    return this.headers.containsKey(name);
  }
}