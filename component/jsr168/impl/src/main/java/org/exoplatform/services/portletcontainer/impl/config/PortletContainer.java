/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.config;

import java.util.* ;
/**
 * Jul 7, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletContainer.java,v 1.2 2004/10/26 18:47:54 benjmestrallet Exp $
 */
public class PortletContainer {
	private Global global ;
	private SharedSession sharedSession ;
	private Cache cache ;
	private List supportedContent ;
	private List customMode ;
	private List customWindowState ;
	private List properties ;

  private DelegatedBundle bundle;
	
  public PortletContainer() {
    supportedContent  = new ArrayList() ;
    customMode = new ArrayList() ;
    customWindowState = new ArrayList() ;
    properties = new ArrayList() ;
  }
    
	public Cache getCache() {		return cache;	}
	public void setCache(Cache cache) { this.cache = cache;	}
    
	public List getCustomMode() {return customMode;	}
	public void setCustomMode(List list) { this.customMode = list;}
  public void addCustomMode(CustomMode mode) {customMode.add(mode);}
    
	public List getCustomWindowState() {return customWindowState;	}
	public void setCustomWindowState(List list) { customWindowState = list;	}
  public void addCustomWindowState(CustomWindowState state) { 
    customWindowState.add(state); 
  }
    
	public Global getGlobal() {	return global; }
	public void setGlobal(Global global) { this.global = global;}
    
	public List getProperties() {	return properties;}
	public void setProperties(List list) {this.properties = list;	}
  public void addProperties(Properties props) {properties.add(props) ; }
    
	public SharedSession getSharedSession() {	return sharedSession;	}
	public void setSharedSession(SharedSession ss) { sharedSession = ss;	}

  
  public void setDelegatedBundle(DelegatedBundle bundle) {
    this.bundle = bundle;
  }
  public DelegatedBundle getDelegatedBundle() {
    return bundle;
  }
  
	public List getSupportedContent() {	return supportedContent;}
	public void setSupportedContent(List list) {supportedContent = list;	}
  public void addSupportedContent(SupportedContent supported) {
    supportedContent.add(supported);
  }

}