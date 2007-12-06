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
package org.exoplatform.services.portletcontainer.plugins.pc.replication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FakeHttpRequest implements HttpServletRequest {

  private FakeHttpSession session;
  private HashMap attrs;

  public FakeHttpRequest(FakeHttpSession session){
    this.session = session;
    attrs = new HashMap();
  }

  public boolean isRequestedSessionIdValid() {
    // TODO Auto-generated method stub
    return false;
  }

  public String getAuthType() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getContextPath() {
    // TODO Auto-generated method stub
    return null;
  }

  public Cookie[] getCookies() {
    // TODO Auto-generated method stub
    return null;
  }

  public long getDateHeader(String arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

  public String getHeader(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public Enumeration getHeaderNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public Enumeration getHeaders(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public int getIntHeader(String arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

  public String getMethod() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getPathInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getPathTranslated() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getQueryString() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getRemoteUser() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getRequestedSessionId() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getRequestURI() {
    // TODO Auto-generated method stub
    return null;
  }

  public StringBuffer getRequestURL() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getServletPath() {
    // TODO Auto-generated method stub
    return null;
  }

  public FakeHttpSession getSession() {
    return session;
  }

  public HttpSession getSession(boolean arg0) {

    return session;
  }

  public Principal getUserPrincipal() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isRequestedSessionIdFromCookie() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isRequestedSessionIdFromUrl() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isUserInRole(String arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  public Object getAttribute(String arg0) {
    // TODO Auto-generated method stub
    //return null;
    return attrs.get(arg0);
  }

  public Enumeration getAttributeNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getCharacterEncoding() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getContentLength() {
    // TODO Auto-generated method stub
    return 0;
  }

  public String getContentType() {
    // TODO Auto-generated method stub
    return null;
  }

  public ServletInputStream getInputStream() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getLocalAddr() {
    // TODO Auto-generated method stub
    return null;
  }

  public Locale getLocale() {
    // TODO Auto-generated method stub
    return null;
  }

  public Enumeration getLocales() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getLocalName() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getLocalPort() {
    // TODO Auto-generated method stub
    return 0;
  }

  public String getParameter(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map getParameterMap() {
    // TODO Auto-generated method stub
    return null;
  }

  public Enumeration getParameterNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public String[] getParameterValues(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public String getProtocol() {
    // TODO Auto-generated method stub
    return null;
  }

  public BufferedReader getReader() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getRealPath(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public String getRemoteAddr() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getRemoteHost() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getRemotePort() {
    // TODO Auto-generated method stub
    return 0;
  }

  public RequestDispatcher getRequestDispatcher(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public String getScheme() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getServerName() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getServerPort() {
    // TODO Auto-generated method stub
    return 0;
  }

  public boolean isSecure() {
    // TODO Auto-generated method stub
    return false;
  }

  public void removeAttribute(String arg0) {
    // TODO Auto-generated method stub

  }

  public void setAttribute(String arg0, Object arg1) {
    attrs.put(arg0, arg1);

  }

  public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
    // TODO Auto-generated method stub

  }

}
