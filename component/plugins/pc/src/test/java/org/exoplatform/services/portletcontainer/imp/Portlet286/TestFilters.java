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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Created by The eXo Platform SAS Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua 24.04.2007
 */
public class TestFilters extends BaseTest2 {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestFilters");

  public TestFilters(String s) {
    super(s);
  }

  public void testFilters() throws PortletContainerException {
    log();
    log.info("testFilters...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    ((ExoWindowID) actionInput.getInternalWindowID()).setPortletName("PortletToTestFilters");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response, actionInput);
    assertEquals("Everything is ok", ((aO.getRenderParameters().get("status")))[0]);

    ((ExoWindowID) renderInput.getInternalWindowID()).setPortletName("PortletToTestFilters");
    RenderOutput o = portletContainer.render(request, response, renderInput);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
    log.info("done.");
  }

  public void testFiltersFailChain() throws PortletContainerException {
    log();
    log.info("testFilters...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    Map<String, String[]> renderParameters = new HashMap<String, String[]>();
    renderParameters.put("EXO_FAIL_CHAIN", new String[] { "true" });

    actionInput.setRenderParameters(renderParameters);
    ((ExoWindowID) actionInput.getInternalWindowID()).setPortletName("PortletToTestFilters");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response, actionInput);
    assertEquals(0, aO.getRenderParameters().size());

    renderInput.setRenderParameters(renderParameters);
    ((ExoWindowID) renderInput.getInternalWindowID()).setPortletName("PortletToTestFilters");
    RenderOutput o = portletContainer.render(request, response, renderInput);
    assertNotNull(o.getContent());
    assertTrue(new String(o.getContent()).startsWith("The filter's html markup!"));
    log.info("done.");
  }

  /**
   * Max Shaposhnik 09/2007 PLT 20.2.4 The portlet container must instantiate
   * exactly one instance of the Java class defining the filter per filter
   * declaration in the deployment descriptor. Filters can be associated with
   * groups of portlets using the '*' character as a wildcard at the end of a
   * string to indicate that the filter must be applied to any portlet whose
   * name starts with the characters before the "*" character. (look at
   * portlet.xml to make sure this is working)
   */

  public void testInstances() throws PortletContainerException {
    log();
    log.info("testInstances...");

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    ((ExoWindowID) actionInput.getInternalWindowID()).setPortletName("PortletToTestFilters");
    actionInput.setPortletMode(PortletMode.VIEW);
    ActionOutput aO = portletContainer.processAction(request, response, actionInput);

    String id1 = ((aO.getRenderParameters().get("filterID")))[0];

    MockServletRequest request2 = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response2 = new MockServletResponse(new EmptyResponse());

    try {
      ((ExoWindowID) actionInput.getInternalWindowID()).setPortletName("PortletToTestFilters2");
      actionInput.setPortletMode(PortletMode.VIEW);
      ActionOutput a2 = portletContainer.processAction(request2, response2, actionInput);
      String id2 = ((a2.getRenderParameters().get("filterID")))[0];

      assertTrue(id1.equals(id2));
      log.info("Done.");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Max Shaposhnik 09/2007 PLT 20.2.1 Before a filter instance can be removed
   * from service by the portlet container, the portlet container must first
   * call the destroy method on the filter to enable the filter to release any
   * resources and perform other cleanup operations.
   */

  public void testDestroy() throws PortletContainerException {
    log();
    log.info("testDestroy...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());

    ((ExoWindowID) actionInput.getInternalWindowID()).setPortletName("PortletToTestFilters");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response, actionInput);
    assertEquals("Everything is ok", ((aO.getRenderParameters().get("status")))[0]);

    try {
      portletApplicationRegister.removePortletApplication(mockServletContext, PORTLET_APP_NAME);
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("Done.");
  }

}
