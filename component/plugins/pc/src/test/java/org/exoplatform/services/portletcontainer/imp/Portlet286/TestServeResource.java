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
import javax.servlet.http.HttpServletRequest;
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
 * Created by The eXo Platform SAS
 * Author : Max Shaposhnik
 *          max.shaposhnik@exoplatform.com.ua
 * 21.08.2007
 */
public class TestServeResource extends BaseTest2{

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestServeResource");

  public TestServeResource(String s) {
    super(s);
  }

  public void testHttpMethod() throws PortletContainerException {
    log.info("testHttpMethod...");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);

    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestServeResource2");
    resourceInput.setPortletMode(PortletMode.VIEW);
    ResourceOutput rO = portletContainer.serveResource(request, response, resourceInput);

    assertNotNull(rO.getContent());
    assertTrue(new String(rO.getContent()).startsWith("Everything is ok"));
    log.info("Done.");
  }





    public void testResourceID() throws PortletContainerException {
    log.info("testResourceID...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestServeResource3");
    resourceInput.setPortletMode(PortletMode.VIEW);
    ResourceOutput rO = portletContainer.serveResource(request, response, resourceInput);
    assertNotNull(rO.getContent());
    String rURL = new String(rO.getContent());
    assertTrue( rURL.indexOf("resourceID=123") > -1);
    String rID = rURL.substring(rURL.indexOf("resourceID=") + "resourceID=".length(), rURL.indexOf("resourceID=") + "resourceID=".length() + 3);
    resourceInput.setResourceID(rID);
    ResourceOutput r2 = portletContainer.serveResource(request, response, resourceInput);
    assertTrue(new String(r2.getContent()).indexOf("OK") > -1);
    log.info("Done.");
  }


}