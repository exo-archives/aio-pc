/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 1 d�c. 2003
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class EmptySession implements HttpSession{

  public long getCreationTime() {
    return 0;
  }

  public String getId() {
    return null;
  }

  public long getLastAccessedTime() {
    return 0;
  }

  public ServletContext getServletContext() {
    return null;
  }

  public void setMaxInactiveInterval(int arg0) {
  }

  public int getMaxInactiveInterval() {
    return 0;
  }

  public HttpSessionContext getSessionContext() {
    return null;
  }

  public Object getAttribute(String arg0) {
    return null;
  }

  public Object getValue(String arg0) {
    return null;
  }

  public Enumeration getAttributeNames() {
    return null;
  }

  public String[] getValueNames() {
    return null;
  }

  public void setAttribute(String arg0, Object arg1) {
  }

  public void putValue(String arg0, Object arg1) {
  }

  public void removeAttribute(String arg0) {
  }

  public void removeValue(String arg0) {
  }

  public void invalidate() {
  }

  public boolean isNew() {
    return false;
  }

}
