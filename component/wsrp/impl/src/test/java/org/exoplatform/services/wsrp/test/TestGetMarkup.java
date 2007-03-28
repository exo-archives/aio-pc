/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 10 janv. 2004
 */
package org.exoplatform.services.wsrp.test;

import java.rmi.RemoteException;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.wsrp.type.ClonePortletRequest;
import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.Property;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.SetPortletPropertiesRequest;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class TestGetMarkup extends BaseTest{ 

  public TestGetMarkup(String s) {
    super(s);        
  }
  
  public void setUp() throws Exception{
    super.setUp();    
  }
  
  public void testGetMarkupForSeveralModes() throws Exception {
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    String portletHandle = "hello/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getMarkupString());
    markupParams.setMode("wsrp:help");
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Help mode", response.getMarkupContext().getMarkupString());
  }

  public void testGetMarkupForSeveralWindowStates() throws Exception {
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    String portletHandle = "hello/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok", response.getMarkupContext().getMarkupString());
    markupParams.setWindowState("wsrp:maximized");
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("HelloWorld title", response.getMarkupContext().getPreferredTitle());
    assertEquals("Everything is ok in Maximized state", response.getMarkupContext().getMarkupString());
  }      
  
  public void testGetMarkupWithRewrittenURLInIt() throws RemoteException{
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    String portletHandle = "hello/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
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
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("value", response.getMarkupContext().getMarkupString());       
  }

  public void testGetMarkupOfAClonedPortlet() throws Exception {
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    RegistrationContext rC = registrationOperationsInterface.register(registrationData);
    resolveRegistrationContext(rC);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/HelloWorld2");
    ClonePortletRequest clonePortlet = new ClonePortletRequest();
    clonePortlet.setRegistrationContext(rC);
    clonePortlet.setPortletContext(portletContext);
    clonePortlet.setUserContext(userContext);
    PortletContext returnedPC = portletManagementOperationsInterface.clonePortlet(clonePortlet);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    //set some new properties
    Property property = new Property();
    property.setLang("en");
    property.setName("test-prop");
    property.setStringValue("test-value");
    PropertyList list = new PropertyList();
    list.setProperties(new Property[]{property});
    SetPortletPropertiesRequest setPortletProperties = new SetPortletPropertiesRequest();
    setPortletProperties.setRegistrationContext(rC);
    setPortletProperties.setPortletContext(returnedPC);
    setPortletProperties.setUserContext(userContext);
    setPortletProperties.setPropertyList(list);
    returnedPC = portletManagementOperationsInterface.setPortletProperties(setPortletProperties);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    MarkupRequest getMarkup = getMarkup(rC, returnedPC);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("Everything is more than ok", response.getMarkupContext().getMarkupString());
  }

}
