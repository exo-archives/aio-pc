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
public class TestIncludes extends GenericPortlet {

  /**
   * JSP to include.
   */
  private static final String HELLO_TEMPLATE = "/WEB-INF/HelloWorld.jsp";

  /**
   * HTML to include.
   */
  private static final String HELLO_TEMPLATE_HTML = "/WEB-INF/HelloWorld.html";

  /**
   * Just a doView method.
   *
   * @param request portlet request
   * @param response portlet response
   * @throws PortletException something may go wrong
   * @throws IOException something may go wrong
   */
  protected void doView(RenderRequest request, RenderResponse response)
    throws PortletException, IOException {

    response.setContentType("text/html;charset=UTF-8");
    response.setTitle("HelloWorld Runtime TITLE");
    response.getWriter().println("<center><font size='3'><b><i>This portlet demonstrates including of servlets, .jsp and .html files into the portlet.</i></b></font></center><br>");


    PortletContext context = getPortletContext();

    //include a jsp page
    int jsptimes = Integer.parseInt(getInitParameter("jsptimes"));
    if (jsptimes > 0) {
      PortletRequestDispatcher rd = context.getRequestDispatcher(HELLO_TEMPLATE);
      for(int i=0;i<jsptimes;i++)
        rd.include(request, response);
    }

    //include an html page
    int htmltimes = Integer.parseInt(getInitParameter("htmltimes"));
    if (htmltimes > 0) {
      PortletRequestDispatcher rd = context.getRequestDispatcher(HELLO_TEMPLATE_HTML);
      for(int i=0;i<htmltimes;i++)
        rd.include(request, response);
    }

    //include a servlet that includes HELLO_TEMPLATE
    int servlettimes = Integer.parseInt(getInitParameter("servlettimes"));
    if (servlettimes > 0) {
      PortletRequestDispatcher rd =
        context.getRequestDispatcher("/TestServlet");
      for(int i=0;i<servlettimes;i++)
        rd.include(request, response);
    }
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException { }
}
