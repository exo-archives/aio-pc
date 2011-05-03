package org.exoplatform;

import org.apache.portals.bridges.common.ServletContextProvider;
import org.apache.portals.bridges.struts.StrutsPortlet;
import org.apache.portals.bridges.struts.StrutsPortletURL;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletContextImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomRequestWrapper;

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
  
  public HttpServletRequest getHttpServletRequest(GenericPortlet portlet, PortletRequest request)
  {
      CustomRequestWrapper result = (CustomRequestWrapper)((HttpServletRequestWrapper)request).getRequest();
      
      if (PortletRequest.RENDER_PHASE.equals((String) request.getAttribute(PortletRequest.LIFECYCLE_PHASE))) {
        result.setParameterMap(request.getParameterMap());

        String pageURL = getStrutsPageURL(request);
        if (pageURL != null)
        {
            if (pageURL.indexOf("?") > -1)
            {
               String parametersToParse = pageURL.substring(pageURL.indexOf("?") + 1);
               String[] paramPair = parametersToParse.split("&");
               for (int i = 0; i < paramPair.length; i++)
               {
                  String paramRaw = paramPair[i];
                  String[] paramNameAndValue = paramRaw.split("=");
                  if (paramNameAndValue.length > 1)
                  {
                     result.setParameter(paramNameAndValue[0], paramNameAndValue[1]);
                  }
               }
            }
        }
      }
      return result;
  }

   /**
    * @param request
    * @return
    */
   private String getStrutsPageURL(PortletRequest request)
   {
      if (StrutsPortlet.ACTION_REQUEST.equals(request.getAttribute(StrutsPortlet.REQUEST_TYPE)))
      {
         return request.getParameter(StrutsPortletURL.PAGE);
      }
      return request.getParameter(StrutsPortletURL.PAGE + request.getPortletMode().toString());
   }
   
  public HttpServletResponse getHttpServletResponse(GenericPortlet portlet, PortletResponse response) {
    return (HttpServletResponse) ((HttpServletResponseWrapper) response).getResponse();
  }

}
