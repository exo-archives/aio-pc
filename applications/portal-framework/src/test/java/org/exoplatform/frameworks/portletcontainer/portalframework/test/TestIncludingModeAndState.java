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
import org.exoplatform.Constants;
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
public class TestIncludingModeAndState extends BaseTest {

  public TestIncludingModeAndState(String s) {
    super(s);
  }

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.portalframework.test.TestIncludingModeAndState");

  public void testIncludingModeAndState() throws PortletContainerException {

    /**
     * PLT 7.1.2
     * If the portlet mode is not set for a URL,
     * it must have the portlet mode of the current request as default
     * If the window state is not set for a URL, it must have the
     * window state of the current request as default
     */

    try{

    log.info("testIncludingModeAndState...");
    String markupType = "text/html";
    ArrayList<String> requestedPortlets = new ArrayList<String>();
    requestedPortlets.add(key1);

    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    request.setParameter(Constants.TYPE_PARAMETER, Constants.PORTAL_RENDER);
    request.setParameter(Constants.WINDOW_STATE_PARAMETER, "minimized");
    request.setParameter(Constants.PORTLET_MODE_PARAMETER, "edit");
    request.setParameter("portal:componentId", key1);

    ArrayList<PortletInfo> resultList  = framework.processRequest(mockServletContext, request, response,
        markupType, requestedPortlets);


    MockServletRequest request2 = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response2 = new MockServletResponse(new EmptyResponse());
    request2.setParameter(Constants.TYPE_PARAMETER, Constants.PORTAL_RENDER);
    //request2.setParameter("portal:windowState", "minimized");
    //request2.setParameter("portal:portletMode", "edit");
    request2.setParameter("portal:componentId", key1);

    ArrayList<PortletInfo> resultList2  = framework.processRequest(mockServletContext, request2, response2,
        markupType, requestedPortlets);
         
    
    Iterator it = resultList.iterator();
    while (it.hasNext()) {
      PortletInfo pInfo = (PortletInfo)it.next();
      if (pInfo.getPortlet().equals(key1)){
        assertTrue(pInfo.getOut().indexOf("minimized") > -1);
         assertTrue(pInfo.getOut().indexOf("edit") > -1);
      }
    }
    log.info("Done.");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
