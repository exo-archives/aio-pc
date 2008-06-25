/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpServletRequest extends HttpServletRequestWrapper {

  private HttpSession       wsrpSession;

  private Map<String, String[]> parameters;

  private Map<String, Object>   attributes;

  private String                scheme;

  private String                serverName;

  private int                   serverPort;

  private String                characterEncoding;

  private Locale                locale;

  private Enumeration<Locale>   locales;

  private String                protocol;

  public WSRPHttpServletRequest(HttpServletRequest request) {
    super(request);
    this.wsrpSession = new WSRPHttpSession(request.getSession());
    this.parameters = new HashMap<String, String[]>();
    this.attributes = new HashMap<String, Object>();
    this.scheme = request.getScheme();
    this.serverName = request.getServerName();
    this.serverPort = request.getServerPort();
    this.locale = request.getLocale();
    this.locales = request.getLocales();
    this.characterEncoding = request.getCharacterEncoding();
    this.protocol = request.getProtocol();
  }

  public String getAuthType() {
    return null;
  }

  public Cookie[] getCookies() {
    return null;
  }

  public long getDateHeader(String arg0) {
    return 0;
  }

  public String getHeader(String arg0) {
    return null;
  }

  public Enumeration getHeaders(String arg0) {
    return null;
  }

  public Enumeration getHeaderNames() {
    return null;
  }

  public int getIntHeader(String arg0) {
    return 0;
  }

  public String getMethod() {
    return "GET";
  }

  public String getPathInfo() {
    return null;
  }

  public String getPathTranslated() {
    return null;
  }

  public String getContextPath() {
    return null;
  }

  public String getQueryString() {
    return null;
  }

  public String getRemoteUser() {
    return null;
  }

  public boolean isUserInRole(String arg0) {
    return false;
  }

  public Principal getUserPrincipal() {
    return null;
  }

  public String getRequestedSessionId() {
    return null;
  }

  public String getRequestURI() {
    return null;
  }

  public StringBuffer getRequestURL() {
    return null;
  }

  public String getServletPath() {
    return null;
  }

  public HttpSession getSession(boolean create) {
    if (wsrpSession == null && create)
      wsrpSession = new WSRPHttpSession();
    return wsrpSession;
  }

  public HttpSession getSession() {
    return getSession(true);
  }

  public boolean isRequestedSessionIdValid() {
    return false;
  }

  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  public Object getAttribute(String arg0) {
    return attributes.get(arg0);
  }

  public Enumeration<String> getAttributeNames() {
    return Collections.enumeration(attributes.keySet());
  }

  public String getCharacterEncoding() {
    return characterEncoding;
  }

  public void setCharacterEncoding(String charenc) throws UnsupportedEncodingException {
    if (charenc != null) {
      this.characterEncoding = charenc;
    } else {
      this.characterEncoding = null;
    }
  }

  public int getContentLength() {
    return 0;
  }

  public String getContentType() {
    return null;
  }

  public ServletInputStream getInputStream() throws IOException {
    return null;
  }

  public String getParameter(String arg0) {
    if (parameters.get(arg0) == null)
      return null;
    return ((String[]) parameters.get(arg0))[0];
  }

  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  public String[] getParameterValues(String arg0) {
    Set<String> keys = parameters.keySet();
    Set<String> values = new HashSet<String>();
    int i = 0;
    for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); i++) {
      String key = (String) iterator.next();
      if (key.equals(arg0)) {
        String[] valuesArray = (String[]) parameters.get(key);
        if (valuesArray != null) {
          for (int j = 0; j < valuesArray.length; j++) {
            values.add(valuesArray[j]);
          }
        }
      }
    }
    return (String[]) values.toArray();
  }

  public Map<String, String[]> getParameterMap() {
    return Collections.unmodifiableMap(parameters);
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getScheme() {
    return scheme;
  }

  public void setServerName(String string) {
    this.serverName = string;
  }

  public String getServerName() {
    return serverName;
  }

  public void setServerPort(int num) {
    this.serverPort = num;
  }

  public int getServerPort() {
    return serverPort;
  }

  public BufferedReader getReader() throws IOException {
    return null;
  }

  public String getRemoteAddr() {
    return null;
  }

  public String getRemoteHost() {
    return null;
  }

  public void setAttribute(String arg0,
                           Object arg1) {
    attributes.put(arg0, arg1);
  }

  public void removeAttribute(String arg0) {
    attributes.remove(arg0);
  }

  public void setLocale(Locale loc) {
    if (loc != null) {
      this.locale = loc;
    } else {
      this.locale = new Locale("en");
    }
  }

  public Locale getLocale() {
    if (locale != null)
      return locale;
    return new Locale("en");
  }

  public void setLocales(Enumeration<Locale> locs) {
    if (locs != null) {
      this.locales = locs;
    } else {
      if (this.locales == null) {
        Vector<Locale> mygetLocales = new Vector<Locale>();
        mygetLocales.add(new Locale("en"));
        this.locales = Collections.enumeration(mygetLocales);
      }
    }
  }

  public Enumeration<Locale> getLocales() {
    if (this.locales != null)
      return locales;
    Vector<Locale> mygetLocales = new Vector<Locale>();
    mygetLocales.add(this.getLocale());
    return Collections.enumeration(mygetLocales);
  }

  public boolean isSecure() {
    return false;
  }

  public RequestDispatcher getRequestDispatcher(String arg0) {
    return null;
  }

  //servlet 2.4 method
  public int getLocalPort() {
    return 0;
  }

  public String getLocalAddr() {
    return "local adress";
  }

  public String getLocalName() {
    return "Local name";
  }

  public int getRemotePort() {
    return 0;
  }

  public void setParameter(String key,
                           String value) {
    String[] valueArray = new String[1];
    valueArray[0] = value;
    parameters.put(key, valueArray);
  }

  public String getRealPath(String arg0) {
    return null;
  }

  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }
  
  public void setWsrpSession(WSRPHttpSession wsrpSession) {
    this.wsrpSession = wsrpSession;
  }

}
