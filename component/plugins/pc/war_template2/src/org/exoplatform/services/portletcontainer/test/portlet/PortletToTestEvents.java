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
import java.util.Map;

public class PortletToTestEvents extends GenericPortlet{

  public void init(PortletConfig portletConfig) throws PortletException {
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortletException, IOException {
    actionResponse.setEvent(new QName("MyEvent"), new String("Everything is ok"));
  }

	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
					throws PortletException, IOException {
    renderResponse.setContentType("text/html");        
		PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	
  }

  public void processEvent(EventRequest eventrequest, EventResponse eventresponse) throws PortletException, IOException {
    Event event = eventrequest.getEvent();
    if (event.getName().toString().equalsIgnoreCase("MyEventProc")) {
      eventresponse.setEvent(new QName("MyEventPub"), new String("Everything is ok"));
      eventresponse.setEvent(new QName("MyEventStub"), new String("Everything is good"));
    }
  }
  
	public void destroy() {
	}
}
