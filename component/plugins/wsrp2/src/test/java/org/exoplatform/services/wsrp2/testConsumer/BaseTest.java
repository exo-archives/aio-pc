/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.testConsumer;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.commons.Environment;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpResponse;
import org.exoplatform.services.wsrp2.consumer.PortletKey;
import org.exoplatform.services.wsrp2.consumer.PortletRegistry;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.consumer.User;
import org.exoplatform.services.wsrp2.consumer.UserRegistry;
import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.consumer.adapters.PortletKeyAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.UserAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPPortletAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2MarkupPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2PortletManagementPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2RegistrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2ServiceDescriptionPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.impl.ProducerImpl;
import org.exoplatform.services.wsrp2.mocks.MockWSRPService;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.producer.impl.utils.CalendarUtils;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;
import org.exoplatform.services.wsrp2.wsdl.WSRPService2;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletContext;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 17:39:19
 * Revision: Max Shaposhnik 17.07.2008
 */

public class BaseTest extends TestCase {

  protected static final String      PORTLET_APP_NAME                                = "war_template2";

  protected static final String      CONTEXT_PATH                                    = "/"
                                                                                         + PORTLET_APP_NAME;

  protected static final String      TEST_PATH                                       = (System.getProperty("testPath") == null ? "."
                                                                                                                              : System.getProperty("testPath"));

  protected static final String      PORTLET_APP_PATH                                = "file:"
                                                                                         + TEST_PATH
                                                                                         + CONTEXT_PATH;

  protected ProducerRegistry         producerRegistry;

  protected Producer                 producer;

  protected RegistrationData         registrationData;

  protected Register                 register;

//  protected PortalContainer          container;

  protected static final String[]    USER_CATEGORIES_ARRAY                           = { "full",
      "standard", "minimal"                                                         };

  public static final String[]       CONSUMER_MODES                                  = {
      "wsrp:view", "wsrp:edit"                                                      };

  public static final String[]       CONSUMER_STATES                                 = {
      "wsrp:normal", "wsrp:maximized"                                               };

  public static final String[]       CONSUMER_SCOPES                                 = { "chunk_data" };

  public static final String[]       CONSUMER_CUSTOM_PROFILES                        = { "what_more" };

  public static final String         PRODUCER_ID                                     = "producerID";

  public static final String         PRODUCER_DESCRIPTION                            = "producerDescription";

  public static final String         PRODUCER_NAME                                   = "producerName";

  public static final String         PRODUCER_MARKUP_INTERFACE_ENDPOINT              = "markupInterfaceEndpoint";

  public static final String         PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT  = "PortletManagementInterfaceEndpoint";

  public static final String         PRODUCER_REGISTRATION_INTERFACE_ENDPOINT        = "RegistrationInterfaceEndpoint";

  public static final String         PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT = "ServiceDescriptionInterfaceEndpoint";

  public static final String[]       desiredLocales                                  = { "en" };

  protected PortletRegistry          portletRegistry;

  protected UserRegistry             userRegistry;

  protected UserContext              userContext;

  protected PersonName               personName;

  protected UserProfile              userProfile;

  public static final String         BASE_URL                                        = "/portal/faces/portal/portal.jsp?portal:ctx="
                                                                                         + Constants.DEFAUL_PORTAL_OWNER;

  protected URLGenerator             urlGenerator;

  protected URLRewriter              urlRewriter;

  private MockServletContext         mockServletContext;

  private PortletApp                 portletApp_;

  private PortletApplicationsHolder  holder;

  private PortletApplicationRegister portletApplicationRegister;

  private MockServletRequest         mockServletRequest;

  private MockServletResponse        mockServletResponse;

  protected int                      platform                                        = 0;

  protected ExoContainer             container;

  public String                      consumerAgent                                   = "exoplatform.2.0";

  protected void setUp() throws Exception {

    URL url = new URL(PORTLET_APP_PATH + "/WEB-INF/portlet.xml");
    InputStream is = url.openStream();
    portletApp_ = XMLParser.parse(is, true);

    try {
      // Leaving for compatibility reasons
      //container = PortalContainer.getInstance();
      //container = RootContainer.getInstance().getPortalContainer("portal");

      StandaloneContainer.addConfigurationPath("src/test/resources/jcr-exo-configuration.xml");
      container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());

//      StandaloneContainer.setConfigurationPath("src/test/java/conf/test-configuration.xml");
//      container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());

    } catch (Throwable t) {
      t.printStackTrace();
    }

    int platform = Environment.getInstance().getPlatform();
    System.out.println(">>> Consumer BaseTest.setUp() platform = " + platform);
    Collection<String> roles = new ArrayList<String>();
    roles.add("auth-user");

    mockServletContext = new MockServletContext("hello", "./war_template2");
    mockServletContext.setInitParameter("test-param", "test-parame-value");

    portletApplicationRegister = (PortletApplicationRegister) container.getComponentInstanceOfType(PortletApplicationRegister.class);

    
    portletApplicationRegister.registerPortletApplication(mockServletContext,
                                                          portletApp_,
                                                          roles,
                                                          "war_template2");
    
    producerRegistry = (ProducerRegistry) container.getComponentInstanceOfType(ProducerRegistry.class);

    portletRegistry = (PortletRegistry) container.getComponentInstanceOfType(PortletRegistry.class);
    userRegistry = (UserRegistry) container.getComponentInstanceOfType(UserRegistry.class);

    registrationData = new RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent(consumerAgent);
    registrationData.setMethodGetSupported(false);
    registrationData.getConsumerModes().addAll(Arrays.asList(CONSUMER_MODES));
    registrationData.getConsumerWindowStates().addAll(Arrays.asList(CONSUMER_STATES));
    registrationData.getConsumerUserScopes().addAll(Arrays.asList(CONSUMER_SCOPES));
//    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);

    register = new Register();
    register.setRegistrationData(registrationData);
    register.setUserContext(userContext);

    producer = new ProducerImpl(container, null, 2);//"http://www.example.org/"
    producer.setID(PRODUCER_ID);
    producer.setDescription(PRODUCER_DESCRIPTION);
    producer.setName(PRODUCER_NAME);

    WSRPService2 service = (WSRPService2) container.getComponentInstanceOfType(MockWSRPService.class);
    if (service != null) {
//      container.unregisterComponentByInstance(service);
      container.unregisterComponent(PRODUCER_ID);
    } else {
      service = new MockWSRPService(container);
    }

    System.out.println(">>> BaseTest.setUp() service = " + service);
    System.out.println(">>> BaseTest.setUp() container = " + container);

    producer.createAdapters(service, container);
    producer.setServiceDescriptionAdapter(new WSRPV2ServiceDescriptionPortTypeAdapter(service.getWSRPV2ServiceDescriptionService()));
    producer.setMarkupAdapter(new WSRPV2MarkupPortTypeAdapter(service.getWSRPV2MarkupService()));
    producer.setRegistrationAdapter(new WSRPV2RegistrationPortTypeAdapter(service.getWSRPV2RegistrationService()));
    producer.setPortletManagementAdapter(new WSRPV2PortletManagementPortTypeAdapter(service.getWSRPV2PortletManagementService()));

//    producer.setMarkupInterfaceEndpoint(PRODUCER_MARKUP_INTERFACE_ENDPOINT);
//    producer.setPortletManagementInterfaceEndpoint(PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT);
//    producer.setRegistrationInterfaceEndpoint(PRODUCER_REGISTRATION_INTERFACE_ENDPOINT);
//    producer.setServiceDescriptionInterfaceEndpoint(PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT);

    personName = new PersonName();
    personName.setNickname("exotest");

    userProfile = new UserProfile();
    userProfile.setBdate(CalendarUtils.getNow());
    userProfile.setGender("male");
    userProfile.setName(personName);

    userContext = new UserContext();
    userContext.getUserCategories().addAll(Arrays.asList(USER_CATEGORIES_ARRAY));
    userContext.setProfile(userProfile);
    userContext.setUserContextKey("exotest");

    urlRewriter = (URLRewriter) container.getComponentInstanceOfType(URLRewriter.class);

    mockServletRequest = new MockServletRequest(new MockHttpSession(), new Locale("en"));
    mockServletResponse = new MockServletResponse(new FakeHttpResponse());
    WSRPHTTPContainer.createInstance((HttpServletRequest) mockServletRequest,
                                     (HttpServletResponse) mockServletResponse);
  }

  public void tearDown() throws Exception {
    PortletApplicationProxy proxy = (PortletApplicationProxy) container
    .getComponentInstance(PORTLET_APP_NAME + PCConstants.PORTLET_APP_ENCODER);
    assertNotNull(proxy);
    
    try {
      //war_template2_portlet_app_
      portletApplicationRegister.removePortletApplication(mockServletContext, PORTLET_APP_NAME);
      HibernateService hservice = (HibernateService) container.getComponentInstanceOfType(HibernateService.class);
      hservice.closeSession();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    proxy = (PortletApplicationProxy) container
    .getComponentInstance(PORTLET_APP_NAME + PCConstants.PORTLET_APP_ENCODER);
    assertNull(proxy);
  }

  protected WSRPPortlet createPortlet(String portletHandle,
                                      String parent,
                                      PortletContext portletContext) {
    PortletKey portletKey = new PortletKeyAdapter();
    portletKey.setProducerId(PRODUCER_ID);
    portletKey.setPortletHandle(portletHandle);

    WSRPPortlet portlet = new WSRPPortletAdapter(portletKey);
    return portlet;
  }

  protected User createUser(String userID) {
    User user = new UserAdapter();
    user.setUserID(userID);
    user.setUserContext(userContext);
    return user;
  }

  protected void log() {
    StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
    System.out.println(">>>>>>>>>>>>>>> >>> " + ste.getClassName() + " - " + ste.getMethodName());
  }

}
