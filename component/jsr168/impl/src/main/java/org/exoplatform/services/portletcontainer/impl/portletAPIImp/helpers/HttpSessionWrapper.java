/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 1 d�c. 2003
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class HttpSessionWrapper implements HttpSession{
  
  private HttpSession session;

  public HttpSessionWrapper(HttpSession session){
    this.session = session;
  }  

  public long getCreationTime() {
    return session.getCreationTime();
  }

  public String getId() {
    return session.getId();
  }

  public long getLastAccessedTime() {
    return session.getLastAccessedTime();
  }

  public ServletContext getServletContext() {
    return session.getServletContext();
  }

  public void setMaxInactiveInterval(int arg0) {
    session.setMaxInactiveInterval(arg0);
  }

  public int getMaxInactiveInterval() {
    return session.getMaxInactiveInterval();
  }

  public HttpSessionContext getSessionContext() {
    return session.getSessionContext();
  }

  public Object getAttribute(String arg0) {
    return session.getAttribute(arg0);
  }

  public Object getValue(String arg0) {
    return session.getValue(arg0);
  }

  public Enumeration getAttributeNames() {
    return session.getAttributeNames();
  }

  public String[] getValueNames() {    
    return session.getValueNames();
  }

  public void setAttribute(String arg0, Object arg1) {    
    session.setAttribute(arg0, arg1);
  }

  public void putValue(String arg0, Object arg1) {  
    session.putValue(arg0, arg1);      
  }

  public void removeAttribute(String arg0) {
    session.removeAttribute(arg0);        
  }

  public void removeValue(String arg0) {
    session.removeValue(arg0);
  }

  public void invalidate() {
    session.invalidate();
  }

  public boolean isNew() {
    return session.isNew();
  }

  public HttpSession getSession() {
    return session;
  }

  public void setSession(HttpSession session) {
    this.session = session;
  }

}
