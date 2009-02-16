/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
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
