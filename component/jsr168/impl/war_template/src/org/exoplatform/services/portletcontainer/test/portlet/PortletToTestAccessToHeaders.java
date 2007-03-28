/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 21 nov. 2003
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class PortletToTestAccessToHeaders extends GenericPortlet {
  
  private static String[] arrayOfHeaders = {"header1", "header2", "header3"};

  public void init(PortletConfig portletConfig) throws PortletException {
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException {
    String headerValue = actionRequest.getProperty("header1");  
    if(!"header-value1".equals(headerValue))
      throw new PortletException("exception in processAction of PortletToTestAccessToHeaders");  
    
    Enumeration e = actionRequest.getPropertyNames();
    while (e.hasMoreElements()) {
      String element = (String) e.nextElement();  
      if(!("header1".equals(element) || "header2".equals(element) || 
           "header3".equals(element) || "test".equals(element)))
        throw new PortletException("exception in processAction of PortletToTestAccessToHeaders");
    }  
         
    e = actionRequest.getProperties("header2");
    while (e.hasMoreElements()) {
      String element = (String) e.nextElement();
      if(!"header-value2".equals(element))
        throw new PortletException("exception in processAction of PortletToTestAccessToHeaders");      
    }
        
    e = actionRequest.getProperties("header3");
    while (e.hasMoreElements()) {
      String element = (String) e.nextElement();
      if(!("header-value3-1".equals(element) || "header-value3-2".equals(element) || "header-value3-3".equals(element)))
        throw new PortletException("exception in processAction of PortletToTestAccessToHeaders");
    }        
    
    actionResponse.setRenderParameter("status", "Everything is ok");
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    renderResponse.setContentType("text/html");  
    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
  }

  public void destroy() {
  }
}
