/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Date: May 25, 2006
 * Time: 12:43:50 am
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
import org.exoplatform.services.log.ExoLogger;
import org.apache.commons.logging.Log;


public class NestedResponseWrapper extends HttpServletResponseWrapper {
  private String contentType;
  private PrintWriter tmpWriter;
  private ProxyServletOutputStream output;
  private URLEncoder urlEncoder_ ;
  private CharArrayWriter charArrayWriter;
  private boolean writerAlreadyCalled;
  private boolean outputStreamAlreadyCalled;
  private static Log log = ExoLogger.getLogger(CustomResponseWrapper.class);

  public NestedResponseWrapper(HttpServletResponse httpServletResponse) {
    super(httpServletResponse);
  }

  public void fillResponseWrapper(HttpServletResponse httpServletResponse) {
    super.setResponse(httpServletResponse);

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

  public void emptyResponseWrapper() {
    output = null;
    tmpWriter = null;
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
    output.close();
    tmpWriter.close();
  }

  public char[] getPortletContent() {
    if (outputStreamAlreadyCalled || writerAlreadyCalled)
      return charArrayWriter.toCharArray();
    else
      return null;
  }

  public PrintWriter getWriter() throws IOException {
//    if(outputStreamAlreadyCalled)
//      throw new IllegalStateException("the output streamobject has already been called");
    writerAlreadyCalled = true;
    log.debug("getWriter()");
    return tmpWriter;
  }

  public ServletOutputStream getOutputStream() throws IOException {
//    if(writerAlreadyCalled)
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
System.out.println(" --- NestedResponseWrapper.equals: this: " + this);
System.out.println(" --- NestedResponseWrapper.equals:  obj: " + obj);
System.out.println(" --- NestedResponseWrapper.equals: obj2: " + getResponse());
    return super.equals(obj) || getResponse().equals(obj);
  }
  
  // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsReturnNullTestServlet on weblogic 9.2
  public String encodeRedirectURL(String url){
    if (url != null) return super.encodeRedirectURL(url) ;
    return null;
  }
  
  // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsReturnNullTestServlet on weblogic 9.2
  public String encodeRedirectUrl(String url){
    if (url != null) return super.encodeRedirectUrl(url) ;
    return null;
  }
  
}
