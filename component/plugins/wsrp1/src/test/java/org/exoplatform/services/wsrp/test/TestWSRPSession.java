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

package org.exoplatform.services.wsrp.test;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ReleaseSessionsRequest;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.ServiceDescriptionRequest;

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
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext("", null, null);
    String portletHandle = CONTEXT_PATH + "/PortletToTestSession";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    markupParams.setMimeTypes(mimeTypes);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");
    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String sessionID = response.getSessionContext().getSessionID();
    runtimeContext.setSessionID(sessionID);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("attribute set in first call", response.getMarkupContext().getMarkupString());
  }

  public void testReleaseSession() throws RemoteException {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext("", null, null);
    PortletContext portletContext = new PortletContext();
    String portletHandle = CONTEXT_PATH + "/PortletToTestSession";
    portletContext.setPortletHandle(portletHandle);
    markupParams.setMimeTypes(mimeTypes);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");
    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String sessionID = response.getSessionContext().getSessionID();
    ReleaseSessionsRequest releaseSessions = new ReleaseSessionsRequest();
    releaseSessions.setRegistrationContext(rc);
    releaseSessions.setSessionIDs(new String[] { sessionID });
    markupOperationsInterface.releaseSessions(releaseSessions);
    runtimeContext.setSessionID(sessionID);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    try {
      markupOperationsInterface.getMarkup(getMarkup);
      fail("Session should not exist anymore");
    } catch (RemoteException e) {
    }

  }
}
