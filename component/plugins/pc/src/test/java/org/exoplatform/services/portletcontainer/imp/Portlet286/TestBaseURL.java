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
* Created by The eXo Platform SAS         .
* Author : Max Shaposhnik
*          max.shaposhnik@exoplatform.com
* Date: 21-Aug-2007
* Time: 11:34:09
*/
public class TestBaseURL extends BaseTest2 {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestBaseURL");

  public TestBaseURL(String s) {
    super(s);
  }


  /**
   * Portlets can add application specific parameters to the PortletBaseURL objects using the
   * setParameter and  setParameters methods. A call  to any of the  setParameter 20
   * methods must replace any parameter with the same name previously set xxxix
   * PLT 7.1.1
   *
   * This test uses the PortletToTestBaseURL portlet class to test
   */

  public void testSetParameterMethods() throws PortletContainerException {
    log.info("testSetParameterMethods...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestBaseURL");
    RenderOutput o = portletContainer.render(request, response, renderInput);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
    log.info("Done.");
  }

  /**
   * test (xxix) : All the parameters a portlet adds to a BaseURL object must be made available to
   *               the portlet as request parameters.
   *
   * PLT.7.1.1
   */


  public void testAvailibilityOfRenderParameters() throws PortletContainerException {
    log.info("testAvailibilityOfRenderParameters...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    Map renderMap = new HashMap();
    renderMap.put("testParam", "testParamValue");
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestBaseURL2");
    renderInput.setRenderParameters(renderMap);
    RenderOutput o = portletContainer.render(request, response, renderInput);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
    log.info("Done");
  }

  /**
   * test (xli) : The portlet-container must ?x-www-form-urlencoded? encode parameter
   *              names and values added to a BaseURL object.
   *
   * PLT.7.1.1
   */

  public void testParameterEncoding() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US,false);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestBaseParameterEncoding");
    RenderOutput o = portletContainer.render(request, response, renderInput);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }


}
