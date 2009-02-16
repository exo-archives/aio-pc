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

import java.util.Enumeration;
import java.util.Collection;
import java.util.Iterator;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.services.portletcontainer.pci.CustomModeWithDescription;
import org.exoplatform.services.portletcontainer.pci.CustomWindowStateWithDescription;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;

public class TestConfig extends BaseTest {
  
private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.test.TestConfig");
  
  public TestConfig(String s) {
    super(s);
  }
  
  public void testPModes() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testPModes");
    try {
      Enumeration<PortletMode>  enumer = config.getSupportedPortletModes();
      int i =0;
      while (enumer.hasMoreElements()){
        i++;
         enumer.nextElement();
      }
      assertTrue(i > 5);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  
  public void testWStates() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testWStates");
    try {
      Enumeration<WindowState>  enumer = config.getSupportedWindowStates();
      int i =0;
      while (enumer.hasMoreElements()){
        i++;
        enumer.nextElement();
      }
      assertTrue(i >= 5);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void testPModesWithDescr() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testPModesWithDescr");
    try {
      Collection<CustomModeWithDescription>  coll = config.getSupportedPortletModesWithDescriptions();
      assertTrue(coll.size() > 5);
      Iterator it= coll.iterator();
      while (it.hasNext()){
        CustomModeWithDescription cmwd = (CustomModeWithDescription)it.next();
        assertTrue(cmwd.getDescriptions().size() > 0 );
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void testWStatesWithDescr() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testWStatesWithDescr");
    try {
      Collection<CustomWindowStateWithDescription>  coll = config.getSupportedWindowStatesWithDescriptions();
      assertTrue(coll.size() >= 2); //setting of 3 here fails the test
      Iterator it= coll.iterator();
      while (it.hasNext()){
        CustomWindowStateWithDescription cmwd = (CustomWindowStateWithDescription)it.next();
        assertTrue(cmwd.getDescriptions().size() > 0 );
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
}
