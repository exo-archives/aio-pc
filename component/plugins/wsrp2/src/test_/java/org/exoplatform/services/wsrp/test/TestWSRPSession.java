/*
 * Copyright 2001-2007 The eXo platform SAS  All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 11 janv. 2004
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
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class TestWSRPSession extends BaseTest{

  public TestWSRPSession(String s) {
    super(s);
  }

  public void testSession() throws Exception {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[]{"en"});
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription) ;
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    String portletHandle = "hello/PortletToTestSession";
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
    getServiceDescription.setDesiredLocales(new String[]{"en"});
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription) ;
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    PortletContext portletContext = new PortletContext();
    String portletHandle = "hello/PortletToTestSession";
    portletContext.setPortletHandle(portletHandle);
    markupParams.setMimeTypes(mimeTypes);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");
    MarkupRequest getMarkup = getMarkup(rc,portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String sessionID = response.getSessionContext().getSessionID();
    ReleaseSessionsRequest releaseSessions = new ReleaseSessionsRequest();
    releaseSessions.setRegistrationContext(rc);
    releaseSessions.setSessionIDs(new String[]{sessionID});
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
