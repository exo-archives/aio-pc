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
    return false;
  }

  public String getAuthType() {
    return null;
  }

  public String getContextPath() {
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

  public Enumeration getHeaderNames() {
    return null;
  }

  public Enumeration getHeaders(String arg0) {
    return null;
  }

  public int getIntHeader(String arg0) {
    return 0;
  }

  public String getMethod() {
    return null;
  }

  public String getPathInfo() {
    return null;
  }

  public String getPathTranslated() {
    return null;
  }

  public String getQueryString() {
    return null;
  }

  public String getRemoteUser() {
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

  public FakeHttpSession getSession() {
    return session;
  }

  public HttpSession getSession(boolean arg0) {

    return session;
  }

  public Principal getUserPrincipal() {
    return null;
  }

  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  public boolean isUserInRole(String arg0) {
    return false;
  }

  public Object getAttribute(String arg0) {
    return attrs.get(arg0);
  }

  public Enumeration getAttributeNames() {
    return null;
  }

  public String getCharacterEncoding() {
    return null;
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

  public String getLocalAddr() {
    return null;
  }

  public Locale getLocale() {
    return null;
  }

  public Enumeration getLocales() {
    return null;
  }

  public String getLocalName() {
    return null;
  }

  public int getLocalPort() {
    return 0;
  }

  public String getParameter(String arg0) {
    return null;
  }

  public Map getParameterMap() {
    return null;
  }

  public Enumeration getParameterNames() {
    return null;
  }

  public String[] getParameterValues(String arg0) {
    return null;
  }

  public String getProtocol() {
    return null;
  }

  public BufferedReader getReader() throws IOException {
    return null;
  }

  public String getRealPath(String arg0) {
    return null;
  }

  public String getRemoteAddr() {
    return null;
  }

  public String getRemoteHost() {
    return null;
  }

  public int getRemotePort() {
    return 0;
  }

  public RequestDispatcher getRequestDispatcher(String arg0) {
    return null;
  }

  public String getScheme() {
    return null;
  }

  public String getServerName() {
    return null;
  }

  public int getServerPort() {
    return 0;
  }

  public boolean isSecure() {
    return false;
  }

  public void removeAttribute(String arg0) {
  }

  public void setAttribute(String arg0, Object arg1) {
    attrs.put(arg0, arg1);
  }

  public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
  }

}
