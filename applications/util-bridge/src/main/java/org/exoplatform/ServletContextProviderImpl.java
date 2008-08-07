package org.exoplatform;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.portals.bridges.common.ServletContextProvider;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletContextImpl;

public class ServletContextProviderImpl implements ServletContextProvider {
  public ServletContext getServletContext(GenericPortlet portlet) {
    return ((PortletContextImpl) portlet.getPortletContext()).getWrappedServletContext();
  }

  public HttpServletRequest getHttpServletRequest(GenericPortlet portlet, PortletRequest request) {
    return (HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest();
  }

  public HttpServletResponse getHttpServletResponse(GenericPortlet portlet, PortletResponse response) {
    return (HttpServletResponse) ((HttpServletResponseWrapper) response).getResponse();
  }
}
