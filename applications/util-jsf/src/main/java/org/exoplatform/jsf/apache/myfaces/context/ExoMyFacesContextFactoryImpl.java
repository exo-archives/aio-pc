package org.exoplatform.jsf.apache.myfaces.context;

import org.apache.myfaces.context.servlet.ServletFacesContextImpl;
import org.apache.myfaces.context.FacesContextFactoryImpl;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.exoplatform.jsf.apache.myfaces.context.servlet.ExoMyServletFacesContextImpl;

public class ExoMyFacesContextFactoryImpl
             extends FacesContextFactoryImpl
{
  public FacesContext getFacesContext(Object context,
                                      Object request,
                                      Object response,
                                      Lifecycle lifecycle)
         throws FacesException
  {
    if (context == null) {
      throw new NullPointerException("context");
    }
    if (request == null) {
      throw new NullPointerException("request");
    }
    if (response == null) {
      throw new NullPointerException("response");
    }
    if (lifecycle == null) {
      throw new NullPointerException("lifecycle");
    }

    if (context instanceof ServletContext)
    {
      return new ServletFacesContextImpl((ServletContext)context,
                                         (ServletRequest)request,
                                         (ServletResponse)response);
    }

    if (context instanceof PortletContext)
    {
      return new ExoMyServletFacesContextImpl((PortletContext)context,
                                              (PortletRequest)request,
                                              (PortletResponse)response);
    }

    throw new FacesException("Unsupported context type " + context.getClass().getName());
  }
}