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
package org.exoplatform.services.portletcontainer.imp;

import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestCaching;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestCharacterEncoding;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestEmbeddedConfig;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestHibernatePersistenceManager;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPersistentManager;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletConfig;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletContext;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletInterface;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletIoCComponent;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletLyfecycleListener;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletMode;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletModelParser;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletPreferences;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletRequests;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestPortletURL;
import org.exoplatform.services.portletcontainer.imp.Portlet168.TestWindowState;

/**
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 24.04.2007
 */
public class TestSuitePortlet168 extends TestSuite {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer.TestSuitePortlet168");

  public TestSuitePortlet168() {
    log.info("Preparing Portlet168 tests....");

    String newProperty = System.getProperty("basedir") + "/war_template";
    System.setProperty("mock.portal.dir", newProperty);
    System.setProperty("maven.exoplatform.dir", newProperty);

    addTestSuite(TestCaching.class);
    addTestSuite(TestCharacterEncoding.class);
    addTestSuite(TestEmbeddedConfig.class);
    addTestSuite(TestHibernatePersistenceManager.class);
    addTestSuite(TestPersistentManager.class);
    addTestSuite(TestPortletConfig.class);
    addTestSuite(TestPortletContext.class);
    addTestSuite(TestPortletInterface.class);
    addTestSuite(TestPortletIoCComponent.class);
    addTestSuite(TestPortletLyfecycleListener.class);
    addTestSuite(TestPortletMode.class);
    addTestSuite(TestPortletModelParser.class);
    addTestSuite(TestPortletPreferences.class);
    addTestSuite(TestPortletRequests.class);
    addTestSuite(TestPortletURL.class);
    addTestSuite(TestWindowState.class);
  }

  public void testVoid() throws Exception {
  }

}
