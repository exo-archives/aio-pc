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

import java.util.Locale;

import javax.portlet.GenericPortlet;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletDataImp;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletAPIObjectFactory;
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
        getComponentInstance("war_template" + PCConstants.PORTLET_APP_ENCODER);
    portletDatas = (PortletDataImp) portletContainer.
      getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
        + "HelloWorld");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
      mockServletContext, portletDatas.getWrappedPortletTyped());

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
    portletDatas = (PortletDataImp) portletContainer.
      getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
        + "PortletWithPortletExceptionWhileInit");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
      mockServletContext, portletDatas.getWrappedPortletTyped());

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
    portletDatas = (PortletDataImp) portletContainer.
      getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
        + "PortletWithUnavailableExceptionWhileInit");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
      mockServletContext, portletDatas.getWrappedPortletTyped());
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
		portletDatas = (PortletDataImp) portletContainer.
		  getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
		      + "PortletWithUnavailableExceptionWhileInit2");
		portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
		    mockServletContext, portletDatas.getWrappedPortletTyped());
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
	  portletDatas = (PortletDataImp) portletContainer.
	    getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
	        + "PortletWithRuntimeExceptionWhileInit");
	  portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
	      mockServletContext, portletDatas.getWrappedPortletTyped());
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
    ((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("PortletWithExceptionWhileProcessAction");
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithExceptionWhileProcessAction");
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		portletContainer.processAction(request, response, actionInput);
    RenderOutput rO = portletContainer.render(request, response, renderInput);
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
		((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		portletContainer.processAction(request, response, actionInput);
		assertTrue(portletMonitor.isBroken("war_template",
						"PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
	  PortletRuntimeData rD = (PortletRuntimeData) portletMonitor.
						getPortletRuntimeDataMap().get("war_template" + PortletContainerMonitorImpl.SEPARATOR +
						"PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		assertNull(rD);
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		portletContainer.render(request, response, renderInput);
		assertEquals("Exception occured", portletContainer.render(request, response, renderInput).getTitle());
		assertEquals("javax.portlet.UnavailableException: Permanent unavailable exception",
        new String(portletContainer.render(request, response, renderInput).getContent()));
    assertTrue(portletMonitor.isBroken("war_template",
        "PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
		ActionOutput o = portletContainer.processAction(request, response, actionInput);
// changed on request of Erez Harari, now exception.toString() is returned in that property instead of static text
//		assertEquals("output generated because of an exception",
//        o.getProperties().get(PCConstants.EXCEPTION));
		assertNotNull(o.getProperties().get(PCConstants.EXCEPTION));
    assertTrue(portletMonitor.isBroken("war_template",
        "PortletWithPermanentUnavailibiltyInProcessActionAndRender"));
		portletApplicationRegister.removePortletApplication(mockServletContext, PORTLET_APP_NAME);
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
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithPermanentUnavailibiltyInProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, renderInput);
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Permanent unavailable exception",
        new String(portletContainer.render(request, response, renderInput).getContent()));
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
		((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("PortletWithNonPermanentUnavailibiltyInProcessActionAndRender");
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
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithNonPermanentUnavailibiltyInProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, renderInput);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Non Permanent unavailable exception",
        new String(portletContainer.render(request, response, renderInput).getContent()));
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
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithNonPermanentUnavailibiltyInProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, renderInput);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Non Permanent unavailable exception",
        new String(portletContainer.render(request, response, renderInput).getContent()));
		Thread.sleep(5000);
		assertTrue(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		o = portletContainer.render(request, response, renderInput);
		assertFalse(portletMonitor.isAvailable("war_template",
        "PortletWithNonPermanentUnavailibiltyInProcessActionAndRender",
        System.currentTimeMillis()));
		assertEquals("Exception occured", o.getTitle());
		assertEquals("javax.portlet.UnavailableException: Non Permanent unavailable exception",
        new String(portletContainer.render(request, response, renderInput).getContent()));
	}

	/**
	 * test (xix) : A RuntimeException thrown during the request handling must be handled as a PortletException
	 *
	 * PTL.5.2.4.4
	 */
	public void testRuntimeExceptionWhileProcessAction() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		portletContainer.processAction(request, response, actionInput);
		assertTrue(portletMonitor.isAvailable("war_template",
        "PortletWithRuntimeExceptionWhileProcessActionAndRender"));
		PortletRuntimeData rD = (PortletRuntimeData) portletMonitor.getPortletRuntimeDataMap().
        get("war_template" + PortletContainerMonitorImpl.SEPARATOR +
        "PortletWithRuntimeExceptionWhileProcessActionAndRender");
//		assertNull(rD);
    assertNotNull(rD);
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		portletContainer.render(request, response, renderInput);
		assertEquals("Exception occured", portletContainer.render(request, response, renderInput).getTitle());
		assertEquals("java.lang.RuntimeException: runtime exception in processAction",
        new String(portletContainer.render(request, response, renderInput).getContent()));
	}

	/**
	 * test (xix) : A RuntimeException thrown during the request handling must be handled as a PortletException
	 *
	 * PTL.5.2.4.4
	 */
	public void testRuntimeExceptionWhileRender() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		RenderOutput o = portletContainer.render(request, response, renderInput);
		assertEquals("Exception occured", o.getTitle());
		assertEquals("java.lang.RuntimeException: runtime exception in render",
        new String(portletContainer.render(request, response, renderInput).getContent()));
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
		((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("HelloWorld");
		ActionOutput aO = portletContainer.processAction(request, response, actionInput);
		assertEquals("output generated because of a destroyed portlet access",
						aO.getProperties().get(PCConstants.DESTROYED));
		assertTrue(portletMonitor.isDestroyed("war_template", "HelloWorld"));
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld");
		RenderOutput o = portletContainer.render(request, response, renderInput);
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
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld");
		RenderOutput o = portletContainer.render(request, response, renderInput);
		assertEquals("Portlet destroyed", o.getTitle());
		assertEquals("Portlet unvailable", new String(o.getContent()));
		request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		o = portletContainer.render(request, response, renderInput);
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
    portletDatas = (PortletDataImp) portletContainer.
      getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
        + "HelloWorld");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
      mockServletContext, portletDatas.getWrappedPortletTyped());

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
    portletDatas = (PortletDataImp) portletContainer.
      getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
        + "PortletWithRuntimeExceptionWhileProcessActionAndRender");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
      mockServletContext, portletDatas.getWrappedPortletTyped());

		proxy.getPortlet(portletContext, "PortletWithRuntimeExceptionWhileProcessActionAndRender");
		proxy.destroy("PortletWithRuntimeExceptionWhileProcessActionAndRender");
		assertTrue(portletMonitor.isDestroyed("war_template",
						"PortletWithRuntimeExceptionWhileProcessActionAndRender"));
	}

}
