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
package org.exoplatform.services.wsrp;

import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.testConsumer.*;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 4.02.2008
 */
public class SuiteForTestConsumer extends TestSuite {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer..wsrp.SuiteForTestConsumer");

  public SuiteForTestConsumer() {
    log.info("Preparing SuiteForTestProducer tests....");

    String newProperty = System.getProperty("basedir") + "/war_template";
    System.setProperty("mock.portal.dir", newProperty);

//    addTestSuite(TestConsumerEnvironement.class); //does nothing
    addTestSuite(TestPortletRegistry.class);
//    addTestSuite(TestProducer.class);
    addTestSuite(TestProducerRegistry.class);
    addTestSuite(TestURLRewriter.class);
    addTestSuite(TestURLTemplateComposer.class);
    addTestSuite(TestUserRegistry.class);

  }

  public void testVoid() throws Exception {
  }

}
