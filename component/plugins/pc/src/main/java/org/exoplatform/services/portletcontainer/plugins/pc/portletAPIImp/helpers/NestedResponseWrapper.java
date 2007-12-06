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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.exoplatform.services.portletcontainer.helper.URLEncoder;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Date: May 25, 2006
 * Time: 12:43:50 am
 */
public class NestedResponseWrapper extends HttpServletResponseWrapper {
  private String contentType;
  private PrintWriter tmpWriter;
  private ProxyServletOutputStream output;
  private boolean noOutput;
  private URLEncoder urlEncoder_ ;
  private CharArrayWriter charArrayWriter;
  private boolean writerAlreadyCalled;
  private boolean outputStreamAlreadyCalled;
  private static Log log = LogFactory.getLog(CustomResponseWrapper.class);

  public NestedResponseWrapper(HttpServletResponse httpServletResponse) {
    super(httpServletResponse);

    charArrayWriter = new CharArrayWriter();
    tmpWriter = new PrintWriter(charArrayWriter) {
      public boolean equals(Object obj) { return true; }
      public void close() {
        log.debug("try to close");
      }
    };
    output = new ProxyServletOutputStream(tmpWriter);

    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;
    contentType = "";
  }

  public String getContentType() {
    if (noOutput)
      return null;
    return this.contentType;
  }

  public void setContentType(java.lang.String type) {
    super.setContentType(type);
    this.contentType = type;
  }

  public void finalize() throws Throwable {
    super.finalize();
    output.close();
    tmpWriter.close();
  }

  public byte[] getPortletContent() {
    if (outputStreamAlreadyCalled || writerAlreadyCalled) {
      try {
        return (new String(charArrayWriter.toCharArray())).getBytes("utf-8");
      } catch(java.io.UnsupportedEncodingException e) {
        return (new String(charArrayWriter.toCharArray())).getBytes();
      }
    } else
      return null;
  }

  public PrintWriter getWriter() throws IOException {
    if (noOutput)
      return null;
//    if (outputStreamAlreadyCalled)
//      throw new IllegalStateException("the output streamobject has already been called");
    writerAlreadyCalled = true;
    log.debug("getWriter()");
    return tmpWriter;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    if (noOutput)
      return null;
//    if (writerAlreadyCalled)
//      throw new IllegalStateException("the PrintWriter  has already been called");
    log.debug("getOutputStream()");
    outputStreamAlreadyCalled = true;
    return output;
  }

  public byte[] toByteArray() {
//    return output.toByteArray();
    try {
      return (new String(charArrayWriter.toCharArray())).getBytes("utf-8");
    } catch(java.io.UnsupportedEncodingException e) {
      return (new String(charArrayWriter.toCharArray())).getBytes();
    }
  }

  public void flushBuffer() throws IOException {
    log.debug("flushBuffer()");
    tmpWriter.flush();
  }

  public void reset() {
    log.debug("reset()");
    charArrayWriter.reset();//sh
  }

  public void close() throws IOException {
    log.debug("close()");
//    tmpWriter.close();
  }

  public int getBufferSize() {
    log.debug("getBufferSize()");
    return 0;
  }

  public String encodeURL(String url) {
    if (urlEncoder_ == null) return super.encodeURL(url) ;
    return urlEncoder_.encodeURL(url) ;
  }

  public void setURLEncoder(URLEncoder encoder) {
    urlEncoder_ = encoder ;
  }

  public void sendRedirect(java.lang.String location)
                  throws java.io.IOException{
    log.debug("Send redirect ["+location+"]");
    super.sendRedirect(location);

  }

  private static class ProxyServletOutputStream extends ServletOutputStream {
    private PrintWriter writer;

    public ProxyServletOutputStream(PrintWriter writer) {
      this.writer = writer;
    }

    public void write(int i) throws IOException {
      writer.write(i);
    }
  }

  // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestServlet
  public boolean equals(Object obj) {
    return super.equals(obj) || getResponse().equals(obj);
  }

  // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsReturnNullTestServlet on weblogic 9.2
  public String encodeRedirectURL(String url) {
    if (url != null) return super.encodeRedirectURL(url) ;
    return null;
  }

  // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsReturnNullTestServlet on weblogic 9.2
  public String encodeRedirectUrl(String url) {
    if (url != null) return super.encodeRedirectUrl(url) ;
    return null;
  }

  public boolean isCommitted() {
    if (noOutput)
      return true;
    return false;
  }

  public boolean isNoOutput() {
    return noOutput;
  }

  public void setNoOutput(boolean noOutput) {
    this.noOutput = noOutput;
  }

  public String getCharacterEncoding() {
    if (noOutput)
      return null;
    return super.getCharacterEncoding();
  }

  public Locale getLocale() {
    if (noOutput)
      return null;
    return super.getLocale();
  }

}
