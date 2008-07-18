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

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.xml.ExoXPPParser;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.pci.model.*;

import java.util.ArrayList;

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
      assertTrue(list.size() == 1);
  
    }
    catch (Exception ex) {
      ex.printStackTrace();
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
      assertTrue(list.size() == 1);
  
    }
    catch (Exception ex) {
      ex.printStackTrace();
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
      assertTrue(list.size() == 1);
  
    }
    catch (Exception ex) {
      ex.printStackTrace();
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
      assertTrue(list.size() == 1);
    }
    catch (Exception ex) {
      ex.printStackTrace();
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
      assertTrue(list.size() == 1);
    }
    catch (Exception ex) {
      ex.printStackTrace();
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
    }
  }
  
  
}
