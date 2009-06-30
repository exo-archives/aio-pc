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
package org.exoplatform.services.portletcontainer.imp.Portlet286;

import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 24.04.2007
 */
public class TestWindowState2 extends BaseTest2{

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestWindowState2");

	public TestWindowState2(String s) {
		super(s);
	}

  public void testWindowStateSupport(){
    log.info("testWindowStateSupport...");
    Collection states = portletContainer.getWindowStates(PORTLET_APP_NAME, "PortletToTestNonUsageOfUndefinedState2", "text/html");

		assertTrue(contains(states,WindowState.NORMAL));
		assertTrue(contains(states,WindowState.MINIMIZED));
		assertTrue(contains(states,WindowState.MAXIMIZED));
		assertTrue(contains(states,new WindowState("half-page")));
		assertFalse(contains(states,new WindowState("max-per-column")));
		assertFalse(contains(states, new WindowState("not_exist")));
    log.info("done.");
	}

	/**
	 * test (xi) : If a custom window state defined in the deployment descriptor is not mapped to a custom
	 *             window state provided by the portal, portlets must not be invoked in that window state.
	 *
	 * PLT.P.4
	 */
	public void testPortletNotCalledWithAnUnsupportedMode() throws PortletContainerException {
    log.info("testPortletNotCalledWithAnUnsupportedMode...");
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestNonUsageOfUndefinedState2");
    renderInput.setWindowState(new WindowState("max-per-column"));
		RenderOutput o = portletContainer.render(request, response, renderInput);
		assertNull(o.getTitle());
		assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
    log.info("done.");
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
