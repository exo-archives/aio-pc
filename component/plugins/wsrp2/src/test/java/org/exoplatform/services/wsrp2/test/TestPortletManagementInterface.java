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
import org.exoplatform.services.wsrp2.type.SetPortletProperties;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestPortletManagementInterface extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestPortletManagementInterface.setUp()");
  }

  public void testClonePortlet() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    String[] keys = StringUtils.split(returnedPH, "/");
    assertTrue(keys.length == 3);
  }

  public void testClonePortletWithBadRegistrationHandle() {
    RegistrationContext rC = new RegistrationContext(null, null, null, null);
    rC.setRegistrationHandle("dummy_handle");
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    try {
      ClonePortlet clonePortlet = new ClonePortlet();
      clonePortlet.setRegistrationContext(rC);
      clonePortlet.setPortletContext(pC);
      clonePortlet.setUserContext(userContext);
      portletManagementOperationsInterface.clonePortlet(clonePortlet);
      fail("The given registration handle was incorrect");
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void testClonePortletWithBadPortletHandle() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/dummy");
    try {
      ClonePortlet clonePortlet = new ClonePortlet();
      clonePortlet.setRegistrationContext(rC);
      clonePortlet.setPortletContext(pC);
      clonePortlet.setUserContext(userContext);
      portletManagementOperationsInterface.clonePortlet(clonePortlet);
      fail("The given portlet handle was incorrect");
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void testCloneAlreadyClonedPortlet() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
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
    getPortletProperties.setNames(new String[] { "time-format" });
    PropertyList list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    Property[] propArray = list.getProperties();
    assertEquals(1, propArray.length);
    Property property = propArray[0];
    assertEquals("HH", property.getStringValue());
  }

  public void testDestroyPortlet() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    String[] array = { returnedPH };
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.setPortletHandles(array);
    DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    assertTrue(response.getFailedPortlets() == null || response.getFailedPortlets().length == 0);
  }

  public void testDestroyNonClonedPortlet() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    String[] array = { "hello/HelloWorld/dummy" };
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.setPortletHandles(array);
    DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    assertEquals("hello/HelloWorld/dummy", response.getFailedPortlets(0).getPortletHandles(0));
  }

  public void testGetPortletProperty() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
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
    Property[] propArray = list.getProperties();
    for (int i = 0; i < propArray.length; i++) {
      Property property = propArray[i];
      if ("time-format".equals(property.getName())) {
        assertEquals("HH", property.getStringValue());
        return;
      }
    }
    fail("A property should have been found!!!");
  }

  public void testWellKnownGetPortletProperty() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
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
    getPortletProperties.setNames(new String[] { "time-format" });
    PropertyList list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    Property[] propArray = list.getProperties();
    assertEquals(1, propArray.length);
    Property property = propArray[0];
    assertEquals("HH", property.getStringValue());
  }

  public void testSetPortletProperty() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
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
    list.setProperties(new Property[] { property });

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
    Property[] propArray = list.getProperties();
    for (int i = 0; i < propArray.length; i++) {
      property = propArray[i];
      System.out.println("prop : " + property.getName());
      if ("test-prop".equals(property.getName())) {
        assertEquals("test-value", property.getStringValue());
        return;
      }
    }
    fail("A property should have been found!!!");
  }

  private PortletContext fillPortletContext(String portletHandle) {
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    return portletContext;
  }

}
