package org.exoplatform.services.portletcontainer.imp;


import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.Locale;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 14:50:42
 */
public class TestPortletIoCComponent extends BaseTest{

	public TestPortletIoCComponent(String s) {
		super(s);
	}

	public void testPortletAsIoCComponent() throws PortletContainerException {
		MockServletRequest request = new MockServletRequest(new MockHttpSession(), Locale.US, true);
		HttpServletResponse response = new MockServletResponse(new EmptyResponse());
		((ExoWindowID)actionInput.getWindowID()).setPortletName("PortletToTestIoC");
    actionInput.setPortletMode(new PortletMode("config"));
		ActionOutput aO = portletContainer.processAction(request, response, actionInput);
		assertEquals("Everything is ok", ((String[])aO.getRenderParameters().get("status"))[0]);
	}
}

