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
public class PortletToTestSession extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {    
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    PortletSession session = renderRequest.getPortletSession();
    if(session.getAttribute("att") == null){
      session.setAttribute("att", "attribute set in first call");
    } else {
      w.print(session.getAttribute("att"));
    }
  }

   public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
     throws PortletException, IOException {
   }

}
