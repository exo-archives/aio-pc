/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.testConsumer;

import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.ServiceDescription;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 4 févr. 2004
 * Time: 10:15:05
 */

public class TestProducer extends BaseTest{

  public void testBasicProperties(){
    assertEquals(PRODUCER_ID, producer.getID());
    assertEquals(PRODUCER_NAME, producer.getName());    
    assertEquals(PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT, producer.getPortletManagementInterfaceEndpoint());
    assertEquals(PRODUCER_REGISTRATION_INTERFACE_ENDPOINT, producer.getRegistrationInterfaceEndpoint());
    assertEquals(PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT, producer.getServiceDescriptionInterfaceEndpoint());
    producer.getServiceDescriptionInterface();
  }

  public void testServiceDescription() throws Exception {
    assertNull(producer.getServiceDescription(false));
    producer.setDesiredLocales(desiredLocales);
    ServiceDescription serviceDescription = producer.getServiceDescription();
    PortletDescription portletDescription = getHelloWorldPortlet(serviceDescription.getOfferedPortlets());
    assertEquals("Usual Hello World Portlet", portletDescription.getDescription().getValue());
  }

  public void testPortletDescription() throws WSRPException {
    producer.setDesiredLocales(new String[] {"fr"});
    PortletDescription portletDescription = producer.getPortletDescription("hello/HelloWorld");
    assertEquals("Salut le monde Portlet", portletDescription.getDescription().getValue());
  }

   public void testRegistration() throws WSRPException {
     assertTrue(producer.isRegistrationInterfaceSupported());
     producer.setDesiredLocales(desiredLocales);
     ServiceDescription serviceDescription = producer.getServiceDescription();
     assertEquals(producer.isRegistrationRequired(), serviceDescription.isRequiresRegistration());
     assertNull(producer.getRegistrationData());
     assertNotNull(producer.register(registrationData));
     assertNotNull(producer.getRegistrationData());
     assertNotNull(producer.getRegistrationContext());
     producer.deregister();
   }

  public void testPortletManagement(){
    assertTrue(producer.isPortletManagementInferfaceSupported());
    assertNotNull(producer.getPortletManagementInterface());
  }   

  private PortletDescription getHelloWorldPortlet(PortletDescription[] psArray) throws Exception{
    for (int i = 0; i < psArray.length; i++) {
      if("hello/HelloWorld".equals(psArray[i].getPortletHandle()))
        return psArray[i];
    }
    return null;
  }
}
