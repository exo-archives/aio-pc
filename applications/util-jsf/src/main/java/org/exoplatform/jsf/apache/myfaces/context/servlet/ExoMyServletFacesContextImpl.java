package org.exoplatform.jsf.apache.myfaces.context.servlet;

import org.apache.myfaces.portlet.MyFacesGenericPortlet;
import org.apache.myfaces.shared_impl.util.NullIterator;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.*;

import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;

import org.apache.myfaces.context.ReleaseableExternalContext;
import org.apache.myfaces.context.portlet.PortletExternalContextImpl;
import org.apache.myfaces.context.servlet.ServletFacesContextImpl;


public class ExoMyServletFacesContextImpl
        extends ServletFacesContextImpl
{

  public ExoMyServletFacesContextImpl(PortletContext portletContext,
                                      PortletRequest portletRequest,
                                      PortletResponse portletResponse)
  {
    super(portletContext, portletRequest, portletResponse);
    /*
    this(new PortletExternalContextImpl(portletContext,
        portletRequest,
        portletResponse));
    */
    if ((portletRequest.getParameter(MyFacesGenericPortlet.VIEW_ID) == null) && (portletRequest instanceof ActionRequest)) {
      responseComplete();
    }
  }
}