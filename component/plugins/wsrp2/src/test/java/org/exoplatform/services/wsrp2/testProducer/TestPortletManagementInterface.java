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

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CopyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletDescription;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.GetPortletPropertyDescription;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.ImportPortlet;
import org.exoplatform.services.wsrp2.type.ImportPortlets;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetime;

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
    registrationContext = registrationOperationsInterface.register(register);
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(registrationContext);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    String returnedPH = portletManagementOperationsInterface.clonePortlet(clonePortlet)
                                                            .getPortletHandle();
    String[] keys = StringUtils.split(returnedPH, "/");
    assertTrue(keys.length == 3);
  }

  public void testClonePortletWithInvalidPortletHandle() throws Exception {
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
    getPortletProperties.getNames().add("time-format");
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

  public void testGetPortletPropertySingle() throws Exception {
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
    assertNotNull(returnedPH);
    assertNotSame(pC.getPortletHandle(), returnedPH);
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
    assertNotNull(pCReturned);
    assertEquals(returnedPH, pCReturned.getPortletHandle());

    GetPortletProperties getPortletProperties = new GetPortletProperties();
    getPortletProperties.setRegistrationContext(rC);
    getPortletProperties.setPortletContext(pC);
    getPortletProperties.setUserContext(userContext);

    list = portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
    assertNotNull(list);
    List<Property> propList = list.getProperties();
    assertNotNull(propList);

    // check set property
    boolean found1 = false;
    for (Property property2 : propList) {
      System.out.println("prop : " + property2.getName().toString());
      if ("test-prop".equals(property2.getName().getLocalPart())) {
        assertEquals("test-value", property2.getStringValue());
        found1 = true;
        break;
      }
    }
    if (!found1) {
      fail("A property \"test-prop\" should have been found with value \"test-value\"!!!");
    }

    // check 'portlet.xml' configured property
    boolean found2 = false;
    for (Property property2 : propList) {
      System.out.println("prop : " + property2.getName().toString());
      if ("time-format".equals(property2.getName().getLocalPart())) {
        assertEquals("HH", property2.getStringValue());
        found2 = true;
        break;
      }
    }
    if (!found2) {
      fail("A property \"time-format\" should have been found with value \"HH\"!!!");
    }
  }

  // Tests With Invalid Registration

  public void testClonePortletWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(registrationContext);
    clonePortlet.setPortletContext(pC);
    clonePortlet.setUserContext(userContext);
    try {
      portletManagementOperationsInterface.clonePortlet(clonePortlet).getPortletHandle();
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testDestroyPortletWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.setRegistrationContext(registrationContext);
    destroyPortlets.getPortletHandles().add(portletHandle);
    try {
      portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testCopyPortletsPortletWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    CopyPortlets copyPortlets = new CopyPortlets();
    copyPortlets.setFromRegistrationContext(registrationContext);
    copyPortlets.setFromUserContext(userContext);
    copyPortlets.setToRegistrationContext(registrationContext);
    copyPortlets.setToUserContext(userContext);

    try {
      portletManagementOperationsInterface.copyPortlets(copyPortlets);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

//  public void testExportPortletsWithInvalidRegistration() throws Exception {
//    log();
//    ServiceDescription sd = getServiceDescription(new String[] { "en" });
//    createRegistrationContext(sd, true);
//    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);
//    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
//    
//    ExportPortlets exportPortlets = new ExportPortlets();
//    exportPortlets.setRegistrationContext(registrationContext);
//    exportPortlets.setUserContext(userContext);
//    exportPortlets.setLifetime(getLifetimeInSec(100));
//    exportPortlets.setExportByValueRequired(false);
//    exportPortlets.getPortletContext().add(pC);
//
//    try {
//      portletManagementOperationsInterface.exportPortlets(exportPortlets);
//      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
//    } catch (InvalidRegistration e) {
//    }
//  }

  public void testGetPortletDescription() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);

    GetPortletDescription getPortletDescription = new GetPortletDescription();
    PortletContext pC = fillPortletContext(CONTEXT_PATH + "/HelloWorld");
    
    getPortletDescription.setRegistrationContext(rC); 
    getPortletDescription.setPortletContext(pC);
    getPortletDescription.setUserContext(userContext);
    getPortletDescription.getDesiredLocales().add("EN");

    PortletDescriptionResponse pdr = portletManagementOperationsInterface.getPortletDescription(getPortletDescription);
    assertNotNull(pdr);
    PortletDescription pd = pdr.getPortletDescription();
    assertNotNull(pd);
    LocalizedString ls = pd.getDescription();
    assertNotNull(ls);
    String groupID = pd.getGroupID();
    assertNotNull(groupID);
  }

  public void testGetPortletDescriptionWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    GetPortletDescription getPortletDescription = new GetPortletDescription();
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    getPortletDescription.setPortletContext(portletContext);
    getPortletDescription.setRegistrationContext(registrationContext);

    try {
      portletManagementOperationsInterface.getPortletDescription(getPortletDescription);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testGetPortletPropertiesWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    GetPortletProperties getPortletProperties = new GetPortletProperties();
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    getPortletProperties.setPortletContext(portletContext);
    getPortletProperties.setRegistrationContext(registrationContext);

    try {
      portletManagementOperationsInterface.getPortletProperties(getPortletProperties);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testDestroyPortletsWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.getPortletHandles().add(portletHandle);
    destroyPortlets.setRegistrationContext(registrationContext);
    destroyPortlets.setUserContext(userContext);

    try {
      portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testGetPortletsLifetimeWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    GetPortletsLifetime getPortletsLifetime = new GetPortletsLifetime();
    getPortletsLifetime.setRegistrationContext(registrationContext);
    getPortletsLifetime.setUserContext(userContext);
    getPortletsLifetime.getPortletContext();

    try {
      portletManagementOperationsInterface.getPortletsLifetime(getPortletsLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testSetPortletsLifetimeWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    SetPortletsLifetime setPortletsLifetime = new SetPortletsLifetime();
    setPortletsLifetime.setLifetime(lifetime);
    setPortletsLifetime.setRegistrationContext(registrationContext);
    setPortletsLifetime.setUserContext(userContext);
    setPortletsLifetime.getPortletContext().add(portletContext);

    try {
      portletManagementOperationsInterface.setPortletsLifetime(setPortletsLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testImportPortletsWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";

    ImportPortlet importPortlet = new ImportPortlet();
    importPortlet.setImportID(portletHandle);
    importPortlet.setExportData(null);

    ImportPortlets importPortlets = new ImportPortlets();
    importPortlets.setImportContext(null);
    importPortlets.setLifetime(lifetime);
    importPortlets.setRegistrationContext(registrationContext);
    importPortlets.setUserContext(userContext);
    importPortlets.getImportPortlet().add(importPortlet);

    try {
      portletManagementOperationsInterface.importPortlets(importPortlets);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testSetPortletPropertiesWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    SetPortletProperties setPortletProperties = new SetPortletProperties();
    setPortletProperties.setPortletContext(portletContext);
    setPortletProperties.setRegistrationContext(registrationContext);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(null);

    try {
      portletManagementOperationsInterface.setPortletProperties(setPortletProperties);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testGetPortletPropertyDescriptionWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    String portletHandle = CONTEXT_PATH + "/HelloWorld";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    GetPortletPropertyDescription getPortletPropertyDescription = new GetPortletPropertyDescription();
    getPortletPropertyDescription.setPortletContext(portletContext);
    getPortletPropertyDescription.setRegistrationContext(registrationContext);
    getPortletPropertyDescription.setUserContext(userContext);

    try {
      portletManagementOperationsInterface.getPortletPropertyDescription(getPortletPropertyDescription);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  private PortletContext fillPortletContext(String portletHandle) {
    log();
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    return portletContext;
  }

}
