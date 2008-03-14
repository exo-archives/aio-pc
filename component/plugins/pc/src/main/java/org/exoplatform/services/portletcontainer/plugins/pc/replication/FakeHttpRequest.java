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

/**
 * Fake http request.
 */
public class FakeHttpRequest implements HttpServletRequest {

  /**
   * Session.
   */
  private final FakeHttpSession session;

  /**
   * Attributes.
   */
  private final HashMap attrs;

  /**
   * @param session session
   */
  public FakeHttpRequest(final FakeHttpSession session) {
    this.session = session;
    attrs = new HashMap();
  }

  /**
   * Overridden method.
   *
   * @return is requested session id valid
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
   */
  public boolean isRequestedSessionIdValid() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @return auth type
   * @see javax.servlet.http.HttpServletRequest#getAuthType()
   */
  public String getAuthType() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return context path
   * @see javax.servlet.http.HttpServletRequest#getContextPath()
   */
  public String getContextPath() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return cookies
   * @see javax.servlet.http.HttpServletRequest#getCookies()
   */
  public Cookie[] getCookies() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return value
   * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
   */
  public long getDateHeader(final String arg0) {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return value
   * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
   */
  public String getHeader(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return names
   * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
   */
  public Enumeration getHeaderNames() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return values
   * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
   */
  public Enumeration getHeaders(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return value
   * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
   */
  public int getIntHeader(final String arg0) {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return method
   * @see javax.servlet.http.HttpServletRequest#getMethod()
   */
  public String getMethod() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return path info
   * @see javax.servlet.http.HttpServletRequest#getPathInfo()
   */
  public String getPathInfo() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return path translated
   * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
   */
  public String getPathTranslated() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return query string
   * @see javax.servlet.http.HttpServletRequest#getQueryString()
   */
  public String getQueryString() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return remote user
   * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
   */
  public String getRemoteUser() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return requested session id
   * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
   */
  public String getRequestedSessionId() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return requested uri
   * @see javax.servlet.http.HttpServletRequest#getRequestURI()
   */
  public String getRequestURI() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return requested url
   * @see javax.servlet.http.HttpServletRequest#getRequestURL()
   */
  public StringBuffer getRequestURL() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return servlet path
   * @see javax.servlet.http.HttpServletRequest#getServletPath()
   */
  public String getServletPath() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return session
   * @see javax.servlet.http.HttpServletRequest#getSession()
   */
  public FakeHttpSession getSession() {
    return session;
  }

  /**
   * Overridden method.
   *
   * @param arg0 if to create
   * @return session
   * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
   */
  public HttpSession getSession(final boolean arg0) {

    return session;
  }

  /**
   * Overridden method.
   *
   * @return user principal
   * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
   */
  public Principal getUserPrincipal() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return is requested session id from cookie
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
   */
  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @return is requested session id from URL
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
   */
  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @return is requested session id from URL
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
   */
  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @param arg0 role
   * @return is user in role
   * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
   */
  public boolean isUserInRole(final String arg0) {
    return false;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return value
   * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
   */
  public Object getAttribute(final String arg0) {
    return attrs.get(arg0);
  }

  /**
   * Overridden method.
   *
   * @return attribute names
   * @see javax.servlet.ServletRequest#getAttributeNames()
   */
  public Enumeration getAttributeNames() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return character encoding
   * @see javax.servlet.ServletRequest#getCharacterEncoding()
   */
  public String getCharacterEncoding() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return content legth
   * @see javax.servlet.ServletRequest#getContentLength()
   */
  public int getContentLength() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return content type
   * @see javax.servlet.ServletRequest#getContentType()
   */
  public String getContentType() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return input stream
   * @throws IOException exception
   * @see javax.servlet.ServletRequest#getInputStream()
   */
  public ServletInputStream getInputStream() throws IOException {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return local addr
   * @see javax.servlet.ServletRequest#getLocalAddr()
   */
  public String getLocalAddr() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return locale
   * @see javax.servlet.ServletRequest#getLocale()
   */
  public Locale getLocale() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return locales
   * @see javax.servlet.ServletRequest#getLocales()
   */
  public Enumeration getLocales() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return local name
   * @see javax.servlet.ServletRequest#getLocalName()
   */
  public String getLocalName() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return local port
   * @see javax.servlet.ServletRequest#getLocalPort()
   */
  public int getLocalPort() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return value
   * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
   */
  public String getParameter(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return parameter map
   * @see javax.servlet.ServletRequest#getParameterMap()
   */
  public Map getParameterMap() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return parameter names
   * @see javax.servlet.ServletRequest#getParameterNames()
   */
  public Enumeration getParameterNames() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return values
   * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
   */
  public String[] getParameterValues(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return protocol
   * @see javax.servlet.ServletRequest#getProtocol()
   */
  public String getProtocol() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return reader
   * @throws IOException exception
   * @see javax.servlet.ServletRequest#getReader()
   */
  public BufferedReader getReader() throws IOException {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 path
   * @return real path
   * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
   */
  public String getRealPath(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return remote addr
   * @see javax.servlet.ServletRequest#getRemoteAddr()
   */
  public String getRemoteAddr() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return remote host
   * @see javax.servlet.ServletRequest#getRemoteHost()
   */
  public String getRemoteHost() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return remote port
   * @see javax.servlet.ServletRequest#getRemotePort()
   */
  public int getRemotePort() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @param arg0 path
   * @return request dispatcher
   * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
   */
  public RequestDispatcher getRequestDispatcher(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return scheme
   * @see javax.servlet.ServletRequest#getScheme()
   */
  public String getScheme() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return server name
   * @see javax.servlet.ServletRequest#getServerName()
   */
  public String getServerName() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return server port
   * @see javax.servlet.ServletRequest#getServerPort()
   */
  public int getServerPort() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return is secure
   * @see javax.servlet.ServletRequest#isSecure()
   */
  public boolean isSecure() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
   */
  public void removeAttribute(final String arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
   */
  public void setAttribute(final String arg0, final Object arg1) {
    attrs.put(arg0, arg1);
  }

  /**
   * Overridden method.
   *
   * @param arg0 encoding
   * @throws UnsupportedEncodingException
   * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
   */
  public void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException {
  }

}
