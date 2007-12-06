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
import java.util.Iterator;
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
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 10 nov. 2003 Time: 11:38:24
 */
public class PortletContainerConf {

  private Map<String, String>                          properties;

  private String                                       name;

  private String                                       description;

  private int                                          majorVersion = -1;

  private int                                          minorVersion = -1;

  private Collection<PortletMode>                      customModes;

  private Collection<WindowState>                      customStates;

  private Collection<CustomModeWithDescription>        customModesWithDescriptions;

  private Collection<CustomWindowStateWithDescription> customStatesWithDescriptions;

  private Collection<String>                           supportedContents;

  private boolean                                      isCacheEnable;

  private PortletContainer                             containerConfs;

  private boolean                                      isBundleLookupDelegated;

  public PortletContainerConf(InitParams params,
                              ConfigurationManager cmanager) {
    try {
      ObjectParameter param = params.getObjectParam("conf");
      containerConfs = (PortletContainer) param.getObject();
      init();
    } catch (Exception e) {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error(" -- PortletContainerConf.<init>: " + e);
    }
  }

  private void init() {
    String c = containerConfs.getCache().getEnable();
    if ("true".equals(c))
      isCacheEnable = true;

    c = containerConfs.getDelegatedBundle().getEnable();
    if ("true".equals(c))
      isBundleLookupDelegated = true;
  }

  public Map<String, String> getProperties() {
    if (properties == null) {
      Map<String, String> map = new HashMap<String, String>();
      List<Properties> l = containerConfs.getProperties();
      for (Iterator<Properties> iterator = l.iterator(); iterator.hasNext();) {
        Properties props = iterator.next();
        map.put(props.getName(), props.getValue());
      }
      properties = map;
    }
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public String getName() {
    if (name == null) {
      name = containerConfs.getGlobal().getName();
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    if (description == null) {
      description = containerConfs.getGlobal().getDescription();
    }
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getMajorVersion() {
    if (majorVersion < 0) {
      majorVersion = containerConfs.getGlobal().getMajorVersion();
    }
    return majorVersion;
  }

  public void setMajorVersion(int version) {
    majorVersion = version;
  }

  public int getMinorVersion() {
    if (minorVersion < 0) {
      minorVersion = containerConfs.getGlobal().getMinorVersion();
    }
    return minorVersion;
  }

  public void setMinorVersion(int version) {
    minorVersion = version;
  }

  public Collection<String> getSupportedContent() {
    if (supportedContents == null) {
      supportedContents = new ArrayList<String>();
      List<SupportedContent> content = containerConfs.getSupportedContent();
      for (Iterator<SupportedContent> iter = content.iterator(); iter.hasNext();) {
        SupportedContent element = iter.next();
        supportedContents.add(element.getName());
      }
    }
    return supportedContents;
  }

  public Enumeration<PortletMode> getSupportedPortletModes() {
    if (customModes == null) {
      Collection<PortletMode> v = new ArrayList<PortletMode>();
      v.add(PortletMode.EDIT);
      v.add(PortletMode.HELP);
      v.add(PortletMode.VIEW);
      List<CustomMode> l = containerConfs.getCustomMode();
      for (Iterator<CustomMode> iterator = l.iterator(); iterator.hasNext();) {
        CustomMode customMode = iterator.next();
        v.add(new PortletMode(customMode.getName()));
      }
      customModes = v;
    }
    return Collections.enumeration(customModes);
  }

  public Enumeration<WindowState> getSupportedWindowStates() {
    if (customStates == null) {
      Collection<WindowState> v = new ArrayList<WindowState>();
      v.add(WindowState.NORMAL);
      v.add(WindowState.MINIMIZED);
      v.add(WindowState.MAXIMIZED);
      List<CustomWindowState> l = containerConfs.getCustomWindowState();
      for (Iterator<CustomWindowState> iterator = l.iterator(); iterator.hasNext();) {
        CustomWindowState customState = iterator.next();
        v.add(new WindowState(customState.getName()));
      }
      customStates = v;
    }
    return Collections.enumeration(customStates);
  }

  public Collection<CustomModeWithDescription> getSupportedPortletModesWithDescriptions() {
    if (customModesWithDescriptions == null) {
      Collection<CustomModeWithDescription> v = new ArrayList<CustomModeWithDescription>();
      List<CustomMode> l = containerConfs.getCustomMode();
      for (Iterator<CustomMode> iterator = l.iterator(); iterator.hasNext();) {
        CustomMode customMode = iterator.next();
        List<Description> l2 = customMode.getDescription();
        List<LocalisedDescription> toBeReturned = new ArrayList<LocalisedDescription>();
        for (Iterator<Description> iter = l2.iterator(); iter.hasNext();) {
          Description element = iter.next();
          LocalisedDescription d = new LocalisedDescription(new Locale(element.getLang()), element.getDescription());
          toBeReturned.add(d);
        }
        CustomModeWithDescription cMWD = new CustomModeWithDescription(new PortletMode(customMode.getName()), toBeReturned);
        v.add(cMWD);
      }
      customModesWithDescriptions = v;
    }
    return customModesWithDescriptions;
  }

  public Collection<CustomWindowStateWithDescription> getSupportedWindowStatesWithDescriptions() {
    if (customStatesWithDescriptions == null) {
      Collection<CustomWindowStateWithDescription> v = new ArrayList<CustomWindowStateWithDescription>();
      List<CustomWindowState> l = containerConfs.getCustomWindowState();
      for (Iterator<CustomWindowState> iterator = l.iterator(); iterator.hasNext();) {
        CustomWindowState customState = iterator.next();
        List<Description> l2 = customState.getDescription();
        List<LocalisedDescription> toBeReturned = new ArrayList<LocalisedDescription>();
        for (Iterator<Description> iter = l2.iterator(); iter.hasNext();) {
          Description element = iter.next();
          LocalisedDescription d = new LocalisedDescription(new Locale(element.getLang()), element.getDescription());
          toBeReturned.add(d);
        }
        CustomWindowStateWithDescription cMWD = new CustomWindowStateWithDescription(new WindowState(customState.getName()), toBeReturned);
        v.add(cMWD);
      }
      customStatesWithDescriptions = v;
    }
    return customStatesWithDescriptions;
  }

  public boolean isModeSupported(PortletMode mode) {
    Enumeration<PortletMode> e = getSupportedPortletModes();
    while (e.hasMoreElements()) {
      PortletMode portletMode = e.nextElement();
      if (portletMode.toString().equals(mode.toString()))
        return true;
    }
    return false;
  }

  public boolean isStateSupported(WindowState state) {
    Enumeration<WindowState> e = getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState windowState = e.nextElement();
      if (windowState.toString().equals(state.toString()))
        return true;
    }
    return false;

  }

  public boolean isCacheEnable() {
    return isCacheEnable;
  }

  public boolean isBundleLookupDelegated() {
    return isBundleLookupDelegated;
  }
}
