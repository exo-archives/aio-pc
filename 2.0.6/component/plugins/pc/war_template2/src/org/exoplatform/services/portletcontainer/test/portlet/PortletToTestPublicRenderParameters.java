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

import javax.portlet.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by The eXo Platform SAS 
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class PortletToTestPublicRenderParameters extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    
    String param = renderRequest.getParameter("public1");
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(param);

    
    Enumeration en = renderRequest.getParameterNames();
    if (en != null)
      w.println("getParameterNamesOk");
    
    String[] arr = renderRequest.getParameterValues("public2");
    if (arr != null)
      w.println("getParameterValuesOk");
    
    String val = renderRequest.getParameter("public2");
    if (val.equals("1"))
      w.println("getParameterOk");
    
    Map map = renderRequest.getParameterMap();
     if (map != null)
       w.println("getParameterMapOk");
     
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }
  
}
