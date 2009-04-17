/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.portals.bridges.struts.PortletServlet;
import org.apache.portals.bridges.struts.PortletServletResponseWrapper;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Jul 30, 2008
 */
public class SpringDelegatingPortletProcessor extends RequestProcessor {

  public SpringDelegatingPortletProcessor() {
    super();
  }

  public void process(HttpServletRequest request, HttpServletResponse response) throws IOException,
                                                                               ServletException {
    if (PortletServlet.isPortletRequest(request)) {
      if (!(response instanceof PortletServletResponseWrapper)) {
        response = new PortletServletResponseWrapper(request, response);
      }
    }
    super.process(request, response);
  }

  protected boolean processRoles(HttpServletRequest request,
                                 HttpServletResponse response,
                                 ActionMapping mapping) throws IOException, ServletException {
    boolean proceed = super.processRoles(request, response, mapping);
    if (proceed && PortletServlet.isPortletRequest(request)
        && ((PortletServlet) super.servlet).performActionRenderRequest(request, response, mapping)) {
      return false;
    } else
      return proceed;
  }

}
