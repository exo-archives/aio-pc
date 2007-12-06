package org.exoplatform.services.wsrp.test;

import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.commons.Environment;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
//import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.wsrp.bind.WSRP_v1_Markup_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.bind.WSRP_v1_Registration_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.bind.WSRP_v1_ServiceDescription_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Markup_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v1_PortletManagement_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Registration_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v1_ServiceDescription_PortType;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.type.BlockingInteractionRequest;
import org.exoplatform.services.wsrp.type.ClientData;
import org.exoplatform.services.wsrp.type.MarkupParams;
import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.PersonName;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.RuntimeContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.ServiceDescriptionRequest;
import org.exoplatform.services.wsrp.type.Templates;
import org.exoplatform.services.wsrp.type.UserContext;
import org.exoplatform.services.wsrp.type.UserProfile;
import org.exoplatform.services.wsrp.wsdl.WSRPServiceLocator;
import org.exoplatform.test.mocks.servlet.MockServletContext;



/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Tuan Nguyen
 * tuan08@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 22:08:31
 */
public class BaseTest extends TestCase {

  protected static final String SERVICE_URL = "http://localhost:8081/portal/services/";
  protected static final String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") + "/war_template";
  static boolean initService_ = true;
  protected PortletContainerService portletContainer;
  protected PortletApplicationsHolder holder;
  protected PortletApp portletApp_;
  protected Collection roles;

  protected WSRP_v1_ServiceDescription_PortType serviceDescriptionInterface;
  protected WSRP_v1_Registration_PortType registrationOperationsInterface;
  protected WSRP_v1_Markup_PortType markupOperationsInterface;
  protected WSRP_v1_PortletManagement_PortType portletManagementOperationsInterface;

  protected PersonName personName;
  protected UserContext userContext;
  protected UserProfile userProfile;

  protected RegistrationData registrationData;
  protected RuntimeContext runtimeContext;
  protected Templates templates;
  protected ClientData clientData;
  protected MarkupParams markupParams;
  protected static final String[] USER_CATEGORIES_ARRAY = {
    "full", "standard", "minimal"
  };
  public static String[] localesArray = {"en"};
  public static String[] markupCharacterSets = {"UF-08", "ISO-8859-1"};
  public static String[] mimeTypes = {"text/html", "text/xhtml"};

  public static final String BASE_URL = "/portal/faces/portal/portal.jsp?portal:ctx=" + Constants.DEFAUL_PORTAL_OWNER;
  public static final String DEFAULT_TEMPLATE = BASE_URL + "&portal:windowState={wsrp-windowState}" +
      "&_mode={wsrp-mode}" +
      "&_isSecure={wsrp-secureURL}" +
      "&_component={wsrp-portletHandle}";
  public static final String RENDER_TEMPLATE = DEFAULT_TEMPLATE + "&portal:type={wsrp-urlType}" +
      "&ns={wsrp-navigationalState}";
  public static final String BLOCKING_TEMPLATE = DEFAULT_TEMPLATE + "&portal:type={wsrp-urlType}" +
      "&ns={wsrp-navigationalState}" +
      "&is={wsrp-interactionState}";

  public static final String[] CONSUMER_MODES = {"wsrp:view", "wsrp:edit"};
  public static final String[] CONSUMER_STATES = {"wsrp:normal", "wsrp:maximized"};
  public static final String[] CONSUMER_SCOPES = {"chunk_data"};
  public static final String[] CONSUMER_CUSTOM_PROFILES = {"what_more"};
  private MockServletContext mockServletContext;
  private OrganizationService orgService_;
  private PortletApplicationRegister portletApplicationRegister;

  public BaseTest(String s) {
    super(s);
  }

  public void setUp() throws Exception {                    
    RootContainer.getInstance();
    if (Environment.getInstance().getPlatform() == Environment.STAND_ALONE) {
      PortalContainer manager = PortalContainer.getInstance();
//      manager.getComponentInstanceOfType(PortalConfigService.class);
      manager.getComponentInstanceOfType(PersistentStateManager.class);
      orgService_ =
          (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
      User user = orgService_.getUserHandler().findUserByName("exotest");
      if (user == null) {
        user = orgService_.getUserHandler().createUserInstance();
        user.setUserName("exotest");
        user.setPassword("exo");
        user.setFirstName("Exo");
        user.setLastName("Platform");
        user.setEmail("exo@exoportal.org");
        orgService_.getUserHandler().createUser(user, true);
      }
      URL url = new URL(PORTLET_APP_PATH + "/WEB-INF/portlet.xml");

      InputStream is = url.openStream();
      portletApp_ = XMLParser.parse(is) ;

      Collection roles = new ArrayList();
      roles.add("auth-user");


      mockServletContext = new MockServletContext("hello", "./war_template");
      mockServletContext.setInitParameter("test-param", "test-parame-value");

      portletContainer = (PortletContainerService) manager.
          getComponentInstanceOfType(PortletContainerService.class);

      portletApplicationRegister = (PortletApplicationRegister) manager.
        getComponentInstanceOfType(PortletApplicationRegister.class); 
      
      portletApplicationRegister.registerPortletApplication(mockServletContext, portletApp_, roles, "war_template");

      serviceDescriptionInterface = new WSRP_v1_ServiceDescription_Binding_SOAPImpl();
      registrationOperationsInterface = new WSRP_v1_Registration_Binding_SOAPImpl();
      markupOperationsInterface = new WSRP_v1_Markup_Binding_SOAPImpl();
      portletManagementOperationsInterface = new WSRP_v1_PortletManagement_Binding_SOAPImpl();
    } else {
      WSRPServiceLocator serviceLocator = new WSRPServiceLocator();
      serviceDescriptionInterface = serviceLocator.
          getWSRPServiceDescriptionService(new URL(SERVICE_URL + "WSRPServiceDescriptionService"));
      registrationOperationsInterface = serviceLocator.
          getWSRPRegistrationService(new URL(SERVICE_URL + "WSRPRegistrationService"));
      markupOperationsInterface = serviceLocator.
          getWSRPBaseService(new URL(SERVICE_URL + "WSRPBaseService"));
      portletManagementOperationsInterface = serviceLocator.
          getWSRPPortletManagementService(new URL(SERVICE_URL + "WSRPPortletManagementService"));
    }

    registrationData = new RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent("exoplatform.1.0");
    registrationData.setMethodGetSupported(false);
    registrationData.setConsumerModes(CONSUMER_MODES);
    registrationData.setConsumerWindowStates(CONSUMER_STATES);
    registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
    registrationData.setRegistrationProperties(null);//allows extension of the specs
    registrationData.setExtensions(null);//allows extension of the specs

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

    runtimeContext = new RuntimeContext();
    runtimeContext.setNamespacePrefix("NamespacePrefix");
//runtimeContext.setPortletInstanceKey("windowID");
    runtimeContext.setSessionID(null);
    runtimeContext.setTemplates(templates);
    runtimeContext.setUserAuthentication("none");

    clientData = new ClientData();
    clientData.setUserAgent("PC");

    markupParams = new MarkupParams();
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
  }

  public void tearDown() throws Exception {
    try {
      portletApplicationRegister.removePortletApplication(mockServletContext);
      PortalContainer manager  = PortalContainer.getInstance();
      HibernateService hservice = 
          (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
      hservice.closeSession();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected ServiceDescription getServiceDescription(String[] locales) throws RemoteException {
    ServiceDescriptionRequest getServiceDescription = new ServiceDescriptionRequest();
    getServiceDescription.setDesiredLocales(locales);
    return serviceDescriptionInterface.getServiceDescription(getServiceDescription);
  }

  protected MarkupRequest getMarkup(RegistrationContext rc, PortletContext portletContext) {
    MarkupRequest getMarkup = new MarkupRequest();
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

  protected void manageUserContextOptimization(ServiceDescription sd, String portletHandle, MarkupRequest getMarkup) {
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

  protected void manageUserContextOptimization(ServiceDescription sd, String portletHandle,
                                               BlockingInteractionRequest performBlockingInteraction) {
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

  protected void resolveRegistrationContext(RegistrationContext registrationContext)
      throws Exception {
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
