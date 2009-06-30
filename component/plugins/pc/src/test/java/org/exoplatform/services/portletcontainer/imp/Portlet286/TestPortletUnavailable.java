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

import java.util.Locale;

import javax.portlet.Event;
import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletProcessingException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * @author Max Shaposhnik
 * PLT 15.2.6
 * If a permanent unavailability is indicated by the UnavailableException, the portlet
 * container must remove the portlet from service immediately, call the portlet’s destroy
 * method, and release the portlet object.   A portlet that throws a permanent
 */

  public class TestPortletUnavailable  extends BaseTest2 {

  public TestPortletUnavailable(String s) {
    super(s);
  }

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestPortletUnavailable");

  public void testPortletUnavailable() throws PortletContainerException {
    log.info("testResourceURL...");

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)eventInput.getInternalWindowID()).setPortletName("PortletToTestPortletUnavailable");
    eventInput.setPortletMode(new PortletMode("view"));
    Event event = new EventImpl(new QName("MyEventProc"), new String("event-value"));
    eventInput.setEvent(event);

    try {
	    EventOutput eo = portletContainer.processEvent(request, response, eventInput);
    } catch (PortletProcessingException e) { }

  try {
  Thread.sleep(500);
  }
  catch (InterruptedException e) {
  }
    assertFalse(portletMonitor.isAvailable("war_template2",
        "PortletToTestPortletUnavailable",
        System.currentTimeMillis()));
    log.info("Done.");
  }




}
