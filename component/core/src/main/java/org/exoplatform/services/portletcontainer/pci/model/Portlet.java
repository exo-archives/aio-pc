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

import javax.portlet.PortletSession;
import javax.portlet.filter.FilterChain;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;

/**
 * Jul 11, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: Portlet.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Portlet {

  /**
   * Descriptions.
   */
  private List<Description> description;

  /**
   * Portlet name.
   */
  private String portletName;

  /**
   * Display names.
   */
  private List<DisplayName> displayName = new ArrayList<DisplayName>();

  /**
   * Portlet class FQN.
   */
  private String portletClass;

  /**
   * Initial parameters.
   */
  private List<InitParam> initParam;

  /**
   * Caching.
   */
  private Integer caching;

  /**
   * Supports.
   */
  private List<Supports> supports;

  /**
   * Supported locales.
   */
  private List<Locale> supportedLocale;

  /**
   * Resource bundle.
   */
  private String resourceBundle;

  /**
   * Portlet info.
   */
  private PortletInfo portletInfo;

  /**
   * Portlet preferences.
   */
  private ExoPortletPreferences portletPreferences;

  /**
   * Secuyrity role ref.
   */
  private List<SecurityRoleRef> securityRoleRef;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * Processing events.
   */
  private List<QName> supportedProcessingEvent;

  /**
   * Publishing events.
   */
  private List<QName> supportedPublishingEvent;

  /**
   * Supported public render parameters.
   */
  private List<String> supportedPublicRenderParameter;

  /**
   * Container runtime options.
   */
  private Map<String, String[]> containerRuntimeOption;

  /**
   * Shared session attributes.
   */
  private List<SharedSessionAttribute> sharedSessionAttribute;

  /**
   * Filter chain.
   */
  private FilterChain filterChain = null;

  /**
   * Portlet app.
   */
  private PortletApp application;

  // exo extension

  /**
   * Global cache.
   */
  private String globalCache;

  /**
   * escapeXML.
   */
  private Boolean escapeXml;

  /**
   * @return portlet app object
   */
  public final PortletApp getApplication() {
    return application;
  }

  /**
   * @param app portlet app object
   */
  public final void setApplication(final PortletApp app) {
    application = app;
  }

  /**
   * @param escapeXml either escape XML
   */
  public final void setEscapeXml(final Boolean escapeXml) {
    this.escapeXml = escapeXml;
  }

  /**
   * @param escapeXml either escape XML
   */
  public final void setEscapeXml(final boolean escapeXml) {
    this.escapeXml = new Boolean(escapeXml);
  }

  /**
   * @return either escape XML
   */
  public final Boolean getEscapeXml() {
    if (escapeXml != null)
      return escapeXml;
    if (getContainerRuntimeOption() != null) {
      String[] valuesPortlet = getContainerRuntimeOption().get("javax.portlet.escapeXml");
      if (valuesPortlet != null)
        return new Boolean(valuesPortlet[0]);
    }
    String[] valuesApplication = null;
    if (application != null)
      if (application.getContainerRuntimeOption() != null)
        valuesApplication = application.getContainerRuntimeOption().get("javax.portlet.escapeXml");
    if (valuesApplication != null)
      return new Boolean(valuesApplication[0]);
    return Boolean.TRUE;
  }

  /**
   * @return portlet session scope
   */
  public final int getPortletSessionScope() {
    if (getContainerRuntimeOption() != null) {
      String[] valuesPortlet = getContainerRuntimeOption().get(
          "javax.portlet.includedPortletSessionScope");
      if ((valuesPortlet != null) && valuesPortlet[0].equals(PCConstants.PORTLET_SCOPE))
        return PortletSession.PORTLET_SCOPE;
    }
    String[] valuesApplication = application.getContainerRuntimeOption().get(
        "javax.portlet.includedPortletSessionScope");
    if ((valuesApplication != null) && valuesApplication[0].equals(PCConstants.PORTLET_SCOPE))
      return PortletSession.PORTLET_SCOPE;

    return PortletSession.APPLICATION_SCOPE;
  }

  /**
   * @return filter chain
   */
  public final FilterChain getFilterChain() {
    return filterChain;
  }

  /**
   * @param filterChain filter chain
   */
  public final void setFilterChain(final FilterChain filterChain) {
    this.filterChain = filterChain;
  }

  /**
   * @return description list
   */
  public final List<Description> getDescription() {
    if (description == null)
      return Constants.EMPTY_LIST;
    return description;
  }

  /**
   * @param lang language
   * @return description
   */
  public final String getDescription(final String lang) {
    return Util.getDescription(lang, description);
  }

  /**
   * @param description description list
   */
  public final void setDescription(final List<Description> description) {
    this.description = description;
  }

  /**
   * @param desc description
   */
  public final void addDescription(final Description desc) {
    if (description == null)
      description = new ArrayList<Description>();
    this.description.add(desc);
  }

  /**
   * @return display name list
   */
  public final List<DisplayName> getDisplayName() {
    return displayName;
  }

  /**
   * @param name display name
   */
  public final void addDisplayName(final DisplayName name) {
    this.displayName.add(name);
  }

  /**
   * @param displayName display name list
   */
  public final void setDisplayName(final List<DisplayName> displayName) {
    this.displayName = displayName;
  }

  /**
   * @return caching value
   */
  public final Integer getCaching() {
    return caching;
  }

  /**
   * @param caching caching value
   */
  public final void setCaching(final Integer caching) {
    this.caching = caching;
  }

  /**
   * @return global cache
   */
  public final String getGlobalCache() {
    return globalCache;
  }

  /**
   * @param globalCache global cache
   */
  public final void setGlobalCache(final String globalCache) {
    this.globalCache = globalCache;
  }

  /**
   * @return id
   */
  public final String getId() {
    return id;
  }

  /**
   * @param id id
   */
  public final void setId(final String id) {
    this.id = id;
  }

  /**
   * @return init param list
   */
  public final List<InitParam> getInitParam() {
    if (initParam == null)
      return Constants.EMPTY_LIST;
    return initParam;
  }

  /**
   * @param initParam init param list
   */
  public final void setInitParam(final List<InitParam> initParam) {
    this.initParam = initParam;
  }

  /**
   * @param param init param
   */
  public final void addInitParam(final InitParam param) {
    if (initParam == null)
      initParam = new ArrayList<InitParam>();

    if (initParam.contains(param)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"init-param\" in portlet description");
    } else
      this.initParam.add(param);
  }

  /**
   * @return class FQN
   */
  public final String getPortletClass() {
    return portletClass;
  }

  /**
   * @param portletClass class FQN
   */
  public final void setPortletClass(final String portletClass) {
    this.portletClass = portletClass;
  }

  /**
   * @return info
   */
  public final PortletInfo getPortletInfo() {
    return portletInfo;
  }

  /**
   * @param portletInfo info
   */
  public final void setPortletInfo(final PortletInfo portletInfo) {
    this.portletInfo = portletInfo;
  }

  /**
   * @return name
   */
  public final String getPortletName() {
    return portletName;
  }

  /**
   * @param portletName name
   */
  public final void setPortletName(final String portletName) {
    this.portletName = portletName;
  }

  /**
   * @return prefs
   */
  public final ExoPortletPreferences getPortletPreferences() {
    return portletPreferences;
  }

  /**
   * @param portletPreferences prefs
   */
  public final void setPortletPreferences(final ExoPortletPreferences portletPreferences) {
    this.portletPreferences = portletPreferences;
  }

  /**
   * @return name
   */
  public final String getResourceBundle() {
    return resourceBundle;
  }

  /**
   * @param resourceBundle name
   */
  public final void setResourceBundle(final String resourceBundle) {
    this.resourceBundle = resourceBundle;
  }

  /**
   * @return SecurityRoleRef object list
   */
  public final List<SecurityRoleRef> getSecurityRoleRef() {
    if (securityRoleRef == null)
      return Constants.EMPTY_LIST;
    return securityRoleRef;
  }

  /**
   * @param securityRoleRef SecurityRoleRef object list
   */
  public final void setSecurityRoleRef(final List<SecurityRoleRef> securityRoleRef) {
    this.securityRoleRef = securityRoleRef;
  }

  /**
   * @param ref SecurityRoleRef object
   */
  public final void addSecurityRoleRef(final SecurityRoleRef ref) {
    if (securityRoleRef == null)
      securityRoleRef = new ArrayList<SecurityRoleRef>();

    if (securityRoleRef.contains(ref)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"security-role-ref\" in portlet description");
    } else
      this.securityRoleRef.add(ref);
  }

  /**
   * @return locale list
   */
  public final List<Locale> getSupportedLocale() {
    if (supportedLocale == null)
      return Constants.EMPTY_LIST;
    return supportedLocale;
  }

  /**
   * @param supportedLocale locale list
   */
  public final void setSupportedLocale(final List<Locale> supportedLocale) {
    this.supportedLocale = supportedLocale;
  }

  /**
   * @param value locale
   */
  public final void addSupportedLocale(final Locale value) {
    if (supportedLocale == null)
      supportedLocale = new ArrayList<Locale>();
    this.supportedLocale.add(value);
  }

  /**
   * @return Supports object list
   */
  public final List<Supports> getSupports() {
    if (supports == null)
      return Constants.EMPTY_LIST;
    return supports;
  }

  /**
   * @param s Supports object
   */
  public final void setSupports(final Supports s) {
    if (supports == null)
      this.supports = new ArrayList<Supports>();

    if (supports.contains(s)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supports\" in portlet description");
    } else
      this.supports.add(s);
  }

  /**
   * @param s Supports object
   */
  public final void addSupports(final Supports s) {
    if (supports == null)
      this.supports = new ArrayList<Supports>();
    this.supports.add(s);
  }

  /**
   * @return attribute list
   */
  public final List<SharedSessionAttribute> getSharedSessionAttribute() {
    return sharedSessionAttribute;
  }

  /**
   * @param ssa attribute
   */
  public final void addSharedSessionAttribute(final SharedSessionAttribute ssa) {
    if (sharedSessionAttribute == null)
      sharedSessionAttribute = new ArrayList<SharedSessionAttribute>();
    this.sharedSessionAttribute.add(ssa);
  }

  /**
   * @return event list
   */
  public final List<QName> getSupportedProcessingEvent() {
    if (supportedProcessingEvent == null)
      supportedProcessingEvent = new ArrayList<QName>();
    return supportedProcessingEvent;
  }

  /**
   * @param list event list
   */
  public final void setSupportedProcessingEvent(final List<QName> list) {
    supportedProcessingEvent = list;
  }

  /**
   * @param spe event FQN
   */
  public final void addSupportedProcessingEvent(final QName spe) {
    if (supportedProcessingEvent == null)
      supportedProcessingEvent = new ArrayList<QName>();
    if (supportedProcessingEvent.contains(spe)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supported-processing-event\" in portlet description");
    } else
      this.supportedProcessingEvent.add(spe);
  }

  /**
   * @return event list
   */
  public final List<QName> getSupportedPublishingEvent() {
    if (supportedPublishingEvent == null)
      supportedPublishingEvent = new ArrayList<QName>();
    return supportedPublishingEvent;
  }

  /**
   * @param list event list
   */
  public final void setSupportedPublishingEvent(final List<QName> list) {
    supportedPublishingEvent = list;
  }

  /**
   * @param spe event FQN
   */
  public final void addSupportedPublishingEvent(final QName spe) {
    if (supportedPublishingEvent == null)
      supportedPublishingEvent = new ArrayList<QName>();

    if (supportedPublishingEvent.contains(spe)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supported-publishing-event\" in portlet description");
    } else
      supportedPublishingEvent.add(spe);
  }

  /**
   * @return param name list
   */
  public final List<String> getSupportedPublicRenderParameter() {
    if (supportedPublicRenderParameter == null)
      supportedPublicRenderParameter = new ArrayList<String>();
    return supportedPublicRenderParameter;
  }

  /**
   * @param srp param name
   */
  public final void addSupportedPublicRenderParameter(final String srp) {
    if (supportedPublicRenderParameter == null)
      supportedPublicRenderParameter = new ArrayList<String>();

    if (supportedPublicRenderParameter.contains(srp)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"supported-public-render-parameter\" in portlet description");
    } else
      supportedPublicRenderParameter.add(srp);
  }

  /**
   * @param containerRuntimeOption1 container runtime option map
   */
  public final void setContainerRuntimeOption(final Map<String, String[]> containerRuntimeOption) {
    if (containerRuntimeOption == null)
      this.containerRuntimeOption = new HashMap<String, String[]>();
    else
      this.containerRuntimeOption = containerRuntimeOption;
  }

  /**
   * @return container runtime option map
   */
  public final Map<String, String[]> getContainerRuntimeOption() {
    if (containerRuntimeOption == null)
      this.containerRuntimeOption = new HashMap<String, String[]>();
    return containerRuntimeOption;
  }

  /**
   * @param name name
   * @param value value
   */
  public final void addContainerRuntimeOption(final String name, final String[] value) {
    if (containerRuntimeOption == null)
      containerRuntimeOption = new HashMap<String, String[]>();
    this.containerRuntimeOption.put(name, value);
  }

  /**
   * @param containerRuntimeOption1 container runtime option map
   */
  public final void addContainerRuntimeOption(final Map<String, String[]> containerRuntimeOption1) {
    if (containerRuntimeOption1 == null)
      this.containerRuntimeOption = new HashMap<String, String[]>();
    else
      this.containerRuntimeOption.putAll(containerRuntimeOption1);
  }

}
