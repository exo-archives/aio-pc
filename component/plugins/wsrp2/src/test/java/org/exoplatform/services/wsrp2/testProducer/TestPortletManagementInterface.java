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

package org.exoplatform.services.wsrp2.testProducer;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestPortletManagementInterface extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testClonePortlet() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    String[] keys = StringUtils.split(returnedPH, "/");
    assertTrue(keys.length == 3);
  }

  public void testClonePortletWithBadRegistrationHandle() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);
    registrationContext.setRegistrationHandle("dummy_handle");
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    try {
      ClonePortlet clonePortlet = new ClonePortlet();
      clonePortlet.setRegistrationContext(registrationContext);
      clonePortlet.setPortletContext(pC);
      clonePortlet.setUserContext(userContext);
      portletManagementOperationsInterface.clonePortlet(clonePortlet);
      fail("The given registration handle was incorrect");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testClonePortletWithBadPortletHandle() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/dummy");
    try {
      ClonePortlet clonePortlet = new ClonePortlet();
      clonePortlet.setRegistrationContext(rC);
      clonePortlet.setPortletContext(pC);
      clonePortlet.setUserContext(userContext);
      portletManagementOperationsInterface.clonePortlet(clonePortlet);
      fail("The given portlet handle was incorrect");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testCloneAlreadyClonedPortlet() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);
    pC = portletManagementOperationsInterface.clonePortlet(clonePortlet);
    assertNotSame(returnedPH, pC.getPortletHandle());

    GetPortletProperties getPortletProperties = new GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    getPortletProperties.getNames().add("time-format");
    PropertyList list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    List<Property> propList = list.getProperties();
    assertEquals(1, propList.size());
    Property property = propList.get(0);
    assertEquals("HH", property.getStringValue());
  }

  public void testDestroyPortlet() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.getPortletHandles().add(returnedPH);
    DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    assertTrue(response.getFailedPortlets() == null || response.getFailedPortlets().size() == 0);
  }

  public void testDestroyNonClonedPortlet() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    String portletHandle = CONTEXT_PATH + "/HelloWorld/dummy";
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.getPortletHandles().add(portletHandle);
    DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    assertEquals(CONTEXT_PATH + "/HelloWorld/dummy", response.getFailedPortlets()
                                                             .get(0)
                                                             .getPortletHandles()
                                                             .get(0));
  }

  public void testGetPortletProperty() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);
    GetPortletProperties getPortletProperties = new GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    PropertyList list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    List<Property> propList = list.getProperties();
    
    for (Property property : propList) {
      if ("time-format".equals(property.getName().toString())) {
        assertEquals("HH", property.getStringValue());
        return;
      }
    }
    fail("A property should have been found!!!");
  }

  public void testWellKnownGetPortletProperty() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);
    GetPortletProperties getPortletProperties = new GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    getPortletProperties.getNames().add("time-format");
    PropertyList list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    List<Property> propList = list.getProperties();
    assertEquals(1, propList.size());
    Property property = propList.get(0);
    assertEquals("HH", property.getStringValue());
  }

  public void testSetPortletProperty() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);

    Property property = new Property();
    QName propertyName = new QName("test-prop");
    property.setName(propertyName);
    property.setStringValue("test-value");

    PropertyList list = new PropertyList();
    list.getProperties().add(property);

    SetPortletProperties setPortletProperties = new SetPortletProperties();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(pC);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(list);
    PortletContext pCReturned = portletManagementOperationsInterface.setPortletProperties(setPortletProperties);
    assertEquals(returnedPH, pCReturned.getPortletHandle());

    GetPortletProperties getPortletProperties = new GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    List<Property> propList = list.getProperties();
    
    for (Property property2 : propList) {
      System.out.println("prop : " + property.getName().toString());
      if ("test-prop".equals(property.getName().toString())) {
        assertEquals("test-value", property.getStringValue());
        return;
      }
    }
    fail("A property should have been found!!!");
  }

  private PortletContext fillPortletContext(String portletHandle) {
    log();
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    return portletContext;
  }

}
