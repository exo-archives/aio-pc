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

import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.producer.impl.helpers.NamedStringWrapper;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 
 */
public class TestGetMarkupTestURLs extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    log();
  }

  // test Producer rewrite in url

  public void testGetMarkupWithRewrittenURL() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, "ns");
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

  public void testGetMarkupWithRewrittenURLWithPortletHandle() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    templates.setRenderTemplate(DEFAULT_TEMPLATE);
    runtimeContext.setTemplates(templates);

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, "_component");

    assertNotNull(s);
    assertEquals(portletHandle, s);
  }

  public void testGetMarkupWithRewrittenURLWithUserContextKey() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE + "&portal:type={wsrp-urlType}"
        + "&ns={wsrp-navigationalState}" + "&user={" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}";
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

    //for 'userKEY' UserContextKey

    userContext.setUserContextKey("userKEY");

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, "user");

    assertNotNull(s);
    assertEquals("userKEY", s);

    //for null UserContextKey

    userContext.setUserContextKey(null);

    getMarkup = getMarkup(registrationContext, portletContext);

    response = markupOperationsInterface.getMarkup(getMarkup);
    s = response.getMarkupContext().getItemString();
    s = getParameter(s, "user");

    assertNotNull(s);
    assertEquals("", s);
  }

  public void testGetMarkupWithRewrittenURLWithWindowState() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE;
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, "portal:windowState");

    assertNotNull(s);
    assertEquals("wsrp:maximized", s);
  }

  public void testGetMarkupWithRewrittenURLWithPortletMode() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE;
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, "_mode");

    assertNotNull(s);
    assertEquals("wsrp:edit", s);
  }

  public void testGetMarkupWithRewrittenURLWithIsSecure() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE;
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, "_isSecure");

    assertNotNull(s);
    assertEquals("false", s);
  }

  public void testGetMarkupWithRewrittenURLWithInstanceKey() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE + "&" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY
        + "={" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}";
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);
    runtimeContext.setPortletInstanceKey("1234567890");

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, WSRPConstants.WSRP_PORTLET_INSTANCE_KEY);

    assertNotNull(s);
    assertEquals("1234567890", s);
  }

  public void testGetMarkupWithRewrittenURLWithSessionID() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE + "&" + WSRPConstants.WSRP_SESSION_ID + "={"
        + WSRPConstants.WSRP_SESSION_ID + "}";
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

//    sessionParams.setSessionID("1234567890");

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s1 = response.getMarkupContext().getItemString();
    s1 = getParameter(s1, WSRPConstants.WSRP_SESSION_ID);

    assertNotNull(s1);

    sessionParams.setSessionID(s1);

    getMarkup = getMarkup(registrationContext, portletContext);

    response = markupOperationsInterface.getMarkup(getMarkup);
    String s2 = response.getMarkupContext().getItemString();
    s2 = getParameter(s2, WSRPConstants.WSRP_SESSION_ID);

    assertNotNull(s2);
    assertEquals(s1, s2);
  }

  public void testGetMarkupWithRewrittenURLWithPageState() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE + "&" + WSRPConstants.WSRP_PAGE_STATE + "={"
        + WSRPConstants.WSRP_PAGE_STATE + "}";
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

    runtimeContext.setPageState("1234567890");

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, WSRPConstants.WSRP_PAGE_STATE);

    assertNotNull(s);
    assertEquals("1234567890", s);
  }

  public void testGetMarkupWithRewrittenURLWithPortletStates() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);

    String newRENDER_TEMPLATE = DEFAULT_TEMPLATE + "&" + WSRPConstants.WSRP_PORTLET_STATES + "={"
        + WSRPConstants.WSRP_PORTLET_STATES + "}";
    templates.setRenderTemplate(newRENDER_TEMPLATE);
    runtimeContext.setTemplates(templates);

    runtimeContext.setPortletStates("1234567890");

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    String s = response.getMarkupContext().getItemString();
    s = getParameter(s, WSRPConstants.WSRP_PORTLET_STATES);

    assertNotNull(s);
    assertEquals("1234567890", s);
  }

  // test Consumer rewrite in url

  public void testGetMarkupWithOutRewrittenURL() throws Exception {
    log();
    boolean isRequiredTemplate = getRequiresTemplate();
    if (isRequiredTemplate) {
      setRequiresTemplate(false);
    }
    try {

      ServiceDescription sd = getServiceDescription(new String[] { "en" });
      createRegistrationContext(sd, false);
      String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
      PortletContext portletContext = new PortletContext();
      portletContext.setPortletHandle(portletHandle);
      portletContext.setPortletState(null);
      GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

      MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
      String s = response.getMarkupContext().getItemString();
      s = urlRewriter.rewriteURLs("", s);
      s = getParameter(s, WSRPConstants.WSRP_NAVIGATIONAL_STATE);
      assertNotNull(s);

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
      String itemString = response.getMarkupContext().getItemString();
      assertNotNull(itemString);
      assertEquals("value", itemString);

    } finally {
      setRequiresTemplate(isRequiredTemplate);
    }
  }

  public void testGetMarkupWithOutRewrittenURLCommonAndRenderParams() throws Exception {
    log();
    boolean isRequiredTemplate = getRequiresTemplate();
    if (isRequiredTemplate) {
      setRequiresTemplate(false);
    }
    try {

      ServiceDescription sd = getServiceDescription(new String[] { "en" });
      createRegistrationContext(sd, false);
      String portletHandle = CONTEXT_PATH + "/PortletToTestMarkupWithRewrittenURL";
      PortletContext portletContext = new PortletContext();
      portletContext.setPortletHandle(portletHandle);
      portletContext.setPortletState(null);
      GetMarkup getMarkup = getMarkup(registrationContext, portletContext);

      MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
      String s = response.getMarkupContext().getItemString();

      // DEBUG
//      assertEquals("", s);

      s = s.replaceAll(WSRPConstants.NEXT_PARAM_AMP, WSRPConstants.NEXT_PARAM);
      s = s.replaceAll("wsrp_rewrite\\?", "baseURL?q=1&");
      s = s.replaceAll(WSRPConstants.WSRP_REWRITE_SUFFFIX, "");

      // DEBUG
//      assertEquals("", s);

      String s1 = getParameter(s, WSRPConstants.WSRP_NAVIGATIONAL_STATE);
      assertNotNull(s1);

      s1 = getParameter(s, WSRPConstants.WSRP_URL_TYPE);
      assertNotNull(s1);
      assertEquals("render", s1);

      s1 = getParameter(s, WSRPConstants.WSRP_FRAGMENT_ID);
      assertNotNull(s1);
      assertEquals("", s1);

      s1 = getParameter(s, WSRPConstants.WSRP_SECURE_URL);
      assertNotNull(s1);
      assertEquals("false", s1);

      s1 = getParameter(s, WSRPConstants.WSRP_MODE);
      assertNotNull(s1);
      assertEquals(Modes._edit_wsrp, s1);

      s1 = getParameter(s, WSRPConstants.WSRP_WINDOW_STATE);
      assertNotNull(s1);
      assertEquals(WindowStates._maximized_wsrp, s1);

      s1 = getParameter(s, WSRPConstants.WSRP_NAVIGATIONAL_STATE);
      assertNotNull(s1);

      s1 = getParameter(s, WSRPConstants.WSRP_NAVIGATIONAL_VALUES);
      assertNotNull(s1);
      assertEquals("", s1);

    } finally {
      setRequiresTemplate(isRequiredTemplate);
    }
  }

  public void testGetResource() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, false);
    String portletHandle = CONTEXT_PATH + "/PortletToTestResource";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    portletContext.setPortletState(null);
    GetResource getResource = getResource(registrationContext, portletContext);
    ResourceResponse response = markupOperationsInterface.getResource(getResource);
    assertEquals("Everything is ok", response.getResourceContext().getItemString());

    NamedString formParameter = new NamedStringWrapper("goal", "image");
//    formParameter.setName("goal");
//    formParameter.setValue("image");
    resourceParams.getFormParameters().add(formParameter);
    response = markupOperationsInterface.getResource(getResource);
    assertEquals("image/jpeg", response.getResourceContext().getMimeType());
  }

  private String getParameter(String s, String paramName) {
    String paramKey = "&" + paramName + "=";
    int index = s.indexOf(paramKey);
    if (index == -1) {
      return "";
    }
    s = s.substring(index + (paramKey).length());
    index = s.indexOf("&");
    s = s.substring(0, index);
    return s;
  }

}
