/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.PrintWriter;
import java.util.*;
import javax.portlet.*;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class GenericBridgePortlet extends GenericPortlet {

  protected PortletContext context;
  protected String defaultPage;
  protected String requestUrl;
  public static String PARAM_PREFIX = "portlet:";

  public void init(PortletConfig config) throws PortletException {
      super.init(config);

      context = config.getPortletContext();
      defaultPage = config.getInitParameter("default-page");
      requestUrl = defaultPage;
  }

  protected void doView(RenderRequest request, RenderResponse response)
      throws PortletException {

//System.out.println(" =====> from within portlet: request: " + request);
//for (Enumeration e = request.getParameterNames(); e.hasMoreElements();)
//System.out.println(" =====> from within portlet: param: " + e.nextElement());
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
//System.out.println(" =========== param url: " + requestUrl);
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

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException {
  }

}
