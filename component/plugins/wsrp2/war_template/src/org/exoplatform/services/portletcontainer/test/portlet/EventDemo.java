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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

import org.exoplatform.services.portletcontainer.test.events.MyEventPub;

/**
 * Created by The eXo Platform SAS Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua
 */
public class EventDemo extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
                                                                                   IOException {
    System.out.println("In doView method of TestPortletNEW...");
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.print("Everything is ok");

  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
                                                                                       IOException {
    System.out.println("In processAction method of TestPortletNEW...");
    MyEventPub sampleAddress = new MyEventPub();
    sampleAddress.setCity("myCity");
    actionResponse.setEvent(new QName("MyEventPub"), sampleAddress);
  }

  public void processEvent(EventRequest req, EventResponse resp) throws PortletException,
                                                                IOException {
    System.out.println("In processEvent method of EventDemo... !!!!!!!!!!!!!");
    Event event = req.getEvent();
    System.out.println(">>> EXOMAN EventDemo.processEvent() event = " + event);
    System.out.println("  -- name: " + event.getName());
    System.out.println("  -- value: " + event.getValue());
    String sample = null;
    MyEventPub sampleAddress = null;
    if (event.getValue() instanceof String) {
      sample = (String) event.getValue();
      sampleAddress = new MyEventPub();
      sampleAddress.setCity(sample);
      resp.setEvent("MyEventPub", sampleAddress);
    } else if (event.getValue() instanceof MyEventPub) {
      sampleAddress = (MyEventPub) event.getValue();
      System.out.println(">>> EXOMAN EventDemo.processEvent() sampleAddress.getCity() = "
          + sampleAddress.getCity());
    }

    System.out.println(">>> EXOMAN EventDemo.processEvent() sample = " + sample);
    System.out.println(">>> EXOMAN EventDemo.processEvent() sampleAddress = " + sampleAddress);

    resp.setPortletMode(PortletMode.EDIT);

  }

}
