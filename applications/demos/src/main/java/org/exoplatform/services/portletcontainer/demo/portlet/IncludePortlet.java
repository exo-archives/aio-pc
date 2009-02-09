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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Includes test.
 */
public class IncludePortlet extends GenericPortlet {

  /**
   * JSP to include.
   */
  private static final String TEMPLATE_JSP  = "/demo.jsp";

  /**
   * HTML to include.
   */
  private static final String TEMPLATE_HTML = "/demo.html";

  /**
   * Servlet to include.
   */
  private static final String TEMPLATE_SRV  = "/demo";

  /**
   * Just a doView method.
   * 
   * @param request portlet request
   * @param response portlet response"org.exoplatform.services.portletcontainer"
   * @throws PortletException something may go wrong
   * @throws IOException something may go wrong
   *           "org.exoplatform.services.portletcontainer"
   */
  protected void doView(RenderRequest request, RenderResponse response) throws PortletException,
                                                                       IOException {

    response.setContentType("text/html; charset=utf-8");
    PrintWriter w = response.getWriter();
    w.println("<center><font size='3'><b><i>This portlet demonstrates including of servlets, .jsp and .html files to the portlet markup</i></b></font></center><br>");

    PortletContext context = getPortletContext();

    //include a jsp page
    w.println("<h2>Below is the content of included JSP page</h2><br/>");
    PortletRequestDispatcher rd = context.getRequestDispatcher(TEMPLATE_JSP);
    rd.include(request, response);
    w.println("<br/>");

    //include an html page
    w.println("<h2>Below is the content of included HTML page</h2><br/>");
    rd = context.getRequestDispatcher(TEMPLATE_HTML);
    rd.include(request, response);
    w.println("<br/>");

    //include a servlet that includes TEMPLATE_JSP
    w.println("<h2>Below is the content of included servlet</h2><br/>");
    rd = context.getRequestDispatcher(TEMPLATE_SRV);
    rd.include(request, response);
    w.println("<br/>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
                                                                                       IOException {
  }

}
