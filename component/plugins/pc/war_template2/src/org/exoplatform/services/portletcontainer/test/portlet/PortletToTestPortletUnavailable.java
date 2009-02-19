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
import javax.xml.namespace.QName;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class PortletToTestPortletUnavailable extends GenericPortlet {
  
  
  public void init(PortletConfig portletConfig) throws PortletException {
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
          throws PortletException, IOException {
    
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
          throws PortletException, IOException {
    
  }

  public void processEvent(EventRequest eventrequest, EventResponse eventresponse) throws PortletException, IOException {
    Event event = eventrequest.getEvent();
    throw new UnavailableException("Portlet Unavailable");
  }
  
  public void destroy() {
    System.out.println("Destroy method called - OK");
  }

}
