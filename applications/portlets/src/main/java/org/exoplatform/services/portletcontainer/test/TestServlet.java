/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.test;

import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class TestServlet extends HttpServlet {

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
    writer.println("</body></html>");
	}
}
