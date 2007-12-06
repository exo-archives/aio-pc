package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 16 nov. 2003
 * Time: 16:58:10
 */
public class PortletToTestIncomingRequestParam extends GenericPortlet{

	public void init(PortletConfig portletConfig) throws PortletException {
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortletException, IOException {
    String param1 = actionRequest.getParameter("param1");
		String param2 = actionRequest.getParameter("param2");
		String param3 = actionRequest.getParameter("param3");

    if(!"param-value1".equals(param1))
			throw new PortletException("getParameter does not work");
    if(!"param-value2".equals(param2))
			throw new PortletException("getParameter does not work");
    if(!"param-value3".equals(param3))
			throw new PortletException("getParameter does not work");

    actionResponse.setRenderParameter("status", "Everything is ok");
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
					throws PortletException, IOException {
		/////test (xlviii)
    renderResponse.setContentType("text/html");
    String param1 = renderRequest.getParameter("renderParam1");
		String param2 = renderRequest.getParameter("renderParam2");
		String param3 = renderRequest.getParameter("renderParam3");

    if(!"param-value1".equals(param1))
			throw new PortletException("getParameter does not work");
    if(!"param-value2".equals(param2))
			throw new PortletException("getParameter does not work");
    if(!"param-value3".equals(param3))
			throw new PortletException("getParameter does not work");

    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}

}
