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

package org.exoplatform.services.wsrp1.test;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpResponse;
import org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType;
import org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType;
import org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp1.type.WS1ClientData;
import org.exoplatform.services.wsrp1.type.WS1MarkupParams;
import org.exoplatform.services.wsrp1.type.WS1PersonName;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1PortletDescription;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1RegistrationData;
import org.exoplatform.services.wsrp1.type.WS1RuntimeContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1Templates;
import org.exoplatform.services.wsrp1.type.WS1UserContext;
import org.exoplatform.services.wsrp1.type.WS1UserProfile;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Date: 11 nov. 2003 Time:
 * 22:08:31 Revision: Max Shaposhnik 17.07.2008
 */
public class BaseTest extends TestCase {
  
  protected static final String                 CONTEXT_PATH             = "hello";
  
  protected static final String                 SERVICE_URL              = "http://localhost:8080/hello/services/";

  protected static final String                 TEST_PATH                = (System.getProperty("testPath") == null ? "."
                                                                                                                  : System.getProperty("testPath"));

  //protected static final String                 PORTLET_APP_PATH         = "file:" + TEST_PATH + CONTEXT_PATH;

  static boolean                                initService_             = true;

  protected PortletContainerService             portletContainer;

  protected PortletApplicationsHolder           holder;

  protected PortletApp                          portletApp_;

  protected Collection                          roles;

  protected WSRPV1ServiceDescriptionPortType serviceDescriptionInterface;

  protected WSRPV1RegistrationPortType       registrationOperationsInterface;

  protected WSRPV1MarkupPortType             markupOperationsInterface;

  protected WSRPV1PortletManagementPortType  portletManagementOperationsInterface;

  protected WS1PersonName                          personName;

  protected WS1UserContext                         userContext;

  protected WS1UserProfile                         userProfile;

  protected WS1RegistrationData                    registrationData;

  protected WS1RuntimeContext                      runtimeContext;

  protected WS1Templates                           templates;

  protected WS1ClientData                          clientData;

  protected WS1MarkupParams                        markupParams;

  protected static final String[]               USER_CATEGORIES_ARRAY    = { "full", "standard",
      "minimal"                                                         };

  public static String[]                        localesArray             = { "en" };

  public static String[]                        markupCharacterSets      = { "UF-08", "ISO-8859-1" };

  public static String[]                        mimeTypes                = { "text/html",
      "text/xhtml"                                                      };

  public static final String                    BASE_URL                 = "/portal/faces/portal/portal.jsp?portal:ctx="
                                                                             + Constants.DEFAUL_PORTAL_OWNER;

  public static final String                    DEFAULT_TEMPLATE         = BASE_URL
                                                                             + "&portal:windowState={wsrp-windowState}"
                                                                             + "&_mode={wsrp-portletMode}"
                                                                             + "&_isSecure={wsrp-secureURL}"
                                                                             + "&_component={wsrp-portletHandle}";

  public static final String                    RENDER_TEMPLATE          = DEFAULT_TEMPLATE
                                                                             + "&portal:type={wsrp-urlType}"
                                                                             + "&ns={wsrp-navigationalState}";

  public static final String                    BLOCKING_TEMPLATE        = DEFAULT_TEMPLATE
                                                                             + "&portal:type={wsrp-urlType}"
                                                                             + "&ns={wsrp-navigationalState}"
                                                                             + "&is={wsrp-interactionState}";

  public static final String[]                  CONSUMER_MODES           = { "wsrp:view",
      "wsrp:edit"                                                       };

  public static final String[]                  CONSUMER_STATES          = { "wsrp:normal",
      "wsrp:maximized"                                                  };

  public static final String[]                  CONSUMER_SCOPES          = { "chunk_data" };

  public static final String[]                  CONSUMER_CUSTOM_PROFILES = { "what_more" };

  private MockServletRequest                    mockServletRequest;

  private MockServletResponse                   mockServletResponse;

//  public BaseTest(String s) {
//    super(s);
//  }

  public void setUp() throws Exception {

    serviceDescriptionInterface = serviceLocator.getWSRPServiceDescriptionService(new URL(SERVICE_URL
        + "WSRPServiceDescriptionService"));
    registrationOperationsInterface = serviceLocator.getWSRPRegistrationService(new URL(SERVICE_URL
        + "WSRPRegistrationService"));
    markupOperationsInterface = serviceLocator.getWSRPMarkupService(new URL(SERVICE_URL
        + "WSRPMarkupService"));
    portletManagementOperationsInterface = serviceLocator.getWSRPPortletManagementService(new URL(SERVICE_URL
        + "WSRPPortletManagementService"));

    registrationData = new WS1RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent("exoplatform.1.0");
    registrationData.setMethodGetSupported(false);
    registrationData.setConsumerModes(CONSUMER_MODES);
    registrationData.setConsumerWindowStates(CONSUMER_STATES);
    registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
    registrationData.setRegistrationProperties(null);//allows extension of the specs
    registrationData.setExtensions(null);//allows extension of the specs

    personName = new WS1PersonName();
    personName.setNickname("exotest");

    userProfile = new WS1UserProfile();
    userProfile.setBdate(new GregorianCalendar());
    userProfile.setGender("male");
    userProfile.setName(personName);

    userContext = new WS1UserContext();
    userContext.setUserCategories(USER_CATEGORIES_ARRAY);
    userContext.setProfile(userProfile);
    userContext.setUserContextKey("exotest");

    templates = new WS1Templates();
    templates.setDefaultTemplate(DEFAULT_TEMPLATE);
    templates.setRenderTemplate(RENDER_TEMPLATE);
    templates.setRenderTemplate(BLOCKING_TEMPLATE);

    runtimeContext = new WS1RuntimeContext();
    runtimeContext.setNamespacePrefix("NamespacePrefix");
//runtimeContext.setPortletInstanceKey("windowID");
    runtimeContext.setSessionID(null);
    runtimeContext.setTemplates(templates);
    runtimeContext.setUserAuthentication("none");

    clientData = new WS1ClientData();
    clientData.setUserAgent("PC");

    markupParams = new WS1MarkupParams();
    markupParams.setClientData(clientData);
    markupParams.setLocales(localesArray);
    markupParams.setMarkupCharacterSets(markupCharacterSets);
    markupParams.setNavigationalState("");
    markupParams.setSecureClientCommunication(false);
    markupParams.setValidateTag(null);
    markupParams.setValidNewModes(null);
    markupParams.setValidNewWindowStates(null);
    markupParams.setMimeTypes(mimeTypes);
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");

    mockServletRequest = new MockServletRequest(new MockHttpSession(), new Locale("en"));
    mockServletResponse = new MockServletResponse(new FakeHttpResponse());
    WSRPHTTPContainer.createInstance((HttpServletRequest) mockServletRequest,
                                     (HttpServletResponse) mockServletResponse);
  }

  public void tearDown() throws Exception {

  }

  protected WS1ServiceDescription getServiceDescription(String[] locales) throws RemoteException {
    WS1ServiceDescriptionRequest getServiceDescription = new WS1ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(locales);
    return serviceDescriptionInterface.getServiceDescription(getServiceDescription);
  }

  protected WS1MarkupRequest getMarkup(WS1RegistrationContext rc, WS1PortletContext portletContext) {
    WS1MarkupRequest getMarkup = new WS1MarkupRequest();
    getMarkup.setRegistrationContext(rc);
    getMarkup.setPortletContext(portletContext);
    getMarkup.setRuntimeContext(runtimeContext);
    getMarkup.setUserContext(userContext);
    getMarkup.setMarkupParams(markupParams);
    return getMarkup;
  }

  protected void manageTemplatesOptimization(WS1ServiceDescription sd, String portletHandle) {
    WS1PortletDescription[] array = sd.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      WS1PortletDescription portletDescription = array[i];
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.getTemplatesStoredInSession().booleanValue()) {
          System.out.println("[test] set templates to null ");
          runtimeContext.setTemplates(null);
        }
      }
    }
  }

  protected void manageUserContextOptimization(WS1ServiceDescription sd,
                                               String portletHandle,
                                               WS1MarkupRequest getMarkup) {
    WS1PortletDescription[] array = sd.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      WS1PortletDescription portletDescription = array[i];
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.getUserContextStoredInSession().booleanValue()) {
          System.out.println("[test] set user context to null ");
          getMarkup.setUserContext(null);
        }
      }
    }
  }

  protected void manageUserContextOptimization(WS1ServiceDescription sd,
                                               String portletHandle,
                                               WS1BlockingInteractionRequest performBlockingInteraction) {
    WS1PortletDescription[] array = sd.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      WS1PortletDescription portletDescription = array[i];
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.getUserContextStoredInSession().booleanValue()) {
          System.out.println("[test] set user context to null ");
          performBlockingInteraction.setUserContext(null);
        }
      }
    }
  }

  protected void resolveRegistrationContext(WS1RegistrationContext registrationContext) throws Exception {
    byte[] registrationState = registrationContext.getRegistrationState();
    if (registrationState != null) {
      System.out.println("[test] Save registration state on consumer");
      Object o = IOUtil.deserialize(registrationState);
      if (!(o instanceof WS1RegistrationData))
        fail("The deserialized object should be of type RegistrationData");
      assertEquals(((WS1RegistrationData) o).getConsumerName(), registrationData.getConsumerName());
    }
  }

}
