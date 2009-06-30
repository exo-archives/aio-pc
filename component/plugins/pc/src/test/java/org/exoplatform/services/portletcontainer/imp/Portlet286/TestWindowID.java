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

public class TestWindowID  extends BaseTest2{

private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestWindowID");

  public TestWindowID(String s) {
    super(s);
  }

  public void testWindowID() throws PortletContainerException {
    log.info("testWindowID...");

    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestWindowID");
    RenderOutput o = portletContainer.render(request, response, renderInput);
    String wid = ((ExoWindowID)renderInput.getInternalWindowID()).getUniqueID();
    assertTrue(new String(o.getContent()).equals(wid));
    log.info("Done.");
  }

}
