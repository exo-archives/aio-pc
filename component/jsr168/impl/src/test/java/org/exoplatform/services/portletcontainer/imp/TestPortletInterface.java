/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.imp;

import java.util.Locale;

import javax.portlet.GenericPortlet;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.PortletContainerConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;


/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 23:43:29
 */
public class TestPortletInterface extends BaseTest {

  PortletApplicationProxy proxy;

	public void setUp() throws Exception {
		super.setUp();
    proxy = (PortletApplicationProxy) PortalContainer.getInstance().
        getComponentInstance("war_template");
	}

	public TestPortletInterface(String s) {
		super(s);
	}

	/**
	 * Test (i)
	 * PLT.5.1
	 */
	public void testPortletUnicity() throws Exception {
		Portlet p1 = proxy.getPortlet(portletContext, "HelloWorld");
		Portlet p2 = proxy.getPortlet(portletContext, "HelloWorld");
		assertEquals(p1, p2);
	}

	/**
	 * Test (iii)
	 * PLT.5.2.1
	 */
	public void testClassLoader() throws PortletException {
		//assertEquals(cl2, proxy.getPortlet(portletContext, "HelloWorld").getClass().getClassLoader());
	}

	/**
	 * Test (iv)
	 * PLT.5.2.2
	 */
	public void testInitialization() throws PortletException {
		GenericPortlet p = (GenericPortlet) proxy.getPortlet(portletContext, "HelloWorld");
		assertNotNull(p.getPortletConfig());
	}

	/**
	 * test PortletConfig unicity per portlet def
	 * PLT.5.2.2
	 */
	public void testPortletConfigUnicity() throws PortletException {
		GenericPortlet p1 = (GenericPortlet) proxy.getPortlet(portletContext, "HelloWorld");
		GenericPortlet p2 = (GenericPortlet) proxy.getPortlet(portletContext, "HelloWorld");
		assertEquals(p1.getPortletConfig(), p2.getPortletConfig());
	}

	/**
	 * test (v)  : test Portlet exception thrown in init method
	 * PLT.5.2.2.1
	 *
	 * test (vi) : test that the destroy method is not called (Should not see any output in conole)
	 * PLT.5.2.2.1
	 */
	public void testPortletExceptionWhileInit() {
		GenericPortlet p = null;
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithPortletExceptionWhileInit");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "exception while initializing portlet");
			assertTrue(e instanceof PortletException);
		}
		assertNull(p);
		assertFalse(portletMonitor.isInitialized("war_template",
						"PortletWithPortletExceptionWhileInit"));    
	}

	/**
	 * test (v) : test unavailable exception thrown in init method (Unavailable time 5s)
	 * PLT.5.2.2.1
	 *
	 * test (vi) : test that the destroy method is not called (Should not see any output in console)
	 * PLT.5.2.2.1
	 *
	 * test (vii) : test that the unavailable time period is respected
	 * PLT.5.2.2.1
	 */
	public void testUnavailableExceptionWhileInit() throws InterruptedException {
		GenericPortlet p = null;
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithUnavailableExceptionWhileInit");
		} catch (Exception e) {
			assertEquals("Unavailable portlet", e.getMessage());
			assertTrue(e instanceof PortletException);
			PortletRuntimeData rD = (PortletRuntimeData) portletMonitor.getPortletRuntimeDataMap().
							get("war_template" + PortletContainerMonitorImpl.SEPARATOR +
							"PortletWithUnavailableExceptionWhileInit");
			assertTrue(rD.getLastInitFailureAccessTime() > 0);
			assertEquals(rD.getUnavailabilityPeriod(), 5000);
		}
		Thread.sleep(100);
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithUnavailableExceptionWhileInit");
		} catch (Exception e) {
			assertEquals("Portlet initialization not possible", e.getMessage());
			assertTrue(e instanceof PortletException);
		}
		Thread.sleep(6000);
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithUnavailableExceptionWhileInit");
		} catch (Exception e) {
			assertEquals("Unavailable portlet", e.getMessage());
			assertTrue(e instanceof PortletException);
		}
		assertNull(p);
		assertFalse(portletMonitor.isInitialized("war_template",
						"PortletWithUnavailableExceptionWhileInit"));       
	}

	/**
	 * test (v) : test unavailable exception thrown in init method (Unavailable time 0s)
	 * PLT.5.2.2.1
	 *
	 * test (vi) : test that the destroy method is not called (Should not see any output in console)
	 * PLT.5.2.2.1
	 *
	 * test (vii) : test that an unavailable time period of 0 sec works too
	 * PLT.5.2.2.1
	 */
	public void testUnavailableExceptionWhileInit2() throws InterruptedException {
		GenericPortlet p = null;
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithUnavailableExceptionWhileInit2");
		} catch (Exception e) {
			assertEquals("Unavailable portlet", e.getMessage());
			assertTrue(e instanceof PortletException);
		}
		Thread.sleep(100);
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithUnavailableExceptionWhileInit2");
		} catch (Exception e) {
			assertEquals("Unavailable portlet", e.getMessage());
			assertTrue(e instanceof PortletException);
		}
		assertNull(p);
		assertFalse(portletMonitor.isInitialized("war_template",
						"PortletWithUnavailableExceptionWhileInit2"));
	}

	/**
	 * test (viii) : test that Runtime Exception is treated like PortletException
	 * PLT.5.2.2.1
	 */
	public void testRuntimeExceptionWhileInit() {
		GenericPortlet p = null;
		try {
			p = (GenericPortlet) proxy.getPortlet(portletContext,
							"PortletWithRuntimeExceptionWhileInit");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "exception while initializing portlet");
			assertTrue(e instanceof PortletException);
		}
		assertNull(p);
		assertFalse(portletMonitor.isInitialized("war_template",
						"PortletWithRuntimeExceptionWhileInit"));
	}

	/**
	 * test (xvii) : If a portlet throws an exception in the processAction method, all operations on
	 *               the ActionResponse must be ignored and the render method must not be invoked within
	 *               the current client request.
	 * PTL.5.2.4.4
	 */
	public void testExceptionWhileProcessAction() throws PortletContainerException {
    ((ExoWindowID)actionInput.getWindowID()).setPortletName("PortletWithExceptionWhileProcessAction");
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithExceptionWhileProcessAction");
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		portletContainer.processAction(request, response, actionInput);                    
    RenderOutput rO = portletContainer.render(request, response, input);
		assertEquals("Exception occured", rO.getTitle());
		assertEquals("javax.portlet.PortletException: Exception in processAction", new String(rO.getContent()));    
    assertTrue(portletMonitor.isAvailable("war_template",
        "PortletWithExceptionWhileProcessAction"));        
	}

	/**
	 * test (xviii) : If a permanent unavailability is indicated by the UnavailableException, the portlet
	 *                container must remove the portlet from service immediately, call the portlets destroy
	 *                method, and release the portlet object.
	 *
	 * test         : A portlet that throws a permanent UnavailableException must be considered unavailable
	 *                until the portlet application containing the portlet is restarted.
	 *
	 * PTL.5.2.4.4
	 */
	public void testPortletWithPermanentUnavailableExceptionInProcessAction()
					throws Exception {
		((ExoWindowID)actionInput.getWindowID()).setPortletName("PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		portletContainer.processAction(request, response, actionInput);
		assertTrue(portletMonitor.isBroken("war_template",
						"PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
	  PortletRuntimeData rD = (PortletRuntimeData) portletMonitor.
						getPortletRuntimeDataMap().get("war_template" + PortletContainerMonitorImpl.SEPARATOR +
						"PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		assertNull(rD);
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		portletContainer.render(request, response, input);
		assertEquals("Exception occured", portletContainer.render(request, response, input).getTitle());
		assertEquals("javax.portlet.UnavailableException: Permanent unavailable exception",
        new String(portletContainer.render(request, response, input).getContent()));
    assertTrue(portletMonitor.isBroken("war_template",
        "PortletWithPermanentUnavailibiltyInProcessActionAndRender"));   
		ActionOutput o = portletContainer.processAction(request, response, actionInput);
// changed on request of Erez Harari, now exception.toString() is returned in that property instead of static text
//		assertEquals("output generated because of an exception",
//        o.getProperties().get(PortletContainerConstants.EXCEPTION));
		assertNotNull(o.getProperties().get(PortletContainerConstants.EXCEPTION));
    assertTrue(portletMonitor.isBroken("war_template",
        "PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
		portletApplicationRegister.removePortletApplication(mockServletContext);
		portletApplicationRegister.registerPortletApplication(mockServletContext, portletApp_, roles, "war_template");
		assertFalse(portletMonitor.isBroken("war_template",
						"PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
		portletContainer.processAction(request, response, actionInput);
		assertNull(rD);
	}

	/**
	 * test (xviii) : If a permanent unavailability is indicated by the UnavailableException, the portlet
	 *                container must remove the portlet from service immediately, call the portlet's destroy
	 *                method, and release the portlet object.
	 *
	 * test         : A portlet that throws a permanent UnavailableException must be considered unavailable
	 *                until the portlet application containing the portlet is restarted.
	 *
	 * PTL.5.2.4.4
	 */
	public void testPortletWithPermanentUnavailableExceptionInRender() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, input);
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Permanent unavailable exception",
        new String(portletContainer.render(request, response, input).getContent()));
		assertTrue(portletMonitor.isBroken("war_template",
        "PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
	}

	/**
	 * test         : When temporary unavailability is indicated by the UnavailableException,
	 *                then the portlet container may choose not to route any requests to the
	 *                portlet during the time period of the temporary unavailability.
	 *
	 * PTL.5.2.4.4
	 */
	public void testNonPermanentUnavailableExceptionInProcessAction() throws PortletContainerException, InterruptedException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)actionInput.getWindowID()).setPortletName("PortletWithNonPermanentUnavailibiltyInProcessActionAndRender");
		portletContainer.processAction(request, response, actionInput);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		Thread.sleep(5000);
		assertTrue(portletMonitor.isAvailable("war_template",
						"PortletWithNonPermanentUnavailibiltyInProcessActionAndRender", System.currentTimeMillis()));
		request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		portletContainer.processAction(request, response, actionInput);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithNonPermanentUnavailibiltyInProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, input);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Non Permanent unavailable exception",
        new String(portletContainer.render(request, response, input).getContent()));
	}

	/**
	 * test         : When temporary unavailability is indicated by the UnavailableException,
	 *                then the portlet container may choose not to route any requests to the
	 *                portlet during the time period of the temporary unavailability.
	 *
	 * PTL.5.2.4.4
	 */
	public void testNonPermanentUnavailableExceptionInRender() throws PortletContainerException, InterruptedException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithNonPermanentUnavailibiltyInProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, input);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Non Permanent unavailable exception",
        new String(portletContainer.render(request, response, input).getContent()));
		Thread.sleep(5000);
		assertTrue(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		o = portletContainer.render(request, response, input);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Non Permanent unavailable exception",
        new String(portletContainer.render(request, response, input).getContent()));
	}

	/**
	 * test (xix) : A RuntimeException thrown during the request handling must be handled as a PortletException
	 *
	 * PTL.5.2.4.4
	 */
	public void testRuntimeExceptionWhileProcessAction() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)actionInput.getWindowID()).setPortletName("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		portletContainer.processAction(request, response, actionInput);
		assertTrue(portletMonitor.isAvailable("war_template",
        "PortletWithRuntimeExceptionWhileProcessActionAndRender"));
		PortletRuntimeData rD = (PortletRuntimeData) portletMonitor.getPortletRuntimeDataMap().
        get("war_template" + PortletContainerMonitorImpl.SEPARATOR +
        "PortletWithRuntimeExceptionWhileProcessActionAndRender");
//		assertNull(rD);
    assertNotNull(rD);
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		portletContainer.render(request, response, input);
		assertEquals("Exception occured", portletContainer.render(request, response, input).getTitle());
		assertEquals("java.lang.RuntimeException: runtime exception in processAction",
        new String(portletContainer.render(request, response, input).getContent()));
	}

	/**
	 * test (xix) : A RuntimeException thrown during the request handling must be handled as a PortletException
	 *
	 * PTL.5.2.4.4
	 */
	public void testRuntimeExceptionWhileRender() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, input);
		assertEquals("Exception occured", o.getTitle());
		assertEquals("java.lang.RuntimeException: runtime exception in render",
        new String(portletContainer.render(request, response, input).getContent()));
		assertTrue(portletMonitor.isAvailable("war_template",
        "PortletWithRuntimeExceptionWhileProcessActionAndRender"));
	}

	/**
	 * test (xix) : Once the destroy method is called on a portlet object, the portlet container must not
	 *              route any requests to that portlet object.
	 *
	 * PTL.5.2.5
	 */
	public void testNonRoutingProcessActionWhenPortletDestroyed() throws PortletContainerException {
		proxy.destroy("HelloWorld");
		assertTrue(portletMonitor.isDestroyed("war_template", "HelloWorld"));
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)actionInput.getWindowID()).setPortletName("HelloWorld");
		ActionOutput aO = portletContainer.processAction(request, response, actionInput);
		assertEquals("output generated because of a destroyed portlet access",
						aO.getProperties().get(PortletContainerConstants.DESTROYED));
		assertTrue(portletMonitor.isDestroyed("war_template", "HelloWorld"));
		((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld");
		RenderOutput o = portletContainer.render(request, response, input);
		assertEquals("Portlet destroyed", o.getTitle());
		assertEquals("Portlet unvailable", new String(o.getContent()));
	}

	/**
	 * test (xix) : Once the destroy method is called on a portlet object, the portlet container must not
	 *              route any requests to that portlet object.
	 *
	 * PTL.5.2.5
	 */
	public void testNonRoutingRenderWhenPortletDestroyed() throws PortletContainerException {
		proxy.destroy("HelloWorld");
		assertTrue(portletMonitor.isDestroyed("war_template", "HelloWorld"));
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld");
		RenderOutput o = portletContainer.render(request, response, input);
		assertEquals("Portlet destroyed", o.getTitle());
		assertEquals("Portlet unvailable", new String(o.getContent()));
		request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		o = portletContainer.render(request, response, input);
		assertEquals("Portlet destroyed", o.getTitle());
		assertEquals("Portlet unvailable", new String(o.getContent()));
	}

	/**
	 * test (xxi) : If the portlet container needs to enable the portlet again, it must do so
	 *              with a new portlet object, which is a new instance of the portlet's class.
	 *
	 * test (xxiii) : After the destroy method completes, the portlet container must release the
	 *                portlet object so that it is eligible for garbage collection. (implicit)
	 *
	 * PTL.5.2.5
	 */
	public void testPortletReUsedAfterDestroy() throws PortletException {
		Portlet p1 = proxy.getPortlet(portletContext, "HelloWorld");
		proxy.destroy("HelloWorld");
		assertTrue(portletMonitor.isDestroyed("war_template", "HelloWorld"));

		proxy.registerPortletToMonitor("HelloWorld");
		Portlet p2 = proxy.getPortlet(portletContext, "HelloWorld");
		assertNotSame(p1, p2);
	}

	/**
	 * test (xxii) : If the portlet object throws a RuntimeException within the
	 *               execution of the destroy method the portlet container must
	 *               consider the portlet object successfully destroyed.
	 *
	 * PTL.5.2.5
	 */
	public void testRuntimeExceptionWhileDestroy() throws PortletException {
		proxy.getPortlet(portletContext, "PortletWithRuntimeExceptionWhileProcessActionAndRender");
		proxy.destroy("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		assertTrue(portletMonitor.isDestroyed("war_template",
						"PortletWithRuntimeExceptionWhileProcessActionAndRender"));
	}

}
