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

import javax.portlet.filter.ResourceFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.UnavailableException;

/**
 * Created by The eXo Platform SAS .
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class MyResourceFilter implements ResourceFilter {

  /**
   * name.
   */
  private String name;

  /**
   * Overridden method.
   *
   * @param config config
   * @throws UnavailableException exception
   * @see javax.portlet.filter.PortletFilter#init(javax.portlet.filter.FilterConfig)
   */
  public void init(FilterConfig config) throws UnavailableException {
    name = config.getInitParameter("name");
  }

  /**
   * Overridden method.
   *
   * @param resourceRequest request
   * @param resourceResponse response
   * @param filterChain chain
   * @throws UnavailableException exception
   * @see javax.portlet.filter.ResourceFilter#doFilter(javax.portlet.ResourceRequest, javax.portlet.ResourceResponse, javax.portlet.filter.FilterChain)
   */
  public void doFilter(ResourceRequest resourceRequest, ResourceResponse resourceResponse, FilterChain filterChain) throws IOException, PortletException {
    resourceRequest.setAttribute("name",name);
    //filterChain.doFilter(resourceRequest, resourceResponse);
  }

  public void destroy() {
  }

}
