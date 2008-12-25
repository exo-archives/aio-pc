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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletProcessingException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 15 oct. 2003
 * Time: 20:51:01
 */
public class TestPortletURL extends BaseTest2{

	public TestPortletURL(String s) {
		super(s);
	}

	/**
	 * test (xxxiv) : A portlet cannot create a portlet URL using a window state that is not
	 *                supported by the portlet container. The setWindowState method must throw
	 *                a WindowStateException if that is the case.
	 *
	 * PLT.7.1.1
   *
   *
   * PC 2.0 Spec - PLT 9.5
   * As all portlets must at least support the  pre-defined window states  NORMAL,
   * MAXIMIZED, MINIMIZED, therefore these window states do not have to be indicated
	 */
	public void testWindowStateSupport() throws PortletContainerException {
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("PortletToTestStateSupport");
		try {
			RenderOutput o = portletContainer.render(request, response, renderInput);
			assertEquals("Exception occured", o.getTitle());
			assertEquals("javax.portlet.WindowStateException: The window state detached is not supported by that portlet",
						new String(o.getContent()));
		} catch (PortletProcessingException e) {
		  assertTrue(e.getCause() instanceof javax.portlet.WindowStateException);
    }

	}

}
