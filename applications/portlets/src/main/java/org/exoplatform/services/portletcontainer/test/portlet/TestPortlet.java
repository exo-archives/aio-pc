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
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin .
 * benjmestrallet@users.sourceforge.net Date: Jul 26, 2003 Time: 3:38:11 PM
 */
public class TestPortlet extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest request
   * @param renderResponse response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
      IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    // System.out.println("In doView method of TestPortlet...");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">Test</h2>");
    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><th colspan=\"2\">Request attributes</th></tr>");
    w.println("<tr><th>attribute</th><th>value</th></tr>");
    Enumeration e = renderRequest.getAttributeNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("<tr><td>" + s + "</td>");
      w.println("<td>" + renderRequest.getAttribute(s) + "</td></tr>");
    }
    w.println("</table>");
    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><th colspan=\"2\">Request parameters</th></tr>");
    w.println("<tr><th>parameter</th><th>value</th></tr>");
    e = renderRequest.getParameterNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("<tr><td>" + s + "</td>");
      w.println("<td>" + renderRequest.getParameter(s) + "</td></tr>");
    }
    w.println("</table>");
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setParameter("action_param_1", "action param test");
    actionURL.setParameter("action_param_2", "action param test 2");
    actionURL.setWindowState(WindowState.MAXIMIZED);
    actionURL.setPortletMode(PortletMode.EDIT);
    w.println("<p><a href=\"" + actionURL.toString() + "\">action URL</a>");

    renderResponse.setTitle("TEST PORTLET");
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
      IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    // System.out.println("In doEdit method of TestPortlet...");
    PrintWriter w = renderResponse.getWriter();

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
      IOException {
    actionResponse.setRenderParameter("test_render_param", "test-----------------2");
  }

}
