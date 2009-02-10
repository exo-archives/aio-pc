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

package org.exoplatform.services.wsrp1.testProducer;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp1.type.WS1ClonePortlet;
import org.exoplatform.services.wsrp1.type.WS1DestroyPortlets;
import org.exoplatform.services.wsrp1.type.WS1DestroyPortletsResponse;
import org.exoplatform.services.wsrp1.type.WS1GetPortletProperties;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1Property;
import org.exoplatform.services.wsrp1.type.WS1PropertyList;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1SetPortletProperties;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestPortletManagementInterface extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestPortletManagementInterface.setUp()");
  }

  public void testClonePortlet() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = clonePortlet(clonePortlet).getPortletHandle();
    String[] keys = StringUtils.split(returnedPH, "/");
    assertTrue(keys.length == 3);
  }

  public void testClonePortletWithBadRegistrationHandle() throws Exception {
    createRegistrationContext(null);
    registrationContext.setRegistrationHandle("dummy_handle");
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    try {
      WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
      clonePortlet.setRegistrationContext(registrationContext);
      clonePortlet.setPortletContext(pC);
      clonePortlet.setUserContext(userContext);
      clonePortlet(clonePortlet);
      fail("The given registration handle was incorrect");
    } catch (InvalidHandle e) {
      e.printStackTrace();
    }
  }

  public void testClonePortletWithBadPortletHandle() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/dummy");
    try {
      WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
      clonePortlet.setRegistrationContext(rC);
      clonePortlet.setPortletContext(pC);
      clonePortlet.setUserContext(userContext);
      clonePortlet(clonePortlet);
      fail("The given portlet handle was incorrect");
    } catch (InvalidHandle e) {
      e.printStackTrace();
    }
  }

  public void testCloneAlreadyClonedPortlet() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = clonePortlet(clonePortlet).getPortletHandle();
    pC.setPortletHandle(returnedPH);
    pC = clonePortlet(clonePortlet);
    assertNotSame(returnedPH, pC.getPortletHandle());

    WS1GetPortletProperties getPortletProperties = new WS1GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    getPortletProperties.getNames().add("time-format");
    WS1PropertyList list = WSRPTypesTransformer.getWS1PropertyList(portletManagementOperationsInterface.getPortletProperties(WSRPTypesTransformer.getWS2GetPortletProperties(getPortletProperties)));
    List<WS1Property> propArray = list.getProperties();
    assertEquals(1, propArray.size());
    WS1Property property = propArray.get(0);
    assertEquals("HH", property.getStringValue());
  }

  public void testDestroyPortlet() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = clonePortlet(clonePortlet).getPortletHandle();
    String[] array = { returnedPH };
    WS1DestroyPortlets destroyPortlets = new WS1DestroyPortlets();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.getPortletHandles().addAll(Arrays.asList(array));
    WS1DestroyPortletsResponse response = WSRPTypesTransformer.getWS1DestroyPortletsResponse(portletManagementOperationsInterface.destroyPortlets(WSRPTypesTransformer.getWS2DestroyPortlets(destroyPortlets)));
    assertTrue(response.getDestroyFailed() == null || response.getDestroyFailed().size() == 0);
  }

  public void testDestroyNonClonedPortlet() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    String[] array = { CONTEXT_PATH + "/HelloWorld/dummy" };
    WS1DestroyPortlets destroyPortlets = new WS1DestroyPortlets();
    destroyPortlets.setRegistrationContext(rC);
    destroyPortlets.getPortletHandles().addAll(Arrays.asList(array));
    WS1DestroyPortletsResponse response = WSRPTypesTransformer.getWS1DestroyPortletsResponse(portletManagementOperationsInterface.destroyPortlets(WSRPTypesTransformer.getWS2DestroyPortlets(destroyPortlets)));
    assertEquals(CONTEXT_PATH + "/HelloWorld/dummy", response.getDestroyFailed().get(0)
                                                             .getPortletHandle());
  }

  public void testGetPortletProperty() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = clonePortlet(clonePortlet).getPortletHandle();
    pC.setPortletHandle(returnedPH);
    WS1GetPortletProperties getPortletProperties = new WS1GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    WS1PropertyList list = WSRPTypesTransformer.getWS1PropertyList(portletManagementOperationsInterface.getPortletProperties(WSRPTypesTransformer.getWS2GetPortletProperties(getPortletProperties)));
    List<WS1Property> propArray = list.getProperties();
    for (int i = 0; i < propArray.size(); i++) {
      WS1Property property = propArray.get(i);
      if ("time-format".equals(property.getName())) {
        assertEquals("HH", property.getStringValue());
        return;
      }
    }
    fail("A property should have been found!!!");
  }

  public void testWellKnownGetPortletProperty() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = clonePortlet(clonePortlet).getPortletHandle();
    pC.setPortletHandle(returnedPH);
    WS1GetPortletProperties getPortletProperties = new WS1GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    getPortletProperties.getNames().add("time-format");
    WS1PropertyList list = WSRPTypesTransformer.getWS1PropertyList(portletManagementOperationsInterface.getPortletProperties(WSRPTypesTransformer.getWS2GetPortletProperties(getPortletProperties)));
    List<WS1Property> propArray = list.getProperties();
    assertEquals(1, propArray.size());
    WS1Property property = propArray.get(0);
    assertEquals("HH", property.getStringValue());
  }

  public void testSetPortletProperty() throws Exception {
    WS1RegistrationContext rC = register(registrationData);
    WS1PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = clonePortlet(clonePortlet).getPortletHandle();
    pC.setPortletHandle(returnedPH);

    WS1Property property = new WS1Property();
    property.setLang("en");
    property.setName("test-prop");
    property.setStringValue("test-value");

    WS1PropertyList ws1PropertyList = new WS1PropertyList();
    ws1PropertyList.getProperties().set(0, property);

    WS1SetPortletProperties setPortletProperties = new WS1SetPortletProperties();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(pC);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(ws1PropertyList);
    WS1PortletContext pCReturned = setPortletProperties(setPortletProperties);
    assertEquals(returnedPH, pCReturned.getPortletHandle());

    WS1GetPortletProperties getPortletProperties = new WS1GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);
    ws1PropertyList = WSRPTypesTransformer.getWS1PropertyList(portletManagementOperationsInterface.getPortletProperties(WSRPTypesTransformer.getWS2GetPortletProperties(getPortletProperties)));
    List<WS1Property> propArray = ws1PropertyList.getProperties();
    for (int i = 0; i < propArray.size(); i++) {
      property = propArray.get(i);
      System.out.println("prop : " + property.getName());
      if ("test-prop".equals(property.getName())) {
        assertEquals("test-value", property.getStringValue());
        return;
      }
    }
    fail("A property should have been found!!!");
  }

  private WS1PortletContext fillPortletContext(String portletHandle) {
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    return portletContext;
  }

}
