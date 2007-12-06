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
package org.exoplatform.services.portletcontainer.test.portlet;

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by The eXo Platform SAS         .
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
