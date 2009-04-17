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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

;

/**
 * Fake http request.
 */
public class FakeHttpResponse implements HttpServletResponse {

  /**
   * Overridden method.
   *
   * @throws IOException
   * @see javax.servlet.ServletResponse#flushBuffer()
   */
  public final void flushBuffer() throws IOException {
  }

  /**
   * Overridden method.
   *
   * @return buffer size
   * @see javax.servlet.ServletResponse#getBufferSize()
   */
  public final int getBufferSize() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return encoding
   * @see javax.servlet.ServletResponse#getCharacterEncoding()
   */
  public final String getCharacterEncoding() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return content type
   * @see javax.servlet.ServletResponse#getContentType()
   */
  public final String getContentType() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return locale
   * @see javax.servlet.ServletResponse#getLocale()
   */
  public final Locale getLocale() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return output stream
   * @throws IOException exception
   * @see javax.servlet.ServletResponse#getOutputStream()
   */
  public final ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return writer
   * @throws IOException exception
   * @see javax.servlet.ServletResponse#getWriter()
   */
  public final PrintWriter getWriter() throws IOException {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return is committed
   * @see javax.servlet.ServletResponse#isCommitted()
   */
  public final boolean isCommitted() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.ServletResponse#reset()
   */
  public final void reset() {
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.ServletResponse#resetBuffer()
   */
  public final void resetBuffer() {
  }

  /**
   * Overridden method.
   *
   * @param arg0 buffer size
   * @see javax.servlet.ServletResponse#setBufferSize(int)
   */
  public final void setBufferSize(final int arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 encoding
   * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
   */
  public final void setCharacterEncoding(final String arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 content length
   * @see javax.servlet.ServletResponse#setContentLength(int)
   */
  public final void setContentLength(final int arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 content type
   * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
   */
  public final void setContentType(final String arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 locale
   * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
   */
  public final void setLocale(final Locale arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 cookie
   * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
   */
  public final void addCookie(final Cookie arg0) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
   */
  public final void addDateHeader(final String arg0, final long arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
   */
  public final void addHeader(final String arg0, final String arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
   */
  public final void addIntHeader(final String arg0, final int arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @return does contain
   * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
   */
  public final boolean containsHeader(final String arg0) {
    return false;
  }

  /**
   * Overridden method.
   *
   * @param arg0 url
   * @return url
   * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
   */
  public final String encodeRedirectUrl(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 url
   * @return url
   * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
   */
  public final String encodeRedirectURL(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 url
   * @return url
   * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
   */
  public final String encodeUrl(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 url
   * @return url
   * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
   */
  public final String encodeURL(final String arg0) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param arg0 value
   * @param arg1 string
   * @throws IOException
   * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
   */
  public final void sendError(final int arg0, final String arg1) throws IOException {
  }

  /**
   * Overridden method.
   *
   * @param arg0 value
   * @throws IOException
   * @see javax.servlet.http.HttpServletResponse#sendError(int)
   */
  public final void sendError(final int arg0) throws IOException {
  }

  /**
   * Overridden method.
   *
   * @param arg0 url
   * @throws IOException
   * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
   */
  public final void sendRedirect(final String arg0) throws IOException {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
   */
  public final void setDateHeader(final String arg0, final long arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
   */
  public final void setHeader(final String arg0, final String arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 name
   * @param arg1 value
   * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
   */
  public final void setIntHeader(final String arg0, final int arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 value
   * @param arg1 string
   * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
   */
  public final void setStatus(final int arg0, final String arg1) {
  }

  /**
   * Overridden method.
   *
   * @param arg0 value
   * @see javax.servlet.http.HttpServletResponse#setStatus(int)
   */
  public final void setStatus(final int arg0) {
  }

}
