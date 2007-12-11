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
package org.exoplatform.services.portletcontainer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerPlugin;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.picocontainer.Startable;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class PortletContainerServiceImpl implements PortletContainerService, Startable {

  private ArrayList<PortletContainerPlugin> plugins;

  private ExoContainer                      container;

  private PortletContainerConf              config;

  public PortletContainerServiceImpl(ExoContainerContext context) {
    this.plugins = new ArrayList<PortletContainerPlugin>();
    this.container = context.getContainer();
    this.config = (PortletContainerConf) container.getComponentInstanceOfType(PortletContainerConf.class);
  }

  public void addPlugin(PortletContainerPlugin plugin) {
    System.out.println(" PortletContainerServiceImpl plugin registered: " + plugin);
    plugins.add(plugin);
  }

  public void start() {
  }

  public void stop() {
  }

  public void setName(String name) {
    config.setName(name);
  }

  public void setDescription(String description) {
    config.setDescription(description);
  }

  public void setMajorVersion(int version) {
    config.setMajorVersion(version);
  }

  public void setMinorVersion(int version) {
    config.setMinorVersion(version);
  }

  public void setProperties(Map<String, String> properties) {
    config.setProperties(properties);
  }

  public Collection<PortletMode> getSupportedPortletModes() {
    ArrayList<PortletMode> result = new ArrayList<PortletMode>();
    for (Object plugin : plugins.toArray()) {
      Collection<PortletMode> forAdd = ((PortletContainerPlugin) plugin).getSupportedPortletModes();
      if (forAdd != null) {
        result.addAll(forAdd);
      }
    }
    return Collections.unmodifiableCollection(result);
  }

  public Collection<WindowState> getSupportedWindowStates() {
    ArrayList<WindowState> result = new ArrayList<WindowState>();
    for (Object plugin : plugins.toArray()) {
      Collection<WindowState> forAdd = ((PortletContainerPlugin) plugin).getSupportedWindowStates();
      if (forAdd != null) {
        result.addAll(forAdd);
      }
    }
    return Collections.unmodifiableCollection(result);
  }

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    try {
      return findPluginByPAPPName(portletAppName).getPortletModes(portletAppName, portletName, markup);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean isModeSuported(String portletAppName,
                                String portletName,
                                String markup,
                                PortletMode mode) {
    return findPluginByPAPPName(portletAppName).isModeSuported(portletAppName, portletName, markup, mode);
  }

  public Collection<WindowState> getWindowStates(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    try {
      return findPluginByPAPPName(portletAppName).getWindowStates(portletAppName, portletName, markup);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  public boolean isStateSupported(String portletAppName,
                                  String portletName,
                                  String markup,
                                  WindowState state) {
    return findPluginByPAPPName(portletAppName).isStateSupported(portletAppName, portletName, markup, state);
  }

  public Map<String, PortletData> getAllPortletMetaData() {
    HashMap<String, PortletData> result = new HashMap<String, PortletData>();
    for (Object plugin : plugins.toArray()) {
      result.putAll(((PortletContainerPlugin) plugin).getAllPortletMetaData());
    }
    return result;
  }

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale) throws PortletContainerException {
    return findPluginByPAPPName(portletAppName).getBundle(request, response, portletAppName, portletName, locale);
  }

  protected PortletContainerPlugin findPluginByPAPPName(String papp) {
    for (Object plugin : plugins.toArray()) {
      Map<String, PortletData> plts = ((PortletContainerPlugin) plugin).getAllPortletMetaData();
      Set<String> keys = plts.keySet();
      Iterator<String> i = keys.iterator();
      while (i.hasNext()) {
        String s = i.next();
        if (s.startsWith(papp + "/"))
          return (PortletContainerPlugin) plugin;
      }
    }
    return null;
  }

  public void setPortletPreference(Input input,
                                   Map<String, String> preferences) throws PortletContainerException {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName());
    plugin.setPortletPreference(input, preferences);
  }

  public Map<String, String[]> getPortletPreference(Input input) {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName());
    return plugin.getPortletPreference(input);
  }

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName()).processAction(request, response, input);
  }

  public EventOutput processEvent(HttpServletRequest request,
                                  HttpServletResponse response,
                                  EventInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName()).processEvent(request, response, input);
  }

  public ResourceOutput serveResource(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ResourceInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName()).serveResource(request, response, input);
  }

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName()).render(request, response, input);
  }

  // sessions replication
  public void sendAttrs(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> attrs,
                        String portletApplicationName) throws PortletContainerException {
    findPluginByPAPPName(portletApplicationName).sendAttrs(request, response, attrs, portletApplicationName);
  }

  public boolean isEventPayloadTypeMatches(String portletAppName,
                                           QName eventName,
                                           Object payload) throws PortletContainerException {
    return findPluginByPAPPName(portletAppName).isEventPayloadTypeMatches(portletAppName, eventName, payload);
  }

}
