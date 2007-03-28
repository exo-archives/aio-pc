/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 9, 2003
 * Time: 12:06:04 PM
 */
package org.exoplatform.services.portletcontainer.imp;


import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.Locale;

public class TestCaching extends BaseTest {

  public TestCaching(String s) {
    super(s);
  }

  public void testProgrammaticExpirationPeriodChange() throws PortletException {
    ((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, input);
      assertEquals(6, portletMonitor.getCacheExpirationPeriod("hello",
                                                              "HelloWorld"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testSimpleCache(){
    ((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, input);
      portletContainer.render(request, response, input);
      Thread.sleep(8000);
      portletContainer.render(request, response, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testNotExpirableCache(){
    ((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld2");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, input);
      //the console output should appear once
      portletContainer.render(request, response, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testPortletWithNoCache(){
    ((ExoWindowID)input.getWindowID()).setPortletName("PortletWithNoCacheTag");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, input);
      portletContainer.render(request, response, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testModeAndStateChange(){
    ((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld2");
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, input);
      portletContainer.render(request, response, input);
      input.setPortletMode(PortletMode.EDIT);
      portletContainer.render(request, response, input);
      portletContainer.render(request, response, input);
      input.setWindowState(WindowState.MAXIMIZED);
      portletContainer.render(request, response, input);
      portletContainer.render(request, response, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testRenderURLRequest(){
    ((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld2");
    input.setPortletMode(PortletMode.EDIT);
    HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {      
      portletContainer.render(request, response, input);
      input.setUpdateCache(true);
      portletContainer.render(request, response, input);
      input.setUpdateCache(false);
      portletContainer.render(request, response, input);
      input.setUpdateCache(true);
      portletContainer.render(request, response, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testGlobalCache(){
    ((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestGlobalCache");
    MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    try {
      portletContainer.render(request, response, input);
      //the console output should appear once
      portletContainer.render(request, response, input);
      request.setRemoteUser("new_remote");
      portletContainer.render(request, response, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
