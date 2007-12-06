/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.testConsumer;



import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.wsrp.consumer.PortletKey;
import org.exoplatform.services.wsrp.consumer.PortletRegistry;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp.consumer.URLGenerator;
import org.exoplatform.services.wsrp.consumer.URLRewriter;
import org.exoplatform.services.wsrp.consumer.User;
import org.exoplatform.services.wsrp.consumer.UserRegistry;
import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.consumer.adapters.PortletKeyAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.UserAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.WSRPPortletAdapter;
import org.exoplatform.services.wsrp.consumer.impl.ProducerImpl;
import org.exoplatform.services.wsrp.type.PersonName;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.UserContext;
import org.exoplatform.services.wsrp.type.UserProfile;
import org.exoplatform.test.mocks.servlet.MockServletContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 17:39:19
 */

public class BaseTest extends TestCase {
  protected static final String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") + "/war_template";

  protected ProducerRegistry producerRegistry;
  protected Producer producer;
  protected RegistrationData registrationData;

  protected static final String[] USER_CATEGORIES_ARRAY = {
    "full", "standard", "minimal"
  };

  public static final String[] CONSUMER_MODES = {"wsrp:view", "wsrp:edit"};
  public static final String[] CONSUMER_STATES = {"wsrp:normal", "wsrp:maximized"};
  public static final String[] CONSUMER_SCOPES = {"chunk_data"};
  public static final String[] CONSUMER_CUSTOM_PROFILES = {"what_more"};

  public static final String PRODUCER_ID = "producerID";
  public static final String PRODUCER_DESCRIPTION = "producerDescription";
  public static final String PRODUCER_NAME = "producerName";
  public static final String PRODUCER_MARKUP_INTERFACE_ENDPOINT = "markupInterfaceEndpoint";
  public static final String PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT = "PortletManagementInterfaceEndpoint";
  public static final String PRODUCER_REGISTRATION_INTERFACE_ENDPOINT = "RegistrationInterfaceEndpoint";
  public static final String PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT = "ServiceDescriptionInterfaceEndpoint";

  public static final String[] desiredLocales = {"en"};
  protected PortletRegistry portletRegistry;
  protected UserRegistry userRegistry;
  protected UserContext userContext;
  protected PersonName personName;
  protected UserProfile userProfile;

  public static final String BASE_URL = "/portal/faces/portal/portal.jsp?portal:ctx=" + Constants.DEFAUL_PORTAL_OWNER;
  protected URLGenerator urlGenerator;
  protected URLRewriter urlRewriter;
  private MockServletContext mockServletContext;
  private PortletApp portletApp_;
  private PortletApplicationsHolder holder;

  private PortletApplicationRegister portletApplicationRegister;

  protected void setUp() throws Exception { 
    try {

      URL url = new URL(PORTLET_APP_PATH + "/WEB-INF/portlet.xml");
      InputStream is = url.openStream();
      portletApp_ = XMLParser.parse(is);

      Collection roles = new ArrayList();
      roles.add("auth-user");

      PortalContainer manager = PortalContainer.getInstance();

      mockServletContext = new MockServletContext("hello", "./war_template");
      mockServletContext.setInitParameter("test-param", "test-parame-value");

      portletApplicationRegister = (PortletApplicationRegister) manager.
        getComponentInstanceOfType(PortletApplicationRegister.class); 
    
      portletApplicationRegister.registerPortletApplication(mockServletContext, portletApp_, roles, "war_template");

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    PortalContainer manager = PortalContainer.getInstance();
    producerRegistry = (ProducerRegistry) manager.getComponentInstanceOfType(ProducerRegistry.class);
    portletRegistry = (PortletRegistry) manager.getComponentInstanceOfType(PortletRegistry.class);
    userRegistry = (UserRegistry) manager.getComponentInstanceOfType(UserRegistry.class);

    registrationData = new RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent("exoplatform.1.0");
    registrationData.setMethodGetSupported(false);
    registrationData.setConsumerModes(CONSUMER_MODES);
    registrationData.setConsumerWindowStates(CONSUMER_STATES);
    registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);

    producer = new ProducerImpl(manager);
    producer.setID(PRODUCER_ID);
    producer.setDescription(PRODUCER_DESCRIPTION);
    producer.setName(PRODUCER_NAME);
    //producer.setMarkupInterfaceEndpoint(PRODUCER_MARKUP_INTERFACE_ENDPOINT);
    producer.setPortletManagementInterfaceEndpoint(PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT);
    producer.setRegistrationInterfaceEndpoint(PRODUCER_REGISTRATION_INTERFACE_ENDPOINT);
    producer.setServiceDescriptionInterfaceEndpoint(PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT);

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

    urlRewriter = (URLRewriter) manager.getComponentInstanceOfType(URLRewriter.class);
  }

  public void tearDown() throws Exception {
    try {
      PortalContainer manager  = PortalContainer.getInstance();
      
      portletApplicationRegister.removePortletApplication(mockServletContext);
      HibernateService hservice = 
          (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
      hservice.closeSession();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected WSRPPortlet createPortlet(String portletHandle,
                                      String parent,
                                      PortletContext portletContext) {
    PortletKey portletKey = new PortletKeyAdapter();
    portletKey.setProducerId(PRODUCER_ID);
    portletKey.setPortletHandle(portletHandle);

    WSRPPortlet portlet = new WSRPPortletAdapter(portletKey);
    portlet.setPortletKey(portletKey);
    return portlet;
  }

  protected User createUser(String userID) {
    User user = new UserAdapter();
    user.setUserID(userID);
    user.setUserContext(userContext);
    return user;
  }
}