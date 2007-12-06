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
package org.exoplatform.services.portletcontainer.test.portlet2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class TestPortlet extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Simple portlet for test additional window states and events procesing.</i></b></font></center><br>");
    w.println("Current method: "+((new Exception()).getStackTrace()[0]).getMethodName());

    PortletURL actionURL = renderResponse.createActionURL();
    w.println("<p><a href=\"" + actionURL.toString() + "\">actionURL</a></p>");
    w.println("----------------------------------------<br>");


    /*
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
    */
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doEdit method of TestPortletNEW...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());

    /*
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
    */
  }

  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doHelp method of TestPortletNEW...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());

    /*
    w.println("<h2 align=\"center\">Help, I need somebody's help! &copy;</h2>");
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
    */
  }

  protected void doConfig(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doHelp method of TestPortletNEW...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());
    w.println("<br>");
    w.println(renderRequest.getPreferences().getMap());
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    System.out.println("In processAction method of TestPortletNEW...");

    //super.processAction(actionRequest,actionResponse);
    /*
    actionRequest.setAttribute("test_attribute", "test");
    actionResponse.setRenderParameter("test_render_param", "test 2");
    */
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In render method of TestPortletNEW...");

    super.render(renderRequest, renderResponse);
  }

  @ProcessEvent(name="MyEventPub")
  public void processMyEventPub(EventRequest req, EventResponse resp) throws PortletException, IOException {
    System.out.println("In processMyEventPub method of TestPortlet... !!!!!!!!!!!!!");
    Event event = req.getEvent();
    System.out.println("  -- value: " + event.getValue());
    resp.setPortletMode(PortletMode.EDIT);
  }

  @ProcessEvent(name="MyEventPub2")
  public void processMyEventPub2(EventRequest req, EventResponse resp) throws PortletException, IOException {
    System.out.println("In processMyEventPub2 method of TestPortlet... !!!!!!!!!!!!!");
    Event event = req.getEvent();
    System.out.println("  -- value: " + event.getValue());
    resp.setPortletMode(PortletMode.EDIT);
  }

  protected void doDispatch(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    if (request.getPortletMode().toString().equalsIgnoreCase("config")) {
      doConfig(request, response);
    } else {
      super.doDispatch(request, response);
    }
  }



}
