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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.EventRequest;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomResponseWrapper;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.NestedResponseWrapper;

/**
 * Created by The eXo Platform SAS
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
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  }

  public void include(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    include((PortletRequest) renderRequest, (PortletResponse) renderResponse);
  }

  public void include(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException, IOException {
    CustomRequestWrapper requestWrapper = null;
    try {
      requestWrapper =
        ((CustomRequestWrapper) ((HttpServletRequestWrapper) portletRequest).getRequest());

      CustomResponseWrapper realResponseWrapper =
        (CustomResponseWrapper) ((HttpServletResponseWrapper) portletResponse).getResponse();
      NestedResponseWrapper responseWrapper = new NestedResponseWrapper(realResponseWrapper);

      portletRequest.setAttribute("javax.portlet.config", ((PortletRequestImp) portletRequest).getPortletConfig());
      portletRequest.setAttribute("javax.portlet.request", portletRequest);
      portletRequest.setAttribute("javax.portlet.response", portletResponse);
      if (portletRequest instanceof ActionRequest) {
        portletRequest.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.ACTION_PHASE);
        responseWrapper.setCommitted();
        responseWrapper.setNoValues(true);
      } else if (portletRequest instanceof EventRequest) {
        portletRequest.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.EVENT_PHASE);
        requestWrapper.setNoInput(true);
        requestWrapper.setNoValues(true);
        responseWrapper.setCommitted();
        responseWrapper.setNoValues(true);
      } else if (portletRequest instanceof ResourceRequest) {
        portletRequest.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.RESOURCE_PHASE);
        responseWrapper.setContentType(((ResourceResponse) portletResponse).getContentType());
      } else {
        requestWrapper.setNoInput(true);
        requestWrapper.setNoValues(true);
        portletRequest.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.RENDER_PHASE);
        responseWrapper.setContentType(((RenderResponse) portletResponse).getContentType());
      }

      responseWrapper.flushBuffer();
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(path);
      requestWrapper.setContextPath(portletRequest.getContextPath());
      if (dispatcher != null)
        dispatcher.include(requestWrapper, responseWrapper);
      byte[] ca = responseWrapper.getPortletContent();
      if (ca == null)
        ca = new byte[0];
      if (realResponseWrapper.isStreamUsed()) {
        realResponseWrapper.getOutputStream().write(ca);
      } else
        realResponseWrapper.getWriter().write(new String(ca, "utf-8"));
    } catch (ServletException e) {
      if (e.getRootCause() != null)
        log.error("Root cause of the exception", e.getRootCause());
      log.error("Problems occur when using PortletDispatcher", e);
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    } finally {
      portletRequest.removeAttribute("javax.portlet.config");
      portletRequest.removeAttribute("javax.portlet.request");
      portletRequest.removeAttribute("javax.portlet.response");
      portletRequest.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
      if(requestWrapper != null)
        requestWrapper.setRedirected(false);
    }
  }

  public void forward(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException, IOException, IllegalStateException {
    CustomRequestWrapper requestWrapper =
      ((CustomRequestWrapper) ((HttpServletRequestWrapper) portletRequest).getRequest());
    CustomResponseWrapper servResponse = (CustomResponseWrapper) ((PortletResponseImp) (portletResponse)).getResponse();
    NestedResponseWrapper responseWrapper = new NestedResponseWrapper(servResponse);
    if ((portletResponse instanceof MimeResponse) && ((MimeResponse) portletResponse).isCommitted())
      throw new IllegalStateException("Can't forward on committed response");
    try {
      ServletContext portalContext = (ServletContext) cont.getComponentInstanceOfType(ServletContext.class);
      ServletContext portletContext = portalContext.getContext(portletRequest.getContextPath());
      RequestDispatcher dispatcher = portletContext.getRequestDispatcher(path);
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(path);
      requestWrapper.setContextPath(portletRequest.getContextPath());
      if (portletRequest instanceof ActionRequest) {
        responseWrapper.setNoValues(true);
      } else if (portletRequest instanceof EventRequest) {
        requestWrapper.setNoInput(true);
        requestWrapper.setNoValues(true);
        responseWrapper.setNoValues(true);
      } else if (portletRequest instanceof ResourceRequest) {
        responseWrapper.setContentType(((ResourceResponse) portletResponse).getContentType());
      } else {
        responseWrapper.setContentType(null);
        requestWrapper.setNoInput(true);
        requestWrapper.setNoValues(true);
      }

      if (dispatcher != null) {
        dispatcher.forward(requestWrapper/*servRequest*/, responseWrapper);
        byte[] ca = responseWrapper.getPortletContent();
        if (ca == null)
          ca = new byte[0];
        if (servResponse.isStreamUsed()) {
          servResponse.getOutputStream().write(ca);
        } else
          servResponse.getWriter().write(new String(ca, "utf-8"));
        ((PortletResponseImp) portletResponse).setAlreadyForwarded();
      }

    } catch (ServletException e) {
      if (e.getRootCause() != null)
        log.error("Root cause of the exception", e.getRootCause());
      log.error("Problems occur when using PortletDispatcher", e);
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    }
  }

}