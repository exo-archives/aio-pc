/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.test;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestWSRPSession extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestWSRPSession.setUp()");
  }

  public void testSession() throws Exception {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, "");
    String portletHandle = CONTEXT_PATH + "/PortletToTestSession";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    markupParams.setMimeTypes(mimeTypes);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");
    GetMarkup getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String sessionID = response.getSessionContext().getSessionID();
    sessionParams.setSessionID(sessionID);
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("attribute set in first call", response.getMarkupContext().getItemString());
  }

  public void testReleaseSession() throws RemoteException {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, "");
    PortletContext portletContext = new PortletContext();
    String portletHandle = CONTEXT_PATH + "/PortletToTestSession";
    portletContext.setPortletHandle(portletHandle);
    markupParams.setMimeTypes(mimeTypes);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");
    GetMarkup getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String sessionID = response.getSessionContext().getSessionID();
    ReleaseSessions releaseSessions = new ReleaseSessions();
    releaseSessions.setRegistrationContext(rc);
    releaseSessions.setSessionIDs(new String[] { sessionID });
    markupOperationsInterface.releaseSessions(releaseSessions);
    sessionParams.setSessionID(sessionID);
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    try {
      markupOperationsInterface.getMarkup(getMarkup);
      fail("Session should not exist anymore");
    } catch (RemoteException e) {
    }

  }
}
