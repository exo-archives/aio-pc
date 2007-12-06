package org.exoplatform.services.portletcontainer.test.portlet;

import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletURLImp;

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
 * Date: 15 oct. 2003
 * Time: 21:54:09
 */
public class PortletToTestPortletURL implements Portlet{
	public void init(PortletConfig portletConfig) throws PortletException {
		//To change body of implemented methods use Options | File Templates.
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {

		/////test (xxviii)
    renderResponse.setContentType("text/html");
    PortletURL pURL = renderResponse.createRenderURL();
		pURL.setParameter("test", "aTest");
		String param = (String) ((PortletURLImp)pURL).getParameter("test");
		if(!"aTest".equals(param))
			throw new PortletException("setParameter does not work");

		pURL.setParameter("test2", "aTest2");
		param = (String) ((PortletURLImp)pURL).getParameter("test2");
		if(!"aTest2".equals(param))
			throw new PortletException("setParameter does not work");

    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}
}
