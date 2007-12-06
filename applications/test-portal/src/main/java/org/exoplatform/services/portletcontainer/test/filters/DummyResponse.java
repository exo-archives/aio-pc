/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.test.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class DummyResponse implements HttpServletResponse {

  private ServletOutputStream output = null;
  private PrintWriter output2 = null;
  
  public DummyResponse() {}
  
  public void addCookie(Cookie cookie) { }

  public boolean containsHeader(String s) { return false; }

  public String encodeURL(String s) { return null; }

  public String encodeRedirectURL(String s) { return null; }

  public String encodeUrl(String s) { return null; }

  public String encodeRedirectUrl(String s) { return null; }

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

  public String getCharacterEncoding() { return null; }

  public ServletOutputStream getOutputStream() throws IOException {
//    if (output == null)
//      output = new ServletOutputStream();
    return output;
  }

  public PrintWriter getWriter() throws IOException {
//    if (output2 == null)
//      output2 = new PrintWriter();
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

  public Locale getLocale() { return null; }
  
  public void setCharacterEncoding(String charset){ }
  
  public String getContentType(){ return null; }
}
