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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;


/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 4.02.2008
 */
public class AllTestsPortlet extends TestCase {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer.wsrp.AllTestsPortlet");

  public static TestSuite suite() {
    log.info("Preparing...");
    System.out.println("TEST LOGGER: " + log);
    TestSuite suite = new TestSuite("portlet-container tests");
    
   assertTrue(ContainerStarter.start());
    
    suite.addTestSuite(SuiteForTestProducer.class);
    suite.addTestSuite(SuiteForTestConsumer.class);

//    suite.addTestSuite(TestPublicRenderParameters.class);
//    suite.addTestSuite(TestFilters.class);

    return suite;
  }
  
  
  protected void tearDown(){
    assertFalse(ContainerStarter.stop());
  }

}
