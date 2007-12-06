/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 22 nov. 2003
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
public class PortletToTestResponseContentTypeMethods extends GenericPortlet{

  public void init(PortletConfig portletConfig) throws PortletException {    
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
          throws PortletException, IOException {
    String responseType = actionRequest.getResponseContentType();
    if(!"text/wml".equals(responseType))
      throw new PortletException("process Action in PortletToTestResponseContentTypeMethods");

    Enumeration e = actionRequest.getResponseContentTypes();
    String text1 = (String)e.nextElement();
    String text2 = (String)e.nextElement();    
    
    if(!"text/wml".equals(text1))
      throw new PortletException("process Action in PortletToTestResponseContentTypeMethods");
    if(!"text/html".equals(text2))
      throw new PortletException("process Action in PortletToTestResponseContentTypeMethods");      

    if(!text1.equals(responseType))
      throw new PortletException("process Action in PortletToTestResponseContentTypeMethods");            
      
    if(e.hasMoreElements())
      throw new PortletException("process Action in PortletToTestResponseContentTypeMethods");      

    actionResponse.setRenderParameter("status", "Everything is ok");
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
          throws PortletException, IOException {
    renderResponse.setContentType("text/html");        
    String responseType = renderRequest.getResponseContentType();
    if(!"text/html".equals(responseType))
      throw new PortletException("render in PortletToTestResponseContentTypeMethods");            
            
    Enumeration e = renderRequest.getResponseContentTypes();
    String text1 = (String)e.nextElement();
    String text2 = (String)e.nextElement();
    if(!"text/html".equals(text1))
      throw new PortletException("render in PortletToTestResponseContentTypeMethods");
    if(!"text/wml".equals(text2))
      throw new PortletException("render in PortletToTestResponseContentTypeMethods");
      
    if(!text1.equals(responseType))
      throw new PortletException("render in PortletToTestResponseContentTypeMethods");      
            
    if(e.hasMoreElements())
      throw new PortletException("render in PortletToTestResponseContentTypeMethods");                  
            
    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
  }

  public void destroy() {
  }
}
