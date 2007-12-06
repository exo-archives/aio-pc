/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 13 janv. 2004
 */
 
package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class PortletToTestMarkupWithRewrittenURL extends GenericPortlet{

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    PortletURL pURL = renderResponse.createRenderURL();
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();    
    if(renderRequest.getWindowState() == WindowState.NORMAL){
      pURL.setPortletMode(PortletMode.EDIT);
      pURL.setWindowState(WindowState.MAXIMIZED);
      pURL.setSecure(false);
      pURL.setParameter("param", "value");
      pURL.setParameter("param2", "value2");
        
      w.print("From PortletURL : " + pURL.toString());              
    } else {
      w.print(renderRequest.getParameter("param"));
    }
  }  
  
  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    PortletURL pURL = renderResponse.createRenderURL();
    pURL.setPortletMode(PortletMode.EDIT);
    pURL.setWindowState(WindowState.MAXIMIZED);
    pURL.setSecure(false);
    pURL.setParameter("param", "value");
    pURL.setParameter("param2", "value2");
    
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    
    w.print("From PortletURL : " + pURL.toString());        
  }    

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {      
  }

}
