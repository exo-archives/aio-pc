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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;
import org.exoplatform.frameworks.portletcontainer.portalframework.Template;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutColumn;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutHtmlTag;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutHtmlText;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutItem;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutNode;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutPlt;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutZone;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class PortalServlet extends HttpServlet {

  /**
   * Log.
   */
  private Log log;

  /**
   * Portlet infos.
   */
  private List<PortletInfo> portletInfos;

  /**
   * Serves http request. Renders portal page.
   *
   * @param request http request
   * @param response http response
   * @throws IOException declared by superclass
   * @throws ServletException declared by superclass
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    try {
      this.log = ExoLogger.getLogger(getClass());
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

      portletInfos = (List<PortletInfo>) session.getAttribute("portletinfos");

      Map<String, List<String>> pList = new HashMap<String, List<String>>();
      for (Iterator<String> i = ((List<String>) session.getAttribute("portletNames")).iterator(); i.hasNext(); ) {
        String pn = i.next();
        String[] ss = pn.split("/");
        List<String> pl = pList.get(ss[0]);
        if (pl == null)
          pList.put(ss[0], pl = new ArrayList<String>());
        pl.add(ss[1]);
      }
      String pNames = "";
      boolean b = false;
      List<String> pal = new ArrayList<String>(pList.keySet());
      Collections.sort(pal);
      for (Iterator<String> i = pal.iterator(); i.hasNext(); ) {
        String pan = i.next();
        if (b)
          pNames += ", ";
        pNames += "\"" + pan + "\": [";
        List<String> pl = pList.get(pan);
        boolean b1 = false;
        for (Iterator<String> i1 = pl.iterator(); i1.hasNext(); ) {
          if (b1)
            pNames += ", ";
          pNames += "\"" + i1.next() + "\"";
          b1 = true;
        }
        pNames += "]";
        b = true;
      }
      session.setAttribute("portletNames", pNames);

      response.setContentType("text/html;charset=UTF-8");
      PrintWriter w = response.getWriter();

      includeJsp(request, response, "/pages/portal_header.jsp");

      List<LayoutItem> layout = Template.getPortletLayout(new FileInputStream((String) session.getAttribute("pageTemplate")));

      renderLayout(layout, request, response, w);

      includeJsp(request, response, "/pages/portal_footer.jsp");
      System.out.println("-------------------------------------------------------------");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void renderLayout(List<LayoutItem> layout, HttpServletRequest request, HttpServletResponse response,
      PrintWriter w) throws ServletException, IOException {
    HttpSession session = request.getSession();
    for (Iterator<LayoutItem> i = layout.iterator(); i.hasNext();) {
      renderItem(i.next(), request, response, session, w);
    }
  }

  private void renderItem(LayoutItem item, HttpServletRequest request, HttpServletResponse response, HttpSession session,
      PrintWriter w) throws ServletException, IOException {
    if (item == null)
      return;
    if (item instanceof LayoutColumn) {
      LayoutColumn col = (LayoutColumn) item;
      request.setAttribute("col_id", col.getId());
      request.setAttribute("col_width", col.getWidth());
      includeJsp(request, response, "/pages/col_header.jsp");
      renderChildren(item, request, response, session, w);
      includeJsp(request, response, "/pages/col_footer.jsp");
    } else if (item instanceof LayoutZone) {
      includeJsp(request, response, "/pages/zone_header.jsp");
      renderChildren(item, request, response, session, w);
      includeJsp(request, response, "/pages/zone_footer.jsp");
    } else if (item instanceof LayoutPlt)
      renderPortlet((LayoutPlt) item, request, response, session);
    else if (item instanceof LayoutHtmlText)
      w.print(item.getName());
    else if (item instanceof LayoutHtmlTag) {
      w.print("<" + item.getName() + " ");
      for (Map.Entry<String, String> attr : ((LayoutHtmlTag) item).getAttrs().entrySet())
        w.print(attr.getKey() + "=\"" + attr.getValue() + "\"");
      w.print(">");
      renderChildren(item, request, response, session, w);
      w.print("</" + item.getName() + ">");
    } else if (item.getName().equals(LayoutItem.HEADER))
      includeJsp(request, response, "/pages/header.jsp");
    else if (item.getName().equals(LayoutItem.FOOTER))
      includeJsp(request, response, "/pages/footer.jsp");
    else if (item.getName().equals(LayoutItem.PAGES))
      includeJsp(request, response, "/pages/tabs.jsp");
  }

  private void renderChildren(LayoutItem item, HttpServletRequest request, HttpServletResponse response,
      HttpSession session, PrintWriter w) throws ServletException, IOException {
    if (item instanceof LayoutNode) {
      for (Iterator<LayoutItem> i = ((LayoutNode) item).getChildren().iterator(); i.hasNext();)
        renderItem(i.next(), request, response, session, w);
    }
  }

  private void renderPortlet(LayoutPlt lp, HttpServletRequest request, HttpServletResponse response,
      HttpSession session) throws ServletException, IOException {
    for (Iterator<PortletInfo> i = portletInfos.iterator(); i.hasNext();) {
      PortletInfo pinf = i.next();
      if (pinf.getWid().equals(lp.getId())) {
        session.setAttribute("pinf", pinf);
        includeJsp(request, response, "/pages/plt.jsp");
      }
    }
  }

  private void includeJsp(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException,
      IOException {
    getServletContext().getRequestDispatcher(page).include(request, response);
  }

}
