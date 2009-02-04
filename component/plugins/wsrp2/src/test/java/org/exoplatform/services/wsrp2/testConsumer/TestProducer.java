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

package org.exoplatform.services.wsrp2.testConsumer;

import java.util.List;

import org.exoplatform.services.wsrp2.type.PortletDescription;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 4 févr. 2004
 * Time: 10:15:05
 */

public class TestProducer extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testBasicProperties() {
    log();
    assertEquals(PRODUCER_ID, producer.getID());
    assertEquals(PRODUCER_NAME, producer.getName());
//    assertEquals(PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT,
//                 producer.getPortletManagementInterfaceEndpoint());
//    assertEquals(PRODUCER_REGISTRATION_INTERFACE_ENDPOINT,
//                 producer.getRegistrationInterfaceEndpoint());
//    assertEquals(PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT,
//                 producer.getServiceDescriptionInterfaceEndpoint());
    producer.getServiceDescriptionAdapter();
  }

  // ERROR: do not uncomment
//  public void testServiceDescription() throws Exception {
//    assertNull(producer.getServiceDescription(false));
//    producer.setDesiredLocales(Arrays.asList(desiredLocales));
//    ServiceDescription serviceDescription = producer.getServiceDescription();
//    PortletDescription portletDescription = getHelloWorldPortlet(serviceDescription.getOfferedPortlets());
//    assertEquals("Usual Hello World Portlet", portletDescription.getDescription().getValue());
//  }
//
//  public void testPortletDescription() throws WSRPException {
//    producer.setDesiredLocales(Arrays.asList(new String[] { "fr" }));
//    PortletDescription portletDescription = producer.getPortletDescription(CONTEXT_PATH.substring(1) + "/HelloWorld");
//    assertEquals("Salut le monde Portlet", portletDescription.getDescription().getValue());
//  }
//
//  public void testRegistration() throws WSRPException {
//    assertTrue(producer.isRegistrationAdapterSupported());
//    producer.setDesiredLocales(Arrays.asList(desiredLocales));
//    ServiceDescription serviceDescription = producer.getServiceDescription();
//    assertEquals(producer.isRegistrationRequired(), serviceDescription.isRequiresRegistration());
//    assertNull(producer.getRegistrationData());
//    assertNotNull(producer.register(register));
//    assertNotNull(producer.getRegistrationData());
//    assertNotNull(producer.getRegistrationContext());
//    Deregister deregister = new Deregister();
//    deregister.setRegistrationContext(producer.getRegistrationContext());
//    deregister.setUserContext(userContext);
//    producer.deregister(deregister);
//  }

  public void testPortletManagement() {
    log();
    assertTrue(producer.isPortletManagementInferfaceSupported());
    assertNotNull(producer.getPortletManagementAdapter());
  }

  private PortletDescription getHelloWorldPortlet(List<PortletDescription> portletDescriptionList) throws Exception {
    log();
    for (PortletDescription portletDescription : portletDescriptionList) {
      if (CONTEXT_PATH.substring(1).concat("/HelloWorld").equals(portletDescription.getPortletHandle()))
        return portletDescription;
    }
    return null;
  }
}
