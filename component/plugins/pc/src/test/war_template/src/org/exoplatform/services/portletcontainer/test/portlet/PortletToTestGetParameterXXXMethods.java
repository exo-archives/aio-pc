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

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 16 nov. 2003
 * Time: 22:54:50
 */
public class PortletToTestGetParameterXXXMethods extends GenericPortlet{

	public void init(PortletConfig portletConfig) throws PortletException {
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortletException, IOException {
		Map map = actionRequest.getParameterMap();    
    
    String param = actionRequest.getParameter("dummy");
    if(param != null)
      throw new PortletException("getParameter does not work");    
    actionRequest.getParameter("dummy");
    actionRequest.getParameter("dummy");
    
		if(!map.isEmpty())
			throw new PortletException("getParameter does not work");

		actionResponse.setRenderParameter("status", "Everything is ok");
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
					throws PortletException, IOException {
		/////tests (lv) & (lvi)
    renderResponse.setContentType("text/html");
    String param1 = renderRequest.getParameter("renderParam1");
    String[] param1Array = renderRequest.getParameterValues("renderParam1");
    String[] param2Array = renderRequest.getParameterValues("renderParam2");

		if(!"param-value1bis".equals(param1Array[1]))
			throw new PortletException("getParameter does not work");

    if(!param1.equals(param1Array[0]))
			throw new PortletException("getParameter does not work");

    if(param2Array.length != 1 || !"param-value2".equals(param2Array[0]))
			throw new PortletException("getParameter does not work");

		try {
			renderRequest.getParameterMap().put("s", new String[] {"unmutable"});
			throw new PortletException("getParameter does not work");
		} catch (Throwable t) {
		}

		PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}

}
