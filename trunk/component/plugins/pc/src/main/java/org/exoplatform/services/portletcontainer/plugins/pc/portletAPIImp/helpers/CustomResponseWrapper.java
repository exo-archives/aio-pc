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
import org.exoplatform.commons.utils.Text;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 8:22:50 PM
 */
public class CustomResponseWrapper extends HttpServletResponseWrapper {

  /**
   * Content type.
   */
  private String contentType;

  /**
   * Temp writer.
   */
  private final PrintWriter tmpWriter;

  /**
   * Output.
   */
  private final ByteArrayOutputStream output;

  /**
   * Servlet output stream.
   */
  private final ByteArrayServletOutputStream servletOutput;

  /**
   * Url encoder.
   */
  private URLEncoder urlEncoder;

  /**
   * Internal writer.
   */
  private final CharArrayWriter charArrayWriter;

  /**
   * Was writer already called.
   */
  private boolean writerAlreadyCalled;

  /**
   * Was output stream already called.
   */
  private boolean outputStreamAlreadyCalled;

  /**
   * Logger.
   */
  private static Log log = LogFactory.getLog(CustomResponseWrapper.class);

  /**
   * @param httpServletResponse http servlet response
   */
  public CustomResponseWrapper(final HttpServletResponse httpServletResponse) {
    super(httpServletResponse);
    output = new ByteArrayOutputStream();
    servletOutput = new ByteArrayServletOutputStream(output);

    charArrayWriter = new CharArrayWriter();
    //    tmpWriter = new PrintWriter(charArrayWriter);
    //wrap PrintWriter deny
    tmpWriter = new PrintWriter(charArrayWriter) {
      public boolean equals(final Object obj) {
        return true;
      }

      public void close() {
        log.debug("try to close");
      }
    };
    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;
    contentType = "";
  }

  /**
   * Overridden method.
   *
   * @return content type
   * @see javax.servlet.ServletResponseWrapper#getContentType()
   */
  public final String getContentType() {
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
    servletOutput.close();
    output.close();
    tmpWriter.close();
  }

  public final Text getPortletMarkup() {
    if (outputStreamAlreadyCalled)
      return Text.create(output.toByteArray());
    else if (writerAlreadyCalled)
      return Text.create(charArrayWriter.toCharArray());
    else
      return null;
  }

  /**
   * @return portlet content
   */
  public final byte[] getPortletContent() {
    if (outputStreamAlreadyCalled)
      return output.toByteArray();
    else if (writerAlreadyCalled)
      try {
        return (new String(charArrayWriter.toCharArray())).getBytes("utf-8");
      } catch (java.io.UnsupportedEncodingException e) {
        return (new String(charArrayWriter.toCharArray())).getBytes();
      }
    else
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
    //!!!!!!!!!!! - should be commented .for.directly.call.include.with.CustomResponseWrapper.
    if (outputStreamAlreadyCalled)
      throw new IllegalStateException("the output streamobject has already been called");
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
    if (writerAlreadyCalled)
      throw new IllegalStateException("the PrintWriter  has already been called");
    log.debug("getOutputStream()");
    outputStreamAlreadyCalled = true;
    return servletOutput;
  }

  /**
   * @return byte array
   */
  public final byte[] toByteArray() {
    return output.toByteArray();
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
    servletOutput.flush();
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.ServletResponseWrapper#reset()
   */
  public final void reset() {
    log.debug("reset()");
    charArrayWriter.reset();//sh
    output.reset();
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
   * @return is stream used
   */
  public final boolean isStreamUsed() {
    return outputStreamAlreadyCalled;
  }

  /**
   * Byte array servlet output stream.
   */
  private static class ByteArrayServletOutputStream extends ServletOutputStream {
    ByteArrayOutputStream baos;

    /**
     * @param baos byte array output stream
     */
    public ByteArrayServletOutputStream(final ByteArrayOutputStream baos) {
      this.baos = baos;
    }

    /**
     * Overridden method.
     *
     * @param i char to write
     * @throws IOException exception
     * @see java.io.OutputStream#write(int)
     */
    public final void write(final int i) throws IOException {
      baos.write(i);
    }
  }

  /**
   * Overridden method.
   *
   * @param obj obj
   * @return equals
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public final boolean equals(final Object obj) {
    // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestServlet
    if (obj instanceof NestedResponseWrapper)
      return super.equals(((NestedResponseWrapper) obj).getResponse());
    return super.equals(obj);
  }

}
