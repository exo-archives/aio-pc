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

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpResponse;
import org.exoplatform.services.wsrp1.intf.WSRPService1;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp1.type.WS1BlockingInteractionResponse;
import org.exoplatform.services.wsrp1.type.WS1ClientData;
import org.exoplatform.services.wsrp1.type.WS1ClonePortlet;
import org.exoplatform.services.wsrp1.type.WS1DestroyPortlets;
import org.exoplatform.services.wsrp1.type.WS1DestroyPortletsResponse;
import org.exoplatform.services.wsrp1.type.WS1GetMarkup;
import org.exoplatform.services.wsrp1.type.WS1GetServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1MarkupParams;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1PerformBlockingInteraction;
import org.exoplatform.services.wsrp1.type.WS1PersonName;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1PortletDescription;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1RegistrationData;
import org.exoplatform.services.wsrp1.type.WS1RuntimeContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1SetPortletProperties;
import org.exoplatform.services.wsrp1.type.WS1Templates;
import org.exoplatform.services.wsrp1.type.WS1UserContext;
import org.exoplatform.services.wsrp1.type.WS1UserProfile;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPMarkupPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPPortletManagementPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPRegistrationPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPServiceDescriptionPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.ext.WSRPV0ServiceAdministrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1MarkupPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1PortletManagementPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1RegistrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1ServiceDescriptionPortTypeAdapter;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Date: 11 nov. 2003 Time:
 * 22:08:31 Revision: Max Shaposhnik 17.07.2008
 */
public class BaseTest extends TestCase {

  protected static final String                      SERVICE_URL              = "http://localhost:8080/hello/soap/services/WSRPService1?wsdl";

  protected static final String                      ADMINISTRATION_ADDRESS   = "http://localhost:8080/hello/soap/services/WSRPService0/WSRP_v0_ServiceAdministration_Service";

  protected static final String                      CONTEXT_PATH             = "hello";

  protected static final String                      TEST_PATH                = (System.getProperty("testPath") == null ? "."
                                                                                                                       : System.getProperty("testPath"));


  static boolean                                     initService_             = true;

  protected PortletContainerService                  portletContainer;

  protected PortletApplicationsHolder                holder;

  protected PortletApp                               portletApp_;

  protected Collection                               roles;

  protected WSRPServiceDescriptionPortTypeAdapterAPI serviceDescriptionInterface;

  protected WSRPRegistrationPortTypeAdapterAPI       registrationOperationsInterface;

  protected WSRPMarkupPortTypeAdapterAPI             markupOperationsInterface;

  protected WSRPPortletManagementPortTypeAdapterAPI  portletManagementOperationsInterface;

  protected WS1PersonName                            personName;

  protected WS1UserContext                           userContext;

  protected WS1UserProfile                           userProfile;

  protected WS1RegistrationData                      registrationData;

  protected WS1RuntimeContext                        runtimeContext;

  protected WS1Templates                             templates;

  protected WS1ClientData                            clientData;

  protected WS1MarkupParams                          markupParams;

  protected WS1RegistrationContext                   registrationContext;

  protected static final String[]                    USER_CATEGORIES_ARRAY    = { "full",
      "standard", "minimal"                                                  };

  public static String[]                             localesArray             = { "en" };

  public static String[]                             markupCharacterSets      = { "UF-08",
      "ISO-8859-1"                                                           };

  public static String[]                             mimeTypes                = { "text/html",
      "text/xhtml"                                                           };

  public static final String                         BASE_URL                 = "/portal/faces/portal/portal.jsp?portal:ctx="
                                                                                  + Constants.DEFAUL_PORTAL_OWNER;

  public static final String                         DEFAULT_TEMPLATE         = BASE_URL
                                                                                  + "&portal:windowState={wsrp-windowState}"
                                                                                  + "&_mode={wsrp-portletMode}"
                                                                                  + "&_isSecure={wsrp-secureURL}"
                                                                                  + "&_component={wsrp-portletHandle}";

  public static final String                         RENDER_TEMPLATE          = DEFAULT_TEMPLATE
                                                                                  + "&portal:type={wsrp-urlType}"
                                                                                  + "&ns={wsrp-navigationalState}";

  public static final String                         BLOCKING_TEMPLATE        = DEFAULT_TEMPLATE
                                                                                  + "&portal:type={wsrp-urlType}"
                                                                                  + "&ns={wsrp-navigationalState}"
                                                                                  + "&is={wsrp-interactionState}";

  public static final String[]                       CONSUMER_MODES           = { "wsrp:view",
      "wsrp:edit"                                                            };

  public static final String[]                       CONSUMER_STATES          = { "wsrp:normal",
      "wsrp:maximized"                                                       };

  public static final String[]                       CONSUMER_SCOPES          = { "chunk_data" };

  public static final String[]                       CONSUMER_CUSTOM_PROFILES = { "what_more" };

  private MockServletRequest                         mockServletRequest;

  private MockServletResponse                        mockServletResponse;

  protected ExoContainer                             container;


  public void setUp() throws Exception {

    WSRPService1 service = new WSRPService1(new URL(SERVICE_URL));
    System.out.println(">>> ProducerImpl.init() service = " + service);

    String producerId = "producer2" + Integer.toString(SERVICE_URL.hashCode());

    StandaloneContainer.addConfigurationPath("src/test/resources/jcr-exo-configuration.xml");
    container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader()); //OK
    System.out.println(">>> BaseTest.setUp() container = " + container);
    if (container.getComponentInstance(producerId) == null)
      container.registerComponentInstance(producerId, service);

    WSRPV1ServiceDescriptionPortType serviceDescriptionPort = service.getWSRPServiceDescriptionService();

    this.serviceDescriptionInterface = new WSRPV1ServiceDescriptionPortTypeAdapter(serviceDescriptionPort);
    this.markupOperationsInterface = new WSRPV1MarkupPortTypeAdapter(service.getWSRPMarkupService());
    this.registrationOperationsInterface = new WSRPV1RegistrationPortTypeAdapter(service.getWSRPRegistrationService());
    this.portletManagementOperationsInterface = new WSRPV1PortletManagementPortTypeAdapter(service.getWSRPPortletManagementService());

    //    serviceDescriptionInterface = serviceLocator.getWSRPServiceDescriptionService(new URL(SERVICE_URL
    //        + "WSRPServiceDescriptionService"));
    //    registrationOperationsInterface = serviceLocator.getWSRPRegistrationService(new URL(SERVICE_URL
    //        + "WSRPRegistrationService"));
    //    markupOperationsInterface = serviceLocator.getWSRPMarkupService(new URL(SERVICE_URL
    //        + "WSRPMarkupService"));
    //    portletManagementOperationsInterface = serviceLocator.getWSRPPortletManagementService(new URL(SERVICE_URL
    //        + "WSRPPortletManagementService"));

    registrationData = new WS1RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent("exoplatform.1.0");
    registrationData.setMethodGetSupported(false);
    registrationData.getConsumerModes().addAll(Arrays.asList(CONSUMER_MODES));
    registrationData.getConsumerWindowStates().addAll(Arrays.asList(CONSUMER_STATES));
    registrationData.getConsumerUserScopes().addAll(Arrays.asList(CONSUMER_SCOPES));

    personName = new WS1PersonName();
    personName.setNickname("exotest");

    userProfile = new WS1UserProfile();
    XMLGregorianCalendar gc = new XMLGregorianCalendarImpl(new GregorianCalendar());
    userProfile.setBdate(gc);
    userProfile.setGender("male");
    userProfile.setName(personName);

    userContext = new WS1UserContext();
    userContext.getUserCategories().addAll(Arrays.asList(USER_CATEGORIES_ARRAY));
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
    markupParams.getLocales().addAll(Arrays.asList(localesArray));
    markupParams.getMarkupCharacterSets().addAll(Arrays.asList(markupCharacterSets));
    markupParams.setNavigationalState("");
    markupParams.setSecureClientCommunication(false);
    markupParams.setValidateTag(null);
    //    markupParams.getValidNewModes().addAll(null);
    //    markupParams.getValidNewWindowStates().addAll(null);
    markupParams.getMimeTypes().addAll(Arrays.asList(mimeTypes));
    markupParams.setMode("wsrp:view");
    markupParams.setWindowState("wsrp:normal");

    mockServletRequest = new MockServletRequest(new MockHttpSession(), new Locale("en"));
    mockServletResponse = new MockServletResponse(new FakeHttpResponse());
    WSRPHTTPContainer.createInstance((HttpServletRequest) mockServletRequest,
                                     (HttpServletResponse) mockServletResponse);
  }

  public void tearDown() throws Exception {
    this.serviceDescriptionInterface = null;
    this.markupOperationsInterface = null;
    this.portletManagementOperationsInterface = null;
    this.registrationOperationsInterface = null;
    //System.gc();
  }

  protected WS1ServiceDescription getServiceDescription(String[] locales) throws Exception {
    WS1GetServiceDescription ws1GetServiceDescription = new WS1GetServiceDescription();
    ws1GetServiceDescription.getDesiredLocales().addAll(Arrays.asList(locales));
    return getServiceDescription(ws1GetServiceDescription);
  }

  protected WS1GetMarkup getMarkup(WS1RegistrationContext rc, WS1PortletContext portletContext) {
    WS1GetMarkup getMarkup = new WS1GetMarkup();
    getMarkup.setRegistrationContext(rc);
    getMarkup.setPortletContext(portletContext);
    getMarkup.setRuntimeContext(runtimeContext);
    getMarkup.setUserContext(userContext);
    getMarkup.setMarkupParams(markupParams);
    return getMarkup;
  }

  protected void manageTemplatesOptimization(WS1ServiceDescription sd, String portletHandle) {
    List<WS1PortletDescription> list = sd.getOfferedPortlets();
    for (WS1PortletDescription portletDescription : list) {
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.isTemplatesStoredInSession().booleanValue()) {
          System.out.println("[test] set templates to null ");
          runtimeContext.setTemplates(null);
        }
      }
    }
  }

  protected void manageUserContextOptimization(WS1ServiceDescription sd,
                                               String portletHandle,
                                               WS1GetMarkup getMarkup) {
    List<WS1PortletDescription> list = sd.getOfferedPortlets();
    for (WS1PortletDescription portletDescription : list) {
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.isUserContextStoredInSession().booleanValue()) {
          System.out.println("[test] set user context to null ");
          getMarkup.setUserContext(null);
        }
      }
    }
  }

  protected void manageUserContextOptimization(WS1ServiceDescription sd,
                                               String portletHandle,
                                               WS1PerformBlockingInteraction performBlockingInteraction) {
    List<WS1PortletDescription> array = sd.getOfferedPortlets();
    for (WS1PortletDescription portletDescription : array) {
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        System.out.println("[test] use of portlet handle : " + portletHandle);
        if (portletDescription.isUserContextStoredInSession().booleanValue()) {
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

  protected void createRegistrationContext(WS1ServiceDescription sd) throws Exception {
    if (sd == null)
      sd = getServiceDescription(new String[] { "en" });
    if (sd.isRequiresRegistration()) {
      registrationContext = new WS1RegistrationContext();
      registrationContext.setRegistrationHandle("");
    } else {
      registrationContext = new WS1RegistrationContext();
    }
  }

  protected void setRequiresRegistration(boolean isRequiresRegistration) {
    WSRPV0ServiceAdministrationPortTypeAdapter administrationPort = null;
    Map<String, String> responseProps = null;
    administrationPort = new WSRPV0ServiceAdministrationPortTypeAdapter(ADMINISTRATION_ADDRESS);
    String requestProps = WSRPConfiguration.REQUIRES_REGISTRATION.concat("=")
                                                                 .concat(String.valueOf(isRequiresRegistration));
    responseProps = administrationPort.getServiceAdministration(requestProps);
    if (!responseProps.containsKey(WSRPConfiguration.REQUIRES_REGISTRATION)) {
      fail("WSRPConfiguration doesn't return REQUIRES_REGISTRATION property");
    }
    if (!responseProps.get(WSRPConfiguration.REQUIRES_REGISTRATION)
                      .equalsIgnoreCase(String.valueOf(isRequiresRegistration))) {
      fail("WSRPConfiguration doesn't return properly modified REQUIRES_REGISTRATION property");
    }
  }

  protected boolean getRequiresRegistration() {
    WSRPV0ServiceAdministrationPortTypeAdapter administrationPort = null;
    Map<String, String> responseProps = null;
    administrationPort = new WSRPV0ServiceAdministrationPortTypeAdapter(ADMINISTRATION_ADDRESS);
    responseProps = administrationPort.getServiceAdministration("");
    if (!responseProps.containsKey(WSRPConfiguration.REQUIRES_REGISTRATION)) {
      fail("WSRPConfiguration doesn't return REQUIRES_REGISTRATION property");
    }
    if (responseProps.get(WSRPConfiguration.REQUIRES_REGISTRATION) == null) {
      fail("WSRPConfiguration returns null REQUIRES_REGISTRATION property");
    }
    return Boolean.parseBoolean(responseProps.get(WSRPConfiguration.REQUIRES_REGISTRATION));
  }

  protected WS1MarkupResponse getMarkup(WS1GetMarkup ws1GetMarkup) throws Exception {
    return WSRPTypesTransformer.getWS1MarkupResponse(markupOperationsInterface.getMarkup(WSRPTypesTransformer.getWS2GetMarkup(ws1GetMarkup)));
  }

  protected WS1RegistrationContext register(WS1RegistrationData ws1RegistrationData) throws Exception {
    Register register = new Register();
    register.setRegistrationData(WSRPTypesTransformer.getWS2RegistrationData(ws1RegistrationData));
    return WSRPTypesTransformer.getWS1RegistrationContext(registrationOperationsInterface.register(register));
  }

  protected WS1PortletContext clonePortlet(WS1ClonePortlet clonePortlet) throws Exception {
    return WSRPTypesTransformer.getWS1PortletContext(portletManagementOperationsInterface.clonePortlet(WSRPTypesTransformer.getWS2ClonePortlet(clonePortlet)));
  }

  protected WS1DestroyPortletsResponse destroyPortlets(WS1DestroyPortlets ws1DestroyPortlets) throws Exception {
    DestroyPortlets destroyPortlets = WSRPTypesTransformer.getWS2DestroyPortlets(ws1DestroyPortlets);
    DestroyPortletsResponse destroyPortletsResponse = portletManagementOperationsInterface.destroyPortlets(destroyPortlets);
    WS1DestroyPortletsResponse response = WSRPTypesTransformer.getWS1DestroyPortletsResponse(destroyPortletsResponse);
    return response;
  }

  protected WS1PortletContext setPortletProperties(WS1SetPortletProperties setPortletProperties) throws Exception {
    return WSRPTypesTransformer.getWS1PortletContext(portletManagementOperationsInterface.setPortletProperties(WSRPTypesTransformer.getWS2SetPortletProperties(setPortletProperties)));
  }

  protected WS1ServiceDescription getServiceDescription(WS1GetServiceDescription getServiceDescription) throws Exception {
    return WSRPTypesTransformer.getWS1ServiceDescription(serviceDescriptionInterface.getServiceDescription(WSRPTypesTransformer.getWS2GetServiceDescription(getServiceDescription)));
  }

  protected WS1BlockingInteractionResponse performBlockingInteraction(WS1PerformBlockingInteraction performBlockingInteraction) throws Exception {
    return WSRPTypesTransformer.getWS1BlockingInteractionResponse(markupOperationsInterface.performBlockingInteraction(WSRPTypesTransformer.getWS2PerformBlockingInteraction(performBlockingInteraction)));
  }

}
