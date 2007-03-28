/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 1 d�c. 2003
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class SharedSessionWrapper extends HttpSessionWrapper{
  
  private static String CREATION_TIME = "javax.portlet.creation-time";
  private static String LAST_ACCESS_TIME = "javax.portlet.last-access-time";
  private static String MAXIMUM_INACTIVE_INTERVAL = "javax.portlet.max-inactive-interval";
  
  private static int defaultMaximalInterval = 900;//in seconds  

  private String applicationId;

  public SharedSessionWrapper(HttpSession session) {
    super(session);    
  }
  
  public void fillSharedSessionWrapper(HttpSession session,
                                       String applicationId) {
    super.setSession(session);
    this.applicationId = applicationId;
  }

  public void emptySharedSessionWrapper() {    
    this.applicationId = null;
  }  
  
  public void init(){
    //look if the associated session has already been created
    String attCreation = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId, CREATION_TIME);
    String attLast = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId, LAST_ACCESS_TIME);      
    if (super.getAttribute(attCreation) == null) {
      String maxInterval = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId, MAXIMUM_INACTIVE_INTERVAL);
      super.setAttribute(attCreation, new Long(System.currentTimeMillis()));
      super.setAttribute(attLast, new Long(System.currentTimeMillis()));
      super.setAttribute(maxInterval, new Integer(defaultMaximalInterval));
    }
//    } else {//update some info about access times
//      super.setAttribute(attLast, new Long(System.currentTimeMillis()));
//    }
  }  
    
  public Object getAttribute(String name) {    
    setLastAccessTime();    
    String key = SharedSessionUtil.encodePortletSessionAttribute(applicationId, name);
    Object value = super.getAttribute(key);
    if(value instanceof HttpSessionBindingListenerProxy){
      return((HttpSessionBindingListenerProxy)value).getListener();      
    }        
    return value;
  }

  public Enumeration getAttributeNames() {  
    setLastAccessTime();
    Enumeration e = super.getAttributeNames();
    Vector v = new Vector();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      if (!SharedSessionUtil.isMetaDataAttribute(applicationId, s)
          && SharedSessionUtil.isAttribute(applicationId, s)) {
        s = SharedSessionUtil.decodePortletSessionAttribute(applicationId, s);       
        v.add(s);        
      }
    }
    return v.elements();    
  }

  public void removeAttribute(String name) {
    String key = SharedSessionUtil.encodePortletSessionAttribute(applicationId, name);
    if(super.getAttribute(key) instanceof HttpSessionBindingListenerProxy){
      HttpSessionBindingListener listener = ((HttpSessionBindingListenerProxy)super.getAttribute(key)).getListener();
      listener.valueUnbound(new HttpSessionBindingEvent(this, name));
    }    
    super.removeAttribute(key);
    setLastAccessTime();    
  }

  public void removeValue(String name) {    
  }

  public void setAttribute(String name, Object value) {
    if(value instanceof HttpSessionBindingListener){
      HttpSessionBindingListener listener = (HttpSessionBindingListener)value;
      listener.valueBound(new HttpSessionBindingEvent(this, name));
      value = new HttpSessionBindingListenerProxy(listener);
    }
    String key = SharedSessionUtil.encodePortletSessionAttribute(applicationId, name);    
    super.setAttribute(key, value);    
    setLastAccessTime();
  }

  public long getCreationTime() {
    setLastAccessTime();
    String key = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId,
                                                                         CREATION_TIME);
    Long creationTime = (Long) super.getAttribute(key);
    return creationTime.longValue();        
  }
  
  public int getMaxInactiveInterval() {
    setLastAccessTime();    
    String key = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId,
                                                                         MAXIMUM_INACTIVE_INTERVAL);
    Integer maxInactivInterval = (Integer) super.getAttribute(key);
    return maxInactivInterval.intValue();
  }  
  
  public void invalidate() {        
    Enumeration e = super.getAttributeNames();
    ArrayList list = new ArrayList(15) ;
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      if (SharedSessionUtil.isMetaDataAttribute(applicationId, s)
          || SharedSessionUtil.isAttribute(applicationId, s)) {
        list.add(s);
      }
    }
    for (int i = 0 ; i < list.size(); i++) {
      String name = (String) list.get(i) ;
      super.removeAttribute(name);
    }        
  }
  
  public boolean isNew() {
    setLastAccessTime();        
    return false;
  }  
  
  public void setMaxInactiveInterval(int i) {
    String key = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId,
                                                                         MAXIMUM_INACTIVE_INTERVAL);    
    super.setAttribute(key, new Integer(i));
    setLastAccessTime();
  }  
  
  private void setLastAccessTime(){
    String attLast = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId, 
                                                                             LAST_ACCESS_TIME);
    super.setAttribute(attLast, new Long(System.currentTimeMillis()));        
  }
  
  public long getLastAccessedTime() {    
    String key = SharedSessionUtil.encodePortletSessionMetaDataAttribute(applicationId,
                                                                         LAST_ACCESS_TIME);
    Long lastAccessTime = (Long) super.getAttribute(key);   
    if(lastAccessTime == null)
      return -1;   
    return lastAccessTime.longValue();
  }    

}
