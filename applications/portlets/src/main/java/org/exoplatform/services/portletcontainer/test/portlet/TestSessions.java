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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.exoplatform.frameworks.portletcontainer.portalframework.WindowID2;

/**
 * sessions test portlet.
 */
public class TestSessions extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest request
   * @param renderResponse response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
      IOException {

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Simple portlet for test storing session params when replication is enabled</i></b></font></center><br>");

    String a = (String) renderRequest.getPortletSession().getAttribute("a");
    if (a == null || a.equals(""))
      a = "";
    w.println(a + "<br>");
    a += "++";
    renderRequest.getPortletSession().setAttribute("a", a);

    String c = (String) renderRequest.getPortletSession().getAttribute("c");
    if (c == null)
      c = "";
    c += "||";
    if (c.length() > 10)
      c = "";
    w.println(c + "<br>");

    renderRequest.getPortletSession().setAttribute("c", c);

    WindowID2 d  = (WindowID2)renderRequest.getPortletSession().getAttribute("d");
    if (d != null) {

      w.println("Object " + d.getClass().getSimpleName() + " received with: ");
      w.println("content: " + d.getRenderParams()+  "<br>");

    }
    d = new WindowID2();
    HashMap map = new HashMap <String, String[]>();
    Random random = new Random();
    map.put("param1", "value1" + random.nextInt());
    map.put("param2", "value2"+ random.nextInt());
    map.put("param3", "value3"+random.nextInt());
    d.setRenderParams(map);

    renderRequest.getPortletSession().setAttribute("d", d);


   }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
      IOException {

  }

}
