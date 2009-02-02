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
import java.util.List;

import org.exoplatform.services.wsrp1.type.WS1GetMarkup;
import org.exoplatform.services.wsrp1.type.WS1GetServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1PortletDescription;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;

/**
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 24
 * janv. 2004 Time: 11:43:58
 */
public class TestSomeScenarios extends BaseTest {

  private static final String PORTLET_HANDLE = CONTEXT_PATH + "/HelloWorld2";

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestSomeScenarios.setUp()");
  }

  public void testFirstConsumerScenario() throws Throwable, RemoteException {
    //get the service description through a monitor that listen on port 8081
    WS1GetServiceDescription request = new WS1GetServiceDescription();
    request.getDesiredLocales().add("en");
    WS1ServiceDescription ws1serviceDescription = getServiceDescription(request);

    //register or not
    WS1RegistrationContext rC = null;
    if (ws1serviceDescription.isRequiresRegistration()) {
      System.out.println("[test] Registration required");
      rC = register(registrationData);
      resolveRegistrationContext(rC);
    } else {
      System.out.println("[test] Registration non required");
    }

    //test the existence of our portlet handle
    boolean go_on = false;
    List<WS1PortletDescription> portletDescrList = ws1serviceDescription.getOfferedPortlets();
    for (WS1PortletDescription portletDescription : portletDescrList) {
      if (PORTLET_HANDLE.equals(portletDescription.getPortletHandle())) {
        go_on = true;
        break;
      }
    }
    if (!go_on)
      fail("The portlet " + PORTLET_HANDLE + " is not deployed");

    //prepare the request arguments
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(PORTLET_HANDLE);
    WS1GetMarkup getMarkup = getMarkup(rC, portletContext);

    //get the markup
    WS1MarkupResponse response = getMarkup(getMarkup);
    response.getSessionContext();
    response.getMarkupContext();
  }
}
