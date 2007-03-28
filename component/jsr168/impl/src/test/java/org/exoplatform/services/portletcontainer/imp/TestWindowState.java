package org.exoplatform.services.portletcontainer.imp;


import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 16 oct. 2003
 * Time: 23:42:13
 */
public class TestWindowState extends BaseTest{
	public TestWindowState(String s) {
		super(s);
	}

  public void testWindowStateSupport(){
    Collection states = portletContainer.getWindowStates("hello");

		assertTrue(contains(states,WindowState.NORMAL));
		assertTrue(contains(states,WindowState.MINIMIZED));
		assertTrue(contains(states,WindowState.MAXIMIZED));
		assertTrue(contains(states,new WindowState("half-page")));
		assertFalse(contains(states,new WindowState("max-per-column")));
		assertFalse(contains(states, new WindowState("not_exist")));
	}

	/**
	 * test (xi) : If a custom window state defined in the deployment descriptor is not mapped to a custom
	 *             window state provided by the portal, portlets must not be invoked in that window state.
	 *
	 * PLT.P.4
	 */
	public void testPortletNotCalledWithAnUnsupportedMode() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)input.getWindowID()).setPortletName("PortletToTestNonUsageOfUndefinedState");
    input.setWindowState(new WindowState("max-per-column"));
		RenderOutput o = portletContainer.render(request, response, input);
		assertNull(o.getTitle());
		assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
	}

	private boolean contains(Collection modes, WindowState state){
		for (Iterator iterator = modes.iterator(); iterator.hasNext();) {
			WindowState windowState = (WindowState) iterator.next();
			if(windowState.toString().equals(state.toString()))
				return true;
		}
		return false;
	}

}
