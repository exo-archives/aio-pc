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

import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * @author Max Shaposhnik
 * PLT 13.6
 * The portlet can create resource URLs pointing back to itself via the createResourceURL
 * PortletResponse. When an end user invokes such a resource
 * method on the URL the portlet container must call the serveResource method of the
 * portlet or return a valid cached result for this resource URL
 */
public class TestResourceURL extends BaseTest2 {

  public TestResourceURL(String s) {
    super(s);
  }

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestResourceURL");

  public void testResourceURL() throws PortletContainerException {
    log.info("testResourceURL...");

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestResourceURL");
    resourceInput.setPortletMode(PortletMode.VIEW);
    ResourceOutput o = portletContainer.serveResource(request, response, resourceInput);
    assertTrue(new String(o.getContent()).length() == 3);
    log.info("Done.");

    //  Uncomment for cache expiring test; testResourceURL2 must be fail when expires.
    //  Sleep time must be over than <expiration-cache> setting into portlet.xml

    //      try {
    //      java.lang.Thread.sleep(35000);
    //       } catch (java.lang.InterruptedException e) {
    //       }

  }

  public void testResourceURL2() throws PortletContainerException {
    log.info("testResourceURL2...");

    try{
    MockServletRequest request2 = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response2 = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestResourceURL");
    resourceInput.setPortletMode(PortletMode.VIEW);
    ResourceOutput o2 = portletContainer.serveResource(request2, response2, resourceInput);
    assertNotNull(o2);
    String out = new String(o2.getContent());
    if (!out.equals(null))
      assertTrue(out.length() == 3);
    log.info("Done.");
    } catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

}
