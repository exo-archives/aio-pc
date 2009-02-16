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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext; //The type HttpSessionContext is deprecated

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class HttpSessionWrapper implements HttpSession {

  /**
   * Session.
   */
  private HttpSession session;

  /**
   * @param session session
   */
  public HttpSessionWrapper(final HttpSession session) {
    this.session = session;
  }

  /**
   * Overridden method.
   *
   * @return creation time
   * @see javax.servlet.http.HttpSession#getCreationTime()
   */
  public final long getCreationTime() {
    return session.getCreationTime();
  }

  /**
   * Overridden method.
   *
   * @return session id
   * @see javax.servlet.http.HttpSession#getId()
   */
  public final String getId() {
    return session.getId();
  }

  /**
   * Overridden method.
   *
   * @return last access time
   * @see javax.servlet.http.HttpSession#getLastAccessedTime()
   */
  public final long getLastAccessedTime() {
    return session.getLastAccessedTime();
  }

  /**
   * Overridden method.
   *
   * @return servlet context
   * @see javax.servlet.http.HttpSession#getServletContext()
   */
  public final ServletContext getServletContext() {
    return session.getServletContext();
  }

  /**
   * Overridden method.
   *
   * @param max max inactive interval
   * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
   */
  public final void setMaxInactiveInterval(final int max) {
    session.setMaxInactiveInterval(max);
  }

  /**
   * Overridden method.
   *
   * @return max inactive interval
   * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
   */
  public final int getMaxInactiveInterval() {
    return session.getMaxInactiveInterval();
  }

  /**
   * Overridden method.
   *
   * @return session context
   * @see javax.servlet.http.HttpSession#getSessionContext()
   */
  public final HttpSessionContext getSessionContext() {
    return session.getSessionContext();
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
   */
  public final Object getAttribute(final String name) {
    return session.getAttribute(name);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
   */
  public final Object getValue(final String name) {
    return session.getValue(name);
  }

  /**
   * Overridden method.
   *
   * @return attribute names
   * @see javax.servlet.http.HttpSession#getAttributeNames()
   */
  public final Enumeration getAttributeNames() {
    return session.getAttributeNames();
  }

  /**
   * Overridden method.
   *
   * @return value names
   * @see javax.servlet.http.HttpSession#getValueNames()
   */
  public final String[] getValueNames() {
    return session.getValueNames();
  }

  /**
   * Overridden method.
   *
   * @param name
   * @param value
   * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
   */
  public final void setAttribute(final String name, final Object value) {
    session.setAttribute(name, value);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
   */
  public final void putValue(final String name, final Object value) {
    session.putValue(name, value);
  }

  /**
   * Overridden method.
   *
   * @param name
   * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
   */
  public final void removeAttribute(final String name) {
    session.removeAttribute(name);
  }

  /**
   * Overridden method.
   *
   * @param name
   * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
   */
  public final void removeValue(final String name) {
    session.removeValue(name);
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.http.HttpSession#invalidate()
   */
  public final void invalidate() {
    session.invalidate();
  }

  /**
   * Overridden method.
   *
   * @return is session new
   * @see javax.servlet.http.HttpSession#isNew()
   */
  public final boolean isNew() {
    return session.isNew();
  }

  /**
   * @return session
   */
  public final HttpSession getSession() {
    return session;
  }

  /**
   * @param session session
   */
  public final void setSession(final HttpSession session) {
    this.session = session;
  }

}
