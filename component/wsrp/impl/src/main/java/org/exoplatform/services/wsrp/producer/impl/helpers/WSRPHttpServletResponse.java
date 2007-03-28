/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Created on 9 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.lang.Deprecated;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpServletResponse implements HttpServletResponse {

  private static String WSRP_CONTAINER = "portal";  

  private String scheme;
  private String serverName;
  private int serverPort;
  
  public WSRPHttpServletResponse() {
    
    try {
      if ( ExoContainerContext.getTopContainer() instanceof RootContainer) {
        //PORTALCONTAINER
        PortalContainer container = RootContainer.getInstance().getPortalContainer(WSRP_CONTAINER);
        WSRPHttpServletRequest wsrpHttpServletRequest = (WSRPHttpServletRequest) container.getComponentInstanceOfType(WSRPHttpServletRequest.class);
        this.scheme = wsrpHttpServletRequest.getScheme();
        this.serverName = wsrpHttpServletRequest.getServerName();
        this.serverPort = wsrpHttpServletRequest.getServerPort();
      } else {
        //STANDALONECONTAINER
        ExoContainer container = ExoContainerContext.getTopContainer();
        WSRPHttpServletRequest wsrpHttpServletRequest = (WSRPHttpServletRequest) container.getComponentInstanceOfType(WSRPHttpServletRequest.class);
        this.scheme = wsrpHttpServletRequest.getScheme();
        this.serverName = wsrpHttpServletRequest.getServerName();
        this.serverPort = wsrpHttpServletRequest.getServerPort();
      }
    } catch (Exception e) {
      System.out.println("Exception: WSRPHttpServletRequest e.getCause() = " + e.getCause());
    }
    
  }
  
  public void addCookie(Cookie arg0) {
  }

  public boolean containsHeader(String arg0) {
    return false;
  }
  
  public String encodeURL(String url) {
    //to absolute url
    String result = new String();
    String schem = (scheme != null)?scheme:"http";
    if (!url.startsWith("http://") && !url.startsWith("https://") 
        && !url.startsWith("ftp://") && !url.startsWith(schem+"://")) {
      if (serverName != null && serverName != ""){
        result = schem + "://" + serverName;
        if (serverPort != -1 && serverPort != 0) {
          result = result + ":" + serverPort;
        }
        if (!url.startsWith("/"))
          url = "/" + url;
        result = result + url;
        return result;
      }
    }
    return url;
  }

  public String encodeRedirectURL(String arg0) {
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

  public void addDateHeader(String arg0, long arg1) {
  }

  public void setHeader(String arg0, String arg1) {
  }

  public void addHeader(String arg0, String arg1) {
  }

  public void setIntHeader(String arg0, int arg1) {
  }

  public void addIntHeader(String arg0, int arg1) {
  }

  public void setStatus(int arg0) {
  }

  public String getCharacterEncoding() {
    return null;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  public PrintWriter getWriter() throws IOException {
    return null;
  }

  public void setContentLength(int arg0) {
  }

  public void setContentType(String arg0) {
  }

  public void setBufferSize(int arg0) {
  }

  public int getBufferSize() {
    return 0;
  }

  public void flushBuffer() throws IOException {
  }

  public void resetBuffer() {
  }

  public boolean isCommitted() {
    return false;
  }

  public void reset() {
  }

  public void setLocale(Locale arg0) {
  }

  public Locale getLocale() {
    return null;
  }

  //servlet 2.4
  public void setCharacterEncoding(String enc){
  }

  public String getContentType(){
    return null;
  }
  
  //depracated methods
  @Deprecated
  public String encodeUrl(String url) {
    return url;
  }

  @Deprecated
  public String encodeRedirectUrl(String arg0) {
    return null;
  }

  @Deprecated
  public void setStatus(int arg0, String arg1) {
  }

}
