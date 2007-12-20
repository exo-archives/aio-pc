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
import javax.servlet.http.HttpServletResponse;;

public class FakeHttpResponse implements HttpServletResponse {

  public void flushBuffer() throws IOException {
  }

  public int getBufferSize() {
    return 0;
  }

  public String getCharacterEncoding() {
    return null;
  }

  public String getContentType() {
    return null;
  }

  public Locale getLocale() {
    return null;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  public PrintWriter getWriter() throws IOException {
    return null;
  }

  public boolean isCommitted() {
    return false;
  }

  public void reset() {
  }

  public void resetBuffer() {
  }

  public void setBufferSize(int arg0) {
  }

  public void setCharacterEncoding(String arg0) {
  }

  public void setContentLength(int arg0) {
  }

  public void setContentType(String arg0) {
  }

  public void setLocale(Locale arg0) {
  }

  public void addCookie(Cookie arg0) {
  }

  public void addDateHeader(String arg0, long arg1) {
  }

  public void addHeader(String arg0, String arg1) {
  }

  public void addIntHeader(String arg0, int arg1) {
  }

  public boolean containsHeader(String arg0) {
    return false;
  }

  public String encodeRedirectUrl(String arg0) {
    return null;
  }

  public String encodeRedirectURL(String arg0) {
    return null;
  }

  public String encodeUrl(String arg0) {
    return null;
  }

  public String encodeURL(String arg0) {
    return null;
  }

  public void sendError(int arg0, String arg1) throws IOException {
  }

  public void sendError(int arg0) throws IOException {
  }

  public void sendRedirect(String arg0) throws IOException {
  }

  public void setDateHeader(String arg0, long arg1) {
  }

  public void setHeader(String arg0, String arg1) {
  }

  public void setIntHeader(String arg0, int arg1) {
  }

  public void setStatus(int arg0, String arg1) {
  }

  public void setStatus(int arg0) {
  }


}
