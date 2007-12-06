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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.utils.CustomRequestWrapperUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 2:24:57 AM
 *
 * This wrapper manages the incoming request to only provide the
 * attributes and parameters that are in the name space of
 * the receiving portlet.
 * <br>
 * This is done using a correct encoding and decoding
 *         windowId?attributeName
 */
public class CustomRequestWrapper extends HttpServletRequestWrapper {

  public String pathInfo;
  public String servletPath;
  public String query;
  private String windowId;
  private boolean redirected;
  public String contextPath;
  private Map parameterMap ;
  private boolean ver2 = false;
  private boolean noInput;

  public CustomRequestWrapper(HttpServletRequest httpServletRequest, String windowId) {
    super(httpServletRequest);
    this.windowId = windowId;
    this.parameterMap = httpServletRequest.getParameterMap() ;
  }

  protected void setVer2(boolean val) {
    ver2 = val;
  }

  public Enumeration getAttributeNames() {
    Enumeration e = super.getAttributeNames();
    Vector v = new Vector();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      s = CustomRequestWrapperUtil.decodeRequestAttribute(windowId, s);
      v.add(s);
    }
    return v.elements();
  }

  //!!! EXOMAN - should be commented out .for.directly.call.include.with.CustomRequestWrapper.
  public Object getAttribute(String s) {
    return super.getAttribute(CustomRequestWrapperUtil.encodeAttribute(windowId, s));
  }

  public void removeAttribute(String s) {
    super.removeAttribute(CustomRequestWrapperUtil.encodeAttribute(windowId, s));
  }

  public void setAttribute(String s, Object o) {
    super.setAttribute(CustomRequestWrapperUtil.encodeAttribute(windowId, s), o);
  }
  public Map getParameterMap() {
    Map superMap = super.getParameterMap();
    if (redirected) {
      Map filteredMap = new HashMap();
      Set keys = superMap.keySet();
      for (Iterator iter = keys.iterator(); iter.hasNext();) {
        String element = (String) iter.next();
        if (!element.startsWith(Constants.PARAMETER_ENCODER))
          filteredMap.put(element, superMap.get(element));
      }
      return filteredMap;
    }
    return superMap;
  }

  public void setParameterMap(Map map) {
    this.parameterMap = map ;
  }

  public boolean isRedirected() {
    return redirected;
  }

  public void setRedirected(boolean b) {
    redirected = b;
  }

  public int getContentLength() {
    if (noInput)
      return 0;
    if (redirected)
      return -1;
    return super.getContentLength();
  }

  public StringBuffer getRequestURL() {
    if (redirected)
      return null;
    return super.getRequestURL();
  }

  public String getCharacterEncoding() {
    if (noInput)
      return null;
    return super.getCharacterEncoding();
  }

  public String getContentType() {
    if (redirected)
      return null;
    return super.getContentType();
  }

  public ServletInputStream getInputStream() throws IOException {
    if (noInput)
      return null;
    return super.getInputStream();
  }

  public BufferedReader getReader() throws IOException {
    if (noInput)
      return null;
    return super.getReader();
  }

  public String getLocalAddr() {
    if (redirected)
      return null;
    return super.getLocalAddr();
  }

  public String getLocalName() {
    if (redirected)
      return null;
    return super.getLocalName();
  }

  public int getLocalPort() {
    if (redirected)
      return 0;
    return super.getLocalPort();
  }

  public int getRemotePort() {
    if (redirected)
      return 0;
    return super.getRemotePort();
  }

  public String getRealPath(String arg0) {
    if (redirected)
      return null;
    return super.getRealPath(arg0);
  }

  public String getRemoteAddr() {
    if (redirected)
      return null;
    return super.getRemoteAddr();
  }

  public String getRemoteHost() {
    if (redirected)
      return null;
    return super.getRemoteHost();
  }

  public void setCharacterEncoding(String arg0)
    throws UnsupportedEncodingException {
    if (redirected)
        return;
    super.setCharacterEncoding(arg0);
  }

  public String getProtocol() {
    if (redirected)
      return "HTTP/1.1";
    return super.getProtocol();
  }

  public HttpSession getSession() {
    return getSession(true);
  }

  public HttpSession getSession(boolean b) {
    return super.getSession(b);
  }

  public boolean isRequestedSessionIdValid() {
    return super.isRequestedSessionIdValid();
  }

  public void setContextPath(String string) {
    contextPath = string;
  }

  public String getContextPath() {
    if (redirected && contextPath != null) {
      return contextPath;
    }
    return super.getContextPath();
  }

  public void setRedirectedPath(String path) {
   if (path == null || path.length() == 0)
     path = "/";

    String[] key = StringUtils.split(path, "?") ;
    String firstPart = "";
    if (key.length > 1) {
      query = key[1];
      firstPart = key[0];
    } else {
      firstPart = path;
    }

    if (firstPart.indexOf("/", 1)>0) {
      servletPath = firstPart.substring(0, firstPart.indexOf("/", 1));
      pathInfo = firstPart.substring(firstPart.indexOf("/", 1));
    } else {
      servletPath = firstPart;
          pathInfo = null;
    }
  }

  public String getPathInfo() {
    if (redirected) {
      return pathInfo;
    }
    return super.getPathInfo();
  }

  public String getServletPath() {
    if (redirected) {
      return servletPath;
    }
    return super.getServletPath();
  }

  public String getQueryString() {
    if (redirected) {
      return query;
    }
    return super.getQueryString();
  }

  public String getRequestURI() {
    if (redirected) {
      return getContextPath() +
                 ((servletPath == null) ? "" : servletPath) +
                 ((pathInfo == null) ? "" : pathInfo);
    }
    return super.getRequestURI();
  }

  public void setNoInput(boolean noInput) {
    this.noInput = noInput;
  }

}
