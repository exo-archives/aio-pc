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
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class TestCache2 extends GenericPortlet {

  public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    boolean istagvalid = request.getAttribute("istagvalid") != null;
    System.out.println("portlet2.TestCache2: ---- tag valid? : " + request.getAttribute("istagvalid"));
    request.setAttribute("istagvalid", "true");
    response.setContentType("text/html;charset=UTF-8");
    Date d = new Date();
    System.out.println("portlet2.TestCache2: ---- etag: " + request.getETag());
    if (request.getETag() != null && istagvalid) {
      response.getCacheControl().setExpirationTime(30);
      response.getCacheControl().setUseCachedContent(true);
      return;
    }
    response.getCacheControl().setExpirationTime(60);
    response.getCacheControl().setETag(request.getWindowID());
    response.setTitle("TestCache2: " + d.toString());

    PrintWriter w = response.getWriter();
    w.println("<center><font size='3'><b><i>Portlet for test cache control of the portlet request. Date and time must not change while cache time not expired or link clicked.</i></b></font></center><br>");
    w.println("<font size='4'>TestCache2: " + d.toString() + "</font>");
    if (request.getParameter("invalidatetag") != null) {
      System.out.println("portlet2.TestCache2: ---- tag invalidated");
    }

    PortletURL actionURL = response.createActionURL();
    actionURL.setParameter("invalidatetag", "true");
    w.println("<p><a href=\"" + actionURL.toString() + "\">Invalidate tag</a>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    System.out.println("portlet2.TestCache2.processAction: ---- invalidatetag: " + actionRequest.getParameter("invalidatetag"));
    if (actionRequest.getParameter("invalidatetag") != null) {
      actionRequest.removeAttribute("istagvalid");
      actionResponse.setRenderParameter("invalidatetag", "true");
    }
  }

}
