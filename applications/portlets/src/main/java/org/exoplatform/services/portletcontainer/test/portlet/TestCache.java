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
import java.util.Date;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Test portlet to test cache work.
 */
public class TestCache extends GenericPortlet {

  /**
   * The GenericPortlet calls this method if the portlet mode is view.
   *
   * @param request the request
   * @param response the response
   *
   * @throws PortletException something may go wrong
   * @throws IOException something may go wrong
   */
  public void doView(RenderRequest request, RenderResponse response)
    throws PortletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    Date d=new Date();
    PrintWriter w = response.getWriter();
    response.setTitle("TestCache: "+ d.toString());
    w.println("<center><font size='3'><b><i>Portlet for test cacheability of the portlet body. Date and time must not change while cache time not expired.</i></b></font></center><br>");
    w.println("TestCache: "+ d.toString());
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException { }

}
