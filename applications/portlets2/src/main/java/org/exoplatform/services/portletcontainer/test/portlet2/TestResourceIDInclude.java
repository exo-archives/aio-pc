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
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

/**
 * Created by The eXo Platform SAS .
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class TestResourceIDInclude extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest request
   * @param renderResponse response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doView method of TestResuorceIDInclude...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Portlet for test resourceID setting effect.</i></b></font></center><br>");
    w.println("Current method: "+((new Exception()).getStackTrace()[0]).getMethodName());

    w.println("<br><br>");
    ResourceURL resourceURL2 = renderResponse.createResourceURL();
    resourceURL2.setResourceID("/test");
    w.println("<p><a href=\"" + resourceURL2.toString() + "\">resource URL</a>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    //System.out.println("In processAction method of TestResuorceIDInclude...");

  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In render method of TestResuorceIDInclude...");
    super.render(renderRequest, renderResponse);
  }

}
