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
package org.exoplatform.services.portletcontainer.imp.Portlet168;

import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.Locale;

/**
 * Created by The eXo Platform SAS         .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 9, 2003
 * Time: 12:06:04 PM
 */
public class TestCaching extends BaseTest {

  public TestCaching(String s) {
    super(s);
  }

  public void testProgrammaticExpirationPeriodChange() throws PortletException {
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      assertEquals(6, portletMonitor.getCacheExpirationPeriod("war_template",
                                                              "HelloWorld"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testSimpleCache(){
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      portletContainer.render(request, response, renderInput);
      Thread.sleep(8000);
      portletContainer.render(request, response, renderInput);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testNotExpirableCache(){
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld2");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      //the console output should appear once
      portletContainer.render(request, response, renderInput);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testPortletWithNoCache(){
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithNoCacheTag");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      portletContainer.render(request, response, renderInput);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testModeAndStateChange(){
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld2");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      portletContainer.render(request, response, renderInput);
      renderInput.setPortletMode(PortletMode.EDIT);
      portletContainer.render(request, response, renderInput);
      portletContainer.render(request, response, renderInput);
      renderInput.setWindowState(WindowState.MAXIMIZED);
      portletContainer.render(request, response, renderInput);
      portletContainer.render(request, response, renderInput);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testRenderURLRequest(){
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld2");
    renderInput.setPortletMode(PortletMode.EDIT);
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      renderInput.setUpdateCache(true);
      portletContainer.render(request, response, renderInput);
      renderInput.setUpdateCache(false);
      portletContainer.render(request, response, renderInput);
      renderInput.setUpdateCache(true);
      portletContainer.render(request, response, renderInput);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testGlobalCache(){
    ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestGlobalCache");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, renderInput);
      //the console output should appear once
      portletContainer.render(request, response, renderInput);
      request.setRemoteUser("new_remote");
      portletContainer.render(request, response, renderInput);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
