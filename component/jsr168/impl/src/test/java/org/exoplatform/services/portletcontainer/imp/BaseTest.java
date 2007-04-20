 /**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.imp;


import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;

import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.impl.PortletContainerServiceImpl;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletAPIObjectFactory;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;
import org.exoplatform.test.mocks.servlet.MockServletContext;

import junit.framework.TestCase;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 22:08:31
 */
public class BaseTest extends TestCase {

  protected static final String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") + "/war_template";
  protected PortletContainerServiceImpl portletContainer;
  //protected PortletMonitor portletMonitor;
  protected PortletApp portletApp_;
  protected OrganizationService orgService_ ;
  protected Collection roles;
  protected URLClassLoader cl;
  protected URLClassLoader cl2;
  protected MockServletContext mockServletContext;
  protected PortletContext portletContext;
  static boolean initService_ = true;
  protected RenderInput input;
  protected ActionInput actionInput;
  protected PortletContainerMonitorImpl portletMonitor;
  protected PortletPreferencesPersister persister;
  protected PortletApplicationRegister portletApplicationRegister;
  protected PortalContainer portalContainer;

  public BaseTest(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    portalContainer = PortalContainer.getInstance();
    WindowInfosContainer.createInstance(portalContainer, "windowinfoscontainer", "anon");
    WindowInfosContainer scontainer = WindowInfosContainer.getInstance();
    
    persister =  (PortletPreferencesPersister)	portalContainer.getComponentInstanceOfType(PortletPreferencesPersister.class) ;
    orgService_ = 
        (OrganizationService)portalContainer.getComponentInstanceOfType(OrganizationService.class) ;
    User user = orgService_.getUserHandler().findUserByName("exotest") ;
    if(user == null) {
    	user = orgService_.getUserHandler().createUserInstance() ;
    	user.setUserName("exotest") ;
    	user.setPassword("exo") ;
    	user.setFirstName("Exo") ;
    	user.setLastName("Platform") ;
    	user.setEmail("exo@exoportal.org") ;
    	orgService_.getUserHandler().createUser(user, true);
    } 
    URL url = new URL(PORTLET_APP_PATH + "/WEB-INF/portlet.xml");
    InputStream is = url.openStream();
    portletApp_ = XMLParser.parse(is);
    
    roles = new java.util.ArrayList() ;
    roles.add("auth-user");

    mockServletContext = new MockServletContext("hello", System.getProperty("testPath") + "/war_template");
    mockServletContext.setInitParameter("test-param", "test-parame-value");

    portletContainer = (PortletContainerServiceImpl) portalContainer.getComponentInstanceOfType(PortletContainerService.class);
    portletApplicationRegister = (PortletApplicationRegister)portalContainer.getComponentInstanceOfType(PortletApplicationRegister.class);
    portletApplicationRegister.registerPortletApplication(mockServletContext, portletApp_, roles);
    portletMonitor = (PortletContainerMonitorImpl) RootContainer.getInstance().getComponentInstanceOfType(PortletContainerMonitorImpl.class);

    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer, mockServletContext);
    ((MockServletContext)portalContainer.getComponentInstanceOfType(MockServletContext.class)).setName("hello");
    //can be overidden for specific usages
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(Constants.ANON_USER);
    windowID.setPortletApplicationName("hello");
    windowID.setUniqueID("windowID");
    windowID.setPersistenceId("persistenceID");


    input = new RenderInput();
    input.setBaseURL("exo/faces/portal/portal.jsp");
    input.setWindowID(windowID);
    input.setUserAttributes(new HashMap());
    input.setPortletMode(PortletMode.VIEW);
    input.setWindowState(WindowState.NORMAL);
    input.setMarkup("text/html");
    input.setRenderParameters(new HashMap());

    actionInput = new ActionInput();
    actionInput.setBaseURL("exo/faces/portal/portal.jsp");
    actionInput.setWindowID(windowID);
    actionInput.setUserAttributes(new HashMap());
    actionInput.setPortletMode(PortletMode.VIEW);
    actionInput.setWindowState(WindowState.NORMAL);
    actionInput.setMarkup("text/html");
  }

  public void tearDown() throws Exception {
    System.out.println("Tear down");
    try {
      portletApplicationRegister.removePortletApplication(mockServletContext);
      PortalContainer manager  = PortalContainer.getInstance();
      HibernateService hservice = 
          (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
      hservice.closeSession();
      WindowInfosContainer.removeInstance(portalContainer, "windowinfoscontainer");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
