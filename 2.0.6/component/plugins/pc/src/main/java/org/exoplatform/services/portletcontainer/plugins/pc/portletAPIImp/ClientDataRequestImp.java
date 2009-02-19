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
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public abstract class ClientDataRequestImp extends PortletRequestImp implements ClientDataRequest {

  /**
   * Either reader was requested.
   */
  private boolean isCallGetReader;

  /**
   * Either stream was requested.
   */
  private boolean isCallGetStream;

  /**
   * Either parameters are touched.
   */
  private boolean areParamsTouched;

  /**
   * @param reqCtx request context
   */
  public ClientDataRequestImp(final RequestContext reqCtx) {
    super(reqCtx);
    isCallGetReader = false;
    isCallGetStream = false;
    areParamsTouched = false;
  }

  /**
   * Overridden method.
   *
   * @param name parameter name
   * @return parameter value
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp#getParameter(java.lang.String)
   */
  public final String getParameter(final String name) {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return null;
    return super.getParameter(name);
  }

  /**
   * Overridden method.
   *
   * @return parameter names
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp#getParameterNames()
   */
  public final Enumeration<String> getParameterNames() {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return new Vector<String>().elements();
    return super.getParameterNames();
  }

  /**
   * Overridden method.
   *
   * @param name parameter name
   * @return parameter values
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp#getParameterValues(java.lang.String)
   */
  public final String[] getParameterValues(final String name) {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return null;
    return super.getParameterValues(name);
  }

  /**
   * Overridden method.
   *
   * @return parameter map
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp#getParameterMap()
   */
  public final Map<String, String[]> getParameterMap() {
    areParamsTouched = true;
    if (this.isCallGetReader || this.isCallGetStream)
      return new HashMap<String, String[]>();
    return super.getParameterMap();
  }

  /**
   * @return parsed form data
   */
  public final byte[] makeFormDataByteArray() {
    if (areParamsTouched)
      return new byte[0];
    try {
      StringBuffer r = new StringBuffer();
      boolean touched = false;
      for (Enumeration<String> e = super.getParameterNames(); e.hasMoreElements();) {
        String n = e.nextElement();
        String[] v = super.getParameterValues(n);
        if ((v != null) && (v.length > 0))
          for (String element : v) {
            if (touched)
              r.append("&");
            else
              touched = true;
            String encToUse = this.enc;
            if (encToUse == null)
              encToUse = "utf-8";
            r.append(n + "=" + java.net.URLEncoder.encode(element, encToUse));
          }
      }
      return r.toString().getBytes("us-ascii");
    } catch (UnsupportedEncodingException e) {
      return new byte[0];
    }
  }

  /**
   * Overridden method.
   *
   * @return input stream
   * @throws java.io.IOException exception
   * @see javax.portlet.ClientDataRequest#getPortletInputStream()
   */
  public final java.io.InputStream getPortletInputStream() throws java.io.IOException {
    if (this.isCallGetReader)
      throw new IllegalStateException("getPortletInputStream() cannot be called, when getReader() method has been called");
    if (this.isCallGetStream)
      throw new IllegalStateException("getPortletInputStream() cannot be called twice");
    String contentType = getHeader("content-type");
    if ("application/x-www-form-urlencoded".equals(contentType)) {
      if (!reqCtx.getPortletDatas().getApplication().getVer2()
          || !getMethod().equalsIgnoreCase("post"))
        throw new IllegalStateException("content type cannot be application/x-www-form-urlencoded");
      this.isCallGetStream = true;
      return new ByteArrayInputStream(makeFormDataByteArray());
    }
    this.isCallGetStream = true;
    return super.getInputStream();
  }

  /**
   * Overridden method.
   *
   * @return reader
   * @throws java.io.UnsupportedEncodingException exception
   * @throws java.io.IOException exception
   * @see javax.servlet.ServletRequestWrapper#getReader()
   */
  public final java.io.BufferedReader getReader() throws java.io.UnsupportedEncodingException,
      java.io.IOException {
    if (this.isCallGetReader)
      throw new IllegalStateException("getPortletInputStream() cannot be called twice");
    if (this.isCallGetStream)
      throw new IllegalStateException("getReader() cannot be called, when getPortletInputStream() method has been called");
    String contentType = getHeader("content-type");
    if ("application/x-www-form-urlencoded".equals(contentType)) {
      if (!reqCtx.getPortletDatas().getApplication().getVer2()
          || !getMethod().equalsIgnoreCase("post"))
        throw new IllegalStateException("content type cannot be application/x-www-form-urlencoded");
      this.isCallGetReader = true;
      return new BufferedReader(new CharArrayReader((new String(makeFormDataByteArray(), "us-ascii"))
          .toCharArray()));
    }
    this.isCallGetReader = true;
    return super.getReader();
  }

  /**
   * Overridden method.
   *
   * @param enc charset
   * @throws java.io.UnsupportedEncodingException exception
   * @see javax.servlet.ServletRequestWrapper#setCharacterEncoding(java.lang.String)
   */
  public final void setCharacterEncoding(final String enc) throws java.io.UnsupportedEncodingException {
    if (this.isCallGetReader || this.isCallGetStream)
      throw new IllegalStateException("This method cannot be called, when getReader() method has been called");
    super.setCharacterEncoding(enc);
    this.enc = enc;
    this.encodingModified = true;
  }

  /**
   * Overridden method.
   *
   * @return charset
   * @see javax.servlet.ServletRequestWrapper#getCharacterEncoding()
   */
  public final String getCharacterEncoding() {
    return super.getCharacterEncoding();
  }

  /**
   * Overridden method.
   *
   * @return content type
   * @see javax.servlet.ServletRequestWrapper#getContentType()
   */
  public final String getContentType() {
    return super.getContentType();
  }

  /**
   * Overridden method.
   *
   * @return content length
   * @see javax.servlet.ServletRequestWrapper#getContentLength()
   */
  public final int getContentLength() {
    return super.getContentLength();
  }

  /**
   * Overridden method.
   *
   * @return http method
   * @see javax.servlet.http.HttpServletRequestWrapper#getMethod()
   */
  public String getMethod() {
    return super.getMethod();
  }

  /**
   * @return lifecycle phase
   */
  public abstract String getLifecyclePhase();

}
