/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.services.portletcontainer.plugins.pc.aop;

import org.exoplatform.container.component.ExecutionContext;
import org.exoplatform.services.log.ExoLogger;
import org.apache.commons.logging.Log;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import java.util.Collections;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PortletSessionIdentityScopingCommand extends BaseCommandUnit {

  /** . */
  private static final Log log = ExoLogger.getLogger(PortletSessionIdentityScopingCommand.class);

  /** . */
  private static final String IDENTITY_TOKEN = "javax.portlet.identity.token";

  @Override
  public Object execute(ExecutionContext context) throws Throwable {
    
    Method getRequest;
    PortletRequest request;
    try {
      getRequest = context.getClass().getDeclaredMethod("getRequest");
      getRequest.setAccessible(true);
      request = (PortletRequest) getRequest.invoke(context);
    } catch (Exception e) {
      throw new IllegalStateException("Error invoking method getRequest() from class " + context.getClass() + " -- " + e.toString());
    }

      // We use the remote user value as identity
      String portalIdentity = request.getRemoteUser();

      //
      check(request, portalIdentity);

      //
      Object o;
      try {
        o = super.execute(context);
      } finally {
        update(request, portalIdentity);
      }
      return o;

    

  }

  private void update(PortletRequest request, String portalIdentity) {
    boolean trace = log.isTraceEnabled();
    String contextPath = request.getContextPath();
    PortletSession session = request.getPortletSession(false);
    if (session != null) {
      String id = session.getId();
      String sessionIdentity = (String)session.getAttribute(IDENTITY_TOKEN, PortletSession.APPLICATION_SCOPE);
      if (portalIdentity != null) {
        if (!portalIdentity.equals(sessionIdentity)) {
          if (trace) {
            log.trace("Updating portlet session " + id + " (" + contextPath + ") from " + sessionIdentity + " to " + portalIdentity);
          }

          //
          session.setAttribute(IDENTITY_TOKEN, portalIdentity, PortletSession.APPLICATION_SCOPE);
        }
      } else {
        if (sessionIdentity != null) {
          if (trace) {
            log.trace("Updating portlet session " + id + " (" + contextPath + ") by removing the " + sessionIdentity + " value");
          }

          //
          session.removeAttribute(IDENTITY_TOKEN, PortletSession.APPLICATION_SCOPE);
        }
      }
    }
  }

  private void check(PortletRequest request, String portalIdentity) {
    boolean trace = log.isTraceEnabled();
    String contextPath = request.getContextPath();
    PortletSession session = request.getPortletSession(false);
    if (session != null) {
      String id = session.getId();
      String sessionIdentity = (String)session.getAttribute(IDENTITY_TOKEN, PortletSession.APPLICATION_SCOPE);

      //
      if (portalIdentity == null) {
        if (sessionIdentity != null) {
          // It means that user is anonymous and the portlet session is still associated to a previous identity
          if (trace) {
            log.trace("Detected user logout for session " + id + " (" + contextPath + ")");
          }

          purge(session);
        }
      } else {
        if (sessionIdentity != null && !sessionIdentity.equals(portalIdentity)) {
          // It means that we don't have the same identity in portal and portlet session
          if (trace) {
            log.trace("Detected different user for session " + id + " (" + contextPath + ")");
          }

          purge(session);
        }
      }
    }
  }

  private void purge(PortletSession session) {
    for (String name : Collections.list(session.getAttributeNames(PortletSession.APPLICATION_SCOPE))) {
      session.removeAttribute(name, PortletSession.APPLICATION_SCOPE);
    }
  }
}