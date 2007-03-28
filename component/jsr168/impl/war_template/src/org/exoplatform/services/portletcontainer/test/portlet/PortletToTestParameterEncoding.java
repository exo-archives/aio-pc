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
 * Time: 00:42:19
 */
public class PortletToTestParameterEncoding implements Portlet{
	public void init(PortletConfig portletConfig) throws PortletException {
		//To change body of implemented methods use Options | File Templates.
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {

		/////test (xxviii)
    renderResponse.setContentType("text/html");
    PortletURL pURL = renderResponse.createRenderURL();
		pURL.setParameter("a test", "a Test");

     
		if(pURL.toString().indexOf("a+test=a+Test") < 0)
			throw new PortletException("getParameter does not work : " + pURL.toString());

    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}
}