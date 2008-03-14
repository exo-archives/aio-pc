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
import java.util.Collection;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;


public class TestModesAndStates  extends BaseTest{
  
  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.test.TestModesAndStates");

  public TestModesAndStates(String s) {
    super(s);
  }

   public void testStates() throws PortletContainerException {
     log.info("org.exoplatform.services.portletcontainer.test.testStates");
     try {
       Collection coll = portletContainer.getSupportedWindowStates();
       assertTrue(coll.size() > 0);
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
   }
   
   public void testModes() throws PortletContainerException {
     log.info("org.exoplatform.services.portletcontainer.test.testModes");
     try {
       Collection coll = portletContainer.getSupportedPortletModes();
       assertTrue(coll.size() > 0);
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
   }
}
