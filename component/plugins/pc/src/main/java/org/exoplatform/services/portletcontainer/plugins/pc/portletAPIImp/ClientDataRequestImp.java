/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.portlet.ClientDataRequest;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko <roman.pedchenko@exoplatform.com.ua>
 */
public abstract class ClientDataRequestImp extends PortletRequestImp implements ClientDataRequest {

  private boolean isCallGetReader;

  private boolean isCallGetStream;

  private boolean areParamsTouched;

  public ClientDataRequestImp(RequestContext reqCtx) {
    super(reqCtx);
    isCallGetReader = false;
    isCallGetStream = false;
    areParamsTouched = false;
  }

  public String getParameter(String param) {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return null;
    return super.getParameter(param);
  }

  public Enumeration<String> getParameterNames() {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return new Vector<String>().elements();
    return super.getParameterNames();
  }

  public String[] getParameterValues(String s) {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return null;
    return super.getParameterValues(s);
  }

  public Map<String, String[]> getParameterMap() {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return new HashMap<String, String[]>();
    return super.getParameterMap();
  }

  public byte[] makeFormDataByteArray() {
    if (areParamsTouched)
      return new byte[0];
    try {
      StringBuffer r = new StringBuffer();
      boolean touched = false;
      for (Enumeration<String> e = super.getParameterNames(); e.hasMoreElements();) {
        String n = e.nextElement();
        String[] v = super.getParameterValues(n);
        if (v != null && v.length > 0) {
          for (int i = 0; i < v.length; i++) {
            if (touched)
              r.append("&");
            else
              touched = true;
            r.append(n + "=" + java.net.URLEncoder.encode(v[i], (this.enc != null) ? this.enc : "utf-8"));
          }
        }
      }
      return r.toString().getBytes("us-ascii");
    } catch (UnsupportedEncodingException e) {
      return new byte[0];
    }
  }

  public java.io.InputStream getPortletInputStream() throws java.io.IOException {
    if (this.isCallGetReader) {
      throw new IllegalStateException("getPortletInputStream() cannot be called, when getReader() method has been called");
    }
    if (this.isCallGetStream) {
      throw new IllegalStateException("getPortletInputStream() cannot be called twice");
    }
    String contentType = getHeader("content-type");
    if ("application/x-www-form-urlencoded".equals(contentType)) {
      if (!reqCtx.getPortletDatas().getApplication().getVer2() || !getMethod().equalsIgnoreCase("post"))
        throw new IllegalStateException("content type cannot be application/x-www-form-urlencoded");
      this.isCallGetStream = true;
      return new ByteArrayInputStream(makeFormDataByteArray());
    }
    this.isCallGetStream = true;
    return super.getInputStream();
  }

  public java.io.BufferedReader getReader() throws java.io.UnsupportedEncodingException,
                                           java.io.IOException {
    if (this.isCallGetReader) {
      throw new IllegalStateException("getPortletInputStream() cannot be called twice");
    }
    if (this.isCallGetStream) {
      throw new IllegalStateException("getReader() cannot be called, when getPortletInputStream() method has been called");
    }
    String contentType = getHeader("content-type");
    if ("application/x-www-form-urlencoded".equals(contentType)) {
      if (!reqCtx.getPortletDatas().getApplication().getVer2() || !getMethod().equalsIgnoreCase("post"))
        throw new IllegalStateException("content type cannot be application/x-www-form-urlencoded");
      this.isCallGetReader = true;
      return new BufferedReader(new CharArrayReader((new String(makeFormDataByteArray(), "us-ascii")).toCharArray()));
    }
    this.isCallGetReader = true;
    return super.getReader();
  }

  public void setCharacterEncoding(String enc) throws java.io.UnsupportedEncodingException {
    if (this.isCallGetReader || this.isCallGetStream) {
      throw new IllegalStateException("This method cannot be called, when getReader() method has been called");
    }
    super.setCharacterEncoding(enc);
    this.enc = enc;
    this.encodingModified = true;
  }

  public String getCharacterEncoding() {
    return super.getCharacterEncoding();
  }

  public String getContentType() {
    return super.getContentType();
  }

  public int getContentLength() {
    return super.getContentLength();
  }

  public String getMethod() {
    return super.getMethod();
  }

  public abstract String getLifecyclePhase();

}
