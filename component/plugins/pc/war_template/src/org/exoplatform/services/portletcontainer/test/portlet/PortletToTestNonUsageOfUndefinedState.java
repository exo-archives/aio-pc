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

/**
 * Created by The eXo Platform SAS         .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 oct. 2003
 * Time: 02:20:19
 */
public class PortletToTestNonUsageOfUndefinedState implements Portlet{
	public void init(PortletConfig portletConfig) throws PortletException {
		//To change body of implemented methods use Options | File Templates.
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
		//To change body of implemented methods use Options | File Templates.
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    renderResponse.setContentType("text/html");
		WindowState state = renderRequest.getWindowState();
		if(state != WindowState.NORMAL)
			throw new PortletException();

		PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
		//To change body of implemented methods use Options | File Templates.
	}
}
