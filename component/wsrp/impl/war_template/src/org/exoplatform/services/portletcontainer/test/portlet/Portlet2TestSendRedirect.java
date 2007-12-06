/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class Portlet2TestSendRedirect extends GenericPortlet{


  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {             
  }     

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException { 
    actionResponse.sendRedirect("/path/to/redirect/to.jsp");             
  }
  
}
