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
package org.exoplatform.services.portletcontainer.demo;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class IncludeServlet extends HttpServlet {

  protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, java.io.IOException {
    PrintWriter w = servletResponse.getWriter();
    w.println("<html>");
    w.println("  <body>");
    w.println("    <h3>Servlet</h3>");
    w.println("    <p>Some markup from within servlet.</p>");
    w.println("    <h2>Below is the content of included JSP page</h2><br/>");
    RequestDispatcher rd = getServletContext().getRequestDispatcher("/demo.jsp");
    rd.include(servletRequest, servletResponse);
    w.println("    <br/>");
    w.println("    <br>-----end-of-/demo-servlet-----<br>");
    w.println("  </body>");
    w.println("</html>");
  }

}
