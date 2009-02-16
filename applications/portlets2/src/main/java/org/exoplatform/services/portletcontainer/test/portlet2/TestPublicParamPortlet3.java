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
 * Created by The eXo Platform SAS .
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class TestPublicParamPortlet3 extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest request
   * @param renderResponse response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Portlet for test public paramethers. Linked to TestPublicParamPortlet1 and TestPublicParamPortlet2</i></b></font></center><br>");

    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><th colspan=\"2\">Request PRIVATE parameters</th></tr>");
    w.println("<tr><th>parameter</th><th>value</th></tr>");
    Map params = renderRequest.getPrivateParameterMap();
    Iterator names = params.keySet().iterator();
    while (names.hasNext()) {
      String name = (String) names.next();
      w.println("<tr><td>" + name + "</td>");
      w.println("<td>" + renderRequest.getParameter(name) + "</td></tr>");
    }
    w.println("<tr><th colspan=\"2\">Request PUBLIC parameters</th></tr>");
    w.println("<tr><th>parameter</th><th>value</th></tr>");
    params = renderRequest.getPublicParameterMap();
    names = params.keySet().iterator();
    while (names.hasNext()) {
      String name = (String) names.next();
      w.println("<tr><td>" + name + "</td>");
      w.println("<td>" + renderRequest.getParameter(name) + "</td></tr>");
    }
    w.println("</table>");

    PortletURL url = renderResponse.createRenderURL();
    url.setParameter("public1", "-- from public3");
    url.setParameter("public2", "-- from public3");
    url.setParameter("private", "private...");
    url.setSecure(true);
    w.println("<p><a href=\"" + url.toString() + "\">set public param</a>");

  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }

}
