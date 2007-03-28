package org.exoplatform.services.portletcontainer.test.portlet;


import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 14:52:54
 */
public class PortletToTestIoC extends GenericPortlet{

	private LogService logService;
	private Log log;

	public PortletToTestIoC(LogService logService) {
		this.logService = logService;
		log = logService.getLog("org.exoplatform.portal.container");
	}

	public void init(PortletConfig portletConfig) throws PortletException {
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortletException, IOException {
		log.debug("Portlet is an IoC type 3 component");
		actionResponse.setRenderParameter("status", "Everything is ok");
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
					throws PortletException, IOException {
    renderResponse.setContentType("text/html");        
		log.debug("Portlet is an IoC type 3 component");
		PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}
}
