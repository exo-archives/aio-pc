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

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

import junit.framework.TestSuite;

public class TestSuitePCCore extends TestSuite {
  
  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.test.TestSuitePCCore");

  public  TestSuitePCCore() {
    log.info("Preparing PC Core Tests....");


    addTestSuite(TestModesAndStates.class);
    addTestSuite(TestPlugins.class);
    //addTestSuite(TestRenderParameters.class);
  }

  public void testVoid() throws Exception {
  }

}
