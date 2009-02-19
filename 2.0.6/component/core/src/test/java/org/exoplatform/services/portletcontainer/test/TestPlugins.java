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

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.test.plugins.FakeHttpResponse;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;

public class TestPlugins extends BaseTest {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.test.TestPlugins");

  public TestPlugins(String s) {
    super(s);
  }

  public void testPlugins() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testPlugins");
    try {
      Collection<WindowState> coll = portletContainer.getSupportedWindowStates();
      assertTrue(coll.size() > 0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testRendering() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testRendering");
    try {
      MockServletRequest req = new MockServletRequest(new MockHttpSession(), Locale.ENGLISH);
      FakeHttpResponse res = new FakeHttpResponse();
      RenderInput input = new RenderInput();
      ExoWindowID windowID1 = new ExoWindowID();
      windowID1.setPortletApplicationName("Test");
      input.setInternalWindowID(windowID1);
      RenderOutput output = portletContainer.render(req, res, input);
      assertTrue(output.getTitle().equalsIgnoreCase("TEstTiTle"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testEventing() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testEventing");
    try {
      MockServletRequest req = new MockServletRequest(new MockHttpSession(), Locale.ENGLISH);
      FakeHttpResponse res = new FakeHttpResponse();
      EventInput input = new EventInput();
      ExoWindowID windowID1 = new ExoWindowID();
      windowID1.setPortletApplicationName("Test");
      input.setInternalWindowID(windowID1);
      EventOutput output = portletContainer.processEvent(req, res, input);
      assertTrue(output.getRenderParameters().size() > 0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testPrefs() throws PortletContainerException {
    log.info("org.exoplatform.services.portletcontainer.test.testPrefs");
    HashMap<String, String> inner_map = new HashMap<String, String>();
    inner_map.put("TestPrefKey1", "TestPrefValue1");
    inner_map.put("TestPrefKey2", "TestPrefValue2");
    inner_map.put("TestPrefKey3", "TestPrefValue3");
    try {
      Input input = new Input();
      ExoWindowID windowID1 = new ExoWindowID();
      windowID1.setPortletApplicationName("Test");
      input.setInternalWindowID(windowID1);
      portletContainer.setPortletPreference(input, inner_map);
      HashMap<String, String[]> out = (HashMap<String, String[]>) portletContainer.getPortletPreference(input);
      String[] arr = out.get("testKey");
      assertTrue(arr[0].indexOf("TestPrefValue") > -1);
      assertTrue(arr[1].indexOf("TestPrefValue") > -1);
      assertTrue(arr[2].indexOf("TestPrefValue") > -1);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
