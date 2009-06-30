/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.services.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.exoplatform.services.log.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 */

@Deprecated
public class ContainerFilter implements Filter {

  private static Log log        = ExoLogger.getLogger(ContainerFilter.class);

  private String containerName = "portal";

  public void init(FilterConfig filterConfig) throws ServletException {
    String containerName = filterConfig.getInitParameter("portal-container-name");
    if (containerName != null) 
      this.containerName = containerName;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    ExoContainer container = ExoContainerContext.getContainerByName(containerName);
    if (container == null)
      container = ExoContainerContext.getTopContainer();

    ExoContainerContext.setCurrentContainer(container);

    chain.doFilter(request, response);
  }

  public void destroy() {
  }
}
