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
package org.exoplatform.services.wsrp2;

import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.testProducer.TestCachingMechanism;
import org.exoplatform.services.wsrp2.testProducer.TestGetMarkup;
import org.exoplatform.services.wsrp2.testProducer.TestGetServiceAdministration;
import org.exoplatform.services.wsrp2.testProducer.TestGetServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.testProducer.TestPerformBlockingInteraction;
import org.exoplatform.services.wsrp2.testProducer.TestPersistentStateManager;
import org.exoplatform.services.wsrp2.testProducer.TestPortletManagementInterface;
import org.exoplatform.services.wsrp2.testProducer.TestRegistrationInterface;
import org.exoplatform.services.wsrp2.testProducer.TestSomeScenarios;
import org.exoplatform.services.wsrp2.testProducer.TestWSRPSession;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 4.02.2008
 */
public class SuiteForTestProducer extends TestSuite {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer.wsrp2.SuiteForTestProducer");

  public SuiteForTestProducer() {
    log.info("Preparing SuiteForTestProducer tests....");

    String newProperty = System.getProperty("basedir") + "/war_template2";
    System.setProperty("mock.portal.dir", newProperty);

    addTestSuite(TestGetMarkup.class);
    addTestSuite(TestRegistrationInterface.class);
    addTestSuite(TestCachingMechanism.class);
    addTestSuite(TestGetServiceAdministration.class);
    addTestSuite(TestGetServiceDescriptionInterface.class);
    addTestSuite(TestPerformBlockingInteraction.class);
    addTestSuite(TestPortletManagementInterface.class);
    addTestSuite(TestSomeScenarios.class);
    addTestSuite(TestWSRPSession.class);
    addTestSuite(TestPersistentStateManager.class);

  }

  public void testVoid() throws Exception {
  }

}
