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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletSessionUtil;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.map.AbstractMap;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.utils.PortletSessionImpUtil;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 26, 2003 Time: 4:30:52 PM
 */
public class PortletSessionImp extends AbstractMap implements PortletSession {

  private HttpSession session;
  private final PortletContext context;
  private String windowId;
  private final String applicationId;
  private boolean invalidated;
  private final Log log;
  protected ExoContainer cont;

  public PortletSessionImp(final ExoContainer cont,
      final HttpSession session,
      final PortletContext context,
      final String windowId) {
    this.cont = cont;
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    this.session = session;
    this.context = context;
    this.windowId = windowId;
    this.applicationId = context.getPortletContextName();
    this.invalidated = this.session == null;
  }

  public Object getAttribute(final String name) {
    return getAttribute(name, PortletSession.PORTLET_SCOPE);
  }

  public Object getAttribute(final String name, final int scope) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    if (PortletSession.APPLICATION_SCOPE == scope)
      return session.getAttribute(name);
    else if (PortletSession.PORTLET_SCOPE == scope) {
      String key = PortletSessionImpUtil.encodePortletSessionAttribute(windowId, name,
          PortletSession.PORTLET_SCOPE);
      return session.getAttribute(key);
    }
    return null;
  }

  public void removeAttribute(final String name) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    removeAttribute(name, PortletSession.PORTLET_SCOPE);
  }

  public void removeAttribute(final String name, final int scope) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    if (PortletSession.APPLICATION_SCOPE == scope)
      session.removeAttribute(name);
    else if (PortletSession.PORTLET_SCOPE == scope) {
      String key = PortletSessionImpUtil.encodePortletSessionAttribute(windowId, name,
          PortletSession.PORTLET_SCOPE);
      session.removeAttribute(key);
    }
  }

  final public void setAttribute(final String name, final Object o) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    setAttribute(name, o, PortletSession.PORTLET_SCOPE);
  }

  public void setAttribute(final String name, final Object o, final int scope) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    if (PortletSession.APPLICATION_SCOPE == scope) {
      if (o == null)
        session.removeAttribute(name);
      else
        session.setAttribute(name, o);
    } else if (PortletSession.PORTLET_SCOPE == scope) {
      String key = PortletSessionImpUtil.encodePortletSessionAttribute(windowId, name,
          PortletSession.PORTLET_SCOPE);
      if (o == null)
        session.removeAttribute(key);
      else
        session.setAttribute(key, o);
    }
  }

  public Enumeration<String> getAttributeNames() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return getAttributeNames(PortletSession.PORTLET_SCOPE);
  }

  public Enumeration<String> getAttributeNames(final int scope) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    Enumeration<String> e = session.getAttributeNames();
    Vector<String> v = new Vector<String>();
    while (e.hasMoreElements()) {
      String s = e.nextElement();
      if (scope == PortletSession.PORTLET_SCOPE) {
        if (PortletSessionUtil.decodeScope(s) == PortletSession.PORTLET_SCOPE)
          v.add(PortletSessionUtil.decodeAttributeName(s));
      } else if (PortletSessionUtil.decodeScope(s) == PortletSession.APPLICATION_SCOPE)
        v.add(s);
    }
    return v.elements();
  }

  public Map getAttributeMap() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return getAttributeMap(PortletSession.PORTLET_SCOPE);
  }

  public Map getAttributeMap(final int scope) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    Enumeration e = session.getAttributeNames();
    Map m = new HashMap();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      if (scope == PortletSession.PORTLET_SCOPE) {
        if (PortletSessionUtil.decodeScope(s) == PortletSession.PORTLET_SCOPE) {
          String key = PortletSessionUtil.decodeAttributeName(s);
          m.put(key, session.getAttribute(key));
        }
      } else if (PortletSessionUtil.decodeScope(s) == PortletSession.APPLICATION_SCOPE)
        m.put(s, session.getAttribute(s));
    }
    return m;
  }

  public long getCreationTime() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getCreationTime();
  }

  public String getId() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getId() + applicationId;
  }

  public long getLastAccessedTime() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getLastAccessedTime();
  }

  public int getMaxInactiveInterval() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getMaxInactiveInterval();
  }

  public void invalidate() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    session.invalidate();
    invalidated = true;
  }

  public boolean isSessionValid() {
    if (session == null)
      return false;
    try {
      long lastAccessTime = session.getLastAccessedTime();
      // tomcat 5
      if (lastAccessTime == 0)
        return true;
      // tomcat 4
      if (lastAccessTime == -1)
        return false;
      int maxInterval = session.getMaxInactiveInterval();
      if (maxInterval < 0)
        return true;
      if ((System.currentTimeMillis() - lastAccessTime) > (maxInterval * 1000)) {
        session.invalidate();
        return false;
      }
      return true;
    } catch (IllegalStateException e) {
      log.error("IllegalStateException in PortletSessionImp for isSessionValid()", e);
      return false;
    }
  }

  public boolean isNew() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.isNew();
  }

  public void setMaxInactiveInterval(final int i) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    session.setMaxInactiveInterval(i);
  }

  public PortletContext getPortletContext() {
    return context;
  }

  public HttpSession getSession() {
    return session;
  }

  public void setSession(final HttpSession session, final String windowId) {
    this.session = session;
    this.invalidated = this.session == null;
    this.windowId = windowId;
  }

}
