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

import javax.portlet.PortletPreferences;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestPerformBlockingInteraction extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestPerformBlockingInteraction.setUp()");
  }

  public void testSimplePerformBlockingInteraction() throws Exception {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, null);
    String portletHandle = "hello/PortletToTestPerformBlockingInteraction";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    NamedString nS1 = new NamedString();
    nS1.setName("name1");
    nS1.setValue("value1");
    NamedString nS2 = new NamedString();
    nS2.setName("name2");
    nS2.setValue("value2");
    NamedString[] array = new NamedString[2];
    array[0] = nS1;
    array[1] = nS2;
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readWrite);
    params.setFormParameters(array);
    PerformBlockingInteraction performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                          portletContext,
                                                                                          params);
    BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
    UpdateResponse updateResponse = response.getUpdateResponse();
    assertEquals(WindowStates._maximized_wsrp, updateResponse.getNewWindowState());
    assertEquals(Modes._edit_wsrp, updateResponse.getNewMode());
    NavigationalContext navigationalContext = updateResponse.getNavigationalContext();
    markupParams.setNavigationalContext(navigationalContext);
    //look if we obtain the portlet state (case of consumer save state)
    byte[] portletState = updateResponse.getPortletContext().getPortletState();
    if (portletState != null) {
      System.out.println("[test] consumer save state");
      portletContext.setPortletState(portletState);
    }
    SessionParams sessionParams = new SessionParams();
    sessionParams.setSessionID(updateResponse.getSessionContext().getSessionID());
    runtimeContext.setSessionParams(sessionParams);
    GetMarkup getMarkup = getMarkup(rc, portletContext);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    MarkupResponse responseMarkup = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("value1", responseMarkup.getMarkupContext().getItemString());
    //test optimization cases
    if (updateResponse.getMarkupContext() != null)
      assertEquals(responseMarkup.getMarkupContext().getItemString(),
                   updateResponse.getMarkupContext().getItemString());
  }

  public void testSendRedirect() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, null);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/Portlet2TestSendRedirect");
    portletContext.setPortletState(null);//TODO
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readWrite);
    PerformBlockingInteraction performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                          portletContext,
                                                                                          params);
    BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
    assertEquals("/path/to/redirect/to.jsp", response.getRedirectURL());
  }

  public void testReadOnlyStateChange() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, null);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/Portlet2TestStateUser");
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readOnly);
    PerformBlockingInteraction performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                          portletContext,
                                                                                          params);
    try {
      markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
      fail("The portlet is in read only state!!!");
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void testCloneBeforeWriteStateChange() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/Portlet2TestStateUser2");
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.cloneBeforeWrite);
    PerformBlockingInteraction performBlockingInteraction = getPerformBlockingInteraction(rC,
                                                                                          portletContext,
                                                                                          params);
    BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
    assertNotSame("hello/Portlet2TestStateUser/windowID", response.getUpdateResponse()
                                                                  .getPortletContext()
                                                                  .getPortletHandle());
  }

  public void testStateSaveOnConsumer() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, null);
    String portletHandle = "hello/Portlet2TestStateUser3";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readWrite);
    PerformBlockingInteraction performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                          portletContext,
                                                                                          params);
    UpdateResponse updateResponse = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction)
                                                             .getUpdateResponse();
    //look if we obtain the portlet state (case of consumer save state)
    byte[] portletState = updateResponse.getPortletContext().getPortletState();
    if (portletState != null) {
      System.out.println("[test] consumer save state : " + portletState);
    } else {
      return; //we don't test portlet test save on producer here
    }
    Object o = null;
    try {
      o = IOUtil.deserialize(portletState);
    } catch (Exception e) {
      fail("The object should be deserializable");
    }
    assertTrue(o instanceof PortletPreferences);
    PortletPreferences pref = (PortletPreferences) o;
    String element = (String) pref.getNames().nextElement();
    assertEquals("attName", element);
    assertEquals("attValue", pref.getValue(element, "default"));
    sessionParams.setSessionID(updateResponse.getSessionContext().getSessionID());
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, performBlockingInteraction);
    portletContext.setPortletState(portletState);
    updateResponse = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction)
                                              .getUpdateResponse();
    portletState = updateResponse.getPortletContext().getPortletState();
    try {
      o = IOUtil.deserialize(portletState);
    } catch (Exception e) {
      fail("The object should be deserializable");
    }
    assertTrue(o instanceof PortletPreferences);
    assertEquals("attValue2", ((PortletPreferences) o).getValue("attName2", "default"));
  }

  private PerformBlockingInteraction getPerformBlockingInteraction(RegistrationContext rc,
                                                                   PortletContext portletContext,
                                                                   InteractionParams params) {
    PerformBlockingInteraction performBlockingInteraction = new PerformBlockingInteraction();
    performBlockingInteraction.setRegistrationContext(rc);
    performBlockingInteraction.setPortletContext(portletContext);
    performBlockingInteraction.setRuntimeContext(runtimeContext);
    performBlockingInteraction.setUserContext(userContext);
    performBlockingInteraction.setMarkupParams(markupParams);
    performBlockingInteraction.setInteractionParams(params);
    return performBlockingInteraction;
  }

}
