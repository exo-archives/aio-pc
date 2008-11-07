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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
import org.exoplatform.services.wsrp2.ContainerStarter;
import org.exoplatform.services.wsrp2.intf.WSRPv2MarkupPortType;
import org.exoplatform.services.wsrp2.intf.WSRPv2PortletManagementPortType;
import org.exoplatform.services.wsrp2.intf.WSRPv2RegistrationPortType;
import org.exoplatform.services.wsrp2.intf.WSRPv2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.EventParams;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.ResourceParams;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;
//import org.exoplatform.services.wsrp2.wsdl.WSRPServiceLocator;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Date: 11 nov. 2003 Time:
 * 22:08:31 Revision: Max Shaposhnik 17.07.2008
 */
public class BaseTest extends TestCase {

  protected static final String                 SERVICE_URL              = "http://localhost:8080/hello/services2/";

  protected static final String                 CONTEXT_PATH             = "hello";

  protected static final String                 TEST_PATH                = (System.getProperty("testPath") == null ? "."
                                                                                                                  : System.getProperty("testPath"));

  //protected static final String                 PORTLET_APP_PATH         = "file:" + TEST_PATH + CONTEXT_PATH;

  protected static final String                 serviceUrlString         = SERVICE_URL.substring(0,
                                                                                                 SERVICE_URL.length() - 1);

  protected static final String                 consumerAgent            = "exoplatform.2.0";

  static boolean                                initService_             = true;

  protected PortletContainerService             portletContainer;

  protected PortletApplicationsHolder           holder;

  protected PortletApp                          portletApp_;

  protected Collection                          roles;

  protected WSRPv2ServiceDescriptionPortType serviceDescriptionInterface;

  protected WSRPv2RegistrationPortType       registrationOperationsInterface;

  protected WSRPv2MarkupPortType             markupOperationsInterface;

  protected WSRPv2PortletManagementPortType  portletManagementOperationsInterface;

  protected PersonName                          personName;

  protected UserContext                         userContext;

  protected UserProfile                         userProfile;

  protected RegistrationData                    registrationData;

  protected RuntimeContext                      runtimeContext;

  protected Templates                           templates;

  protected ClientData                          clientData;

  protected MarkupParams                        markupParams;

  protected ResourceParams                      resourceParams;

  protected EventParams                         eventParams;

  protected NavigationalContext                 navigationalContext;

  protected SessionParams                       sessionParams;

  protected Register                            register;

  protected Lifetime                            lifetime;

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

  public static boolean                         cargoCustomStatus        = false;

  public void setUp() throws Exception {

    // To use cargo container for dedicated test case. Note: it takes more time than run all tests. 
    /*
        String propertyExoTestCargo = System.getProperty("exo.test.cargo.skip");
        System.out.println("BaseTest.setUp() - propertyExoTestCargo = " + propertyExoTestCargo);

        if (System.getProperty("exo.test.cargo.skip") == null
            || !System.getProperty("exo.test.cargo.skip").equalsIgnoreCase("true")) {
          URL serviceUrl = new URL(serviceUrlString);
          URLConnection servletConnect = null;
          Object content = null;
          try {
            servletConnect = serviceUrl.openConnection();
            content = servletConnect.getContent();
            System.out.println("BaseTest.setUp() Service is up - OK");
          } catch (java.net.ConnectException e) {
            System.out.println("BaseTest.setUp() - going to start Cargo container.");
          }
          if (content == null) {
            cargoCustomStatus = ContainerStarter.start();
            assertTrue(cargoCustomStatus);
            System.out.println("BaseTest.setUp() cargoCustomStatus = " + cargoCustomStatus);
          }
        }
    */

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
    registrationData.setConsumerAgent(consumerAgent);
    registrationData.setMethodGetSupported(false);
    registrationData.setConsumerModes(CONSUMER_MODES);
    registrationData.setConsumerWindowStates(CONSUMER_STATES);
    registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
//    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
    registrationData.setRegistrationProperties(null);//allows extension of the specs
    registrationData.setExtensions(null);//allows extension of the specs

    Calendar currentTime = new GregorianCalendar();
    currentTime.setTime(new Date(System.currentTimeMillis()));

    Calendar terminationTime = new GregorianCalendar();
    terminationTime.setTime(new Date(System.currentTimeMillis() + 1));

    lifetime = null;

    register = new Register(registrationData, lifetime, userContext);

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

    resourceParams = new ResourceParams();
    resourceParams.setClientData(clientData);
    resourceParams.setLocales(localesArray);
    resourceParams.setMarkupCharacterSets(markupCharacterSets);
    resourceParams.setNavigationalContext(navigationalContext);
    resourceParams.setSecureClientCommunication(false);
    resourceParams.setValidateTag(null);
    resourceParams.setValidNewModes(null);
    resourceParams.setValidNewWindowStates(null);
    resourceParams.setMimeTypes(mimeTypes);
    resourceParams.setMode("wsrp:view");
    resourceParams.setWindowState("wsrp:normal");

    eventParams = new EventParams();
    eventParams.setEvents(null);

    mockServletRequest = new MockServletRequest(new MockHttpSession(), new Locale("en"));
    mockServletResponse = new MockServletResponse(new FakeHttpResponse());
    WSRPHTTPContainer.createInstance((HttpServletRequest) mockServletRequest,
                                     (HttpServletResponse) mockServletResponse);
  }

  public void tearDown() throws Exception {
    if (cargoCustomStatus) {
      assertFalse(ContainerStarter.stop());
    }
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

  protected GetResource getResource(RegistrationContext rc, PortletContext portletContext) {
    GetResource getResource = new GetResource();
    getResource.setRegistrationContext(rc);
    getResource.setPortletContext(portletContext);
    getResource.setRuntimeContext(runtimeContext);
    getResource.setUserContext(userContext);
    getResource.setResourceParams(resourceParams);
    return getResource;
  }

  protected HandleEvents handleEvents(RegistrationContext rc, PortletContext portletContext) {
    eventParams.setPortletStateChange(StateChange.readOnly);
    HandleEvents handleEvents = new HandleEvents();
    handleEvents.setRegistrationContext(rc);
    handleEvents.setPortletContext(portletContext);
    handleEvents.setRuntimeContext(runtimeContext);
    handleEvents.setUserContext(userContext);
    handleEvents.setMarkupParams(markupParams);
    handleEvents.setEventParams(eventParams);
    return handleEvents;
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

  protected void log() {
    StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
    System.out.println(">>>>>>>>>>>>>>> >>> " + ste.getClassName() + " - " + ste.getMethodName());
  }
  
  protected Lifetime getLifetimeInSec(int i){
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();
    calendar2.add(Calendar.SECOND, i);
    Lifetime lf = new Lifetime();
    lf.setCurrentTime(calendar1);
    lf.setTerminationTime(calendar2);
    return lf;
  }
  
}
