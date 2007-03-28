/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 11 janv. 2004
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class PortletToTestPerformBlockingInteraction extends GenericPortlet{


  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {      
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();    
    w.print(renderRequest.getParameter("renderParam1"));        
  }     

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {            
    String param1 = actionRequest.getParameter("name1");
    String param2 = actionRequest.getParameter("name2");
    actionResponse.setPortletMode(PortletMode.EDIT);
    actionResponse.setWindowState(WindowState.MAXIMIZED);
    actionResponse.setRenderParameter("renderParam1", param1);
    actionResponse.setRenderParameter("renderParam2", param2);    
  }

}
