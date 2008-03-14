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
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 4:30:52 PM
 */
public class PortletSessionImp extends AbstractMap implements PortletSession {

  /**
   * Http session.
   */
  private HttpSession session;

  /**
   * Portlet context.
   */
  private final PortletContext context;

  /**
   * Window id.
   */
  private String windowId;

  /**
   * Application id.
   */
  private final String applicationId;

  /**
   * Is session invaludated.
   */
  private boolean invalidated;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Exo container.
   */
  protected ExoContainer cont;

  /**
   * @param cont exo container
   * @param session http session
   * @param context portlet context
   * @param windowId window id
   */
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

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see org.exoplatform.commons.map.AbstractMap#getAttribute(java.lang.String)
   */
  public final Object getAttribute(final String name) {
    return getAttribute(name, PortletSession.PORTLET_SCOPE);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param scope scope
   * @return value
   * @see javax.portlet.PortletSession#getAttribute(java.lang.String, int)
   */
  public final Object getAttribute(final String name, final int scope) {
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

  /**
   * Overridden method.
   *
   * @param name name
   * @see org.exoplatform.commons.map.AbstractMap#removeAttribute(java.lang.String)
   */
  public final void removeAttribute(final String name) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    removeAttribute(name, PortletSession.PORTLET_SCOPE);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param scope scope
   * @see javax.portlet.PortletSession#removeAttribute(java.lang.String, int)
   */
  public final void removeAttribute(final String name, final int scope) {
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

  /**
   * Overridden method.
   *
   * @param name name
   * @param o value
   * @see org.exoplatform.commons.map.AbstractMap#setAttribute(java.lang.String, java.lang.Object)
   */
  public final void setAttribute(final String name, final Object o) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    setAttribute(name, o, PortletSession.PORTLET_SCOPE);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param o value
   * @param scope scope
   * @see javax.portlet.PortletSession#setAttribute(java.lang.String, java.lang.Object, int)
   */
  public final void setAttribute(final String name, final Object o, final int scope) {
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

  /**
   * Overridden method.
   *
   * @return attribute names
   * @see org.exoplatform.commons.map.AbstractMap#getAttributeNames()
   */
  public final Enumeration<String> getAttributeNames() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return getAttributeNames(PortletSession.PORTLET_SCOPE);
  }

  /**
   * Overridden method.
   *
   * @param scope scope
   * @return attribute names
   * @see javax.portlet.PortletSession#getAttributeNames(int)
   */
  public final Enumeration<String> getAttributeNames(final int scope) {
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

  /**
   * Overridden method.
   *
   * @return attribute map
   * @see javax.portlet.PortletSession#getAttributeMap()
   */
  public final Map getAttributeMap() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return getAttributeMap(PortletSession.PORTLET_SCOPE);
  }

  /**
   * Overridden method.
   *
   * @param scope scope
   * @return attribute map
   * @see javax.portlet.PortletSession#getAttributeMap(int)
   */
  public final Map getAttributeMap(final int scope) {
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

  /**
   * Overridden method.
   *
   * @return creation time
   * @see javax.portlet.PortletSession#getCreationTime()
   */
  public final long getCreationTime() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getCreationTime();
  }

  /**
   * Overridden method.
   *
   * @return id
   * @see javax.portlet.PortletSession#getId()
   */
  public final String getId() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getId() + applicationId;
  }

  /**
   * Overridden method.
   *
   * @return last accessed time
   * @see javax.portlet.PortletSession#getLastAccessedTime()
   */
  public final long getLastAccessedTime() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getLastAccessedTime();
  }

  /**
   * Overridden method.
   *
   * @return max inactive interval
   * @see javax.portlet.PortletSession#getMaxInactiveInterval()
   */
  public final int getMaxInactiveInterval() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.getMaxInactiveInterval();
  }

  /**
   * Overridden method.
   *
   * @see javax.portlet.PortletSession#invalidate()
   */
  public final void invalidate() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    session.invalidate();
    invalidated = true;
  }

  /**
   * @return is session valid
   */
  public final boolean isSessionValid() {
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

  /**
   * Overridden method.
   *
   * @return is new
   * @see javax.portlet.PortletSession#isNew()
   */
  public final boolean isNew() {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    return session.isNew();
  }

  /**
   * Overridden method.
   *
   * @param i max inactive interval
   * @see javax.portlet.PortletSession#setMaxInactiveInterval(int)
   */
  public final void setMaxInactiveInterval(final int i) {
    if (invalidated)
      throw new IllegalStateException("session invalidated");
    session.setMaxInactiveInterval(i);
  }

  /**
   * Overridden method.
   *
   * @return portlet context
   * @see javax.portlet.PortletSession#getPortletContext()
   */
  public final PortletContext getPortletContext() {
    return context;
  }

  /**
   * @return session
   */
  public final HttpSession getSession() {
    return session;
  }

  /**
   * @param session session
   * @param windowId window id
   */
  public final void setSession(final HttpSession session, final String windowId) {
    this.session = session;
    this.invalidated = this.session == null;
    this.windowId = windowId;
  }

}
