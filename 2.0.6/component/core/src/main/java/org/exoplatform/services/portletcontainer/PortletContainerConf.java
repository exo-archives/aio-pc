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
package org.exoplatform.services.portletcontainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ObjectParameter;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.config.CustomMode;
import org.exoplatform.services.portletcontainer.config.CustomWindowState;
import org.exoplatform.services.portletcontainer.config.Description;
import org.exoplatform.services.portletcontainer.config.Properties;
import org.exoplatform.services.portletcontainer.config.SupportedContent;
import org.exoplatform.services.portletcontainer.pci.CustomModeWithDescription;
import org.exoplatform.services.portletcontainer.pci.CustomWindowStateWithDescription;
import org.exoplatform.services.portletcontainer.pci.LocalisedDescription;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin .
 * benjmestrallet@users.sourceforge.net Date: 10 nov. 2003 Time: 11:38:24
 */
public class PortletContainerConf {

  /**
   * Properties.
   */
  private Map<String, String> properties;

  /**
   * Name.
   */
  private String name;

  /**
   * Description.
   */
  private String description;

  /**
   * Major version.
   */
  private int majorVersion = -1;

  /**
   * Minor version.
   */
  private int minorVersion = -1;

  /**
   * Portlet modes.
   */
  private Collection<PortletMode> customModes;

  /**
   * Window states.
   */
  private Collection<WindowState> customStates;

  /**
   * Custom portlet modes.
   */
  private Collection<CustomModeWithDescription> customModesWithDescriptions;

  /**
   * Custom window states.
   */
  private Collection<CustomWindowStateWithDescription> customStatesWithDescriptions;

  /**
   * Supported content types.
   */
  private Collection<String> supportedContents;

  /**
   * Either cache enabled.
   */
  private boolean isCacheEnable;

  /**
   * Actual PC configuration.
   */
  private PortletContainer containerConfs;

  /**
   * Either bundle look up delegated.
   */
  private boolean isBundleLookupDelegated;

  /**
   * @param params init params
   * @param cmanager configuration manager
   */
  public PortletContainerConf(final InitParams params, final ConfigurationManager cmanager) {
    try {
      ObjectParameter param = params.getObjectParam("conf");
      containerConfs = (PortletContainer) param.getObject();
      init();
    } catch (Exception e) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error(" -- PortletContainerConf.<init>: " + e);
    }
  }

  /**
   * Initialization.
   */
  private void init() {
    String c = containerConfs.getCache().getEnable();
    if ("true".equals(c))
      isCacheEnable = true;

    c = containerConfs.getDelegatedBundle().getEnable();
    if ("true".equals(c))
      isBundleLookupDelegated = true;
  }

  /**
   * @return properties
   */
  public final Map<String, String> getProperties() {
    if (properties == null) {
      Map<String, String> map = new HashMap<String, String>();
      List<Properties> l = containerConfs.getProperties();
      for (Properties props : l) {
        map.put(props.getName(), props.getValue());
      }
      properties = map;
    }
    return properties;
  }

  /**
   * @param properties properties
   */
  public final void setProperties(final Map<String, String> properties) {
    this.properties = properties;
  }

  /**
   * @return name
   */
  public final String getName() {
    if (name == null)
      name = containerConfs.getGlobal().getName();
    return name;
  }

  /**
   * @param name name
   */
  public final void setName(final String name) {
    this.name = name;
  }

  /**
   * @return description
   */
  public final String getDescription() {
    if (description == null)
      description = containerConfs.getGlobal().getDescription();
    return description;
  }

  /**
   * @param description description
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @return major version
   */
  public final int getMajorVersion() {
    if (majorVersion < 0)
      majorVersion = containerConfs.getGlobal().getMajorVersion();
    return majorVersion;
  }

  /**
   * @param version major version
   */
  public final void setMajorVersion(final int version) {
    majorVersion = version;
  }

  /**
   * @return minor version
   */
  public final int getMinorVersion() {
    if (minorVersion < 0)
      minorVersion = containerConfs.getGlobal().getMinorVersion();
    return minorVersion;
  }

  /**
   * @param version minor version
   */
  public final void setMinorVersion(final int version) {
    minorVersion = version;
  }

  /**
   * @return supported content types
   */
  public final synchronized Collection<String> getSupportedContent() {
    if (supportedContents == null) {
      supportedContents = new ArrayList<String>();
      List<SupportedContent> content = containerConfs.getSupportedContent();
      for (SupportedContent element : content) {
        supportedContents.add(element.getName());
      }
    }
    return supportedContents;
  }

  /**
   * @return all portlet modes supported
   */
  public final Enumeration<PortletMode> getSupportedPortletModes() {
    if (customModes == null) {
      Collection<PortletMode> v = new ArrayList<PortletMode>();
      v.add(PortletMode.EDIT);
      v.add(PortletMode.HELP);
      v.add(PortletMode.VIEW);
      List<CustomMode> l = containerConfs.getCustomMode();
      for (CustomMode customMode : l) {
        v.add(new PortletMode(customMode.getName()));
      }
      customModes = v;
    }
    return Collections.enumeration(customModes);
  }

  /**
   * @return all window states supported
   */
  public final Enumeration<WindowState> getSupportedWindowStates() {
    if (customStates == null) {
      Collection<WindowState> v = new ArrayList<WindowState>();
      v.add(WindowState.NORMAL);
      v.add(WindowState.MINIMIZED);
      v.add(WindowState.MAXIMIZED);
      List<CustomWindowState> l = containerConfs.getCustomWindowState();
      for (CustomWindowState customState : l) {
        v.add(new WindowState(customState.getName()));
      }
      customStates = v;
    }
    return Collections.enumeration(customStates);
  }

  /**
   * @return custom portlet modes
   */
  public final Collection<CustomModeWithDescription> getSupportedPortletModesWithDescriptions() {
    if (customModesWithDescriptions == null) {
      Collection<CustomModeWithDescription> v = new ArrayList<CustomModeWithDescription>();
      List<CustomMode> l = containerConfs.getCustomMode();
      for (CustomMode customMode : l) {
        List<Description> l2 = customMode.getDescription();
        List<LocalisedDescription> toBeReturned = new ArrayList<LocalisedDescription>();
        for (Description element : l2) {
          LocalisedDescription d = new LocalisedDescription(new Locale(element.getLang()), element
              .getDescription());
          toBeReturned.add(d);
        }
        CustomModeWithDescription cModeWD = new CustomModeWithDescription(new PortletMode(customMode
            .getName()), toBeReturned);
        v.add(cModeWD);
      }
      customModesWithDescriptions = v;
    }
    return customModesWithDescriptions;
  }

  /**
   * @return custom window states
   */
  public final Collection<CustomWindowStateWithDescription> getSupportedWindowStatesWithDescriptions() {
    if (customStatesWithDescriptions == null) {
      Collection<CustomWindowStateWithDescription> v = new ArrayList<CustomWindowStateWithDescription>();
      List<CustomWindowState> l = containerConfs.getCustomWindowState();
      for (CustomWindowState customState : l) {
        List<Description> l2 = customState.getDescription();
        List<LocalisedDescription> toBeReturned = new ArrayList<LocalisedDescription>();
        for (Description element : l2) {
          LocalisedDescription d = new LocalisedDescription(new Locale(element.getLang()), element
              .getDescription());
          toBeReturned.add(d);
        }
        CustomWindowStateWithDescription cWindowStateWD = new CustomWindowStateWithDescription(new WindowState(customState
            .getName()),
            toBeReturned);
        v.add(cWindowStateWD);
      }
      customStatesWithDescriptions = v;
    }
    return customStatesWithDescriptions;
  }

  /**
   * @param mode mode to check
   * @return either the mode is supported
   */
  public final boolean isModeSupported(final PortletMode mode) {
    Enumeration<PortletMode> e = getSupportedPortletModes();
    while (e.hasMoreElements()) {
      PortletMode portletMode = e.nextElement();
      if (portletMode.toString().equals(mode.toString()))
        return true;
    }
    return false;
  }

  /**
   * @param state state to check
   * @return either the state is supported
   */
  public final boolean isStateSupported(final WindowState state) {
    Enumeration<WindowState> e = getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState windowState = e.nextElement();
      if (windowState.toString().equals(state.toString()))
        return true;
    }
    return false;

  }

  /**
   * @return is cache enabled
   */
  public final boolean isCacheEnable() {
    return isCacheEnable;
  }

  /**
   * @return either bundle look up delegated
   */
  public final boolean isBundleLookupDelegated() {
    return isBundleLookupDelegated;
  }

  public boolean isHookPortletExceptions() {
    return containerConfs.getGlobal().getHookPortletExceptions() != null &&
      containerConfs.getGlobal().getHookPortletExceptions().equals("true");
  }
}
