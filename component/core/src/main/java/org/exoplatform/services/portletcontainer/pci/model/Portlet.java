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
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.portlet.filter.FilterChain;
import javax.portlet.PortletSession;
import javax.portlet.PortletURLGenerationListener;

import org.exoplatform.services.portletcontainer.PortletContainerConstants;
import org.exoplatform.Constants;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Jul 11, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Portlet.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Portlet {
	private List<Description>			description;
	private String							  portletName;
	private List<DisplayName>			displayName = new ArrayList<DisplayName>() ;
	private String							  portletClass;
	private List<InitParam>			  initParam;
	private Integer        			  caching;
	private List<Supports>        supports;
	private List<Locale>				  supportedLocale;
	private String							  resourceBundle;
	private PortletInfo					  portletInfo;
	private ExoPortletPreferences	portletPreferences;
	private List<SecurityRoleRef>	securityRoleRef;
	// portlet api 2.0
	private String							  id;
  private List<QName>           supportedProcessingEvent;
  private List<QName>           supportedPublishingEvent;
  private List<String>          supportedPublicRenderParameter;
  private List<String>          urlGenerationListener;
  private Map<String,String[]>  containerRuntimeOption;
  private List<SharedSessionAttribute> sharedSessionAttribute;
  private FilterChain           filterChain = null;
  private PortletApp            application;
	//exo extension
	private String                globalCache;
	private Boolean               escapeXml;// = true;
  private List<PortletURLGenerationListener> urlListeners;

  public PortletApp getApplication() {
    return application;
  }

  public void setApplication(PortletApp app) {
    application = app;
  }

  public void setEscapeXml(Boolean escapeXml) {
    this.escapeXml = escapeXml;
  }

  public void setEscapeXml(boolean escapeXml) {
    this.escapeXml = new Boolean(escapeXml);
  }

  public Boolean getEscapeXml() {
    if (escapeXml != null)
      return escapeXml;
    if (getContainerRuntimeOption() != null) {
      String[] valuesPortlet = getContainerRuntimeOption().get("javax.portlet.escapeXml");
      if (valuesPortlet != null)
        return new Boolean(valuesPortlet[0]);
    }
    String[] valuesApplication = null;
    if (application != null) {
      if (application.getContainerRuntimeOption() != null ) {
        valuesApplication = (String[])application.getContainerRuntimeOption().get("javax.portlet.escapeXml");
      }
    }
    if (valuesApplication != null)
      return new Boolean(valuesApplication[0]);
    return Boolean.TRUE;
  }

  public int getPortletSessionScope() {
    if (getContainerRuntimeOption() != null) {
      String[] valuesPortlet = getContainerRuntimeOption().get("javax.portlet.includedPortletSessionScope");
      if (valuesPortlet != null && valuesPortlet[0].equals(PortletContainerConstants.PORTLET_SCOPE))
        return PortletSession.PORTLET_SCOPE;
    }
    String[] valuesApplication = (String[])application.getContainerRuntimeOption().get("javax.portlet.includedPortletSessionScope");
    if (valuesApplication != null && valuesApplication[0].equals(PortletContainerConstants.PORTLET_SCOPE))
        return PortletSession.PORTLET_SCOPE;

    return PortletSession.APPLICATION_SCOPE;
  }

  public FilterChain getFilterChain() {
    return filterChain;
  }

  public void setFilterChain(FilterChain filterChain) {
    this.filterChain = filterChain;
  }

  public List<PortletURLGenerationListener> getUrlListeners() {
    return urlListeners;
  }

  public void setUrlListeners(List<PortletURLGenerationListener> urlListeners) {
    this.urlListeners = urlListeners;
  }

	public List<Description> getDescription() {
    if (description == null) return Constants.EMPTY_LIST ;
		return description;
	}

  public String getDescription(String lang) {
    return Util.getDescription(lang, description) ;
  }

	public void setDescription(List<Description> description) {
		this.description = description;
	}

  public void addDescription(Description desc) {
    if (description == null) description = new ArrayList<Description>() ;
  	this.description.add(desc) ;
  }

	public List<DisplayName> getDisplayName() {
		return displayName;
	}

  public void addDisplayName(DisplayName name) {
  	this.displayName.add(name) ;
  }

	public void setDisplayName(List<DisplayName> displayName) {
		this.displayName = displayName;
	}

	public Integer getCaching() {
		return caching;
	}

	public void setCaching(Integer caching) {
		this.caching = caching;
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

	public List<InitParam> getInitParam() {
    if (initParam == null) return Constants.EMPTY_LIST ;
		return initParam;
	}

	public void setInitParam(List<InitParam> initParam) {
		this.initParam = initParam;
	}

  public void addInitParam(InitParam param) {
    if (initParam == null) initParam = new ArrayList<InitParam>() ;

    if (initParam.contains(param)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"init-param\" in portlet description");
    } else {
      this.initParam.add(param);
    }
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

	public List<SecurityRoleRef> getSecurityRoleRef() {
    if (securityRoleRef == null) return Constants.EMPTY_LIST ;
		return securityRoleRef;
	}

	public void setSecurityRoleRef(List<SecurityRoleRef> securityRoleRef) {
		this.securityRoleRef = securityRoleRef;
	}

  public void addSecurityRoleRef(SecurityRoleRef ref) {
    if (securityRoleRef == null) securityRoleRef = new ArrayList<SecurityRoleRef>() ;

    if (securityRoleRef.contains(ref)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"security-role-ref\" in portlet description");
    } else {
      this.securityRoleRef.add(ref) ;
    }
  }

	public List<Locale> getSupportedLocale() {
    if (supportedLocale == null) return Constants.EMPTY_LIST ;
		return supportedLocale;
	}

	public void setSupportedLocale(List<Locale> supportedLocale) {
		this.supportedLocale = supportedLocale;
	}

  public void addSupportedLocale(Locale value) {
    if (supportedLocale == null) supportedLocale = new ArrayList<Locale>() ;
    this.supportedLocale.add(value) ;
  }

	public List<Supports> getSupports() {
    if (supports == null) return Constants.EMPTY_LIST ;
		return supports;
	}

	public void setSupports(Supports s) {
    if (supports == null) this.supports = new ArrayList<Supports>() ;

    if (supports.contains(s)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supports\" in portlet description");
    } else {
      this.supports.add(s) ;
    }
	}

  public void addSupports(Supports s) {
    if (supports == null) this.supports = new ArrayList<Supports>() ;
    this.supports.add(s) ;
  }

  public List<SharedSessionAttribute> getSharedSessionAttribute() { return sharedSessionAttribute; }

  public void addSharedSessionAttribute(SharedSessionAttribute ssa) {
    if (sharedSessionAttribute == null)
      sharedSessionAttribute = new ArrayList<SharedSessionAttribute>();
  	this.sharedSessionAttribute.add(ssa);
  }

  public List<QName> getSupportedProcessingEvent() { return supportedProcessingEvent; }
  public void setSupportedProcessingEvent(List<QName> list) { supportedProcessingEvent = list; }

  public void addSupportedProcessingEvent(QName spe) {
    if (supportedProcessingEvent == null)
      supportedProcessingEvent = new ArrayList<QName>();
    if (supportedProcessingEvent.contains(spe)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supported-processing-event\" in portlet description");
    } else {
      this.supportedProcessingEvent.add(spe);
    }
  }

  public List<QName> getSupportedPublishingEvent() { return supportedPublishingEvent; }
  public void setSupportedPublishingEvent(List<QName> list) { supportedPublishingEvent = list; }

  public void addSupportedPublishingEvent(QName spe) {
    if (supportedPublishingEvent == null)
      supportedPublishingEvent = new ArrayList<QName>();

    if (supportedPublishingEvent.contains(spe)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supported-publishing-event\" in portlet description");
    } else {
      supportedPublishingEvent.add(spe);
    }
  }

  public List<String> getSupportedPublicRenderParameter() { return supportedPublicRenderParameter; }

  public void addSupportedPublicRenderParameter(String srp) {
    if (supportedPublicRenderParameter == null)
      supportedPublicRenderParameter = new ArrayList<String>();

    if (supportedPublicRenderParameter.contains(srp)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supported-public-render-parameter\" in portlet description");
    } else {
      supportedPublicRenderParameter.add(srp);
    }
  }

  public List<String> getUrlGenerationListener() { return urlGenerationListener; }

  public void addUrlGenerationListener(String listener) {
    if (urlGenerationListener == null)
      urlGenerationListener = new ArrayList<String>();

    if (urlGenerationListener.contains(listener)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"url-generation-listener\" in portlet description");
    } else {
      urlGenerationListener.add(listener);
    }
  }

  public void setContainerRuntimeOption(Map<String, String[]> containerRuntimeOption) {
    if (containerRuntimeOption == null)
      containerRuntimeOption = new HashMap<String,String[]>();
    this.containerRuntimeOption = containerRuntimeOption;
  }

  public Map<String, String[]> getContainerRuntimeOption() {
    return containerRuntimeOption;
  }

  public void addContainerRuntimeOption(String name, String[] value) {
    if (containerRuntimeOption == null)
      containerRuntimeOption = new HashMap<String,String[]>();
    this.containerRuntimeOption.put(name,value);
  }

  public void addContainerRuntimeOption(Map<String, String[]> containerRuntimeOption) {
    if (containerRuntimeOption == null)
      containerRuntimeOption = new HashMap<String,String[]>();
    this.containerRuntimeOption.putAll(containerRuntimeOption);
  }

}