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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Max Shaposhnik
 * Date: 28-Aug-2007
 * Time: 10:51:00
 */
public class TestUserInfo extends BaseTest2{

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestUserInfo");

  public TestUserInfo(String s) {
    super(s);
  }
   /**
    * PLT 11.1.3.1
    *
    * The portlet can access a map with user information attributes via the request attribute
    * PortletRequest.USER_INFO.
    *
    * PLT 11.1.3.2
    * The PortletRequest.CCPP_PROFILE request attribute must returned a javax.ccpp.profile
    * based on the current portlet request.
    */

  public void testUserInfo() throws PortletContainerException {

    log.info("testUserInfo...");

    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestUserInfo");
    RenderOutput o = portletContainer.render(request, response, renderInput);
    assertTrue(new String(o.getContent()).indexOf("UserInfoDone") != -1);
    assertTrue(new String(o.getContent()).indexOf("CCPPProfileDone") != -1);
    log.info("Done.");
  }

  /**
   * PLT 11.1.3.4
   * The LIFECYCLE_PHASE request attribute of the PortletRequest interface allows a portlet
   * to determine the current lifecycle phase of this request
   */

  public void testLifecyclePhase() throws PortletContainerException {

    log.info("testLifecyclePhase...");

    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)resourceInput.getInternalWindowID()).setPortletName("PortletToTestUserInfo");
    RenderOutput o = portletContainer.serveResource(request, response, resourceInput);
    assertTrue(new String(o.getContent()).startsWith("OK"));
    log.info("Done.");
  }

}
