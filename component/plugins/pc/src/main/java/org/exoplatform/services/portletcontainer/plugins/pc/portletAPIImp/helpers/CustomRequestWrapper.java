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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.utils.CustomRequestWrapperUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * Created by The eXo Platform SAS. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 29, 2003 Time: 2:24:57 AM This
 * wrapper manages the incoming request to only provide the attributes and
 * parameters that are in the name space of the receiving portlet. <br>
 * This is done using a correct encoding and decoding windowId?attributeName
 */
public class CustomRequestWrapper extends HttpServletRequestWrapper {

  /**
   * Path info.
   */
  public String                 pathInfo;

  /**
   * Servlet path.
   */
  public String                 servletPath;

  /**
   * Query.
   */
  public String                 query;

  /**
   * Window id.
   */
  private final String          windowId;

  /**
   * Redirected.
   */
  private boolean               redirected;

  /**
   * Context path.
   */
  public String                 contextPath;

  /**
   * Parameter map.
   */
  private Map<String, String[]> parameterMap;

  /**
   * No input.
   */
  private boolean               noInput;

  /**
   * No values.
   */
  private boolean               noValues;

  /**
   * @param httpServletRequest http servlet request
   * @param windowId window id
   */
  public CustomRequestWrapper(final HttpServletRequest httpServletRequest, final String windowId) {
    super(httpServletRequest);
    this.windowId = windowId;
    this.parameterMap = (Map<String, String[]>) httpServletRequest.getParameterMap();
  }

  /**
   * Overridden method.
   * 
   * @return attribute names
   * @see javax.servlet.ServletRequestWrapper#getAttributeNames()
   */
  public final Enumeration<String> getAttributeNames() {
    Enumeration<String> e = (Enumeration<String>) super.getAttributeNames();
    Vector<String> v = new Vector<String>();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      s = CustomRequestWrapperUtil.decodeRequestAttribute(windowId, s);
      v.add(s);
    }
    return v.elements();
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @return value
   * @see javax.servlet.ServletRequestWrapper#getAttribute(java.lang.String)
   */
  public final Object getAttribute(final String s) {
    // !!! - should be commented out
    // .for.directly.call.include.with.CustomRequestWrapper.
    return super.getAttribute(CustomRequestWrapperUtil.encodeAttribute(windowId, s));
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @see javax.servlet.ServletRequestWrapper#removeAttribute(java.lang.String)
   */
  public final void removeAttribute(final String s) {
    super.removeAttribute(CustomRequestWrapperUtil.encodeAttribute(windowId, s));
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @param o value
   * @see javax.servlet.ServletRequestWrapper#setAttribute(java.lang.String,
   *      java.lang.Object)
   */
  public final void setAttribute(final String s, final Object o) {
    super.setAttribute(CustomRequestWrapperUtil.encodeAttribute(windowId, s), o);
  }

  /**
   * Overridden method.
   * 
   * @return parameter map
   * @see javax.servlet.ServletRequestWrapper#getParameterMap()
   */
  public final Map<String, String[]> getParameterMap() {
    Map<String, String[]> superMap = (Map<String, String[]>) this.parameterMap;
    if (redirected) {
      Map<String, String[]> filteredMap = new HashMap<String, String[]>();
      Set<String> keys = superMap.keySet();
      for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
        String element = (String) iter.next();
        if (!element.startsWith(Constants.PARAMETER_ENCODER))
          filteredMap.put(element, superMap.get(element));
      }
      return filteredMap;
    }
    return superMap;
  }

  @Override
  public String getParameter(String name) {
    return parameterMap.get(name) != null && parameterMap.get(name).length != 0 ? parameterMap.get(name)[0]
                                                                               : super.getParameter(name);
  }

  @Override
  public String[] getParameterValues(String name) {
    return parameterMap.get(name);
  }

  @Override
  public Enumeration getParameterNames() {
    return Collections.enumeration(this.parameterMap.keySet());
  }

  /**
   * @param map parameter map
   */
  public final void setParameterMap(final Map<String, String[]> map) {
    this.parameterMap = map;
  }

  /**
   * @return is redirected
   */
  public final boolean isRedirected() {
    return redirected;
  }

  /**
   * @param b redirected
   */
  public final void setRedirected(final boolean b) {
    redirected = b;
  }

  /**
   * Overridden method.
   * 
   * @return content length
   * @see javax.servlet.ServletRequestWrapper#getContentLength()
   */
  public final int getContentLength() {
    if (noValues)
      return 0;
    if (redirected)
      return -1;
    return super.getContentLength();
  }

  /**
   * Overridden method.
   * 
   * @return requested url
   * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURL()
   */
  public final StringBuffer getRequestURL() {
    if (redirected)
      return null;
    return super.getRequestURL();
  }

  /**
   * Overridden method.
   * 
   * @return character encoding
   * @see javax.servlet.ServletRequestWrapper#getCharacterEncoding()
   */
  public final String getCharacterEncoding() {
    if (noValues)
      return null;
    return super.getCharacterEncoding();
  }

  /**
   * Overridden method.
   * 
   * @return content type
   * @see javax.servlet.ServletRequestWrapper#getContentType()
   */
  public final String getContentType() {
    if (redirected)
      return null;
    return super.getContentType();
  }

  /**
   * Overridden method.
   * 
   * @return input stream
   * @throws IOException exception
   * @see javax.servlet.ServletRequestWrapper#getInputStream()
   */
  public final ServletInputStream getInputStream() throws IOException {
    if (noInput)
      return null;
    return super.getInputStream();
  }

  /**
   * Overridden method.
   * 
   * @return reader
   * @throws IOException exception
   * @see javax.servlet.ServletRequestWrapper#getReader()
   */
  public final BufferedReader getReader() throws IOException {
    if (noInput)
      return null;
    return super.getReader();
  }

  /**
   * Overridden method.
   * 
   * @return local addr
   * @see javax.servlet.ServletRequestWrapper#getLocalAddr()
   */
  public final String getLocalAddr() {
    if (redirected)
      return null;
    return super.getLocalAddr();
  }

  /**
   * Overridden method.
   * 
   * @return local name
   * @see javax.servlet.ServletRequestWrapper#getLocalName()
   */
  public final String getLocalName() {
    if (redirected)
      return null;
    return super.getLocalName();
  }

  /**
   * Overridden method.
   * 
   * @return local port
   * @see javax.servlet.ServletRequestWrapper#getLocalPort()
   */
  public final int getLocalPort() {
    if (redirected)
      return 0;
    return super.getLocalPort();
  }

  /**
   * Overridden method.
   * 
   * @return remote port
   * @see javax.servlet.ServletRequestWrapper#getRemotePort()
   */
  public final int getRemotePort() {
    if (redirected)
      return 0;
    return super.getRemotePort();
  }

  /**
   * Overridden method.
   * 
   * @param arg0 path
   * @return real path
   * @see javax.servlet.ServletRequestWrapper#getRealPath(java.lang.String)
   */
  public final String getRealPath(final String arg0) {
    if (redirected)
      return null;
    return super.getRealPath(arg0);
  }

  /**
   * Overridden method.
   * 
   * @return remote addr
   * @see javax.servlet.ServletRequestWrapper#getRemoteAddr()
   */
  public final String getRemoteAddr() {
    if (redirected)
      return null;
    return super.getRemoteAddr();
  }

  /**
   * Overridden method.
   * 
   * @return remote host
   * @see javax.servlet.ServletRequestWrapper#getRemoteHost()
   */
  public final String getRemoteHost() {
    if (redirected)
      return null;
    return super.getRemoteHost();
  }

  /**
   * Overridden method.
   * 
   * @param arg0 character encoding
   * @throws UnsupportedEncodingException exception
   * @see javax.servlet.ServletRequestWrapper#setCharacterEncoding(java.lang.String)
   */
  public final void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException {
    if (redirected)
      return;
    super.setCharacterEncoding(arg0);
  }

  /**
   * Overridden method.
   * 
   * @return http protocol
   * @see javax.servlet.ServletRequestWrapper#getProtocol()
   */
  public final String getProtocol() {
    if (redirected)
      return "HTTP/1.1";
    return super.getProtocol();
  }

  /**
   * Overridden method.
   * 
   * @return http session
   * @see javax.servlet.http.HttpServletRequestWrapper#getSession()
   */
  public final HttpSession getSession() {
    return getSession(true);
  }

  /**
   * Overridden method.
   * 
   * @param b if to create
   * @return http session
   * @see javax.servlet.http.HttpServletRequestWrapper#getSession(boolean)
   */
  public final HttpSession getSession(final boolean b) {
    return super.getSession(b);
  }

  /**
   * Overridden method.
   * 
   * @return is requested session id valid
   * @see javax.servlet.http.HttpServletRequestWrapper#isRequestedSessionIdValid()
   */
  public final boolean isRequestedSessionIdValid() {
    return super.isRequestedSessionIdValid();
  }

  /**
   * @param string context path
   */
  public final void setContextPath(final String string) {
    contextPath = string;
  }

  /**
   * Overridden method.
   * 
   * @return context path
   * @see javax.servlet.http.HttpServletRequestWrapper#getContextPath()
   */
  public final String getContextPath() {
    if (redirected && (contextPath != null))
      return contextPath;
    return super.getContextPath();
  }

  /**
   * @param path redirected path
   */
  public final void setRedirectedPath(String path) {
    if ((path == null) || (path.length() == 0))
      path = "/";

    String[] key = StringUtils.split(path, "?");
    String firstPart = "";
    if (key.length > 1) {
      query = key[1];
      firstPart = key[0];
    } else
      firstPart = path;

    if (firstPart.indexOf("/", 1) > 0) {
      servletPath = firstPart.substring(0, firstPart.indexOf("/", 1));
      pathInfo = firstPart.substring(firstPart.indexOf("/", 1));
    } else {
      servletPath = firstPart;
      pathInfo = null;
    }
  }

  /**
   * Overridden method.
   * 
   * @return path info
   * @see javax.servlet.http.HttpServletRequestWrapper#getPathInfo()
   */
  public final String getPathInfo() {
    if (redirected)
      return pathInfo;
    return super.getPathInfo();
  }

  /**
   * Overridden method.
   * 
   * @return servlet path
   * @see javax.servlet.http.HttpServletRequestWrapper#getServletPath()
   */
  public final String getServletPath() {
    if (redirected)
      return servletPath;
    return super.getServletPath();
  }

  /**
   * Overridden method.
   * 
   * @return query string
   * @see javax.servlet.http.HttpServletRequestWrapper#getQueryString()
   */
  public final String getQueryString() {
    if (redirected)
      return query;
    return super.getQueryString();
  }

  /**
   * Overridden method.
   * 
   * @return request uri
   * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURI()
   */
  public final String getRequestURI() {
    if (redirected)
      return getContextPath() + ((servletPath == null) ? "" : servletPath)
          + ((pathInfo == null) ? "" : pathInfo);
    return super.getRequestURI();
  }

  /**
   * @param noInput noInput
   */
  public final void setNoInput(final boolean noInput) {
    this.noInput = noInput;
  }

  /**
   * @param noValues noValues
   */
  public final void setNoValues(final boolean noValues) {
    this.noValues = noValues;
  }

  public void setParameter(String key, String value) {
    this.parameterMap.put(key, new String[] { value });
  }

}
