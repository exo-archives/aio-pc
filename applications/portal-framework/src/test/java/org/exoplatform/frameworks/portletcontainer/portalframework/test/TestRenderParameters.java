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
package org.exoplatform.frameworks.portletcontainer.portalframework.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Created by The eXo Platform SAS  2007 .
 *
 * @author Max Shaposhnik
 * @version $Id$
 */
public class TestRenderParameters extends BaseTest {

  public TestRenderParameters(String s) {
    super(s);
  }

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.portalframework.test.testRenderParameters");

  public void testRenderParameters() throws PortletContainerException {

     /** PLT 11.1.1.2
      * If a portlet wants to do that in either the processAction or processEvent methods, it
      * must use the  setRenderParameter or  setRenderParameters  methods of the
      * StateAwareResponse object within the processAction or processEvent call. The set
      * render parameters must be provided to the processEvent and render calls of at least
      * the current client request.
      */

    log.info("testRenderParameters...");
    String markupType = "text/html";
    ArrayList<String> requestedPortlets = new ArrayList<String>();
    requestedPortlets.add(key3);

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    request.setParameter("portal:type", "action");
    request.setParameter("portal:windowState", "normal");
    request.setParameter("portal:portletMode", "view");
    request.setParameter("portal:componentId", key3);

    ArrayList resultList  = framework.processRequest(mockServletContext, request, response,
        markupType, requestedPortlets);

    Iterator it = resultList.iterator();
    while (it.hasNext()) {
      PortletInfo pInfo = (PortletInfo)it.next();
      if (pInfo.getPortlet().equals(key3)){
        assertTrue(pInfo.getOut().indexOf("OK") > -1);
      }
      
    }
     log.info("Done.");
  }

}
