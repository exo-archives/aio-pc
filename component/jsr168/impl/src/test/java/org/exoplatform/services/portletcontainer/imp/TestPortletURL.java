package org.exoplatform.services.portletcontainer.imp;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL     
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 15 oct. 2003
 * Time: 20:51:01
 */
public class TestPortletURL extends BaseTest{

	public TestPortletURL(String s) {
		super(s);
	}


  /**
	 * test (xxviii) : A call to any of the setParameter methods must replace
	 *                 any parameter with the same name previously set.
	 *
	 * PLT.7.1
	 *
	 * This test uses the PortletToTestPortletURL portlet class to test
	 */
  public void testSetParameterMethods() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestPortletURL");
		RenderOutput o = portletContainer.render(request, response, input);
		assertNull(o.getTitle());
		assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
	}

	/**
	 * test (xxix) : All the parameters a portlet adds to a PortletURL object must be made available to
	 *               the portlet as request parameters.
	 *
	 * PLT.7.1
	 */
	public void testAvailibilityOfRenderParameters() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		Map renderMap = new HashMap();
		renderMap.put("testParam", "testParamValue");
    ((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestPortletURL2");
    input.setRenderParameters(renderMap);
		RenderOutput o = portletContainer.render(request, response, input);
		assertNull(o.getTitle());
		assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
	}

	/**
	 * test (xxx) : The portlet-container must ?x-www-form-urlencoded? encode parameter
	 *              names and values added to a PortletURL object.
	 *
	 * PLT.7.1
	 */
	public void testParameterEncoding() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US,false);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestParameterEncoding");
		RenderOutput o = portletContainer.render(request, response, input);		
		assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
	}

	/**
	 * test (xxxii) : A portlet cannot create a portlet URL using a portlet mode that is not defined as
	 *                supported by the portlet or that the user it is not allowed to use. The setPortletMode
	 *                methods must throw a PortletModeException in that situation.
	 *
	 * PLT.7.1.1
	 */
	public void testModeSupport() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestModeSupport");
		RenderOutput o = portletContainer.render(request, response, input);
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.PortletModeException: The mode edit is not supported by that portlet",
						new String(o.getContent()));
	}

	/**
	 * test (xxxiv) : A portlet cannot create a portlet URL using a window state that is not
	 *                supported by the portlet container. The setWindowState method must throw
	 *                a WindowStateException if that is the case.
	 *
	 * PLT.7.1.1
	 */
	public void testWindowStateSupport() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestStateSupport");
		RenderOutput o = portletContainer.render(request, response, input);
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.WindowStateException: The window state detached is not supported by the portlet container",
						new String(o.getContent()));
	}

	/**
	 * test (xxxv) : If the setSecure method is not used, the portlet URL must be of the same security
	 *               level of the current request.
	 *
	 * PLT.7.1.2
	 */
	public void testPortletURLSecurity() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestPortletURLSecurity");
		RenderOutput o = portletContainer.render(request, response, input);
		assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
	}

}
