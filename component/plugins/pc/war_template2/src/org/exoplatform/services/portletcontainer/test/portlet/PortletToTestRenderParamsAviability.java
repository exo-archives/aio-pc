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
 * Author : Max Shaposhnik
 *          max.shaposhnik@exoplatform.com.ua
 */

public class PortletToTestRenderParamsAviability extends GenericPortlet {
   
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {

  renderResponse.setContentType("text/html; charset=UTF-8");
  PrintWriter w = renderResponse.getWriter();
  
  ResourceURL rURL = renderResponse.createResourceURL();
  rURL.setCacheability(ResourceURL.PAGE); //try to set FULL, then test must fail
  rURL.setParameter("test", "not_A_numbers");
  w.print(rURL.toString());  
  }

public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
  throws PortletException, IOException {
}

  
}
