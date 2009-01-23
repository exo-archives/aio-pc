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
package org.exoplatform.services.portletcontainer.test.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */

/**
 * some ASs commit ServletResponse's that they get with include() method so we have to construct
 * dummy responses to be committed :)
 */
public class DummyResponse implements HttpServletResponse {

  private ServletOutputStream output = null;
  private PrintWriter output2 = null;
  private HttpServletResponse embedded;

  public DummyResponse(HttpServletResponse embedded) {
    this.embedded = embedded;
  }

  public void addCookie(Cookie cookie) { }

  public boolean containsHeader(String s) { return false; }

  public String encodeURL(String s) { return embedded.encodeURL(s); }

  public String encodeRedirectURL(String s) { return embedded.encodeRedirectURL(s); }

  public String encodeUrl(String s) { return embedded.encodeURL(s); }

  public String encodeRedirectUrl(String s) { return embedded.encodeRedirectURL(s); }

  public void sendError(int i, String s) throws IOException { }

  public void sendError(int i) throws IOException { }

  public void sendRedirect(String s) throws IOException { }

  public void setDateHeader(String s, long l) { }

  public void addDateHeader(String s, long l) { }

  public void setHeader(String s, String s1) { }

  public void addHeader(String s, String s1) { }

  public void setIntHeader(String s, int i) { }

  public void addIntHeader(String s, int i) { }

  public void setStatus(int i) { }

  public void setStatus(int i, String s) { }

  public String getCharacterEncoding() { return embedded.getCharacterEncoding(); }

  public ServletOutputStream getOutputStream() throws IOException {
    return output;
  }

  public PrintWriter getWriter() throws IOException {
    return output2;
  }

  public void setContentLength(int i) { }

  public void setContentType(String s) { }

  public void setBufferSize(int i) { }

  public int getBufferSize() { return 0; }

  public void flushBuffer() throws IOException { }

  public void resetBuffer() { }

  public boolean isCommitted() { return false; }

  public void reset() { }

  public void setLocale(Locale locale) { }

  public Locale getLocale() { return embedded.getLocale(); }

  public void setCharacterEncoding(String charset){ }

  public String getContentType() { return embedded.getContentType(); }
}
