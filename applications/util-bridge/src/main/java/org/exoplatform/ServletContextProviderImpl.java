package org.exoplatform;

import org.apache.portals.bridges.common.ServletContextProvider;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletContextImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomRequestWrapper;

import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ServletContextProviderImpl implements ServletContextProvider {

  public ServletContext getServletContext(GenericPortlet portlet) {
    return ((PortletContextImpl) portlet.getPortletContext()).getWrappedServletContext();
  }

  public HttpServletRequest getHttpServletRequest(GenericPortlet portlet, PortletRequest request) {
    HttpServletRequest result = (HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest();
    // if it is RENDER request
    if (PortletRequest.RENDER_PHASE.equals((String) request.getAttribute(PortletRequest.LIFECYCLE_PHASE))) {
      Map<String, String[]> map = ((PortletRequestImp) request).getInput().getRenderParameters();

      ((CustomRequestWrapper) result).setParameterMap(map);

    }
    return result;
  }

  public HttpServletResponse getHttpServletResponse(GenericPortlet portlet, PortletResponse response) {
    return (HttpServletResponse) ((HttpServletResponseWrapper) response).getResponse();
  }

}
