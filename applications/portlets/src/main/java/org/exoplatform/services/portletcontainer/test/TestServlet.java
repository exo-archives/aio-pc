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
package org.exoplatform.services.portletcontainer.test;

import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
//import javax.servlet.ServletContext;
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
public class TestServlet extends HttpServlet {

  /**
   * Overridden method.
   *
   * @param servletConfig config
   * @throws ServletException exception
   * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
   */
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
  }

  protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
                  throws ServletException, java.io.IOException {
    PrintWriter writer = servletResponse.getWriter();
    writer.println("<html><head><title>test servlet</title></head><body><h1>header</h1><p>content</p><p><a href=\"\">link</a></p>");
    writer.println("<p><ul>");
    for (Enumeration e = servletRequest.getParameterNames(); e.hasMoreElements();) {
      String n = (String) e.nextElement();
      writer.println("<li><b>" + n + "</b>: <u>" + servletRequest.getParameter(n) + "</u></li>");
    }
    writer.println("</ul></p>");
    writer.println("<br>-----end-of-/TestServlet----<br>");
    writer.println("</body></html>");
  }
}
