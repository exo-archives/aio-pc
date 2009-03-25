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

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.BaseURLImp;

import javax.portlet.*;

import java.io.IOException;
import java.io.PrintWriter;

public class PortletToTestNamespacing extends GenericPortlet {
  
  public void init(PortletConfig portletConfig) throws PortletException {
    //To change body of implemented methods use Options | File Templates.
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    w.print(renderResponse.getNamespace());
    
    }
  
  public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
  throws PortletException, IOException {
   
    resourceResponse.setContentType("text/html");
    PrintWriter w = resourceResponse.getWriter();
    w.print(resourceResponse.getNamespace());
    
}
  
  public void destroy() {
    //To change body of implemented methods use Options | File Templates.
  }

}
