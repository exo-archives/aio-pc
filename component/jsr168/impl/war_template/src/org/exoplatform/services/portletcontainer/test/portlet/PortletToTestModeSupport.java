package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 16 oct. 2003
 * Time: 01:44:04
 */
public class PortletToTestModeSupport implements Portlet{

	public void init(PortletConfig portletConfig) throws PortletException {
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
		/////test (xxxii)
    renderResponse.setContentType("text/html");
    PortletURL pURL = renderResponse.createRenderURL();
		pURL.setPortletMode(PortletMode.VIEW);
    pURL.setPortletMode(PortletMode.HELP);
		pURL.setPortletMode(new PortletMode("config"));

		pURL.setPortletMode(PortletMode.EDIT);
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}
}
