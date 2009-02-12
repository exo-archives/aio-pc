/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp1.testProducer;

import java.util.Arrays;

import org.exoplatform.services.wsrp1.type.WS1GetMarkup;
import org.exoplatform.services.wsrp1.type.WS1GetServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1ReleaseSessions;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestWSRPSession extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestWSRPSession.setUp()");
  }

//  public void testSession() throws Exception {
//    WS1GetServiceDescription getServiceDescription = new WS1GetServiceDescription();
//    getServiceDescription.getDesiredLocales().add("en");
//    WS1ServiceDescription sd = getServiceDescription(getServiceDescription);
//    createRegistrationContext(sd);
//    String portletHandle = CONTEXT_PATH + "/PortletToTestSession";
//    WS1PortletContext portletContext = new WS1PortletContext();
//    portletContext.setPortletHandle(portletHandle);
//    portletContext.setPortletState(null);
//    markupParams.getMimeTypes().addAll(Arrays.asList(mimeTypes));
//    markupParams.setMode("wsrp:view");
//    markupParams.setWindowState("wsrp:normal");
//    WS1GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
//    WS1MarkupResponse response = getMarkup(getMarkup);
//    String sessionID = response.getSessionContext().getSessionID();
//    runtimeContext.setSessionID(sessionID);
//    manageTemplatesOptimization(sd, portletHandle);
//    manageUserContextOptimization(sd, portletHandle, getMarkup);
//    response = getMarkup(getMarkup);
//    assertEquals("attribute set in first call", response.getMarkupContext().getMarkupString());
//  }

  public void testReleaseSession() throws Exception {
    WS1GetServiceDescription getServiceDescription = new WS1GetServiceDescription();
    getServiceDescription.getDesiredLocales().add("en");
    WS1ServiceDescription sd = getServiceDescription(getServiceDescription);
    createRegistrationContext(sd);
    WS1PortletContext portletContext = new WS1PortletContext();
    String portletHandle = CONTEXT_PATH + "/PortletToTestSession";
    portletContext.setPortletHandle(portletHandle);
    markupParams.getMimeTypes().addAll(Arrays.asList(mimeTypes));
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");
    WS1GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    WS1MarkupResponse response = getMarkup(getMarkup);
    String sessionID = response.getSessionContext().getSessionID();
    WS1ReleaseSessions releaseSessions = new WS1ReleaseSessions();
    releaseSessions.setRegistrationContext(registrationContext);
    releaseSessions.getSessionIDs().add(sessionID);
    markupOperationsInterface.releaseSessions(WSRPTypesTransformer.getWS2ReleaseSessions(releaseSessions));
    runtimeContext.setSessionID(sessionID);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    try {
      getMarkup(getMarkup);
      fail("Session should not exist anymore");
    } catch (InvalidSession e) {
    }

  }
}
