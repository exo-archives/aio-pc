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
package org.exoplatform.services.portletcontainer.test;


import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.PortletContainerPlugin;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.impl.PortletContainerServiceImpl;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;
import org.exoplatform.services.portletcontainer.test.plugins.TestPlugin1;
import org.exoplatform.services.portletcontainer.test.plugins.TestPlugin2;
import org.exoplatform.test.mocks.servlet.MockServletContext;

import junit.framework.TestCase;



public class BaseTest extends TestCase{
  
  protected PortalContainer portalContainer;
  protected PortletPreferencesPersister persister;
  //protected OrganizationService orgService_ ;
  protected PortletApp portletApp_;
  protected Collection roles;
  protected MockServletContext mockServletContext;
  protected PortletContainerServiceImpl portletContainer;
  protected RootContainer rootContainer;
  protected PortletContainerConf config;
  
  
  protected static final String CONTEXT_PATH = "/src/test/java/org/exoplatform/services/portletcontainer/test/xml";
  protected static final String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") + CONTEXT_PATH;
  
  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.test.BaseTest");
  
  public BaseTest(String s) {
    super(s);
  }
  
  
  public void setUp() throws Exception {
    log.info("BaseTest.setUp");
    
    
    portalContainer = PortalContainer.getInstance();
    
    URL url = new URL(PORTLET_APP_PATH + "/portlet.xml");
    InputStream is = url.openStream();
    portletApp_ = XMLParser.parse(is,true); //Second portlet specification - JSR 286


    portletContainer = (PortletContainerServiceImpl) portalContainer.getComponentInstanceOfType(PortletContainerService.class);
    
    //PortletContainerPlugin plugin1 =  new  TestPlugin1();
   // PortletContainerPlugin plugin2 =  new  TestPlugin2();
    //System.out.println("Portal Container " + portalContainer);
    //System.out.println("Portlet Container " + portletContainer);
    
//    TestPlugin1 plugin1 =  new  TestPlugin1();
    TestPlugin2 plugin2 =  new  TestPlugin2();
    plugin2.addPortletApp("TESTAPP1", portletApp_);
//    
//    portletContainer.addPlugin(plugin1);
    portletContainer.addPlugin((PortletContainerPlugin) plugin2);
    
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    this.config =  (PortletContainerConf) container.getComponentInstanceOfType(PortletContainerConf.class);
    
  }

  
  public void tearDown() throws Exception {
    System.out.println("Tear down");
    //portletContainer = null;
  }
  
  

}
