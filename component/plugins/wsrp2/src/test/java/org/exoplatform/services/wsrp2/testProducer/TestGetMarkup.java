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

package org.exoplatform.services.wsrp2.testProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.wsrp2.producer.impl.helpers.NamedStringWrapper;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InitCookie;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.utils.JAXBEventTransformer;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestGetMarkup extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testGetMarkupForSeveralModes() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getItemString());
    markupParams.setMode("wsrp:help");
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Help mode", response.getMarkupContext().getItemString());
  }

  public void testGetMarkupForSeveralWindowStates() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getItemString());
    markupParams.setWindowState("wsrp:maximized");
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Maximized state", response.getMarkupContext().getItemString());
  }

  public void testGetMarkupWithRewrittenURLInIt() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    int index = s.indexOf("&ns=");
    s = s.substring(index + "&ns=".length());
    index = s.indexOf("&");
    s = s.substring(0, index);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:maximized");
    navigationalContext.setOpaqueValue(s);
    markupParams.setNavigationalContext(navigationalContext);
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("value", response.getMarkupContext().getItemString());
  }

  public void testGetMarkupOfAClonedPortlet() throws Exception {
    log();
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertRegistrationContext(rC);

    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/HelloWorld2");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(portletContext);
    
    clonePortlet.setUserContext(userContext);
    PortletContext returnedPC = portletManagementOperationsInterface.clonePortlet(clonePortlet);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    
    //set some new properties
    Property property = new Property();
    QName propertyName = new QName("test-prop");
    property.setName(propertyName);
    property.setStringValue("test-value");
    PropertyList list = new PropertyList();
    list.getProperties().add(property);
    SetPortletProperties setPortletProperties = new SetPortletProperties();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(returnedPC);
    
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(list);
    returnedPC = portletManagementOperationsInterface.setPortletProperties(setPortletProperties);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    GetMarkup getMarkup = getMarkup(rC, returnedPC);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    
    assertEquals("Everything is more than ok", response.getMarkupContext().getItemString());
  }

  public void testGetResource() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestResource";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetResource getResource = getResource(registrationContext, portletContext);
    ResourceResponse response = markupOperationsInterface.getResource(getResource);
    assertEquals("Everything is ok", response.getResourceContext().getItemString());

    NamedString formParameter = new NamedStringWrapper("goal", "image");
//    formParameter.setName("goal");
//    formParameter.setValue("image");
    resourceParams.getFormParameters().add(formParameter);
    response = markupOperationsInterface.getResource(getResource);
    assertEquals("image/jpeg", response.getResourceContext().getMimeType());
  }

  public void testHandleEvents() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestEvent";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    javax.portlet.Event event286 = new EventImpl(new QName("MyEventProc"),
                                                 new String("event-value"));
    Event event = JAXBEventTransformer.getEventMarshal(event286);
    eventParams.getEvents().add(event);
    HandleEvents handleEvents = handleEvents(registrationContext, portletContext);
    HandleEventsResponse response = markupOperationsInterface.handleEvents(handleEvents);

    assertNotNull(response);
    UpdateResponse updateResponse = response.getUpdateResponse();
    assertNotNull(updateResponse);

    List<Event> events = updateResponse.getEvents();
    assertNotNull(events);
    assertEquals(1, events.size());

    Event event1 = events.get(0);
    assertEquals("MyEventPub", event1.getName().getLocalPart());
    assertEquals("org.exoplatform.services.portletcontainer.test.events.MyEventPub",
                 event1.getType().getLocalPart());
  }

  public void testGetMarkupForSeveralModesWithLifetime() throws Exception {
    log();
    // WARNING: this test depends on time
    register.setLifetime(getLifetimeInSec(10));
    RegistrationContext rc = registrationOperationsInterface.register(register);
    assertNotNull(rc);
    assertNotNull(rc.getRegistrationHandle());
    assertNotNull(rc.getScheduledDestruction());

    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    updateCurrentTime(rc);

    GetMarkup getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertNotNull(response.getMarkupContext());
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getItemString());
    register.setLifetime(null);
  }

  /**
   * WARNING: this test depends on time.
   * 
   * @throws Exception
   */
  public void testGetMarkupForSeveralModesWithInvalidLifetime() throws Exception {
    log();
    register.setLifetime(getLifetimeInSec(1));
    RegistrationContext rc = registrationOperationsInterface.register(register);
    assertNotNull(rc.getRegistrationHandle());
    assertNotNull(rc.getScheduledDestruction());
    Thread.currentThread().sleep(2000);

    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    updateCurrentTime(rc);

    GetMarkup getMarkup = getMarkup(rc, portletContext);

    try {
      markupOperationsInterface.getMarkup(getMarkup);
      fail("the getMarkup of the markupOperationsInterface should return a WS Fault");
    } catch (Exception e) {
    }
    register.setLifetime(null);
  }

  /**
   * WARNING: this test depends on time.
   * 
   * @throws Exception
   */
  public void testGetMarkupForSeveralModesWithLifetime2() throws Exception {
    log();
    register.setLifetime(getLifetimeInSec(3));
    RegistrationContext rc = registrationOperationsInterface.register(register);
    assertNotNull(rc.getRegistrationHandle());
    assertNotNull(rc.getScheduledDestruction());

    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    updateCurrentTime(rc);

    GetMarkup getMarkup = getMarkup(rc, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertNotNull(response.getMarkupContext());

    Thread.currentThread().sleep(3000);

    updateCurrentTime(rc);
    try {
      markupOperationsInterface.getMarkup(getMarkup);
      fail("the getMarkup of the markupOperationsInterface should return a WS Fault");
    } catch (Exception e) {
    }
    register.setLifetime(null);
  }

  public void testInitCookie() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    InitCookie initCookie = new InitCookie();
    initCookie.setRegistrationContext(registrationContext);
    initCookie.setUserContext(userContext);
    List<Extension> response = markupOperationsInterface.initCookie(initCookie);
    assertEquals(new ArrayList<Extension>(), response);
  }

}
