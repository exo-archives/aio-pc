/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.NestedResponseWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyRequest;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 25, 2003
 * Time: 6:46:52 PM
 */
public class PortletRequestDispatcherImp implements PortletRequestDispatcher {

  private String path;
  private RequestDispatcher dispatcher;
  private Log log;
  protected ExoContainer cont;

  public PortletRequestDispatcherImp(ExoContainer cont, RequestDispatcher dispatcher, String path) {
    this.dispatcher = dispatcher;
    this.path = path;
    this.cont = cont;
    this.log = ((LogService) cont.getComponentInstanceOfType(LogService.class)).
        getLog("org.exoplatform.services.portletcontainer");
  }

  public void include(RenderRequest request, RenderResponse response)
      throws PortletException, IOException {
    CustomRequestWrapper requestWrapper = null;    
    PortletContainerConf conf = (PortletContainerConf) cont.
        getComponentInstanceOfType(PortletContainerConf.class);
    boolean isSharedSessionEnable = conf.isSharedSessionEnable();
    try {      
      requestWrapper = 
        ((CustomRequestWrapper) ((HttpServletRequestWrapper)request).getRequest());
/*
      CustomResponseWrapper responseWrapper = 
        (CustomResponseWrapper) ((HttpServletResponseWrapper)response).getResponse();
*/
      CustomResponseWrapper realResponseWrapper = 
        (CustomResponseWrapper) ((HttpServletResponseWrapper)response).getResponse();
      NestedResponseWrapper responseWrapper = new NestedResponseWrapper(new EmptyResponse());
      responseWrapper.fillResponseWrapper(realResponseWrapper);
            
      request.setAttribute("javax.portlet.config",((RenderRequestImp)request).getPortletConfig());
      request.setAttribute("javax.portlet.request", request);
      request.setAttribute("javax.portlet.response", response);
      responseWrapper.flushBuffer();
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(path);
      if(isSharedSessionEnable){
        PortletSessionImp pS = (PortletSessionImp)request.getPortletSession();        
        requestWrapper.setSharedSession(pS.getSession());                
      }
      requestWrapper.setContextPath(request.getContextPath());
      if (dispatcher != null)
        dispatcher.include(requestWrapper, responseWrapper);
      char[] ca = responseWrapper.getPortletContent();
      if (ca == null)
        ca = new char[0];
      if (realResponseWrapper.isStreamUsed()) {
        realResponseWrapper.getOutputStream().write((new String(ca)).getBytes("utf-8"));
      } else
        realResponseWrapper.getWriter().write(ca);
    } catch (ServletException e) {
      if (e.getRootCause() != null)
        log.error("Root cause of the exception", e.getRootCause());
      log.error("Problems occur when using PortletDispatcher", e);
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    } finally {
      request.removeAttribute("javax.portlet.config");
      request.removeAttribute("javax.portlet.request");
      request.removeAttribute("javax.portlet.response");      
      if(requestWrapper != null)
        requestWrapper.setRedirected(false);
      if(isSharedSessionEnable){               
        requestWrapper.setSharedSession(null);
      }        
    }    
  }
}
