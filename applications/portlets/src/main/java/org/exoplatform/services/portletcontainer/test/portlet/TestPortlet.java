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


public class TestPortlet extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
//    System.out.println("In doView method of TestPortlet...");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">Test</h2>");
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
    //renderResponse.createRenderURL().setParameter("","");
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setParameter("action_param_1", "action param test");
    actionURL.setParameter("action_param_rus", "ПРИВЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕТ!!!!!");
    
    String rus = new String("йцукенгшщзхъфывапролджэячсмитьбю.ЙЦУКЕНГШЩЗХФЫВАПРОЛДЖЧСМИТЬБЮ".getBytes("Cp1251"),"UTF-8");
    actionURL.setParameter("action_param_rus2", rus);
    
    actionURL.setWindowState(WindowState.MAXIMIZED);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">action URL</a>");

    renderResponse.setTitle("TEST PORTLET");
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
//    System.out.println("In doEdit method of TestPortlet...");
    PrintWriter w = renderResponse.getWriter();

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    if (actionRequest.getParameterValues("action_param_rus")!=null)
      actionResponse.setRenderParameter("action_param_rus",actionRequest.getParameterValues("action_param_rus"));      
    
    if (actionRequest.getParameterValues("action_param_rus2")!=null)
      actionResponse.setRenderParameter("action_param_rus2",actionRequest.getParameterValues("action_param_rus2"));
    
    actionResponse.setRenderParameter("test_render_param", "test-----------------2");
    actionResponse.setRenderParameter("test_render_param_rus", "ПРИВЕТ ВСЕМ !!!");
  }

}
