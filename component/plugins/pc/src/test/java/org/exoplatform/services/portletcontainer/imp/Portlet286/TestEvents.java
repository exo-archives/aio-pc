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
package org.exoplatform.services.portletcontainer.imp.Portlet286;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.portlet.Event;
import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.EventDefinition;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 24.04.2007
 */
public class TestEvents extends BaseTest2{

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestEvents");

	public TestEvents(String s) {
		super(s);
	}

  public void testEventsInDeploymentDescriptor() throws PortletContainerException {
    log.info("testEventsInDeploymentDescriptor...");

    List events_definition = portletApp_.getEventDefinition();
    assertNotNull(events_definition);
    assertFalse(events_definition.isEmpty());

    boolean has_event = false;
    boolean has_proc = false;
    boolean has_pub = false;
    boolean has_proc_false = false;
    boolean has_pub_false = false;

    Iterator iter = events_definition.iterator();
    while (iter.hasNext()) {
      EventDefinition event = (EventDefinition) iter.next();
      if (event.getPrefferedName().getLocalPart().equalsIgnoreCase("MyEvent"))
        has_event = true;
      if (event.getPrefferedName().getLocalPart().equalsIgnoreCase("MyEventProc"))
        has_proc = true;
      if (event.getPrefferedName().getLocalPart().equalsIgnoreCase("MyEventPub"))
        has_pub = true;
      if (event.getPrefferedName().getLocalPart().equalsIgnoreCase("MyEventProcFalse"))
        has_proc_false = true;
      if (event.getPrefferedName().getLocalPart().equalsIgnoreCase("MyEventPubFalse"))
        has_pub_false = true;
    }

    assertTrue(has_event);
    assertTrue(has_proc);
    assertTrue(has_pub);
    assertFalse(has_proc_false);
    assertFalse(has_pub_false);

    PortletData portlet = (PortletData)allPortletMetaData.get(PORTLET_APP_NAME + "/PortletToTestEvents");
    List events_proc = portlet.getSupportedProcessingEvent();
    assertTrue(events_proc.contains(new QName("test.name.space", "MyEvent")));
    assertTrue(events_proc.contains(new QName("test.name.space", "MyEventProc")));
    assertFalse(events_proc.contains(new QName("test.name.space", "MyEventProcFalse")));

    List events_pub = portlet.getSupportedPublishingEvent();
    assertTrue(events_proc.contains(new QName("test.name.space", "MyEvent")));
    assertTrue(events_pub.contains(new QName("test.name.space", "MyEventPub")));
    assertFalse(events_pub.contains(new QName("test.name.space", "MyEventPubFalse")));
    log.info("done.");
  }
  /**
   * PLT 15.2.3: The portlet can publish events via the
   *             StateAwareResponse.setEvent method. The StateAwareReponse
   *             methods are exposed via the ActionResponse and EventResponse
   *             interfaces. It is also valid to call StateAwareResponse.setEvent
   *             multiple times in the current processAction or processEvent
   *             method
   */


	public void testEventsInProcessEvent() throws PortletContainerException {
    log.info("testEventsInProcessEvent...");

		MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    ((ExoWindowID)eventInput.getInternalWindowID()).setPortletName("PortletToTestEvents");
	  eventInput.setPortletMode(new PortletMode("view"));
    Event event = new EventImpl(new QName("test.name.space", "MyEventProc"), new String("event-value"));
    eventInput.setEvent(event);

		EventOutput eO = portletContainer.processEvent(request, response, eventInput);

    assertNotNull(eO);
    assertNotNull(eO.getEvents());
    Object value = eO.getEventByName("MyEventPub");
    assertNotNull(value);
    assertTrue(value instanceof Event);
    Object payload = ((Event) value).getValue();
    assertTrue(payload instanceof String);
    assertEquals("Everything is ok", (String) payload);


    Object value2 = eO.getEventByName("MyEventStub");
    assertNotNull(value2);
    assertTrue(value2 instanceof Event);
    Object payload2 = ((Event) value2).getValue();
    assertTrue(payload2 instanceof String);
    assertEquals("Everything is good", (String) payload2);

    log.info("done.");
	}

  public void testEventsPublishByProcessAction() throws PortletContainerException {
    log.info("testEventsPublishByProcessAction...");

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    ((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("PortletToTestEvents");
    actionInput.setPortletMode(new PortletMode("view"));

    EventOutput eO = portletContainer.processAction(request, response, actionInput);

    assertNotNull(eO.getEvents());
    Object value = eO.getEventByName("MyEvent");
    assertNotNull(value);
    assertTrue(value instanceof Event);
    Object payload = ((Event) value).getValue();
    assertTrue(payload instanceof String);
    assertEquals("Everything is ok", (String) payload);

    log.info("done.");
  }


}
