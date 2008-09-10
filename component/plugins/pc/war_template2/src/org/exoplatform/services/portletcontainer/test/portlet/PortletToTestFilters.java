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
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 17 nov. 2003 Time: 22:23:43
 * Revision: Max Shaposhnik Sep'07
 */
public class PortletToTestFilters extends GenericPortlet {

  public void init(PortletConfig portletConfig) throws PortletException {
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
                                                                                       IOException {
    actionResponse.setRenderParameter("status", "Everything is ok");

    if (actionRequest.getAttribute("filterID") != null) {
      actionResponse.setRenderParameter("filterID", (String) actionRequest.getAttribute("filterID"));
    }

  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
                                                                                IOException {
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();

    if ("default-param-value".equalsIgnoreCase(renderRequest.getAttribute("param").toString()))
      w.println("Everything is ok");
  }

  public void destroy() {

  }
}
