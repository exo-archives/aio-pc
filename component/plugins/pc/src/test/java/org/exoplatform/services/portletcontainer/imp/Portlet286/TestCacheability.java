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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.ResourceURL;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

public class TestCacheability extends BaseTest2 {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestCacheability");

  public TestCacheability(String s) {
    super(s);
  }

  /**Max Shaposhnik  @09.2007
   * PLT 13.7
   * Attempts to create URLs that are
   * not of type FULL or are not resource URLs in the current, or a downstream
   * response must result in an IllegalStateException
   */

  public void testCacheability() throws PortletContainerException {
    log.info("testCacheability...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestCacheability");
    resourceInput.setPortletMode(PortletMode.VIEW);
    Map pms = new HashMap();
    pms.put("test", "1");
    resourceInput.setRenderParameters(pms);

    ResourceOutput o2 = portletContainer.serveResource(request, response, resourceInput);
    String rURL = new String(o2.getContent());
    String temp = org.exoplatform.Constants.CACHELEVEL_PARAMETER + "=";

    pms.put("test", "2");
    resourceInput.setRenderParameters(pms);
    resourceInput.setCacheability(rURL.substring(rURL.indexOf(temp) + temp.length()));//, rURL.indexOf("|", rURL.indexOf(temp) + temp.length())));
    ResourceOutput o3 = portletContainer.serveResource(request, response, resourceInput);
    assertTrue(new String(o3.getContent()).startsWith("OK"));

    pms.put("test", "3");
    resourceInput.setRenderParameters(pms);
    ResourceOutput o4 = portletContainer.serveResource(request, response, resourceInput);
    assertTrue(new String(o4.getContent()).indexOf(ResourceURL.FULL) > -1);
    log.info("Done.");
  }

  /**
   * PLT 13.7
   * Creating other URLs, e.g. resource URLs of type PAGE or
   * action or render URLs, must result in an IllegalStateException
   *
   */

  public void testCacheabilityLimitCreatingOtherURLs() throws PortletContainerException {
    log.info("testCacheabilityLimitCreatingOtherURLs...");

    Map pms = new HashMap();

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestCacheability");

    resourceInput.setPortletMode(PortletMode.VIEW);
    pms.put("test", "7");
    resourceInput.setRenderParameters(pms);

    resourceInput.setCacheability(ResourceURL.FULL);
    ResourceOutput o2 = portletContainer.serveResource(request, response, resourceInput);
    assertTrue(new String(o2.getContent()).startsWith("OK"));
    log.info("Done.");
  }



}
