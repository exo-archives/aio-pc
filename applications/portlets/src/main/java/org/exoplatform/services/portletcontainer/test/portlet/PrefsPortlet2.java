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
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Preference testing 2.
 */
public class PrefsPortlet2 extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param config portlet config
   * @throws PortletException something may go wrong
   * @see javax.portlet.GenericPortlet#init(javax.portlet.PortletConfig)
   */
  public void init(PortletConfig config) throws PortletException {
    super.init(config);
  }

  /**
   * Overridden method.
   *
   * @param request
   * @param response
   * @throws PortletException
   * @throws IOException
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    response.setContentType("text/html");
    PrintWriter w = response.getWriter();
    PortletPreferences prefs = request.getPreferences();
    PortletURL url = null;
    w.println("<center><font size='3'><b><i>More complex portlet for testing storing/retrieving params from PortletPreferences</i></b></font></center><br>");

    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr bgcolor=\"#666666\"><th>name</th><th>value</th><th>is read only</th><th>&nbsp;</th><th>&nbsp;</th></tr>");
    for (Enumeration e = prefs.getNames(); e.hasMoreElements(); ) {
      String name = (String) e.nextElement();
      String value = prefs.getValue(name, "[no value]");
      boolean ro = prefs.isReadOnly(name);
      String editUrl = "";
      String delUrl = "";
      if (!ro) {
        url = response.createRenderURL();
        url.setPortletMode(PortletMode.EDIT);
        url.setParameter("pref_name", name);
        editUrl = "<a href=\"" + url.toString() + "\">edit</a>";
        url = response.createActionURL();
        url.setParameter("pref_name", name);
        url.setParameter("del", "true");
        delUrl = "<a href=\"" + url.toString() + "\">del/reset</a>";
      } else {
        editUrl = "&nbsp;";
        delUrl = "&nbsp;";
      }
      w.println("<tr><td>" + name + "</td><td>" + value + "</td><td>" + ro + "</td><td>" + editUrl + "</td><td>" +
        delUrl + "</td></tr>");
    }
    url = response.createRenderURL();
    url.setPortletMode(PortletMode.EDIT);
    String createUrl = "<a href=\"" + url.toString() + "\">[create new]</a>";
    w.println("<tr><td>" + createUrl + "</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
    w.println("</table>");
  }

  /**
   * Overridden method.
   *
   * @param request
   * @param response
   * @throws PortletException
   * @throws IOException
   * @see javax.portlet.GenericPortlet#doEdit(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    response.setContentType("text/html");
    PrintWriter w = response.getWriter();
    PortletPreferences prefs = request.getPreferences();

    String name = request.getParameter("pref_name");
    String value = "";

    PortletURL url = response.createActionURL();
    w.println("<form action=\"" + url.toString() + "\" method=\"post\">");
    if (name != null) {
      value = prefs.getValue(name, "");
      w.println("<p>Edit:</p>");
      w.println("<input type=\"hidden\" name=\"pref_name\" value=\"" + name + "\">");
      w.println("<table><tr><th>" + name + "</th><td>");
      w.println("<input type=\"text\" name=\"pref_value\" value=\"" + value + "\"></td></tr></table>");
    } else {
      w.println("<p>Create new:</p>");
      w.println("<input type=\"text\" name=\"pref_name\" value=\"new_pref\">");
      w.println("<input type=\"text\" name=\"pref_value\" value=\"\">");
    }
    w.println("<input type=\"submit\" name=\"save\" value=\"Save!\"><input type=\"submit\" value=\"Cancel\">");
    w.println("</form>");
  }

  /**
   * Overridden method.
   *
   * @param request
   * @param response
   * @throws PortletException
   * @throws IOException
   * @see javax.portlet.GenericPortlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
   */
  public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
    if (request.getParameter("del") != null && request.getParameter("pref_name") != null) {
      PortletPreferences prefs = request.getPreferences();
      prefs.reset(request.getParameter("pref_name"));
      prefs.store();
    } else if (request.getParameter("save") != null && request.getParameter("pref_name") != null) {
      PortletPreferences prefs = request.getPreferences();
      prefs.setValue(request.getParameter("pref_name"), request.getParameter("pref_value"));
      prefs.store();
    }
    response.setPortletMode(PortletMode.VIEW);
  }

}
