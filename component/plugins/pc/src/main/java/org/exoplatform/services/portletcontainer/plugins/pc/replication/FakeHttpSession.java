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

public class FakeHttpSession implements HttpSession {

  private String id;
  private ServletContext context;

  public FakeHttpSession(String sid){
    this.id =  sid;
  }

  public FakeHttpSession(String sid, ServletContext context){
    this.id =  sid;
    this.context = context;
  }

  public Object getAttribute(String arg0) {
    return null;
  }

  public Enumeration getAttributeNames() {
    return null;
  }

  public long getCreationTime() {
    return 0;
  }

  public String getId() {
    return id;
  }

  public long getLastAccessedTime() {
    return 0;
  }

  public int getMaxInactiveInterval() {
    return 0;
  }

  public ServletContext getServletContext() {
    return null;
  }

  public HttpSessionContext getSessionContext() {
    return null;
  }

  public Object getValue(String arg0) {
    return null;
  }

  public String[] getValueNames() {
    return null;
  }

  public void invalidate() {
  }

  public boolean isNew() {
    return false;
  }

  public void putValue(String arg0, Object arg1) {
  }

  public void removeAttribute(String arg0) {
  }

  public void removeValue(String arg0) {
  }

  public void setAttribute(String arg0, Object arg1) {
  }

  public void setMaxInactiveInterval(int arg0) {
  }

}
