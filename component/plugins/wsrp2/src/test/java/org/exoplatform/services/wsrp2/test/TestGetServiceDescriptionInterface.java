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

import org.exoplatform.services.wsrp.type.ItemDescription;
import org.exoplatform.services.wsrp.type.MarkupType;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.ServiceDescriptionRequest;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestGetServiceDescriptionInterface extends BaseTest {

  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestGetServiceDescriptionInterface.setUp()");
  }

  public void testGetDescription() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("Usual Hello World Portlet", ps.getDescription().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("Salut le monde Portlet", ps.getDescription().getValue());
  }

  public void testGetDisplayName() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("HelloWorldPortlet", ps.getDisplayName().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("SalutLeMondePortlet", ps.getDisplayName().getValue());
  }

  public void testGetTitle() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("HelloWorld title", ps.getTitle().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("Bonjour le monde Portlet", ps.getTitle().getValue());
  }

  public void testGetShortTitle() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("Hello World", ps.getShortTitle().getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("Bonjour", ps.getShortTitle().getValue());
  }

  public void testGetKeyWords() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("sample", ps.getKeywords(0).getValue());
    assertEquals("hello", ps.getKeywords(1).getValue());

    ps = getHelloWorldPortlet("fr");
    assertEquals("exemple", ps.getKeywords(0).getValue());
    assertEquals("bonjour", ps.getKeywords(1).getValue());
  }

  public void testGetPortletHandle() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("hello/HelloWorld", ps.getPortletHandle());
  }

  public void testGetGroupId() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("hello", ps.getGroupID());
  }

  public void testGetMarkup() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    MarkupType mT = ps.getMarkupTypes(0);

    assertEquals("text/html", mT.getMimeType());
    assertEquals("wsrp:config", mT.getModes(0));
    assertEquals("wsrp:help", mT.getModes(2));
//    assertEquals("wsrp:minimized", mT.getWindowStates(0));
//    assertEquals("wsrp:normal", mT.getWindowStates(1));
//    assertEquals("wsrp:maximized", mT.getWindowStates(2));
//    assertEquals("wsrp:half-page", mT.getWindowStates(3));
    assertEquals("en", mT.getLocales()[0]);
  }

  public void testPortletNeedsSecureTransportation() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
//    assertEquals(false, ps.getDefaultMarkupSecure().booleanValue());
//    assertEquals(false, ps.getOnlySecure().booleanValue());
//  !!! changed to true 'cause there is security constraint for the portlet in portlet.xml
    assertEquals(true, ps.getDefaultMarkupSecure().booleanValue());
    assertEquals(true, ps.getOnlySecure().booleanValue());
  }

  public void testRequiresRegistration() throws Exception {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    assertEquals(true, sd.isRequiresRegistration());
  }

  public void testGetCustomModes() throws Exception {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    ItemDescription[] iDArray = sd.getCustomModeDescriptions();

    ItemDescription iD = iDArray[0];
    assertEquals("config", iD.getItemName());
    assertEquals("en", iD.getDescription().getLang());
    assertEquals("to let admin config portlets", iD.getDescription().getValue());

    iD = iDArray[1];
    assertEquals("config", iD.getItemName());
    assertEquals("fr", iD.getDescription().getLang());
  }

  public void testGetCustomWindowStates() throws Exception {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    ItemDescription[] iDArray = sd.getCustomWindowStateDescriptions();

    ItemDescription iD = iDArray[0];
    assertEquals("half-page", iD.getItemName());
    assertEquals("en", iD.getDescription().getLang());
    assertEquals("portlet takes half of the page", iD.getDescription().getValue());

    iD = iDArray[1];
    assertEquals("half-page", iD.getItemName());
    assertEquals("fr", iD.getDescription().getLang());
    assertEquals("portlet sure une demi page", iD.getDescription().getValue());

    iD = iDArray[2];
    assertEquals("max-per-column", iD.getItemName());
    assertEquals("en", iD.getDescription().getLang());
    assertEquals("portlet the whole column", iD.getDescription().getValue());
  }

  public void testGetSupportedLocales() throws Exception {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { "en" });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    String[] localesArray = sd.getLocales();
    assertEquals("en", localesArray[0]);
    assertEquals("fr", localesArray[1]);
  }

  public void testGetUserAttributes() throws Exception {
    PortletDescription ps = getHelloWorldPortlet("en");
    assertEquals("workInfo/telephone", ps.getUserProfileItems(0));
  }

  private PortletDescription getHelloWorldPortlet(String locale) throws Exception {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(new String[] { locale });
    ServiceDescription sd = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    PortletDescription[] psArray = sd.getOfferedPortlets();
    for (int i = 0; i < psArray.length; i++) {
      if ("hello/HelloWorld".equals(psArray[i].getPortletHandle()))
        return psArray[i];
    }
    return null;
  }

}
