/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 9 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

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
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpSession implements HttpSession {
  
  private String sessionID;
  private Map attributsMap = new HashMap();
  private long creationTime;
  private long lastAccessTime;
  private int maxInactiveInterval = 900;
  private boolean isNew = false;
  private boolean invalidated = false;
  
  public WSRPHttpSession(String sessionID, int maxInactiveInterval) {
    creationTime = System.currentTimeMillis();
    this.sessionID = sessionID;    
    this.maxInactiveInterval = maxInactiveInterval;
    isNew = true;
  }  

  public long getCreationTime() {
    if(invalidated)
      throw new IllegalStateException();    
    return creationTime;
  }

  public String getId() {
    if(invalidated)
      throw new IllegalStateException();    
    return sessionID;
  }

  public long getLastAccessedTime() {
    if(invalidated)
      throw new IllegalStateException();    
    return lastAccessTime;
  }
  
  public void setLastAccessTime(long lastAccessTime){
    this.lastAccessTime = lastAccessTime;
  }

  public ServletContext getServletContext() {
    if(invalidated)
      throw new IllegalStateException();    
    return null;
  }

  public void setMaxInactiveInterval(int arg0) {
    if(invalidated)
      throw new IllegalStateException();    
    maxInactiveInterval = arg0;
  }

  public int getMaxInactiveInterval() {
    if(invalidated)
      throw new IllegalStateException();    
    return maxInactiveInterval;
  }

  public Object getAttribute(String arg0) {
    if(invalidated)
      throw new IllegalStateException();    
    return attributsMap.get(arg0);
  }

  public Enumeration getAttributeNames() {
    if(invalidated)
      throw new IllegalStateException();    
    return Collections.enumeration(attributsMap.keySet());
  }

  public void setAttribute(String arg0, Object arg1) {
    if(invalidated)
      throw new IllegalStateException();    
    attributsMap.put(arg0, arg1);
  }

  public void removeAttribute(String arg0) {
    if(invalidated)
      throw new IllegalStateException();    
    attributsMap.remove(arg0);
  }

  public void invalidate() {
    Set keys = attributsMap.entrySet();
    for (Iterator iter = keys.iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      attributsMap.remove(key);
    }     
    invalidated = true;    
  }
  
  public boolean isInvalidated(){
    return invalidated;
  }

  public boolean isNew() {
    if(invalidated)
      throw new IllegalStateException();
    return false;
  }
  
  public void setNew(boolean isNew){
    this.isNew = isNew;
  }
  
  //deprecated methods
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
