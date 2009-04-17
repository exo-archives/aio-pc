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

import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.imp.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by The eXo Platform SAS         .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 16 oct. 2003
 * Time: 18:49:32
 */
public class TestPortletMode extends BaseTest{

	public TestPortletMode(String s) {
		super(s);
	}

	/**
	 * test (xxxvii) : As all portlets must support the VIEW portlet mode, VIEW does not
	 *                have to be indicated.
	 *
	 * PLT.8.6
	 */
	public void testImplicitViewMode(){
		Collection c = portletContainer.getPortletModes("war_template", "HelloWorld", "text/html");

		assertTrue(contains(c, PortletMode.VIEW));
	}

	public void testOtherModes(){
		Collection c = portletContainer.getPortletModes("war_template", "HelloWorld", "text/html");

		assertTrue(contains(c, PortletMode.EDIT));
		assertTrue(contains(c, PortletMode.HELP));
		assertTrue(contains(c, new PortletMode("config")));
		assertFalse(contains(c, new PortletMode("about")));
		assertFalse(contains(c, new PortletMode("not_exist")));

		assertTrue(portletContainer.isModeSuported("war_template", "HelloWorld", "text/html", PortletMode.VIEW));
		assertTrue(portletContainer.isModeSuported("war_template", "HelloWorld", "text/html", PortletMode.EDIT));
		assertTrue(portletContainer.isModeSuported("war_template", "HelloWorld", "text/html", new PortletMode("config")));
		assertFalse(portletContainer.isModeSuported("war_template", "HelloWorld", "text/html", new PortletMode("about")));
		assertFalse(portletContainer.isModeSuported("war_template", "HelloWorld", "text/html", new PortletMode("not_exist")));
	}

	public void testOtherMarkup(){
    Collection c = portletContainer.getPortletModes("war_template", "HelloWorld", "text/wml");

	  assertTrue(contains(c, PortletMode.EDIT));
		assertTrue(contains(c, PortletMode.HELP));
		assertFalse(contains(c, new PortletMode("config")));
		assertFalse(contains(c, new PortletMode("not_exist")));

		assertTrue(portletContainer.isModeSuported("war_template", "HelloWorld", "text/wml", PortletMode.VIEW));
		assertTrue(portletContainer.isModeSuported("war_template", "HelloWorld", "text/wml", PortletMode.EDIT));
		assertTrue(portletContainer.isModeSuported("war_template", "HelloWorld", "text/wml", PortletMode.HELP));
		assertFalse(portletContainer.isModeSuported("war_template", "HelloWorld", "text/wml", new PortletMode("config")));
		assertFalse(portletContainer.isModeSuported("war_template", "HelloWorld", "text/wml", new PortletMode("not_exist")));
	}

	/**
	 * test (xxxviii) : The portlet must not be invoked in a portlet mode that has not been declared as supported
	 *                  for a given markup type.
	 *
	 * PLT.8.6
	 */
	public void testNonPortletAccessWhenModeIsNotDefined() {
		((ExoWindowID)actionInput.getInternalWindowID()).setPortletName("HelloWorld");
    actionInput.setPortletMode(new PortletMode("config"));
		actionInput.setMarkup("text/wml");

	  ((ExoWindowID)renderInput.getInternalWindowID()).setPortletName("HelloWorld2");
		renderInput.setPortletMode(PortletMode.HELP);
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		try {
			portletContainer.processAction(request, response, actionInput);
		} catch (PortletContainerException e) {
			assertEquals( "The portlet mode config is not supported for the text/wml markup language.", e.getMessage());
		}
		try {
			portletContainer.render(request, response, renderInput);
		} catch (PortletContainerException e) {
			assertEquals( "The portlet mode help is not supported for the text/html markup language.", e.getMessage());
		}
	}

	/**
	 * test (xxxix) : The portlet container must ignore all references to custom portlet modes that are not
	 *                supported by the portal implementation, or that have no mapping to portlet modes supported
	 *                by the portal.
	 *
	 * PLT.8.6
	 */
	public void testIgnoreCustomModesNotSupportedByPortal(){
		Collection c = portletContainer.getPortletModes("war_template", "HelloWorld2", "text/html");

		assertFalse(contains(c, new PortletMode("not_supported")));
	}


	private boolean contains(Collection modes, PortletMode mode){
		for (Iterator iterator = modes.iterator(); iterator.hasNext();) {
			PortletMode portletMode = (PortletMode) iterator.next();
			if(portletMode.toString().equals(mode.toString()))
				return true;
		}
		return false;
	}

}
