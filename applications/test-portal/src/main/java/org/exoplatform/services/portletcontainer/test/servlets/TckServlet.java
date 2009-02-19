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
package org.exoplatform.services.portletcontainer.test.servlets;

import static org.exoplatform.frameworks.portletcontainer.portalframework.PFConstants.PORTLET_INFOS;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class TckServlet extends HttpServlet {

  /**
   * Overridden method.
   * 
   * @param request request
   * @param response response
   * @throws ServletException exception
   * @throws IOException exception
   * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                  IOException {
    HttpSession session = request.getSession();
    byte[] resource = (byte[]) session.getAttribute("resource");
    if (resource != null) {
      String cntType = (String) session.getAttribute("resourceType");
      if (cntType != null)
        response.setContentType(cntType);
      else
        response.setContentType("text/html");
      Map<String, String> headers = (Map<String, String>) session.getAttribute("resourceHeaders");
      if (headers != null) {
        for (Iterator<String> i = headers.keySet().iterator(); i.hasNext();) {
          String name = i.next();
          response.setHeader(name, headers.get(name));
        }
      }
      response.setStatus((Integer) session.getAttribute("resourceStatus"));
      OutputStream w = response.getOutputStream();
      w.write(resource);
      w.close();
      session.removeAttribute("resource");
      session.removeAttribute("resourceType");
      return;
    }
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter w = response.getWriter();
    if (session.getAttribute("portletName") != null) {
      // especially for TCK tests
      ArrayList<PortletInfo> portletinfos = (ArrayList) session.getAttribute(PORTLET_INFOS);
      for (Iterator<PortletInfo> i = portletinfos.iterator(); i.hasNext();) {
        PortletInfo pinf = i.next();
        w.println(pinf.getOut());
      }
      return;
    }
  }

}
