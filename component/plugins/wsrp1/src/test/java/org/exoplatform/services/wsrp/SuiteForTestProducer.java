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
package org.exoplatform.services.wsrp;

import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.test.TestCachingMechanism;
import org.exoplatform.services.wsrp.test.TestGetMarkup;
import org.exoplatform.services.wsrp.test.TestGetServiceDescriptionInterface;
import org.exoplatform.services.wsrp.test.TestPerformBlockingInteraction;
import org.exoplatform.services.wsrp.test.TestPersistentStateManager;
import org.exoplatform.services.wsrp.test.TestPortletManagementInterface;
import org.exoplatform.services.wsrp.test.TestRegistrationInterface;
import org.exoplatform.services.wsrp.test.TestSomeScenarios;
import org.exoplatform.services.wsrp.test.TestWSRPSession;

/**
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 4.02.2008
 */
public class SuiteForTestProducer extends TestSuite {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer..wsrp.SuiteForTestProducer");

  public SuiteForTestProducer() {
    log.info("Preparing SuiteForTestProducer tests....");

    String newProperty = System.getProperty("basedir") + "/war_template";
    System.setProperty("mock.portal.dir", newProperty); // = ${basedir}/war_template
    System.setProperty("maven.exoplatform.dir", newProperty); // = ${basedir}/war_template

    addTestSuite(TestCachingMechanism.class);
    addTestSuite(TestGetMarkup.class);
    addTestSuite(TestGetServiceDescriptionInterface.class);
    addTestSuite(TestPerformBlockingInteraction.class);
    addTestSuite(TestPersistentStateManager.class);
    addTestSuite(TestPortletManagementInterface.class);
    addTestSuite(TestRegistrationInterface.class);
    addTestSuite(TestSomeScenarios.class);
    addTestSuite(TestWSRPSession.class);

  }

  public void testVoid() throws Exception {
  }

}
