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
package org.exoplatform.services.portletcontainer.plugins.pc.replication;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * Fake http session.
 */
public class FakeHttpSession implements HttpSession {

  /**
   * Session id.
   */
  private String id;

  /**
   * Servlet context.
   */
  private ServletContext context;

  /**
   * @param sid session id
   */
  public FakeHttpSession(String sid) {
    this.id = sid;
  }

  /**
   * @param sid session id
   * @param context context
   */
  public FakeHttpSession(String sid, ServletContext context) {
    this.id = sid;
    this.context = context;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
   */
  public final Object getAttribute(String name) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return attribute names
   * @see javax.servlet.http.HttpSession#getAttributeNames()
   */
  public final Enumeration getAttributeNames() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return creation time
   * @see javax.servlet.http.HttpSession#getCreationTime()
   */
  public final long getCreationTime() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return id
   * @see javax.servlet.http.HttpSession#getId()
   */
  public final String getId() {
    return id;
  }

  /**
   * Overridden method.
   *
   * @return last access time
   * @see javax.servlet.http.HttpSession#getLastAccessedTime()
   */
  public final long getLastAccessedTime() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return max inactive interval
   * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
   */
  public final int getMaxInactiveInterval() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return servlet context
   * @see javax.servlet.http.HttpSession#getServletContext()
   */
  public final ServletContext getServletContext() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return session context
   * @see javax.servlet.http.HttpSession#getSessionContext()
   */
  public final HttpSessionContext getSessionContext() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
   */
  public final Object getValue(String name) {
    return null;
  }

  /**
   * Overridden method.
   *
   * @return value names
   * @see javax.servlet.http.HttpSession#getValueNames()
   */
  public final String[] getValueNames() {
    return null;
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.http.HttpSession#invalidate()
   */
  public final void invalidate() {
  }

  /**
   * Overridden method.
   *
   * @return is new
   * @see javax.servlet.http.HttpSession#isNew()
   */
  public final boolean isNew() {
    return false;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
   */
  public final void putValue(String name, Object value) {
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
   */
  public final void removeAttribute(String name) {
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
   */
  public final void removeValue(String name) {
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
   */
  public final void setAttribute(String name, Object value) {
  }

  /**
   * Overridden method.
   *
   * @param max max inactive interval
   * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
   */
  public final void setMaxInactiveInterval(int max) {
  }

}
