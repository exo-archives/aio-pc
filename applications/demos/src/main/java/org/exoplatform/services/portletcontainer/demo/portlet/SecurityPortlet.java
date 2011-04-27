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
package org.exoplatform.services.portletcontainer.demo.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

/**
 * Test portlet SecurityPortlet.
 */
public class SecurityPortlet extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest render request
   * @param renderResponse render response
   * @throws PortletException something may go wrong
   * @throws IOException something may go wrong
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();

    //user and roles
    w.println("User: " + renderRequest.getRemoteUser() + "<br>");
    w.println("Principal: " + renderRequest.getUserPrincipal().getName() + "<br>");
    w.println("Is in 'admin' role: " + renderRequest.isUserInRole("admin") + "<br>");
    w.println("Is in 'users' role: " + renderRequest.isUserInRole("users") + "<br>");
    w.println("Is in 'exo' role: " + renderRequest.isUserInRole("exo") + "<br>");
    
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">action url</a>");

    //PARAMETERS
    w.println("<table width=\"100%\" border=\"2\" style=\"border-collapse:collapse;border-style:solid;\">");
    w.println("<tr><th colspan=\"2\">Request parameters</th></tr>");
    w.println("<tr><th colspan=\"2\">parameter &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp; value</th></tr>");
    Enumeration e = renderRequest.getParameterNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("<tr><td colspan=\"2\" style=\"padding-left: 5px; font-size: small;\">" + s + "</td></tr>");
      w.println("<tr><td colspan=\"2\" style=\"padding-left: 20px; font-size: small;\">" + renderRequest.getParameter(s) + "</td></tr>");
    }
    w.println("</table>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
	  actionResponse.setRenderParameter("User:", actionRequest.getRemoteUser());
	  actionResponse.setRenderParameter("Principal:", actionRequest.getUserPrincipal().getName());
	  actionResponse.setRenderParameter("Is in 'admin' role:", String.valueOf(actionRequest.isUserInRole("admin")));
	  actionResponse.setRenderParameter("Is in 'users' role:", String.valueOf(actionRequest.isUserInRole("users")));
	  actionResponse.setRenderParameter("Is in 'exo' role:", String.valueOf(actionRequest.isUserInRole("exo")));
  }

}
