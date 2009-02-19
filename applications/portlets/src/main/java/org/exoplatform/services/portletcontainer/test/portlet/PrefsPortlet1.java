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
import javax.portlet.WindowState;

/**
 * Test prefs portlet.
 */
public class PrefsPortlet1 extends GenericPortlet {

  /**
   * A life cycle method. This method gets inovked by the container.
   *
   * @param ponfig a object that holds the portlet configuration information
   * @throws PortletException an exception that you throw if things go wrong
   *           while starting the portlet
   */
  public void init(PortletConfig ponfig) throws PortletException {
    super.init(ponfig);
  }

  /**
   * The GenericPortlet calls this method if the portlet mode is view
   *
   * @param pRequest the request
   * @param pResponse the response
   * @throws PortletException
   * @throws IOException
   */
  public void doView(RenderRequest request, RenderResponse response) throws PortletException,
      IOException {
    response.setContentType("text/html");
    PrintWriter writer = response.getWriter();
    PortletPreferences prefs = request.getPreferences();
    writer
        .println("<center><font size='3'><b><i>This portlet tests storing/retrieving params from PortletPreferences</i></b></font></center><br>");
    writer.println("<div style='background-color:" + prefs.getValue("color1", "black")
        + "'>color1</div><br>");
    writer.println("<div style='background-color:" + prefs.getValue("color2", "black")
        + "'>color2</div><br>");

    PortletURL renderURL = response.createRenderURL();
    // PortletURL actionURL = renderResponse.createActionURL();
    renderURL.setWindowState(WindowState.NORMAL);
    renderURL.setPortletMode(PortletMode.EDIT);
    writer.println("<p><a href=\"" + renderURL.toString() + "\">EDIT</a></p>");
  }

  /**
   * The GenericPortlet calls this method if the portlet mode is edit
   *
   * @param request the request
   * @param response the response
   * @throws PortletException
   * @throws IOException
   */
  public void doEdit(RenderRequest request, RenderResponse response) throws PortletException,
      IOException {
    response.setContentType("text/html");

    PrintWriter writer = response.getWriter();
    PortletPreferences prefs = request.getPreferences();
    writer.println("<p>current color2: " + prefs.getValue("color2", ""));

    PortletURL actionURL = response.createActionURL();
    actionURL.setParameter("color2", "green");
    writer.println("<p><a href=\"" + actionURL.toString()
        + "\"><font color=\"green\">change color2 to green</font></a></p>");

    actionURL = response.createActionURL();
    actionURL.setParameter("color2", "red");
    writer.println("<p><a href=\"" + actionURL.toString()
        + "\"><font color=\"red\">change color2 to red</font></a></p>");

    actionURL = response.createActionURL();
    actionURL.setParameter("color2", "blue");
    writer.println("<p><a href=\"" + actionURL.toString()
        + "\"><font color=\"blue\">change color2 to blue</font></a></p>");
  }

  // @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest,
  // javax.portlet.ActionResponse)
  public void processAction(ActionRequest request, ActionResponse response) throws PortletException,
      IOException {
    if (request.getParameter("color2") != null) {
      PortletPreferences prefs = request.getPreferences();
      prefs.setValue("color2", request.getParameter("color2"));
      prefs.store();
      response.setPortletMode(PortletMode.VIEW);
    }
  }
}
