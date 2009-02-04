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
package org.exoplatform.services.wsrp2;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua 4.02.2008
 */
public class AllTestsPortlet extends TestCase {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer.wsrp2.AllTestsPortlet");

  public static TestSuite suite() {
    log.info("Preparing...");
    System.out.println("TEST LOGGER: " + log);
    TestSuite suite = new TestSuite("portlet-container tests");

    // Whether we skip cargo container. In case standalone Tomcat instance. 
    if (System.getProperty("exo.test.cargo.skip") == null
        || !System.getProperty("exo.test.cargo.skip").equalsIgnoreCase("true")) {
      assertTrue(ContainerStarter.start());
    }

//    suite.addTestSuite(SuiteForTest.class);

    suite.addTestSuite(SuiteForTestProducer.class);

//    suite.addTestSuite(SuiteForTestConsumer.class);

    // for e.g. to run custom test
//    suite.addTestSuite(WSRPServiceTestCase.class);

    return suite;
  }

  protected void tearDown() {
    if (System.getProperty("exo.test.cargo.skip") == null
        || !System.getProperty("exo.test.cargo.skip").equalsIgnoreCase("true")) {
      assertFalse(ContainerStarter.stop());
    }
  }

}
