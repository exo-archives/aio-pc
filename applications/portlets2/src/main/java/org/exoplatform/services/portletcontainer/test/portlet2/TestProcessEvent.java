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
package org.exoplatform.services.portletcontainer.test.portlet2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class TestProcessEvent extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doView method of TestPortletNEW...");
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Sample portlet that can create 'MyEventPub' event. The TestPortlet can process this event and change mode to edit.</i></b></font></center><br>");

    w.println("Current method: "+((new Exception()).getStackTrace()[0]).getMethodName());
    w.println("<br>");
    w.println("<p><a href=\"" + renderResponse.createActionURL().toString() + "\">send event</a>");

  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doEdit method of TestPortletNEW...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());

  }

  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doHelp method of TestPortletNEW...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());

  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    //System.out.println("In processAction method of TestPortletNEW...");
    MyEventPub sampleAddress = new MyEventPub();
    sampleAddress.setStreet("myStreet");
    sampleAddress.setCity("myCity");
    actionResponse.setEvent(new QName("MyEventPub"), sampleAddress);

  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In render method of TestPortletNEW...");

    super.render(renderRequest, renderResponse);
  }

  public void processEvent(EventRequest req, EventResponse resp)
      throws PortletException, IOException {
    System.out.println("In processEvent method of TestProcessEvent... !!!!!!!!!!!!!");
    Event event = req.getEvent();
    System.out.println("  -- name: " + event.getName());
    System.out.println("  -- value: " + event.getValue());
    MyEventPub sampleAddress = new MyEventPub();
    sampleAddress = (MyEventPub) event.getValue();

    //resp.setPortletMode(PortletMode.EDIT);

    /*  
    MyEventPub sampleAddress = new MyEventPub();
    sampleAddress.setStreet("myStreet");
    sampleAddress.setCity("myCity");
    resp.setEvent("MyEventPub", sampleAddress);
    */
  }

}
