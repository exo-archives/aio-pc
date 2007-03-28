/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 8:22:50 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.exoplatform.services.portletcontainer.helper.URLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.exoplatform.services.log.LogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CustomResponseWrapper extends HttpServletResponseWrapper {
  private String contentType;
  private PrintWriter tmpWriter;
  private ByteArrayOutputStream output;
  private ByteArrayServletOutputStream servletOutput;
  private URLEncoder urlEncoder_ ;
  private CharArrayWriter charArrayWriter;
  private boolean writerAlreadyCalled;
  private boolean outputStreamAlreadyCalled;
  private static Log log = LogFactory.getLog(CustomResponseWrapper.class);

  public CustomResponseWrapper(HttpServletResponse httpServletResponse) {
    super(httpServletResponse);
  }

  public void fillResponseWrapper(HttpServletResponse httpServletResponse) {
    super.setResponse(httpServletResponse);
    output = new ByteArrayOutputStream();
    servletOutput = new ByteArrayServletOutputStream(output);

    charArrayWriter = new CharArrayWriter();
//    tmpWriter = new PrintWriter(charArrayWriter);
    //wrap PrintWriter deny
    tmpWriter = new PrintWriter(charArrayWriter){
        public boolean equals(Object obj) { return true; }
        public void close() {
            log.debug("try to close");
        }
    };
    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;
    contentType = "";
  }

  public void emptyResponseWrapper() {
    output = null;
    tmpWriter = null;
    servletOutput = null;
    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;
    contentType = "";
  }

  public String getContentType(){
      return this.contentType;
  }

  public void setContentType(java.lang.String type){
      super.setContentType(type);
      this.contentType = type;
  }

  public void finalize() throws Throwable {
    super.finalize();
    servletOutput.close();
    output.close();
    tmpWriter.close();
  }

  public char[] getPortletContent() {
    if (outputStreamAlreadyCalled) {
      try {
        return (new String(output.toByteArray(), "utf-8")).toCharArray();
      } catch(java.io.UnsupportedEncodingException e) {
        return (new String(output.toByteArray())).toCharArray();
      }
    }
    else
    if (writerAlreadyCalled)
      return charArrayWriter.toCharArray();
    else
      return null;
  }

  public PrintWriter getWriter() throws IOException {
    if(outputStreamAlreadyCalled)
      throw new IllegalStateException("the output streamobject has already been called");
    writerAlreadyCalled = true;
    log.debug("getWriter()");
    return tmpWriter;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    if(writerAlreadyCalled)
      throw new IllegalStateException("the PrintWriter  has already been called");
    log.debug("getOutputStream()");
    outputStreamAlreadyCalled = true;
    return servletOutput;
  }

  public byte[] toByteArray() {
    return output.toByteArray();
  }

  public void flushBuffer() throws IOException {
    log.debug("flushBuffer()");

    tmpWriter.flush();
    servletOutput.flush();
  }

  public void reset() {
    log.debug("reset()");
    charArrayWriter.reset();//sh
    output.reset();
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

  public boolean isStreamUsed() {
    return outputStreamAlreadyCalled;
  }


  private static class ByteArrayServletOutputStream extends ServletOutputStream {
    ByteArrayOutputStream baos;
    public ByteArrayServletOutputStream(ByteArrayOutputStream baos) {
      this.baos = baos;
    }
    public void write(int i) throws IOException {
      baos.write(i);
    }
  }

  // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestServlet
  public boolean equals(Object obj) {
System.out.println(" --- CustomResponseWrapper.equals: this: " + this);
System.out.println(" --- CustomResponseWrapper.equals:  obj: " + obj);
    if (obj instanceof NestedResponseWrapper) {
System.out.println(" --- CustomResponseWrapper.equals: obj2: " + (((NestedResponseWrapper) obj).getResponse()));
      return super.equals(((NestedResponseWrapper) obj).getResponse());
    }
    return super.equals(obj);
  }

}
