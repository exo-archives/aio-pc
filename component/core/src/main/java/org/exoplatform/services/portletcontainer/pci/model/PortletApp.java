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

import org.exoplatform.Constants;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Jul 11, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: PortletApp.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class PortletApp {
  private List<Portlet>                portlet;

  private String                       version;

  private List<CustomWindowState>      customWindowState;

  private List<CustomPortletMode>      customPortletMode;

  private List<SecurityConstraint>     securityConstraint;

  private List<UserAttribute>          userAttribute;

  private List<EventDefinition>        eventDefinition;

  private List<SharedSessionAttribute> sharedSessionAttribute;

  private List<PublicRenderParameter>  publicRenderParameter;

  private List<Filter>                 filter;

  private List<FilterMapping>          filterMapping;

  private String                       id;

  // Portlet spec 2 add:
  private String                       resourceBundle;

  private Map<String, String[]>        containerRuntimeOption;

  private boolean                      ver2;

  private String                       defaultNamespace = javax.xml.XMLConstants.NULL_NS_URI;

  public PortletApp() {
    portlet = new ArrayList<Portlet>();
    customWindowState = new ArrayList<CustomWindowState>();
    customPortletMode = new ArrayList<CustomPortletMode>();
    securityConstraint = new ArrayList<SecurityConstraint>();
    userAttribute = new ArrayList<UserAttribute>();
    eventDefinition = new ArrayList<EventDefinition>();
    sharedSessionAttribute = new ArrayList<SharedSessionAttribute>();
    publicRenderParameter = new ArrayList<PublicRenderParameter>();
    containerRuntimeOption = new HashMap<String, String[]>();
    filter = new ArrayList<Filter>();
    filterMapping = new ArrayList<FilterMapping>();
  }

  public void setDefaultNamespace(String namespace) {
    defaultNamespace = namespace;
  }

  public String getDefaultNamespace() {
    return defaultNamespace;
  }

  public boolean getVer2() {
    return this.ver2;
  }

  public void setVer2(boolean value) {
    this.ver2 = value;
  }

  public List<Portlet> getPortlet() {
    return this.portlet;
  }

  public void addPortlet(Portlet p) {
    if (portlet.contains(p)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"Portlet-Name\" in portlet-application description");
    } else {
      this.portlet.add(p);
      p.setApplication(this);
    }
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String value) {
    this.version = value;
  }

  public List<Filter> getFilter() {
    if (filter == null)
      return Constants.EMPTY_LIST;
    return filter;
  }

  public void setFilter(List<Filter> filter) {
    this.filter = filter;
  }

  public void addFilter(Filter f) {
    if (f != null) {
      if (filter == null)
        filter = new ArrayList<Filter>();

      if (filter.contains(f)) {
        Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
        log.error("Duplicate field \"filter\" in portlet-application description");
      } else
        this.filter.add(f);
    }
  }

  public List<FilterMapping> getFilterMapping() {
    if (filterMapping == null)
      return Constants.EMPTY_LIST;
    return filterMapping;
  }

  public void setFilterMapping(List<FilterMapping> filterMapping) {
    this.filterMapping = filterMapping;
  }

  public void addFilterMapping(FilterMapping f) {
    if (filterMapping == null)
      filterMapping = new ArrayList<FilterMapping>();
    this.filterMapping.add(f);
  }

  public List<CustomWindowState> getCustomWindowState() {
    return this.customWindowState;
  }

  public void addCustomWindowState(CustomWindowState value) {
    if (customWindowState.contains(value)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"custom-window-state\" in portlet-application description");
    } else {
      this.customWindowState.add(value);
    }
  }

  public List<CustomPortletMode> getCustomPortletMode() {
    return this.customPortletMode;
  }

  public void addCustomPortletMode(CustomPortletMode mode) {
    if (customPortletMode.contains(mode)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"custom-window-state\" in portlet-application description");
    } else {
      this.customPortletMode.add(mode);
    }
  }

  public List<SecurityConstraint> getSecurityConstraint() {
    return this.securityConstraint;
  }

  public void addSecurityConstraint(SecurityConstraint sc) {
    this.securityConstraint.add(sc);
  }

  public List<UserAttribute> getUserAttribute() {
    return userAttribute;
  }

  public void addUserAttribute(UserAttribute att) {
    if (userAttribute.contains(att)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"user-attribute\" in portlet-application description");
    } else
      this.userAttribute.add(att);
  }

  public List<EventDefinition> getEventDefinition() {
    return eventDefinition;
  }

  public void addEventDefinition(EventDefinition edef) {
    if (eventDefinition.contains(edef)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"event-definition\" in portlet-application description");
    } else
      this.eventDefinition.add(edef);
  }

  public List<SharedSessionAttribute> getSharedSessionAttribute() {
    return sharedSessionAttribute;
  }

  public void addSharedSessionAttribute(SharedSessionAttribute ssa) {
    this.sharedSessionAttribute.add(ssa);
  }

  public List<PublicRenderParameter> getPublicRenderParameter() {
    return publicRenderParameter;
  }

  public void addPublicRenderParameter(PublicRenderParameter srp) {
    if (publicRenderParameter.contains(srp)) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("Duplicate field \"public-render-parameter\" in portlet-application description");
    } else
      this.publicRenderParameter.add(srp);
  }

  public void setContainerRuntimeOption(Map<String, String[]> containerRuntimeOption) {
    this.containerRuntimeOption = containerRuntimeOption;
  }

  public Map getContainerRuntimeOption() {
    return containerRuntimeOption;
  }

  public void addContainerRuntimeOption(String name,
                                        String[] value) {
    this.containerRuntimeOption.put(name, value);
  }

  public void addContainerRuntimeOption(Map<String, String[]> containerRuntimeOption) {
    this.containerRuntimeOption.putAll(containerRuntimeOption);
  }

  public String getId() {
    return this.id;
  }

  public void setId(String value) {
    this.id = value;
  }

  public String getResourceBundle() {
    return resourceBundle;
  }

  public void setResourceBundle(String resourceBundle) {
    this.resourceBundle = resourceBundle;
  }
}
