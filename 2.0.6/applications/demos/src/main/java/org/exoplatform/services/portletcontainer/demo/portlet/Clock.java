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
package org.exoplatform.services.portletcontainer.demo.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class Clock extends GenericPortlet {

  private String clockURL = "/clock.html";

  @Override
  protected void doView(RenderRequest request,
                        RenderResponse response) throws PortletException,
                                                IOException {
    response.setContentType("text/html;charset=UTF-8");
    response.setTitle("Clock");
    PortletContext context = getPortletContext();
    PortletRequestDispatcher rd = context.getRequestDispatcher(clockURL);
    rd.include(request, response);
  }

  @Override
  public void processAction(ActionRequest request,
                            ActionResponse response) throws PortletException,
                                                    IOException {
    super.processAction(request, response);
  }
  
  @Override
  protected void doHelp(RenderRequest request,
                        RenderResponse response) throws PortletException,
                                                IOException {
    response.setContentType("text/html;charset=UTF-8");
    response.setTitle("Clock");
    PrintWriter w = response.getWriter();
    w.println("Demo clock portlet.");
  }

}
