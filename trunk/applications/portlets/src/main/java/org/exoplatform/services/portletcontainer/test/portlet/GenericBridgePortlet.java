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

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class GenericBridgePortlet extends GenericPortlet {

  /**
   * Portlet context.
   */
  private PortletContext context;

  /**
   * Default page to include.
   */
  private String defaultPage;

  /**
   * URL to include.
   */
  private String requestUrl;

  /**
   * Parameter prefix.
   */
  public static final String PARAM_PREFIX = "portlet:";

  /**
   * Overridden method.
   *
   * @param config portlet config
   * @throws PortletException something may go wrong
   * @see javax.portlet.GenericPortlet#init(javax.portlet.PortletConfig)
   */
  public void init(PortletConfig config) throws PortletException {
    super.init(config);

    context = config.getPortletContext();
    defaultPage = config.getInitParameter("default-page");
    requestUrl = defaultPage;
  }

  public void doView(RenderRequest request, RenderResponse response) throws PortletException {
    try {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter w = response.getWriter();

      // --- simple navigation bar ---------
      w.println("<table width=\"100%\"><tr><td>");

      PortletURL renderURL = response.createRenderURL();
      renderURL.setParameter("url", "/test");
      w.println("<p><a href=\"" + renderURL.toString() + "\">Embed servlet</a> | ");

      renderURL = response.createRenderURL();
      renderURL.setParameter("url", "/test.jsp");
      w.println("<a href=\"" + renderURL.toString() + "\">Embed JSP</a></p>");

      w.println("</td></tr><tr><td>");

      requestUrl = request.getParameter("url");
      if (requestUrl == null)
        requestUrl = defaultPage;

      w.println("<b><u>" + requestUrl + "</u></b>");

      w.println("</td></tr><tr><td>");

      w.println("<table width=\"100%\" border=\"1\">");
      w.println("<tr><th colspan=\"2\">Request parameters</th></tr>");
      w.println("<tr><th>parameter</th><th>value</th></tr>");
      Enumeration e = request.getParameterNames();
      while (e.hasMoreElements()) {
        String s = (String) e.nextElement();
        w.println("<tr><td>" + s + "</td>");
        w.println("<td>" + request.getParameter(s) + "</td></tr>");
      }
      w.println("</table>");

      w.println("</td></tr><tr><td>");

      PortletRequestDispatcher dispatcher = context.getRequestDispatcher(requestUrl);
      dispatcher.include(request, response);

      w.println("</td></tr></table>");
    }
    catch (Exception e) {
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    }
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException { }

}
