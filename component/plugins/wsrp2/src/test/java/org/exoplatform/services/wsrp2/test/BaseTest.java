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

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpResponse;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Markup_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_PortletManagement_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Registration_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_ServiceDescription_PortType;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;
import org.exoplatform.services.wsrp2.wsdl.WSRPServiceLocator;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Date: 11 nov. 2003 Time:
 * 22:08:31 Revision: Max Shaposhnik 17.07.2008
 */
public class BaseTest extends TestCase {

  protected static final String                 SERVICE_URL              = "http://localhost:8080/wsrp/services2/";

  //protected static final String                 CONTEXT_PATH             = "/war_template";
  protected static final String                 TEST_PATH                = (System.getProperty("testPath") == null ? "."
                                                                                                                  : System.getProperty("testPath"));

  //protected static final String                 PORTLET_APP_PATH         = "file:" + TEST_PATH + CONTEXT_PATH;

  static boolean                                initService_             = true;

  protected PortletContainerService             portletContainer;

  protected PortletApplicationsHolder           holder;

  protected PortletApp                          portletApp_;

  protected Collection                          roles;

  protected WSRP_v2_ServiceDescription_PortType serviceDescriptionInterface;

  protected WSRP_v2_Registration_PortType       registrationOperationsInterface;

  protected WSRP_v2_Markup_PortType             markupOperationsInterface;

  protected WSRP_v2_PortletManagement_PortType  portletManagementOperationsInterface;

  protected PersonName                          personName;

  protected UserContext                         userContext;

  protected UserProfile                         userProfile;

  protected RegistrationData                    registrationData;

  protected RuntimeContext                      runtimeContext;

  protected Templates                           templates;

  protected ClientData                          clientData;

  protected MarkupParams                        markupParams;

  protected NavigationalContext                 navigationalContext;

  protected SessionParams                       sessionParams;

  protected Register                            register;

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

    WSRPServiceLocator serviceLocator = new WSRPServiceLocator();
    serviceDescriptionInterface = serviceLocator.getWSRPServiceDescriptionService(new URL(SERVICE_URL
        + "WSRPServiceDescriptionService"));
    registrationOperationsInterface = serviceLocator.getWSRPRegistrationService(new URL(SERVICE_URL
        + "WSRPRegistrationService"));
    markupOperationsInterface = serviceLocator.getWSRPMarkupService(new URL(SERVICE_URL
        + "WSRPMarkupService"));
    portletManagementOperationsInterface = serviceLocator.getWSRPPortletManagementService(new URL(SERVICE_URL
        + "WSRPPortletManagementService"));

    registrationData = new RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent("exoplatform.1.0");
    registrationData.setMethodGetSupported(false);
    registrationData.setConsumerModes(CONSUMER_MODES);
    registrationData.setConsumerWindowStates(CONSUMER_STATES);
    registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
//    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
    registrationData.setRegistrationProperties(null);//allows extension of the specs
    registrationData.setExtensions(null);//allows extension of the specs

    register = new Register(registrationData, null, userContext);

    personName = new PersonName();
    personName.setNickname("exotest");

    userProfile = new UserProfile();
    userProfile.setBdate(new GregorianCalendar());
    userProfile.setGender("male");
    userProfile.setName(personName);

    userContext = new UserContext();
    userContext.setUserCategories(USER_CATEGORIES_ARRAY);
    userContext.setProfile(userProfile);
    userContext.setUserContextKey("exotest");

    templates = new Templates();
    templates.setDefaultTemplate(DEFAULT_TEMPLATE);
    templates.setRenderTemplate(RENDER_TEMPLATE);
    templates.setRenderTemplate(BLOCKING_TEMPLATE);

    sessionParams = new SessionParams();
    sessionParams.setSessionID(null);

    runtimeContext = new RuntimeContext();
    runtimeContext.setNamespacePrefix("NamespacePrefix");
    runtimeContext.setPortletInstanceKey("windowID");
    runtimeContext.setSessionParams(sessionParams);
    runtimeContext.setTemplates(templates);
    runtimeContext.setUserAuthentication("none");

    clientData = new ClientData();
    clientData.setUserAgent("PC");

    navigationalContext = new NavigationalContext();
    navigationalContext.setOpaqueValue("");

    markupParams = new MarkupParams();
    markupParams.setClientData(clientData);
    markupParams.setLocales(localesArray);
    markupParams.setMarkupCharacterSets(markupCharacterSets);
    markupParams.setNavigationalContext(navigationalContext);
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

  protected ServiceDescription getServiceDescription(String[] locales) throws RemoteException {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setDesiredLocales(locales);
    return serviceDescriptionInterface.getServiceDescription(getServiceDescription);
  }

  protected GetMarkup getMarkup(RegistrationContext rc, PortletContext portletContext) {
    GetMarkup getMarkup = new GetMarkup();
    getMarkup.setRegistrationContext(rc);
    getMarkup.setPortletContext(portletContext);
    getMarkup.setRuntimeContext(runtimeContext);
    getMarkup.setUserContext(userContext);
    getMarkup.setMarkupParams(markupParams);
    return getMarkup;
  }

  protected void manageTemplatesOptimization(ServiceDescription sd, String portletHandle) {
    PortletDescription[] array = sd.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      PortletDescription portletDescription = array[i];
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.getTemplatesStoredInSession().booleanValue()) {
          System.out.println("[test] set templates to null ");
          runtimeContext.setTemplates(null);
        }
      }
    }
  }

  protected void manageUserContextOptimization(ServiceDescription sd,
                                               String portletHandle,
                                               GetMarkup getMarkup) {
    PortletDescription[] array = sd.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      PortletDescription portletDescription = array[i];
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.getUserContextStoredInSession().booleanValue()) {
          System.out.println("[test] set user context to null ");
          getMarkup.setUserContext(null);
        }
      }
    }
  }

  protected void manageUserContextOptimization(ServiceDescription sd,
                                               String portletHandle,
                                               PerformBlockingInteraction performBlockingInteraction) {
    PortletDescription[] array = sd.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      PortletDescription portletDescription = array[i];
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.getUserContextStoredInSession().booleanValue()) {
          System.out.println("[test] set user context to null ");
          performBlockingInteraction.setUserContext(null);
        }
      }
    }
  }

  protected void resolveRegistrationContext(RegistrationContext registrationContext) throws Exception {
    byte[] registrationState = registrationContext.getRegistrationState();
    if (registrationState != null) {
      System.out.println("[test] Save registration state on consumer");
      Object o = IOUtil.deserialize(registrationState);
      if (!(o instanceof RegistrationData))
        fail("The deserialized object should be of type RegistrationData");
      assertEquals(((RegistrationData) o).getConsumerName(), registrationData.getConsumerName());
    }
  }

}
