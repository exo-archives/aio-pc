/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.frameworks.portletcontainer.portalframework.test;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.exoplatform.services.log.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortalFramework;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.impl.PortletContainerServiceImpl;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletContainerMonitorImpl;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletContext;

public class BaseTest extends TestCase {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.portalframework.test.BaseTest");

  protected static final String CONTEXT_PATH = "/war_template";
  protected static final String PORTLET_APP_NAME = CONTEXT_PATH.substring(1);
  protected static final String TEST_PATH = (System.getProperty("testPath")==null?".":System.getProperty("testPath"));
  protected static final String PORTLET_APP_PATH = "file:" + TEST_PATH + "/src/test" + CONTEXT_PATH;
  protected PortletContainerServiceImpl portletContainer;
  //protected PortletMonitor portletMonitor;
  protected PortletApp portletApp_;
  protected OrganizationService orgService_ ;
  protected Collection<String> roles;
  protected URLClassLoader cl;
  protected URLClassLoader cl2;
  protected MockServletContext mockServletContext;
  static boolean initService_ = true;
  protected RenderInput renderInput;
  protected ActionInput actionInput;
  protected ResourceInput resourceInput;
  protected EventInput eventInput;
  protected PortletContainerMonitorImpl portletMonitor;
  protected PortletPreferencesPersister persister;
  protected PortletApplicationRegister portletApplicationRegister;
  protected PortalContainer portalContainer;
  protected Map<String, PortletData> allPortletMetaData = null;
  protected ExoWindowID windowID;
  protected String key1, key2, key3;
//  protected PortletContext portletContext;
//  protected PortletDataImp portletDatas;


  public ThreadLocal frameworks = new ThreadLocal();
  public PortalFramework framework = null;



  public BaseTest(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    log.info("BaseTest.setUp");
    portalContainer = PortalContainer.getInstance();
    WindowInfosContainer.createInstance(portalContainer, "windowinfoscontainer", "anon");
    WindowInfosContainer scontainer = WindowInfosContainer.getInstance();

    persister = (PortletPreferencesPersister) portalContainer.getComponentInstanceOfType(PortletPreferencesPersister.class) ;
    orgService_ = (OrganizationService)portalContainer.getComponentInstanceOfType(OrganizationService.class) ;
//    User user = orgService_.getUserHandler().findUserByName("exotest") ;
//    if(user == null) {
//      user = orgService_.getUserHandler().createUserInstance() ;
//      user.setUserName("exotest") ;
//      user.setPassword("exo") ;
//      user.setFirstName("Exo") ;
//      user.setLastName("Platform") ;
//      user.setEmail("exo@exoportal.org") ;
//      orgService_.getUserHandler().createUser(user, true);
//    }
    URL url = new URL(PORTLET_APP_PATH + "/WEB-INF/portlet.xml");
    InputStream is = url.openStream();
    portletApp_ = XMLParser.parse(is,true); //Second portlet specification - JSR 286

    roles = new java.util.ArrayList<String>() ;
    roles.add("auth-user");

    mockServletContext = new MockServletContext(PORTLET_APP_NAME, System.getProperty("testPath") + "/src/test" +CONTEXT_PATH);
    mockServletContext.setInitParameter("test-param", "test-parame-value");


    portletContainer = (PortletContainerServiceImpl) portalContainer.getComponentInstanceOfType(PortletContainerService.class);
    portletApplicationRegister = (PortletApplicationRegister)portalContainer.getComponentInstanceOfType(PortletApplicationRegister.class);
    portletApplicationRegister.registerPortletApplication(mockServletContext, portletApp_, roles, PORTLET_APP_NAME);
    portletMonitor = (PortletContainerMonitorImpl) RootContainer.getInstance().getComponentInstanceOfType(PortletContainerMonitorImpl.class);

     ((MockServletContext)portalContainer.getComponentInstanceOfType(MockServletContext.class)).setName(PORTLET_APP_NAME);
    allPortletMetaData = portletContainer.getAllPortletMetaData();


    framework = (PortalFramework) frameworks.get();
    if (framework == null) {
      framework = new PortalFramework(portalContainer);
      frameworks.set(framework);
    }
    
    framework.init(new MockHttpSession());
    
    key1 = framework.addPortlet(PORTLET_APP_NAME, "PortletToTestIncludingModeAndState", Constants.ANON_USER);
    key2 = framework.addPortlet(PORTLET_APP_NAME, "PortletToTestParametersIsolation", Constants.ANON_USER);
    key3 = framework.addPortlet(PORTLET_APP_NAME, "PortletToTestRenderParameters", Constants.ANON_USER);
    //String portal_container_name = framework.getPortalName();
    
  }

  public void tearDown() throws Exception {
    System.out.println("Tear down");
    try {
      portletApplicationRegister.removePortletApplication(mockServletContext, PORTLET_APP_NAME);
      PortalContainer manager  = PortalContainer.getInstance();
      HibernateService hservice =
          (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
      hservice.closeSession();
      WindowInfosContainer.removeInstance(PortalContainer.getInstance(), "windowinfoscontainer");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

