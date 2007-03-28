/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.Constants;

/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Portlet.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Portlet {
	private List								description;
	private String							portletName;
	private List								displayName = new ArrayList() ;
	private String							portletClass;
	private List								initParam;
	private String        			expirationCache;
	private List								supports;
	private List								supportedLocale;
	private String							resourceBundle;
	private PortletInfo					portletInfo;
	private ExoPortletPreferences	portletPreferences;
	private List								securityRoleRef;
	//exo extension
	private List								filter;
	private String							globalCache;
	private String							id;

	public List getDescription() {
    if(description == null) return Constants.EMPTY_LIST ;  
		return description;
	}

  public String getDescription(String lang) {
    return Util.getDescription(lang, description) ;
  }
  
	public void setDescription(List description) {
		this.description = description;
	}
	
  public void addDescription(Description desc) {
    if(description == null) description = new ArrayList() ;
  	this.description.add(desc) ;
  }
  
	public List getDisplayName() {
		return displayName;
	}
  
  public void addDisplayName(DisplayName name) {
  	this.displayName.add(name) ;
  }

	public void setDisplayName(List displayName) {
		this.displayName = displayName;
	}

	public String getExpirationCache() {
		return expirationCache;
	}

	public void setExpirationCache(String expirationCache) {
		this.expirationCache = expirationCache;
	}

	public List getFilter() {
    if(filter == null) return Constants.EMPTY_LIST ;  
		return filter;
	}

	public void setFilter(List filter) {
		this.filter = filter;
	}

  public void addFilter(Filter f) {
   if(filter == null) filter = new ArrayList();  
   this.filter.add(f) ; 
  }
  
	public String getGlobalCache() {
		return globalCache;
	}

	public void setGlobalCache(String globalCache) {
		this.globalCache = globalCache;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getInitParam() {
    if(initParam == null) return Constants.EMPTY_LIST ;  
		return initParam;
	}

	public void setInitParam(List initParam) {
		this.initParam = initParam;
	}
  
  public void addInitParam(InitParam param) {
    if(initParam == null) initParam = new ArrayList() ;
  	this.initParam.add(param) ;
  }

	public String getPortletClass() {
		return portletClass;
	}

	public void setPortletClass(String portletClass) {
		this.portletClass = portletClass;
	}

	public PortletInfo getPortletInfo() {
		return portletInfo;
	}

	public void setPortletInfo(PortletInfo portletInfo) {
		this.portletInfo = portletInfo;
	}

	public String getPortletName() {
		return portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public ExoPortletPreferences getPortletPreferences() {
		return portletPreferences;
	}

	public void setPortletPreferences(ExoPortletPreferences portletPreferences) {
		this.portletPreferences = portletPreferences;
	}

	public String getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public List getSecurityRoleRef() {
    if(securityRoleRef == null) return Constants.EMPTY_LIST ;  
		return securityRoleRef;
	}

	public void setSecurityRoleRef(List securityRoleRef) {
		this.securityRoleRef = securityRoleRef;
	}
  
  public void addSecurityRoleRef(SecurityRoleRef ref) {
   if(securityRoleRef == null ) securityRoleRef = new ArrayList() ;
   this.securityRoleRef.add(ref) ; 
  }

	public List getSupportedLocale() {
    if(supportedLocale == null) return Constants.EMPTY_LIST ;  
		return supportedLocale;
	}

	public void setSupportedLocale(List supportedLocale) {
		this.supportedLocale = supportedLocale;
	}
	
  public void addSupportedLocale(String value) {
   if(supportedLocale == null) supportedLocale = new ArrayList() ;
   this.supportedLocale.add(value) ; 
  }
  
	public List getSupports() {
    if(supports == null) return Constants.EMPTY_LIST ;  
		return supports;
	}

	public void setSupports(List supports) {
		this.supports = supports;
	}
  
  public void addSupports(Supports s) {
    if(supports == null) this.supports = new ArrayList() ;
    this.supports.add(s) ;
  }
}