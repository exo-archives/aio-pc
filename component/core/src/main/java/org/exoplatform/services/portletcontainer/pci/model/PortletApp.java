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
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletURLGenerationListener;

import org.exoplatform.Constants;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Jul 11, 2004.
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: PortletApp.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class PortletApp {

  /**
   * Portlets.
   */
  private final List<Portlet>                portlet;

  /**
   * Version.
   */
  private String                             version;

  /**
   * Custom window states.
   */
  private final List<CustomWindowState>      customWindowState;

  /**
   * Custom portlet modes.
   */
  private final List<CustomPortletMode>      customPortletMode;

  /**
   * Security constraints.
   */
  private final List<SecurityConstraint>     securityConstraint;

  /**
   * User attributes.
   */
  private final List<UserAttribute>          userAttribute;

  /**
   * Event definitions.
   */
  private final List<EventDefinition>        eventDefinition;

  /**
   * Public render parameters.
   */
  private final List<PublicRenderParameter>  publicRenderParameter;

  /**
   * Filters.
   */
  private List<Filter>                       filter;

  /**
   * Filter mappings.
   */
  private List<FilterMapping>                filterMapping;

  /**
   * Id.
   */
  private String                             id;

  // Portlet spec 2 add:

  /**
   * Resource bundle.
   */
  private String                             resourceBundle;

  /**
   * Container runtime options.
   */
  private Map<String, String[]>              containerRuntimeOption;

  /**
   * Either v2 portlet app.
   */
  private boolean                            ver2;

  /**
   * Default namespace.
   */
  private String                             defaultNamespace = javax.xml.XMLConstants.NULL_NS_URI;

  /**
   * URL generation listeners.
   */
  private List<String>                       urlGenerationListener;

  /**
   * Runtime URL listeners objects.
   */
  private List<PortletURLGenerationListener> urlListeners;

  /**
   * Logger.
   */
  private Log                                log              = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

  /**
   * simple constructor.
   */
  public PortletApp() {
    portlet = new ArrayList<Portlet>();
    customWindowState = new ArrayList<CustomWindowState>();
    customPortletMode = new ArrayList<CustomPortletMode>();
    securityConstraint = new ArrayList<SecurityConstraint>();
    userAttribute = new ArrayList<UserAttribute>();
    eventDefinition = new ArrayList<EventDefinition>();
    publicRenderParameter = new ArrayList<PublicRenderParameter>();
    containerRuntimeOption = new HashMap<String, String[]>();
    filter = new ArrayList<Filter>();
    filterMapping = new ArrayList<FilterMapping>();
  }

  /**
   * @return listeners
   */
  public final List<PortletURLGenerationListener> getUrlListeners() {
    return urlListeners;
  }

  /**
   * @param urlListeners listeners
   */
  public final void setUrlListeners(final List<PortletURLGenerationListener> urlListeners) {
    this.urlListeners = urlListeners;
  }

  /**
   * @return listeners
   */
  public final List<String> getUrlGenerationListener() {
    return urlGenerationListener;
  }

  /**
   * @param listener listeners
   */
  public final void addUrlGenerationListener(final String listener) {
    if (urlGenerationListener == null)
      urlGenerationListener = new ArrayList<String>();

    if (urlGenerationListener.contains(listener)) {
      log.error("Duplicate field \"listener\" in portlet app description");
    } else
      urlGenerationListener.add(listener);
  }

  /**
   * @param namespace default namespace
   */
  public final void setDefaultNamespace(final String namespace) {
    defaultNamespace = namespace;
  }

  /**
   * @return default namespace
   */
  public final String getDefaultNamespace() {
    return defaultNamespace;
  }

  /**
   * @return either version is 2
   */
  public final boolean getVer2() {
    return this.ver2;
  }

  /**
   * @param value version
   */
  public final void setVer2(final boolean value) {
    this.ver2 = value;
  }

  /**
   * @return portlets
   */
  public final List<Portlet> getPortlet() {
    return this.portlet;
  }

  /**
   * @return portlet
   */
  public final Portlet getPortlet(String portletName) {
    // for each portlet
    for (int i = 0; i < this.portlet.size(); i++) {
      Portlet portletItem = this.portlet.get(i);
      if (portletItem.getPortletName().equalsIgnoreCase(portletName)) {
        return portletItem;
      }
    }
    return null;
  }

  /**
   * @param p portlet definition
   */
  public final void addPortlet(final Portlet p) {
    if (portlet.contains(p)) {
      log.error("Duplicate field \"Portlet-Name\" in portlet-application description");
    } else {
      this.portlet.add(p);
      p.setApplication(this);
    }
  }

  /**
   * @return version
   */
  public final String getVersion() {
    return this.version;
  }

  /**
   * @param value version
   */
  public final void setVersion(final String value) {
    this.version = value;
  }

  /**
   * @return filters
   */
  public final List<Filter> getFilter() {
    if (filter == null)
      return Constants.EMPTY_LIST;
    return filter;
  }

  /**
   * @param filter filters
   */
  public final void setFilter(final List<Filter> filter) {
    this.filter = filter;
  }

  /**
   * @param f filter
   */
  public final void addFilter(final Filter f) {
    if (f != null) {
      if (filter == null)
        filter = new ArrayList<Filter>();
      if (filter.contains(f)) {
        log.error("Duplicate field \"filter\" in portlet-application description");
      } else
        this.filter.add(f);
    }
  }

  /**
   * @return filter mappings
   */
  public final List<FilterMapping> getFilterMapping() {
    if (filterMapping == null)
      return Constants.EMPTY_LIST;
    return filterMapping;
  }

  /**
   * @param filterMapping filter mappings
   */
  public final void setFilterMapping(final List<FilterMapping> filterMapping) {
    this.filterMapping = filterMapping;
  }

  /**
   * @param f filter mapping
   */
  public final void addFilterMapping(final FilterMapping f) {
    if (filterMapping == null)
      filterMapping = new ArrayList<FilterMapping>();
    this.filterMapping.add(f);
  }

  /**
   * @return window state
   */
  public final List<CustomWindowState> getCustomWindowState() {
    return this.customWindowState;
  }

  /**
   * @param value window state
   */
  public final void addCustomWindowState(final CustomWindowState value) {
    if (customWindowState.contains(value)) {
      log.error("Duplicate field \"custom-window-state\" in portlet-application description");
    } else
      this.customWindowState.add(value);
  }

  /**
   * @return portlet mode
   */
  public final List<CustomPortletMode> getCustomPortletMode() {
    return this.customPortletMode;
  }

  /**
   * @param mode portlet mode
   */
  public final void addCustomPortletMode(final CustomPortletMode mode) {
    if (customPortletMode.contains(mode)) {
      log.error("Duplicate field \"custom-window-state\" in portlet-application description");
    } else
      this.customPortletMode.add(mode);
  }

  /**
   * @return security constraint
   */
  public final List<SecurityConstraint> getSecurityConstraint() {
    return this.securityConstraint;
  }

  /**
   * @param sc security constraint
   */
  public final void addSecurityConstraint(final SecurityConstraint sc) {
    this.securityConstraint.add(sc);
  }

  /**
   * @return user attribute
   */
  public final List<UserAttribute> getUserAttribute() {
    return userAttribute;
  }

  /**
   * @param att user attribute
   */
  public final void addUserAttribute(final UserAttribute att) {
    if (userAttribute.contains(att)) {
      log.error("Duplicate field \"user-attribute\" in portlet-application description");
    } else
      this.userAttribute.add(att);
  }

  /**
   * @return event definition
   */
  public final List<EventDefinition> getEventDefinition() {
    return eventDefinition;
  }

  /**
   * @param edef event definition
   */
  public final void addEventDefinition(final EventDefinition edef) {
    if (eventDefinition.contains(edef)) {
      log.error("Duplicate field \"event-definition\" in portlet-application description");
    } else
      this.eventDefinition.add(edef);
  }

  /**
   * @return public parameters
   */
  public final List<PublicRenderParameter> getPublicRenderParameter() {
    return publicRenderParameter;
  }

  /**
   * @param srp public parameter
   */
  public final void addPublicRenderParameter(final PublicRenderParameter srp) {
    if (publicRenderParameter.contains(srp)) {
      log.error("Duplicate field \"public-render-parameter\" in portlet-application description");
    } else
      this.publicRenderParameter.add(srp);
  }

  /**
   * @param containerRuntimeOption container runtime option
   */
  public final void setContainerRuntimeOption(final Map<String, String[]> containerRuntimeOption) {
    this.containerRuntimeOption = containerRuntimeOption;
  }

  /**
   * @return container runtime option
   */
  public final Map<String, String[]> getContainerRuntimeOption() {
    return containerRuntimeOption;
  }

  /**
   * @param name container runtime option name
   * @param value value
   */
  public final void addContainerRuntimeOption(final String name, final String[] value) {
    this.containerRuntimeOption.put(name, value);
  }

  /**
   * @param containerRuntimeOption1 container runtime option
   */
  public final void addContainerRuntimeOption(final Map<String, String[]> containerRuntimeOption1) {
    this.containerRuntimeOption.putAll(containerRuntimeOption1);
  }

  /**
   * @return id
   */
  public final String getId() {
    return this.id;
  }

  /**
   * @param value id
   */
  public final void setId(final String value) {
    this.id = value;
  }

  /**
   * @return resource bundle
   */
  public final String getResourceBundle() {
    return resourceBundle;
  }

  /**
   * @param resourceBundle resource bundle
   */
  public final void setResourceBundle(final String resourceBundle) {
    this.resourceBundle = resourceBundle;
  }

}
