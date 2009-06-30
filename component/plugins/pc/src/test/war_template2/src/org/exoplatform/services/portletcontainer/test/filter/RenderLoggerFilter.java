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

import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.FilterChain;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import org.exoplatform.services.log.Log;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 17 nov. 2003 Time: 19:06:35
 */
public class RenderLoggerFilter implements RenderFilter {

  public RenderLoggerFilter() {
  }

  public void init(FilterConfig filterConfig) throws PortletException {
  }

  public void doFilter(RenderRequest portletRequest,
                       RenderResponse portletResponse,
                       FilterChain filterChain) throws IOException, PortletException {
    portletRequest.setAttribute("param", "default-param-value");
    if (portletRequest.getParameter("EXO_FAIL_CHAIN") == null) {
      filterChain.doFilter(portletRequest, portletResponse);
    } else {
      portletResponse.setContentType("text/html; charset=UTF-8");
      PrintWriter w = portletResponse.getWriter();
      w.println("The filter's html markup!");
    }
  }

  public void destroy() {
  }

}
