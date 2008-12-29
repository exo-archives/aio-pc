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

package org.exoplatform.services.wsrp1.test;

import java.rmi.RemoteException;

import javax.portlet.PortletPreferences;

import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.wsrp1.type.WS1BlockingInteractionResponse;
import org.exoplatform.services.wsrp1.type.WS1InteractionParams;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1NamedString;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1StateChange;
import org.exoplatform.services.wsrp1.type.WS1UpdateResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
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
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    WS1RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new WS1RegistrationContext("", null, null);
    String portletHandle = CONTEXT_PATH + "/PortletToTestPerformBlockingInteraction";
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(portletHandle);
    WS1NamedString nS1 = new WS1NamedString();
    nS1.setName("name1");
    nS1.setValue("value1");
    WS1NamedString nS2 = new WS1NamedString();
    nS2.setName("name2");
    nS2.setValue("value2");
    WS1NamedString[] array = new WS1NamedString[2];
    array[0] = nS1;
    array[1] = nS2;
    WS1InteractionParams params = new WS1InteractionParams();
    params.setPortletStateChange(WS1StateChange.readWrite);
    params.setFormParameters(array);
    WS1BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                             portletContext,
                                                                                             params);
    WS1BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
    WS1UpdateResponse updateResponse = response.getUpdateResponse();
    assertEquals(WindowStates._maximized_wsrp, updateResponse.getNewWindowState());
    assertEquals(Modes._edit_wsrp, updateResponse.getNewMode());
    String navigationalState = updateResponse.getNavigationalState();
    markupParams.setNavigationalState(navigationalState);
    //look if we obtain the portlet state (case of consumer save state)
    byte[] portletState = updateResponse.getPortletContext().getPortletState();
    if (portletState != null) {
      System.out.println("[test] consumer save state");
      portletContext.setPortletState(portletState);
    }
    runtimeContext.setSessionID(updateResponse.getSessionContext().getSessionID());
    WS1MarkupRequest getMarkup = getMarkup(rc, portletContext);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    WS1MarkupResponse responseMarkup = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("value1", responseMarkup.getMarkupContext().getMarkupString());
    //test optimization cases
    if (updateResponse.getMarkupContext() != null)
      assertEquals(responseMarkup.getMarkupContext().getMarkupString(),
                   updateResponse.getMarkupContext().getMarkupString());
  }

  public void testSendRedirect() throws RemoteException {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    WS1RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new WS1RegistrationContext("", null, null);
    WS1PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/Portlet2TestSendRedirect");
    portletContext.setPortletState(null);//TODO
    WS1InteractionParams params = new WS1InteractionParams();
    params.setPortletStateChange(WS1StateChange.readWrite);
    WS1BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                             portletContext,
                                                                                             params);
    WS1BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
    assertEquals("/path/to/redirect/to.jsp", response.getRedirectURL());
  }

  public void testReadOnlyStateChange() throws RemoteException {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    WS1RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new WS1RegistrationContext("", null, null);
    WS1PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/Portlet2TestStateUser");
    WS1InteractionParams params = new WS1InteractionParams();
    params.setPortletStateChange(WS1StateChange.readOnly);
    WS1BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
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
    WS1RegistrationContext rc = registrationOperationsInterface.register(registrationData);
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/Portlet2TestStateUser2");
    WS1InteractionParams params = new WS1InteractionParams();
    params.setPortletStateChange(WS1StateChange.cloneBeforeWrite);
    WS1BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                             portletContext,
                                                                                             params);
    WS1BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
    assertNotSame(CONTEXT_PATH + "/Portlet2TestStateUser/windowID", response.getUpdateResponse()
                                                                            .getPortletContext()
                                                                            .getPortletHandle());
  }

  public void testStateSaveOnConsumer() throws RemoteException {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    WS1RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new WS1RegistrationContext("", null, null);
    String portletHandle = CONTEXT_PATH + "/Portlet2TestStateUser3";
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(portletHandle);
    WS1InteractionParams params = new WS1InteractionParams();
    params.setPortletStateChange(WS1StateChange.readWrite);
    WS1BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
                                                                                             portletContext,
                                                                                             params);
    WS1UpdateResponse updateResponse = markupOperationsInterface.performBlockingInteraction(performBlockingInteraction)
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
    runtimeContext.setSessionID(updateResponse.getSessionContext().getSessionID());
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

  private WS1BlockingInteractionRequest getPerformBlockingInteraction(WS1RegistrationContext rc,
                                                                      WS1PortletContext portletContext,
                                                                      WS1InteractionParams params) {
    WS1BlockingInteractionRequest performBlockingInteraction = new WS1BlockingInteractionRequest();
    performBlockingInteraction.setRegistrationContext(rc);
    performBlockingInteraction.setPortletContext(portletContext);
    performBlockingInteraction.setRuntimeContext(runtimeContext);
    performBlockingInteraction.setUserContext(userContext);
    performBlockingInteraction.setMarkupParams(markupParams);
    performBlockingInteraction.setInteractionParams(params);
    return performBlockingInteraction;
  }

}
