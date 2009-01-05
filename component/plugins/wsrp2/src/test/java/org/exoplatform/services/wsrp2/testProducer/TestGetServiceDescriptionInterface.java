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

import java.util.List;
import java.util.Map;

import org.exoplatform.services.wsrp2.bind.extensions.WSRPV2ServiceAdministrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.ItemDescription;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestGetServiceDescriptionInterface extends BaseTest {

  public void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testGetDescription() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("Usual Hello World Portlet", ps.getDescription().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("Salut le monde Portlet", ps.getDescription().getValue());
  }

  public void testGetDisplayName() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("HelloWorldPortlet", ps.getDisplayName().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("SalutLeMondePortlet", ps.getDisplayName().getValue());
  }

  public void testGetTitle() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("HelloWorld title", ps.getTitle().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("Bonjour le monde Portlet", ps.getTitle().getValue());
  }

  public void testGetShortTitle() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("Hello World", ps.getShortTitle().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("Bonjour", ps.getShortTitle().getValue());
  }

  public void testGetKeyWords() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("sample", ps.getKeywords().get(0).getValue());
    assertEquals("hello", ps.getKeywords().get(1).getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("exemple", ps.getKeywords().get(0).getValue());
    assertEquals("bonjour", ps.getKeywords().get(1).getValue());
  }

  public void testGetPortletHandle() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals(CONTEXT_PATH + "/HelloWorld", ps.getPortletHandle());
  }

  public void testGetGroupId() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("hello", ps.getGroupID());
  }

  public void testGetMarkup() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    MarkupType mT = ps.getMarkupTypes().get(0);

    assertEquals("text/html", mT.getMimeType());
    assertEquals("wsrp:config", mT.getModes().get(0));
    assertEquals("wsrp:help", mT.getModes().get(2));
//    assertEquals("wsrp:minimized", mT.getWindowStates(0));
//    assertEquals("wsrp:normal", mT.getWindowStates(1));
//    assertEquals("wsrp:maximized", mT.getWindowStates(2));
//    assertEquals("wsrp:half-page", mT.getWindowStates(3));
    assertEquals("en", mT.getLocales().get(0));
  }

  public void testPortletNeedsSecureTransportation() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
//    assertEquals(false, ps.getDefaultMarkupSecure().booleanValue());
//    assertEquals(false, ps.getOnlySecure().booleanValue());
//  !!! changed to true 'cause there is security constraint for the portlet in portlet.xml
    assertEquals(true, ps.isDefaultMarkupSecure().booleanValue());
    assertEquals(true, ps.isOnlySecure().booleanValue());
  }

  public void testRequiresRegistration() throws Exception {
    log();
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.getDesiredLocales().add("en");
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    assertEquals(true, sd.isRequiresRegistration());
  }

  public void testGetCustomModes() throws Exception {
    log();
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.getDesiredLocales().add("en");
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    List<ItemDescription> list = sd.getCustomModeDescriptions();

    ItemDescription iD = list.get(0);
    assertEquals("config", iD.getItemName());
//    assertEquals("en", iD.getDescription().getLang());
    assertEquals("to let admin config portlets", iD.getDescription().getValue());

    iD = list.get(1);
    assertEquals("config", iD.getItemName());
//    assertEquals("fr", iD.getDescription().getLang());
  }

  public void testGetCustomWindowStates() throws Exception {
    log();
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.getDesiredLocales().add("en");
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    List<ItemDescription> list = sd.getCustomWindowStateDescriptions();

    ItemDescription iD = list.get(0);
    assertEquals("half-page", iD.getItemName());
//    assertEquals("en", iD.getDescription().getLang());
    assertEquals("portlet takes half of the page", iD.getDescription().getValue());

    iD = list.get(1);
    assertEquals("half-page", iD.getItemName());
//    assertEquals("fr", iD.getDescription().getLang());
    assertEquals("portlet sure une demi page", iD.getDescription().getValue());

    iD = list.get(2);
    assertEquals("max-per-column", iD.getItemName());
//    assertEquals("en", iD.getDescription().getLang());
    assertEquals("portlet the whole column", iD.getDescription().getValue());
  }

  public void testGetSupportedLocales() throws Exception {
    log();
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.getDesiredLocales().add("en");
    getServiceDescription.getDesiredLocales().add("fr");
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    List<String> localesList = sd.getLocales();
    assertEquals("en", localesList.get(0));
    assertEquals("fr", localesList.get(1));
    assertEquals(2, localesList.size());
  }

  public void testGetUserAttributes() throws Exception {
    log();
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("workInfo/telephone", ps.getUserProfileItems().get(0));
  }

  public void testGetServiceDescriptionWithPortletHandle() throws Exception {
    PortletDescription ps = getHelloWorldPortletDescription(new RegistrationContext());
    assertEquals("HelloWorldPortlet", ps.getDisplayName().getValue());
  }

  public void testGetServiceDescriptionWithInvalidPortletHandle() throws Exception {
    PortletDescription ps = null;
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.getDesiredLocales().add("en");
    getServiceDescription.getPortletHandles().add(CONTEXT_PATH.concat("/HelloWorld2"));
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    List<PortletDescription> list = sd.getOfferedPortlets();
    for (PortletDescription portletDescription : list) {
      if (CONTEXT_PATH.concat("/HelloWorld").equals(portletDescription.getPortletHandle()))
        ps = portletDescription;
    }
    assertNull(ps);
  }

  private PortletDescription getHelloWorldPortletDescription(RegistrationContext registrationContext) throws ResourceSuspended,
                                                                                                     InvalidRegistration,
                                                                                                     ModifyRegistrationRequired,
                                                                                                     OperationFailed {
    PortletDescription ps = null;
    GetServiceDescription getServiceDescription = getServiceDescription(registrationContext);
    getServiceDescription.getDesiredLocales().add("en");
    getServiceDescription.getPortletHandles().add(CONTEXT_PATH.concat("/HelloWorld"));
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    List<PortletDescription> list = sd.getOfferedPortlets();
    for (PortletDescription portletDescription : list) {
      if (CONTEXT_PATH.concat("/HelloWorld").equals(portletDescription.getPortletHandle()))
        ps = portletDescription;
    }
    return ps;
  }

  private PortletDescription getHelloWorldPortlet(String locale) throws Exception {
    log();
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.getDesiredLocales().add(locale);
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    List<PortletDescription> list = sd.getOfferedPortlets();
    for (PortletDescription portletDescription : list) {
      if (CONTEXT_PATH.concat("/HelloWorld").equals(portletDescription.getPortletHandle()))
        return portletDescription;
    }
    return null;
  }



}
