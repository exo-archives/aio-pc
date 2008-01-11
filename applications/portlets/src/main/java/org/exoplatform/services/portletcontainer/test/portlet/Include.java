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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class Include extends GenericPortlet {

  @Override
  protected void doDispatch(RenderRequest request,
                            RenderResponse response) throws PortletException,
                                                    IOException {
    WindowState state = request.getWindowState();

    if (!state.equals(WindowState.MINIMIZED)) {
      response.setContentType("text/html;charset=UTF-8");
      response.setTitle("Include TITLE");

      PortletMode mode = request.getPortletMode();
      System.out.println(">>> EXOMAN Include.doDispatch() mode.toString() = " + mode.toString());
      
      String forInclude = getInitParameter(mode.toString());
      System.out.println(">>> EXOMAN Include.doDispatch() forInclude = " + forInclude);
      if (forInclude == null)
        forInclude = getInitParameter(mode.toString().toLowerCase());
      System.out.println(">>> EXOMAN Include.doDispatch() forInclude = " + forInclude);
      if (forInclude == null)
        forInclude = getInitParameter(mode.toString().toUpperCase());
      System.out.println(">>> EXOMAN Include.doDispatch() forInclude = " + forInclude);
      
      PortletContext context = getPortletContext();
      PortletRequestDispatcher rd = context.getRequestDispatcher(forInclude);
      rd.include(request, response);
    }

  }

  @Override
  public void processAction(ActionRequest request,
                            ActionResponse response) throws PortletException,
                                                    IOException {

    WindowState state = request.getWindowState();

    if (!state.equals(WindowState.MINIMIZED)) {

      PortletMode mode = request.getPortletMode();
      String forInclude = getInitParameter(mode.toString() + "Action");

      PortletContext context = getPortletContext();
      PortletRequestDispatcher rd = context.getRequestDispatcher(forInclude);
      rd.include(request, response);
    }

  }

}
