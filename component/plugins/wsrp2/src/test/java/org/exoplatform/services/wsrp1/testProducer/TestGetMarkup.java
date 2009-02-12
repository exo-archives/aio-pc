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

package org.exoplatform.services.wsrp1.testProducer;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp1.type.WS1ClonePortlet;
import org.exoplatform.services.wsrp1.type.WS1GetMarkup;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1Property;
import org.exoplatform.services.wsrp1.type.WS1PropertyList;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1SetPortletProperties;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestGetMarkup extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestGetMarkup.setUp()");
  }

  public void testGetMarkupForSeveralModes() throws Exception {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);
    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    WS1GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    WS1MarkupResponse response = getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getMarkupString());
    markupParams.setMode("wsrp:help");
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Help mode", response.getMarkupContext().getMarkupString());
  }

  public void testGetMarkupForSeveralWindowStates() throws Exception {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);
    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    WS1GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    WS1MarkupResponse response = getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getMarkupString());
    markupParams.setWindowState("wsrp:maximized");
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Maximized state", response.getMarkupContext()
                                                                .getMarkupString());
  }

  public void testGetMarkupWithRewrittenURLInIt() throws Exception {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    WS1GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    WS1MarkupResponse response = getMarkup(getMarkup);
    String s = response.getMarkupContext().getMarkupString();
    int index = s.indexOf("&ns=");
    s = s.substring(index + "&ns=".length());
    index = s.indexOf("&is=");
    s = StringUtils.left(s, index);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:maximized");
    markupParams.setNavigationalState(s);
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = getMarkup(getMarkup);
    assertEquals("value", response.getMarkupContext().getMarkupString());
  }

  public void testGetMarkupOfAClonedPortlet() throws Exception {
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    WS1RegistrationContext rC = register(registrationData);
    resolveRegistrationContext(rC);
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/HelloWorld2");
    WS1ClonePortlet clonePortlet = new WS1ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(portletContext);
    clonePortlet.setUserContext(userContext);

    WS1PortletContext returnedPC = clonePortlet(clonePortlet);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    //set some new properties
    WS1Property property = new WS1Property();
    property.setLang("en");
    property.setName("test-prop");
    property.setStringValue("test-value");
    WS1PropertyList list = new WS1PropertyList();
    list.getProperties().addAll(Arrays.asList(new WS1Property[] { property }));
    WS1SetPortletProperties setPortletProperties = new WS1SetPortletProperties();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(returnedPC);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(list);
    returnedPC = setPortletProperties(setPortletProperties);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    WS1GetMarkup getMarkup = getMarkup(rC, returnedPC);
    WS1MarkupResponse response = getMarkup(getMarkup);
    assertEquals("Everything is more than ok", response.getMarkupContext().getMarkupString());
  }



}
