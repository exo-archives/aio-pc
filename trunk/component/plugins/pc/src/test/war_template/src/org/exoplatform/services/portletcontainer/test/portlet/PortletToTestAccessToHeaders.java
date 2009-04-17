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
