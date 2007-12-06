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

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

public class TestRenderParamsAviability extends BaseTest2 {
  
  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestRenderParamsAviability");

  public TestRenderParamsAviability(String s) {
    super(s);
  }

  /**Max Shaposhnik  @12.2007
   * PLT 11.1.1.4
   * For serveResource requests the portlet must receive any resource parameters that were 
   * explicitly set on the ResourceURL that triggered the request. If the cacheability level of 
   * that resource URL (see PLT.13.7) was PORTLET or PAGE, the portlet must also receive the 
   * render parameters present in the request in which the URL was created 
   */

  public void testRenderParamsAviability() throws PortletContainerException {
    log.info("testRenderParamsAviability...");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestRenderParamsAviability");
    renderInput.setPortletMode(PortletMode.VIEW);
    Map pms = new HashMap();
    pms.put("test", "12345");
    pms.put("test2", "67890");
    renderInput.setRenderParameters(pms);

    RenderOutput o2 = portletContainer.render(request, response, renderInput);
    String rURL = new String(o2.getContent());
    //System.out.println("RESULT" + rURL);
    assertTrue(rURL.indexOf("test=not_A_numbers") > 0);
    assertTrue(rURL.indexOf("test=12345") > 0);
    assertTrue(rURL.indexOf("test2=67890") > 0);
    log.info("Done.");
  }

}
