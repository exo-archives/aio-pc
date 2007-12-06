package org.exoplatform.services.wsrp.test;

/**
 * Copyright 2001-2007 The eXo platform SAS  All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/


import java.rmi.RemoteException;

import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.ServiceDescriptionRequest;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 24 janv. 2004
 * Time: 11:43:58
 */
public class TestSomeScenarios extends BaseTest {

  private static final String PORTLET_HANDLE = "hello/HelloWorld2";

  public TestSomeScenarios(String s) {
    super(s);
  }

  public void testFirstConsumerScenario() throws Throwable, RemoteException {
    //get the service description through a monitor that listen on port 8081
    ServiceDescriptionRequest request = new ServiceDescriptionRequest();
    request.setDesiredLocales(new String[]{"en"});
    ServiceDescription serviceDescription = null;
    serviceDescription = serviceDescriptionInterface.getServiceDescription(request);

    //register or not
    RegistrationContext rC = null;
    if (serviceDescription.isRequiresRegistration()) {
      System.out.println("[test] Registration required");
      rC = registrationOperationsInterface.register(registrationData);
      resolveRegistrationContext(rC);
    } else {
      System.out.println("[test] Registration non required");
    }

    //test the existence of our portlet handle
    boolean go_on = false;
    PortletDescription[] array = serviceDescription.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      PortletDescription portletDescription = array[i];
      if(PORTLET_HANDLE.equals(portletDescription.getPortletHandle())){
        go_on = true;
        break;
      }
    }
    if(!go_on)
      fail("The portlet " + PORTLET_HANDLE + " is not deployed");

    //prepare the request arguments
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(PORTLET_HANDLE);
    MarkupRequest getMarkup = getMarkup(rC, portletContext);

    //get the markup
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    response.getSessionContext();
    response.getMarkupContext();
  }
}
