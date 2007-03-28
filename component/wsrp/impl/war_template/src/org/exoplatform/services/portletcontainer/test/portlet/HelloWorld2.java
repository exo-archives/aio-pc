/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 10 janv. 2004
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
public class HelloWorld2  extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    if(renderRequest.getWindowState() == WindowState.MAXIMIZED){
      w.print("Everything is ok in Maximized state");
      return;
    }
    if(!"".equals(renderRequest.getPreferences().getValue("test-prop",""))) {
      w.print("Everything is more than ok");
      return;
    }
    w.print("Everything is ok");
  }
  
  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    w.print("Everything is ok in Help mode");        
  }  

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }

}
