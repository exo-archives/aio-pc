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
import org.exoplatform.commons.xml.ExoXPPParser;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.pci.model.CustomPortletMode;
import org.exoplatform.services.portletcontainer.pci.model.CustomWindowState;
import org.exoplatform.services.portletcontainer.pci.model.EventDefinition;
import org.exoplatform.services.portletcontainer.pci.model.Filter;
import org.exoplatform.services.portletcontainer.pci.model.FilterMapping;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestParser extends BaseTest {
  
  private PortletApp portletApp;
  
  public TestParser(String s) {
    super(s);
  }
  
  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.test.TestParser");
  
  public void testReadCustomMode() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testReadCustomMode");
    try {
      URL url = new URL(PORTLET_APP_PATH + "/portletmode.xml");
      InputStream is = url.openStream();
      ArrayList<CustomPortletMode> list = new ArrayList<CustomPortletMode>();  
      
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      while (xpp.node("custom-portlet-mode")) {
         CustomPortletMode cm = XMLParser.readCustomPortletMode(xpp);
         list.add(cm);
      }
      assertEquals(1, list.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testReadCustomMode: " + ex.getMessage());
    }
  }
  
  public void testReadCustomState() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testReadCustomState");
    try {
      URL url = new URL(PORTLET_APP_PATH + "/windowstate.xml");
      InputStream is = url.openStream();
      ArrayList<CustomWindowState> list = new ArrayList();  
      
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      while (xpp.node("custom-window-state")) {
        CustomWindowState ws = XMLParser.readCustomWindowState(xpp);
         list.add(ws);
      }
      assertEquals(1, list.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testReadCustomState: " + ex.getMessage());
    }
  }
  
  public void testReadFilter() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testReadFilter");
    try {
      URL url = new URL(PORTLET_APP_PATH + "/filter.xml");
      InputStream is = url.openStream();
      ArrayList<Filter> list = new ArrayList();  
      
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      while (xpp.node("filter")) {
        Filter fl = XMLParser.readFilter(xpp);
         list.add(fl);
      }
      assertEquals(1, list.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testReadFilter: " + ex.getMessage());
    }
  }

  public void testReadFilterMapping() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testReadFilterMapping");
    try {
      URL url = new URL(PORTLET_APP_PATH + "/filtermapping.xml");
      InputStream is = url.openStream();
      ArrayList<FilterMapping> list = new ArrayList();  
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      while (xpp.node("filter-mapping")) {
        FilterMapping fm = XMLParser.readFilterMapping(xpp);
         list.add(fm);
      }
      assertEquals(1, list.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testReadFilterMapping: " + ex.getMessage());
    }
  }

  public void testReadEventDefinition() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testReadEventDefinition");
    try {
      URL url = new URL(PORTLET_APP_PATH + "/eventdefinition.xml");
      InputStream is = url.openStream();
      ArrayList<EventDefinition> list = new ArrayList();  
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      while (xpp.node("event-definition")) {
        EventDefinition ed = XMLParser.readEventDefinition(xpp);
         list.add(ed);
      }
      assertEquals(1, list.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testReadEventDefinition: " + ex.getMessage());
    }
  }

  public void testReadContainerRuntimeOption() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testReadContainerRuntimeOption");
    
    try {
      URL url = new URL(PORTLET_APP_PATH + "/container-runtime-option.xml");
      InputStream is = url.openStream();
      Map<String, String[]> cros = new HashMap<String, String[]>();
      
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      while (xpp.node("container-runtime-option")) {
        Map<String, String[]> cro = XMLParser.readContainerRuntimeOption(xpp);
         cros.putAll(cro);
      }
      assertEquals(1, cros.size());
      Portlet portlet = new Portlet();
      portlet.addContainerRuntimeOption(cros);
      assertEquals(1, portlet.getContainerRuntimeOption().size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testReadContainerRuntimeOption: " + ex.getMessage());
    }
  }
  
  public void testFullParsing() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testFullParsing");
    try {
      URL url = new URL(PORTLET_APP_PATH + "/portlet.xml");
      InputStream is = url.openStream();
      portletApp = XMLParser.parse(is,true);
      
      assertTrue(portletApp.getFilterMapping().get(0).getFilterName().equals("LoggerFilter1"));
      assertTrue(portletApp.getFilterMapping().get(1).getFilterName().equals("LoggerFilter2"));
      
      assertTrue(portletApp.getFilter().get(0).getFilterName().equals("LoggerFilter1"));
      assertTrue(portletApp.getFilter().get(0).getFilterClass().equals("org.exoplatform.services.portletcontainer.test.filter.LoggerFilter1"));
      assertTrue(portletApp.getFilter().get(0).getInitParam().size() >0);
      assertTrue(portletApp.getFilter().get(1).getFilterName().equals("LoggerFilter2"));
      assertTrue(portletApp.getFilter().get(1).getFilterClass().equals("org.exoplatform.services.portletcontainer.test.filter.LoggerFilter2"));
      assertTrue(portletApp.getFilter().get(1).getInitParam().size() == 0);
      
      assertTrue(portletApp.getCustomWindowState().get(0).getWindowState().equals("half-page"));
      assertTrue(portletApp.getCustomWindowState().get(1).getWindowState().equals("full-page"));
      
      assertTrue(portletApp.getCustomPortletMode().get(0).getPortletMode().equals("CONFIG"));
      
    }
    catch (Exception ex) {
      ex.printStackTrace();
      fail("testFullParsing: " + ex.getMessage());
    }
  }
  
}
