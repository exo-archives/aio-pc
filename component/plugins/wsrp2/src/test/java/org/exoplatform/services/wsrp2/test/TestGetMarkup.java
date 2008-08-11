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

package org.exoplatform.services.wsrp2.test;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;

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
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, "");
    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getItemString());
    markupParams.setMode("wsrp:help");
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Help mode", response.getMarkupContext().getItemString());
  }

  public void testGetMarkupForSeveralWindowStates() throws Exception {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, "");
    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getItemString());
    markupParams.setWindowState("wsrp:maximized");
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Maximized state", response.getMarkupContext().getItemString());
  }

  public void testGetMarkupWithRewrittenURLInIt() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, "");
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    int index = s.indexOf("&ns=");
    s = s.substring(index + "&ns=".length());
    index = s.indexOf("&is=");
    s = StringUtils.left(s, index);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:maximized");
    navigationalContext.setOpaqueValue(s);
    markupParams.setNavigationalContext(navigationalContext);
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("value", response.getMarkupContext().getItemString());
  }

  public void testGetMarkupOfAClonedPortlet() throws Exception {
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    RegistrationContext rC = registrationOperationsInterface.register(register);
    resolveRegistrationContext(rC);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/HelloWorld2");
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(portletContext);
    clonePortlet.setUserContext(userContext);
    PortletContext returnedPC = portletManagementOperationsInterface.clonePortlet(clonePortlet);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    //set some new properties
    Property property = new Property();
    QName propertyName = new QName("test-prop");
    property.setName(propertyName);
    property.setStringValue("test-value");
    PropertyList list = new PropertyList();
    list.setProperties(new Property[] { property });
    SetPortletProperties setPortletProperties = new SetPortletProperties();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(returnedPC);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(list);
    returnedPC = portletManagementOperationsInterface.setPortletProperties(setPortletProperties);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    GetMarkup getMarkup = getMarkup(rC, returnedPC);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("Everything is more than ok", response.getMarkupContext().getItemString());
  }

  
  public void testGetResource() throws Exception {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext(null, null, null, "");
    String portletHandle = CONTEXT_PATH + "/ResourceDemo";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetResource getResource = getResource(rc, portletContext);
    ResourceResponse response = markupOperationsInterface.getResource(getResource);
    assertEquals("Everything is ok", response.getResourceContext().getItemString());
    
    resourceParams.setFormParameters(new NamedString[]{new NamedString("goal","image")});
    response = markupOperationsInterface.getResource(getResource);
    
    System.out.println(">>> EXOMAN TestGetMarkup.testGetResource() response.getResourceContext().getMimeType() = "
        + response.getResourceContext().getMimeType());
    
  }
  
}
