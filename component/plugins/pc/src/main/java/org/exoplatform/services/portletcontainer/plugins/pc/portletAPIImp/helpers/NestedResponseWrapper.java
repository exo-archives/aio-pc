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
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Date: May 25, 2006
 * Time: 12:43:50 am
 */
public class NestedResponseWrapper extends HttpServletResponseWrapper {

  /**
   * Content type.
   */
  private String contentType;

  /**
   * Writer.
   */
  private final PrintWriter tmpWriter;

  /**
   * Output stream.
   */
  private final ProxyServletOutputStream output;

  /**
   * NoOutput.
   */
  private boolean noOutput;

  /**
   * NoValues.
   */
  private boolean noValues;

  /**
   * Url encoder.
   */
  private URLEncoder urlEncoder;

  /**
   * Internal writer.
   */
  private final CharArrayWriter charArrayWriter;

  /**
   * Has writer already been gotten.
   */
  private boolean writerAlreadyCalled;

  /**
   * Has output stream already been gotten.
   */
  private boolean outputStreamAlreadyCalled;

  /**
   * Logger.
   */
  private static Log log = LogFactory.getLog(CustomResponseWrapper.class);

  /**
   * Is committed.
   */
  private boolean committed;

  /**
   * @param httpServletResponse http servlet response
   */
  public NestedResponseWrapper(final HttpServletResponse httpServletResponse) {
    super(httpServletResponse);

    charArrayWriter = new CharArrayWriter();
    tmpWriter = new PrintWriter(charArrayWriter) {
      public boolean equals(final Object obj) {
        return true;
      }

      public void close() {
        log.debug("try to close");
      }
    };
    output = new ProxyServletOutputStream(tmpWriter);

    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;
    contentType = "";
    committed = false;
  }

  /**
   * Overridden method.
   *
   * @return content type
   * @see javax.servlet.ServletResponseWrapper#getContentType()
   */
  public final String getContentType() {
    if (noValues)
      return null;
    return this.contentType;
  }

  /**
   * Overridden method.
   *
   * @param type content type
   * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
   */
  public final void setContentType(final java.lang.String type) {
    super.setContentType(type);
    this.contentType = type;
  }

  /**
   * Overridden method.
   *
   * @throws Throwable throwable
   * @see java.lang.Object#finalize()
   */
  public final void finalize() throws Throwable {
    super.finalize();
    output.close();
    tmpWriter.close();
  }

  /**
   * @return portlet content
   */
  public final byte[] getPortletContent() {
    if (outputStreamAlreadyCalled || writerAlreadyCalled)
      try {
        return (new String(charArrayWriter.toCharArray())).getBytes("utf-8");
      } catch (java.io.UnsupportedEncodingException e) {
        return (new String(charArrayWriter.toCharArray())).getBytes();
      }
    return null;
  }

  /**
   * Overridden method.
   *
   * @return writer
   * @throws IOException exception
   * @see javax.servlet.ServletResponseWrapper#getWriter()
   */
  public final PrintWriter getWriter() throws IOException {
    if (noOutput)
      return null;
    //    if (outputStreamAlreadyCalled)
    //      throw new IllegalStateException("the output streamobject has already been called");
    writerAlreadyCalled = true;
    log.debug("getWriter()");
    return tmpWriter;
  }

  /**
   * Overridden method.
   *
   * @return output stream
   * @throws IOException exception
   * @see javax.servlet.ServletResponseWrapper#getOutputStream()
   */
  public final ServletOutputStream getOutputStream() throws IOException {
    if (noOutput)
      return null;
    //    if (writerAlreadyCalled)
    //      throw new IllegalStateException("the PrintWriter  has already been called");
    log.debug("getOutputStream()");
    outputStreamAlreadyCalled = true;
    return output;
  }

  /**
   * @return byte array containing output markup
   */
  public final byte[] toByteArray() {
    try {
      return (new String(charArrayWriter.toCharArray())).getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      return (new String(charArrayWriter.toCharArray())).getBytes();
    }
  }

  /**
   * Overridden method.
   *
   * @throws IOException exception
   * @see javax.servlet.ServletResponseWrapper#flushBuffer()
   */
  public final void flushBuffer() throws IOException {
    log.debug("flushBuffer()");
    tmpWriter.flush();
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.ServletResponseWrapper#reset()
   */
  public final void reset() {
    log.debug("reset()");
    charArrayWriter.reset();//sh
  }

  /**
   * @throws IOException exception
   */
  public final void close() throws IOException {
    log.debug("close()");
    //    tmpWriter.close();
  }

  /**
   * Overridden method.
   *
   * @return buffer size
   * @see javax.servlet.ServletResponseWrapper#getBufferSize()
   */
  public final int getBufferSize() {
    log.debug("getBufferSize()");
    return 0;
  }

  /**
   * Overridden method.
   *
   * @param url url
   * @return encoded url
   * @see javax.servlet.http.HttpServletResponseWrapper#encodeURL(java.lang.String)
   */
  public final String encodeURL(final String url) {
    if (urlEncoder == null)
      return super.encodeURL(url);
    return urlEncoder.encodeURL(url);
  }

  /**
   * @param encoder encoder
   */
  public final void setURLEncoder(final URLEncoder encoder) {
    urlEncoder = encoder;
  }

  /**
   * Overridden method.
   *
   * @param location location
   * @throws java.io.IOException exception
   * @see javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)
   */
  public final void sendRedirect(final java.lang.String location) throws java.io.IOException {
    log.debug("Send redirect [" + location + "]");
    super.sendRedirect(location);

  }

  /**
   * Proxy output stream.
   */
  private static class ProxyServletOutputStream extends ServletOutputStream {
    /**
     * Writer.
     */
    private final PrintWriter writer;

    /**
     * @param writer writer
     */
    public ProxyServletOutputStream(final PrintWriter writer) {
      this.writer = writer;
    }

    /**
     * Overridden method.
     *
     * @param i char to write
     * @throws IOException exception
     * @see java.io.OutputStream#write(int)
     */
    public final void write(final int i) throws IOException {
      writer.write(i);
    }
  }

  /**
   * Overridden method.
   *
   * @param obj object
   * @return does it equal to this
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public final boolean equals(final Object obj) {
    // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestServlet
    return super.equals(obj) || getResponse().equals(obj);
  }

  /**
   * Overridden method.
   *
   * @param url url
   * @return encoded url
   * @see javax.servlet.http.HttpServletResponseWrapper#encodeRedirectURL(java.lang.String)
   */
  public final String encodeRedirectURL(final String url) {
    // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsReturnNullTestServlet on weblogic 9.2
    if (url != null)
      return super.encodeRedirectURL(url);
    return null;
  }

  /**
   * Overridden method.
   *
   * @param url url
   * @return encoded url
   * @see javax.servlet.http.HttpServletResponseWrapper#encodeRedirectUrl(java.lang.String)
   */
  public final String encodeRedirectUrl(final String url) {
    // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsReturnNullTestServlet on weblogic 9.2
    if (url != null)
      return super.encodeRedirectUrl(url);
    return null;
  }

  /**
   * Overridden method.
   *
   * @return committed flag
   * @see javax.servlet.ServletResponseWrapper#isCommitted()
   */
  public final boolean isCommitted() {
    return committed;
  }

  /**
   * Sets committed flag to true.
   */
  public final void setCommitted() {
    committed = true;
  }

  /**
   * @return noOutput
   */
  public final boolean isNoOutput() {
    return noOutput;
  }

  /**
   * @param noOutput noOutput
   */
  public final void setNoOutput(final boolean noOutput) {
    this.noOutput = noOutput;
  }

  /**
   * Overridden method.
   *
   * @return char encoding
   * @see javax.servlet.ServletResponseWrapper#getCharacterEncoding()
   */
  public final String getCharacterEncoding() {
    if (noValues)
      return null;
    return super.getCharacterEncoding();
  }

  /**
   * Overridden method.
   *
   * @return locale
   * @see javax.servlet.ServletResponseWrapper#getLocale()
   */
  public final Locale getLocale() {
    if (noValues)
      return null;
    return super.getLocale();
  }

  /**
   * @param noValues noValues
   */
  public final void setNoValues(final boolean noValues) {
    this.noValues = noValues;
  }

}
