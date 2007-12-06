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
package org.exoplatform.services.portletcontainer.test.portlet2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class TestUrlListenersPortlet2 extends GenericPortlet {

  public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    response.setContentType("text/html");
    PrintWriter w = response.getWriter();
    w.println("<center><font size='3'><b><i>Portlet for test paramethers translating from URL through processAction into doView.</i></b></font></center><br>");

    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><th colspan=\"2\">Render parameters</th></tr>");
    w.println("<tr><th>parameter</th><th>value</th></tr>");
    Map<String, String[]> params = request.getParameterMap();
    Iterator<String> names = params.keySet().iterator();
    while (names.hasNext()) {
      String name = names.next();
      w.println("<tr><td>" + name + "</td>");
      w.println("<td>" + request.getParameter(name) + "</td></tr>");
    }
    w.println("</table>");

    PortletURL url = response.createRenderURL();
    url.setParameter("param", "val");
    w.println("<a href=\"");
    url.write(w);
    w.println("\">render url</a>");
    url = response.createActionURL();
    url.setParameter("param", "val");
    w.println("<a href=\"");
    url.write(w);
    w.println("\">action url</a>");
  }

  public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
    Map<String, String[]> params = request.getParameterMap();
    Iterator<String> names = params.keySet().iterator();
    while (names.hasNext()) {
      String name = names.next();
      response.setRenderParameter("fromAction_" + name, request.getParameter(name));
    }
  }

}
