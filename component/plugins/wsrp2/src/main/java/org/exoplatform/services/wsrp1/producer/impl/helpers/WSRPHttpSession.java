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

package org.exoplatform.services.wsrp1.producer.impl.helpers;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpSession implements HttpSession {

  private String              sessionID;

  private Map<String, Object> attributsMap        = new HashMap<String, Object>();

  private long                creationTime;

  private long                lastAccessTime;

  private int                 maxInactiveInterval = 900;

  private boolean             isNew               = false;

  private boolean             invalidated         = false;

  public WSRPHttpSession() {
  }

  public WSRPHttpSession(HttpSession httpSession) {
    this.creationTime = System.currentTimeMillis();
    this.sessionID = httpSession.getId();
    this.maxInactiveInterval = httpSession.getMaxInactiveInterval();
    this.isNew = true;
  }

  public WSRPHttpSession(String sessionID, int maxInactiveInterval) {
    creationTime = System.currentTimeMillis();
    this.sessionID = sessionID;
    this.maxInactiveInterval = maxInactiveInterval;
    isNew = true;
  }

  public long getCreationTime() {
    if (invalidated)
      throw new IllegalStateException();
    return creationTime;
  }

  public String getId() {
    if (invalidated)
      throw new IllegalStateException();
    return sessionID;
  }

  public long getLastAccessedTime() {
    if (invalidated)
      throw new IllegalStateException();
    return lastAccessTime;
  }

  public void setLastAccessTime(long lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public ServletContext getServletContext() {
    if (invalidated)
      throw new IllegalStateException();
    return null;
  }

  public void setMaxInactiveInterval(int arg0) {
    if (invalidated)
      throw new IllegalStateException();
    maxInactiveInterval = arg0;
  }

  public int getMaxInactiveInterval() {
    if (invalidated)
      throw new IllegalStateException();
    return maxInactiveInterval;
  }

  public Object getAttribute(String arg0) {
    if (invalidated)
      throw new IllegalStateException();
    return attributsMap.get(arg0);
  }

  public Enumeration<String> getAttributeNames() {
    if (invalidated)
      throw new IllegalStateException();
    return Collections.enumeration(attributsMap.keySet());
  }

  public void setAttribute(String arg0, Object arg1) {
    if (invalidated)
      throw new IllegalStateException();
    attributsMap.put(arg0, arg1);
  }

  public void removeAttribute(String arg0) {
    if (invalidated)
      throw new IllegalStateException();
    attributsMap.remove(arg0);
  }

  public void invalidate() {
    Set<String> keys = attributsMap.keySet();
    for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      attributsMap.remove(key);
    }
    invalidated = true;
  }

  public boolean isInvalidated() {
    return invalidated;
  }

  public boolean isNew() {
    if (invalidated)
      throw new IllegalStateException();
    return false;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public void removeValue(String arg0) {
  }

  public void putValue(String arg0, Object arg1) {
  }

  public String[] getValueNames() {
    return null;
  }

  public Object getValue(String arg0) {
    return null;
  }

  public HttpSessionContext getSessionContext() {
    return null;
  }

}
