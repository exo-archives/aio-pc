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

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp.type.ClonePortletRequest;
import org.exoplatform.services.wsrp.type.DestroyPortletsRequest;
import org.exoplatform.services.wsrp.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp.type.GetPortletPropertiesRequest;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.Property;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.SetPortletPropertiesRequest;

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
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    String[] keys = StringUtils.split(returnedPH, "/");
    assertTrue(keys.length == 3);
  }

  public void testClonePortletWithBadRegistrationHandle() {
    RegistrationContext rC = new RegistrationContext("", null, null);
    rC.setRegistrationHandle("dummy_handle");
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    try {
      ClonePortletRequest clonePortlet = new ClonePortletRequest();
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
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/dummy");
    try {
      ClonePortletRequest clonePortlet = new ClonePortletRequest();
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
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);
    pC = portletManagementOperationsInterface.clonePortlet(clonePortlet);
    assertNotSame(returnedPH, pC.getPortletHandle());

    GetPortletPropertiesRequest getPortletProperties = new GetPortletPropertiesRequest();
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
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    String[] array = { returnedPH };
    DestroyPortletsRequest destroyPortlets = new DestroyPortletsRequest();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.setPortletHandles(array);
    DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    assertTrue(response.getDestroyFailed() == null || response.getDestroyFailed().length == 0);
  }

  public void testDestroyNonClonedPortlet() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    String[] array = { "hello/HelloWorld/dummy" };
    DestroyPortletsRequest destroyPortlets = new DestroyPortletsRequest();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.setPortletHandles(array);
    DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    assertEquals("hello/HelloWorld/dummy", response.getDestroyFailed(0).getPortletHandle());
  }

  public void testGetPortletProperty() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);
    GetPortletPropertiesRequest getPortletProperties = new GetPortletPropertiesRequest();
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
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);
    GetPortletPropertiesRequest getPortletProperties = new GetPortletPropertiesRequest();
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
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    PortletContext pC = fillPortletContext("hello/HelloWorld");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    pC.setPortletHandle(returnedPH);

    Property property = new Property();
    property.setLang("en");
    property.setName("test-prop");
    property.setStringValue("test-value");

    PropertyList list = new PropertyList();
    list.setProperties(new Property[] { property });

    SetPortletPropertiesRequest setPortletProperties = new SetPortletPropertiesRequest();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(pC);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(list);
    PortletContext pCReturned = portletManagementOperationsInterface.setPortletProperties(setPortletProperties);
    assertEquals(returnedPH, pCReturned.getPortletHandle());

    GetPortletPropertiesRequest getPortletProperties = new GetPortletPropertiesRequest();
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
