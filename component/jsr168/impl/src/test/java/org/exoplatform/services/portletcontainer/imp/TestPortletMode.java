package org.exoplatform.services.portletcontainer.imp;



import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
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
		Collection c = portletContainer.getPortletModes("hello", "HelloWorld", "text/html");

		assertTrue(contains(c, PortletMode.VIEW));
	}

	public void testOtherModes(){
		Collection c = portletContainer.getPortletModes("hello", "HelloWorld", "text/html");

		assertTrue(contains(c, PortletMode.EDIT));
		assertTrue(contains(c, PortletMode.HELP));
		assertTrue(contains(c, new PortletMode("config")));
		assertFalse(contains(c, new PortletMode("about")));
		assertFalse(contains(c, new PortletMode("not_exist")));

		assertTrue(portletContainer.isModeSuported("hello", "HelloWorld", "text/html", PortletMode.VIEW));
		assertTrue(portletContainer.isModeSuported("hello", "HelloWorld", "text/html", PortletMode.EDIT));
		assertTrue(portletContainer.isModeSuported("hello", "HelloWorld", "text/html", new PortletMode("config")));
		assertFalse(portletContainer.isModeSuported("hello", "HelloWorld", "text/html", new PortletMode("about")));
		assertFalse(portletContainer.isModeSuported("hello", "HelloWorld", "text/html", new PortletMode("not_exist")));
	}

	public void testOtherMarkup(){
    Collection c = portletContainer.getPortletModes("hello", "HelloWorld", "text/wml");

	  assertTrue(contains(c, PortletMode.EDIT));
		assertTrue(contains(c, PortletMode.HELP));
		assertFalse(contains(c, new PortletMode("config")));
		assertFalse(contains(c, new PortletMode("not_exist")));

		assertTrue(portletContainer.isModeSuported("hello", "HelloWorld", "text/wml", PortletMode.VIEW));
		assertTrue(portletContainer.isModeSuported("hello", "HelloWorld", "text/wml", PortletMode.EDIT));
		assertTrue(portletContainer.isModeSuported("hello", "HelloWorld", "text/wml", PortletMode.HELP));
		assertFalse(portletContainer.isModeSuported("hello", "HelloWorld", "text/wml", new PortletMode("config")));
		assertFalse(portletContainer.isModeSuported("hello", "HelloWorld", "text/wml", new PortletMode("not_exist")));
	}

	/**
	 * test (xxxviii) : The portlet must not be invoked in a portlet mode that has not been declared as supported
	 *                  for a given markup type.
	 *
	 * PLT.8.6
	 */
	public void testNonPortletAccessWhenModeIsNotDefined() {
		((ExoWindowID)actionInput.getWindowID()).setPortletName("HelloWorld");
    actionInput.setPortletMode(new PortletMode("config"));
		actionInput.setMarkup("text/wml");

	  ((ExoWindowID)input.getWindowID()).setPortletName("HelloWorld2");
		input.setPortletMode(PortletMode.HELP);
		HttpServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		try {
			portletContainer.processAction(request, response, actionInput);
		} catch (PortletContainerException e) {
			assertEquals( "The portlet mode config is not supported for the text/wml markup language.", e.getMessage());
		}
		try {
			portletContainer.render(request, response, input);
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
		Collection c = portletContainer.getPortletModes("hello", "HelloWorld2", "text/html");

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
