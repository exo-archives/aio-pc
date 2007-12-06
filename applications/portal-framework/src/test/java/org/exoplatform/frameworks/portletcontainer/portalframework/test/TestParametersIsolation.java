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
public class TestParametersIsolation extends BaseTest {


  public TestParametersIsolation(String s) {
    super(s);
  }

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.portalframework.test.TestParametersIsolation");


 public void testParametersIsolation() throws PortletContainerException {

    /**
     * PLT 11.1.1.2
     * Must be into portal-framework tests
     * The portlet-container must not propagate parameters received in an action,or
     * event request to subsequent render requests of the portlet.  The portlet-container must
     * not propagate parameters received in an action to subsequent event requests of the portlet.
     */

   log.info("testParametersIsolation...");
   String markupType = "text/html";
   ArrayList requestedPortlets = new ArrayList();
   requestedPortlets.add("war_template/PortletToTestParametersIsolation");

   MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
   HttpServletResponse response = new MockServletResponse(new EmptyResponse());
   request.setParameter("portal:action", "event");
   // optionally can be action or event
   //request.setParameter("portal:action", "action");
   request.setParameter("portal:windowState", "normal");
   request.setParameter("portal:portletMode", "view");
   request.setParameter("portal:testParamether", "123");
   request.setParameter("portal:componentId", "war_template/PortletToTestParametersIsolation");

   ArrayList resultList  = framework.processRequest(mockServletContext, request, response,
       markupType, requestedPortlets);

   PortletInfo pInfo = (PortletInfo)resultList.get(1);
   assertTrue(pInfo.getOut().indexOf("OK") > -1);
   log.info("Done.");
 }
}
