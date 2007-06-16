/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 4:30:52 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import java.util.Enumeration;
import java.util.Vector;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletSessionUtil;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.map.AbstractMap;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.utils.PortletSessionImpUtil;

public class PortletSessionImp extends AbstractMap implements PortletSession {  

	private HttpSession session;
	private PortletContext context;
	private String windowId;
  private String applicationId ;
  private boolean invalidated;
  private Log log;
  protected ExoContainer cont;

  public PortletSessionImp(ExoContainer cont) {
    this.cont = cont;
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  }

	public void fillPortletSession(HttpSession session,
																 PortletContext context,
																 String windowId) {
		this.session = session;
		this.context = context;
		this.windowId = windowId;
    this.applicationId = context.getPortletContextName() ;         
    invalidated = false ;
	}    

	public void emptyPortletSession() {
		this.windowId = "";
	}

	public Object getAttribute(String name) {
		return getAttribute(name, PortletSession.PORTLET_SCOPE);
	}

	public Object getAttribute(String name, int i) {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null") ;
    }
		if (PortletSession.APPLICATION_SCOPE == i) {            
			return session.getAttribute(name);
		} else if (PortletSession.PORTLET_SCOPE == i) {
      String key = PortletSessionImpUtil.encodePortletSessionAttribute(windowId, name, PortletSession.PORTLET_SCOPE) ;      
			return session.getAttribute(key);
		}
		return null;
	}

	public void removeAttribute(String name) {
		removeAttribute(name, PortletSession.PORTLET_SCOPE);
	}

	public void removeAttribute(String name, int i) {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null") ;
    }
		if (PortletSession.APPLICATION_SCOPE == i) {      
			session.removeAttribute(name);
		} else if (PortletSession.PORTLET_SCOPE == i) {
      String key = PortletSessionImpUtil.encodePortletSessionAttribute(windowId, name, PortletSession.PORTLET_SCOPE) ;
			session.removeAttribute(key);
		}
	}

	final public void setAttribute(String name, Object o) {
    setAttribute(name, o, PortletSession.PORTLET_SCOPE);
	}

	public void setAttribute(String name, Object o, int i) {    
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null") ;
    }
		if (PortletSession.APPLICATION_SCOPE == i) {      
      if (o == null) session.removeAttribute(name);
      else session.setAttribute(name, o);
		} else if (PortletSession.PORTLET_SCOPE == i) {
      String key = PortletSessionImpUtil.encodePortletSessionAttribute(windowId, name, PortletSession.PORTLET_SCOPE) ;      
      if (o == null) session.removeAttribute(key);
      else session.setAttribute(key, o);
		}
	}  

  public Enumeration getAttributeNames() {    
    return getAttributeNames(PortletSession.PORTLET_SCOPE);
  }

  public Enumeration getAttributeNames(int i) {
    if(invalidated)
      throw new IllegalStateException("session invalidated");               
    Enumeration e = session.getAttributeNames();
    Vector v = new Vector();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      if(i == PortletSession.PORTLET_SCOPE){
        if (PortletSessionUtil.decodeScope(s) == PortletSession.PORTLET_SCOPE)
          v.add(PortletSessionUtil.decodeAttributeName(s));                         
      } else {
        if (PortletSessionUtil.decodeScope(s) == PortletSession.APPLICATION_SCOPE)
          v.add(s);
      }            
    }
    return v.elements();
  }
  

  public long getCreationTime() {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    return session.getCreationTime();
  }

  public String getId() {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    return session.getId() + applicationId;
  }

  public long getLastAccessedTime() {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    return session.getLastAccessedTime();
  }

  public int getMaxInactiveInterval() {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    return session.getMaxInactiveInterval();
  }

  public void invalidate() {    
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    session.invalidate();
    invalidated = true;
  }
  
  public boolean isSessionValid() {    
    try{    
      long lastAccessTime = session.getLastAccessedTime();        
      //tomcat 5    
      if(lastAccessTime == 0)
        return true;
      //tomcat 4  
      if(lastAccessTime == -1)
        return false;
      int maxInterval = session.getMaxInactiveInterval(); 
      if(maxInterval < 0)
        return true;                 
      if((System.currentTimeMillis() - lastAccessTime) > (maxInterval * 1000)){
        session.invalidate();              
        return false;            
      }
      return true;
    } catch(IllegalStateException e){
      log.error("IllegalStateException in PortletSessionImp for isSessionValid()", e);
      return false;
    }
  }
  
  public boolean isNew() {
    if(invalidated)
      throw new IllegalStateException("session invalidated");        
    return session.isNew();
  }
 
  public void setMaxInactiveInterval(int i) {
    if(invalidated)
      throw new IllegalStateException("session invalidated");    
    session.setMaxInactiveInterval(i);   
  }

	public PortletContext getPortletContext() {
		return context;
	}

  public HttpSession getSession() {
    return session;
  }

  public void setSession(HttpSession session) {
    this.session = session;
  }

}
