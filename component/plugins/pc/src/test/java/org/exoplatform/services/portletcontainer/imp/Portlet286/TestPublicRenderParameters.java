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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.model.PublicRenderParameter;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 24.04.2007
 */
public class TestPublicRenderParameters extends BaseTest2{

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestPublicRenderParameters");

	public TestPublicRenderParameters(String s) {
		super(s);
	}

  public void testPublicRenderParametersInDeploymentDescriptor() throws PortletContainerException {
    log.info("testPublicRenderParametersInDeploymentDescriptor...");
    List public_params = portletApp_.getPublicRenderParameter();
    assertFalse(public_params.isEmpty());

    boolean has_public1 = false;
    boolean has_public2 = false;
    boolean has_publicFalse = false;

    Iterator iter = public_params.iterator();
    while (iter.hasNext()) {
      PublicRenderParameter element = (PublicRenderParameter) iter.next();
      String name = element.getIdentifier();
      if (name.equalsIgnoreCase("public1")) has_public1 = true;
      if (name.equalsIgnoreCase("public2")) has_public2 = true;
      if (name.equalsIgnoreCase("publicFalse")) has_publicFalse = true;
    }

    assertTrue(has_public1);
    assertTrue(has_public2);
    assertFalse(has_publicFalse);

    PortletData portlet = (PortletData) allPortletMetaData.get(PORTLET_APP_NAME + "/PortletToTestPublicRenderParameters");
    assertNotNull(portlet);
    List srp = portlet.getSupportedPublicRenderParameter();
    assertTrue(srp.contains("public1"));
    assertTrue(srp.contains("public2"));
    assertFalse(srp.contains("public1False"));
    assertFalse(srp.contains("public2False"));
    log.info("done.");
  }

  /**
   *PLT 11.1.1
   * The getParameterValues method returns an array of String objects containing all the
   * parameter values associated with a parameter name. The value returned from the
   * getParameter method must be the first value in the array of String objects returned by
   * getParameterValues  . If there is a single parameter value associated with a 25
   * parameter name the method returns must return an array of size one containing the
   * parameter value. . The getParameterMap method must return an unmodifiable Map
   * object . If the request does not have any parameter, the getParameterMap must return
   * an empty Map object .
   *
   *
   */

  public void testPublicRenderParametersSet() throws PortletContainerException {
    log.info("testPublicRenderParametersSet...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestPublicRenderParameters");
    renderInput.setPortletMode(PortletMode.VIEW);
    HashMap params = new HashMap();
    params.put("public1",new String[]{"Everything is ok"});
    params.put("public2", new String[]{"1", "2","3"});

    renderInput.setRenderParameters(params);

    RenderOutput rO = portletContainer.render(request, response, renderInput);

    assertNotNull(rO.getContent());
    assertTrue(new String(rO.getContent()).startsWith("Everything is ok"));

    assertTrue(new String(rO.getContent()).indexOf("getParameterNamesOk") != -1);
    assertTrue(new String(rO.getContent()).indexOf("getParameterValuesOk") != -1);
    assertTrue(new String(rO.getContent()).indexOf("getParameterMapOk") != -1);

    log.info("done.");
  }


}
