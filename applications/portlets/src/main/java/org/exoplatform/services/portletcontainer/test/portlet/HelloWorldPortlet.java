/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 3:38:11 PM
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;



public class HelloWorldPortlet extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
//    System.out.println("In doView method of HelloWorldPortlet...");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">Hello World</h2>");
    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><th colspan=\"2\">Request attributes</th></tr>");
    w.println("<tr><th>attibute</th><th>value</th></tr>");
    Enumeration e = renderRequest.getAttributeNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("<tr><td>" + s + "</td>");
      w.println("<td>" + renderRequest.getAttribute(s) + "</td></tr>");
    }
    w.println("</table>");
    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><th colspan=\"2\">Request parameters</th></tr>");
    w.println("<tr><th>parameter</th><th>value</th></tr>");
    e = renderRequest.getParameterNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("<tr><td>" + s + "</td>");
      w.println("<td>" + renderRequest.getParameter(s) + "</td></tr>");
    }
    w.println("</table>");
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setParameter("action_param_1", "action param test");
    actionURL.setParameter("action_param_2", "action param test 2");
    actionURL.setSecure(true);
    actionURL.setWindowState(WindowState.MAXIMIZED);
    actionURL.setPortletMode(PortletMode.EDIT);

    PortletURL renderURL = renderResponse.createRenderURL();
    renderURL.setParameter("render_param", "render param");

    w.println("<p>Create Portlet URL...</p>");
    w.println("<p><a href=\"" + actionURL.toString() + "\">action URL</a>");
    w.println("<a href=\"" + renderURL.toString() + "\">render URL</a></p>");

    renderResponse.setTitle("test title");
    renderResponse.setProperty(RenderResponse.EXPIRATION_CACHE, ""+6);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    (new Exception()).printStackTrace(pw);
    w.println("<table width=\"100%\" border=\"1\">");
    w.println("<tr><td><blockquote>\n" + sw.toString() + "\n</blockquote></td></tr>\n");
    w.println("</table>");
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
//    System.out.println("In doEdit method of HelloWorldPortlet...");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">If you want so much to edit something try this =)</h2>");
    w.println("<form><input type=\"text\" value=\"something to edit =)\"/></form>");

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.HELP);
    w.println("<p><a href=\"" + actionURL.toString() + "\">do you need help?..</a>");
    actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
  }

  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
//    System.out.println("In doHelp method of HelloWorldPortlet...");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">Help, I need somebody's help! &copy;</h2>");

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    actionRequest.setAttribute("test_attribute", "test");
    actionResponse.setRenderParameter("test_render_param", "test 2");
    
//    System.out.println("   **User Principal : "+actionRequest.getUserPrincipal());
//    System.out.println("   **Remote User : "+actionRequest.getRemoteUser());
//    System.out.println("   **User in role coco : "+actionRequest.isUserInRole("coco"));
//    System.out.println("   **User in role trustedUser : "+actionRequest.isUserInRole("trustedUser"));
  }

}
