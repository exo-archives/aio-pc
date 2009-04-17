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
package org.exoplatform.services.portletcontainer.test.filter;

import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.FilterChain;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import java.io.IOException;

import org.apache.commons.logging.Log;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 17 nov. 2003 Time: 18:55:31
 */
public class ActionLoggerFilter implements ActionFilter {

  private String param;

  public ActionLoggerFilter() {
  }

  public void init(FilterConfig filterConfig) throws PortletException {
    if (!"default-param-value".equals(filterConfig.getInitParameter("default-param")))
      throw new PortletException();
    param = filterConfig.getInitParameter("default-param");
  }

  public void doFilter(ActionRequest portletRequest,
                       ActionResponse portletResponse,
                       FilterChain filterChain) throws IOException, PortletException {

    portletRequest.setAttribute("param", param);
    portletRequest.setAttribute("filterID", this.toString());
    if (portletRequest.getParameter("EXO_FAIL_CHAIN") == null) {
      filterChain.doFilter(portletRequest, portletResponse);
    }
  }

  public void destroy() {
    System.out.println("destroy() method called - OK");
  }

}
