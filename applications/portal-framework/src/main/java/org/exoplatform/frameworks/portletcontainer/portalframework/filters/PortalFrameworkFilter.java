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
package org.exoplatform.frameworks.portletcontainer.portalframework.filters;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortalFramework;
import org.exoplatform.frameworks.portletcontainer.portalframework.replication.SessionReplicator;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: PortletFilter.java 8554 2006-09-04 15:28:35Z sunman $
 */

/**
 * PortletFilter class does portal's work using portal-framework it processes
 * user http requests and invokes portlets.
 */
public class PortalFrameworkFilter implements Filter {

  /**
   * Frameworks. One per http session.
   */
  public static final HashMap<String, PortalFramework> FRAMEWORKS        = new HashMap<String, PortalFramework>();

  /**
   * Session replicator instance.
   */
  private SessionReplicator                            sessionReplicator = null;
  
  
  /**
   * Session replicator option.
   */
  private boolean                            isReplicationEnabled = true;


  /**
   * Does nothing.
   * 
   * @param filterConfig filter config
   */
  public void init(final FilterConfig filterConfig) {
  }

  /**
   * Actual request processing.
   * 
   * @param servletRequest servlet request
   * @param servletResponse servlet respnse
   * @param filterChain filter chain
   * @throws IOException something may go wrong
   * @throws ServletException something may go wrong
   */
  public synchronized void doFilter(ServletRequest servletRequest,
                                    ServletResponse servletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpSession httpSession = httpRequest.getSession();

    PortalFramework framework = null;

    ExoContainer container = null;
    try {
      container = ExoContainerContext.getTopContainer();
      // create WindowInfosContainer instance if there's no one
      WindowInfosContainer.createInstance(container,
                                          httpSession.getId(),
                                          httpRequest.getRemoteUser());

      // create/get PortalFramework instance
      framework = FRAMEWORKS.get(httpSession.getId());
      if (framework == null) {
        framework = new PortalFramework(container);
        FRAMEWORKS.put(httpSession.getId(), framework);
      }
      PortalFramework.setInstance(framework);
      framework.init(httpSession);

    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);

    // Session Replication
    try {
      if (sessionReplicator == null && isReplicationEnabled) {
        sessionReplicator = (SessionReplicator) container.getComponentInstanceOfType(SessionReplicator.class);
        if (sessionReplicator == null){
          isReplicationEnabled = false;
          return;
        }
      sessionReplicator.send(httpSession.getId(),
                             framework.getPortalName(),
                             framework.getRenderedPortletInfos());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Does nothing.
   */
  public void destroy() {
  }

}
