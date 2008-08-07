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
import javax.portlet.PortletPreferences;
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
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.picocontainer.Startable;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class PortletContainerServiceImpl implements PortletContainerService, Startable {

  /**
   * Plugins list.
   */
  private final ArrayList<PortletContainerPlugin> plugins;

  /**
   * Container.
   */
  private final ExoContainer container;

  /**
   * Config.
   */
  private final PortletContainerConf config;

  /**
   * @param context exo container context
   */
  public PortletContainerServiceImpl(final ExoContainerContext context) {
    this.plugins = new ArrayList<PortletContainerPlugin>();
    this.container = context.getContainer();
    this.config = (PortletContainerConf) container
        .getComponentInstanceOfType(PortletContainerConf.class);
  }

  /**
   * Overridden method.
   *
   * @param plugin plugin
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#addPlugin(org.exoplatform.services.portletcontainer.PortletContainerPlugin)
   */
  public final void addPlugin(final PortletContainerPlugin plugin) {
    System.out.println(" PortletContainerServiceImpl plugin registered: " + plugin);
    plugins.add(plugin);
  }

  /**
   * Overridden method.
   *
   * @see org.picocontainer.Startable#start()
   */
  public final void start() {
  }

  /**
   * Overridden method.
   *
   * @see org.picocontainer.Startable#stop()
   */
  public final void stop() {
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#setName(java.lang.String)
   */
  public final void setName(final String name) {
    config.setName(name);
  }

  /**
   * Overridden method.
   *
   * @param description description
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#setDescription(java.lang.String)
   */
  public final void setDescription(final String description) {
    config.setDescription(description);
  }

  /**
   * Overridden method.
   *
   * @param version major
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#setMajorVersion(int)
   */
  public final void setMajorVersion(final int version) {
    config.setMajorVersion(version);
  }

  /**
   * Overridden method.
   *
   * @param version minor
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#setMinorVersion(int)
   */
  public final void setMinorVersion(final int version) {
    config.setMinorVersion(version);
  }

  /**
   * Overridden method.
   *
   * @param properties properties
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#setProperties(java.util.Map)
   */
  public final void setProperties(final Map<String, String> properties) {
    config.setProperties(properties);
  }

  /**
   * Overridden method.
   *
   * @return supportlet window modes for all registered portlets
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getSupportedPortletModes()
   */
  public final Collection<PortletMode> getSupportedPortletModes() {
    ArrayList<PortletMode> result = new ArrayList<PortletMode>();
    for (Object plugin : plugins.toArray()) {
      Collection<PortletMode> forAdd = ((PortletContainerPlugin) plugin).getSupportedPortletModes();
      if (forAdd != null)
        result.addAll(forAdd);
    }
    return Collections.unmodifiableCollection(result);
  }

  /**
   * Overridden method.
   *
   * @return supported windows states for all registered portlets
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getSupportedWindowStates()
   */
  public final Collection<WindowState> getSupportedWindowStates() {
    ArrayList<WindowState> result = new ArrayList<WindowState>();
    for (Object plugin : plugins.toArray()) {
      Collection<WindowState> forAdd = ((PortletContainerPlugin) plugin).getSupportedWindowStates();
      if (forAdd != null)
        result.addAll(forAdd);
    }
    return Collections.unmodifiableCollection(result);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup makrup type
   * @return portlet modes that are supported by the specified portlet for the
   *         specified markup type
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getPortletModes(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public final Collection<PortletMode> getPortletModes(final String portletAppName,
      final String portletName,
      final String markup) {
    try {
      return findPluginByPAPPName(portletAppName).getPortletModes(portletAppName, portletName,
          markup);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public final String[] getPortalManagedPortletModes(final String portletAppName,
                                                     final String portletName) {
    return findPluginByPAPPName(portletAppName).getPortalManagedPortletModes(portletAppName, portletName);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup type
   * @param mode portlet mode
   * @return either the specified mode is supported by the specified portlet for
   *         the specified markup type
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#isModeSuported(java.lang.String,
   *      java.lang.String, java.lang.String, javax.portlet.PortletMode)
   */
  public final boolean isModeSuported(final String portletAppName,
      final String portletName,
      final String markup,
      final PortletMode mode) {
    return findPluginByPAPPName(portletAppName).isModeSuported(portletAppName, portletName, markup,
        mode);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup type
   * @return window states that are supported by the specified portlet for the
   *         specified markup type
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getWindowStates(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public final Collection<WindowState> getWindowStates(final String portletAppName,
      final String portletName,
      final String markup) {
    try {
      return findPluginByPAPPName(portletAppName).getWindowStates(portletAppName, portletName,
          markup);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * Overridden method.
   *
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup type (MIME type)
   * @param state window state
   * @return either the specified state is supported by the specified portlet
   *         for the specified markup type
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#isStateSupported(java.lang.String,
   *      java.lang.String, java.lang.String, javax.portlet.WindowState)
   */
  public final boolean isStateSupported(final String portletAppName,
      final String portletName,
      final String markup,
      final WindowState state) {
    return findPluginByPAPPName(portletAppName).isStateSupported(portletAppName, portletName,
        markup, state);
  }

  /**
   * Overridden method.
   *
   * @return all portlet metadata
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getAllPortletMetaData()
   */
  public final Map<String, PortletData> getAllPortletMetaData() {
    HashMap<String, PortletData> result = new HashMap<String, PortletData>();
    for (Object plugin : plugins.toArray())
      result.putAll(((PortletContainerPlugin) plugin).getAllPortletMetaData());
    return result;
  }

  /**
   * Overridden method.
   *
   * @param portletAppName app name
   * @return PortletApp object
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getPortletApp(java.lang.String)
   */
  public final PortletApp getPortletApp(final String portletAppName) {
    return findPluginByPAPPName(portletAppName).getPortletApp(portletAppName);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param portletAppName app name
   * @param portletName portlet name
   * @param locale locale
   * @return resource bundle
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getBundle(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.lang.String,
   *      java.lang.String, java.util.Locale)
   */
  public final ResourceBundle getBundle(final HttpServletRequest request,
      final HttpServletResponse response,
      final String portletAppName,
      final String portletName,
      final Locale locale) throws PortletContainerException {
    return findPluginByPAPPName(portletAppName).getBundle(request, response, portletAppName,
        portletName, locale);
  }

  /**
   * @param papp portlet app
   * @return plugin object
   */
  protected final PortletContainerPlugin findPluginByPAPPName(final String papp) {
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

  /**
   * Overridden method.
   *
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#setPortletPreference(org.exoplatform.services.portletcontainer.pci.Input,
   *      java.util.Map)
   */
  public final void setPortletPreference(final Input input, final Map<String, String> preferences) throws PortletContainerException {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID()
        .getPortletApplicationName());
    plugin.setPortletPreference(input, preferences);
  }

  /**
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  public void setPortletPreference2(Input input, Map<String, String[]> preferences) throws PortletContainerException {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID()
        .getPortletApplicationName());
    plugin.setPortletPreference2(input, preferences);
  }

  /**
   * Overridden method.
   *
   * @param input input
   * @return preferences map
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#getPortletPreference(org.exoplatform.services.portletcontainer.pci.Input)
   */
  public final Map<String, String[]> getPortletPreference(final Input input) {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID()
        .getPortletApplicationName());
    return plugin.getPortletPreference(input);
  }

  /**
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  public void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID()
        .getPortletApplicationName());
    plugin.setPortletPreferences(input, preferences);
  }

  /**
   * @param input input
   * @return portlet preferences
   */
  public PortletPreferences getPortletPreferences(Input input) {
    PortletContainerPlugin plugin = findPluginByPAPPName(input.getInternalWindowID()
        .getPortletApplicationName());
    return plugin.getPortletPreferences(input);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#processAction(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.ActionInput)
   */
  public final ActionOutput processAction(final HttpServletRequest request,
      final HttpServletResponse response,
      final ActionInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName())
        .processAction(request, response, input);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#processEvent(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.EventInput)
   */
  public final EventOutput processEvent(final HttpServletRequest request,
      final HttpServletResponse response,
      final EventInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName())
        .processEvent(request, response, input);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#serveResource(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.ResourceInput)
   */
  public final ResourceOutput serveResource(final HttpServletRequest request,
      final HttpServletResponse response,
      final ResourceInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName())
        .serveResource(request, response, input);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#render(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.RenderInput)
   */
  public final RenderOutput render(final HttpServletRequest request,
      final HttpServletResponse response,
      final RenderInput input) throws PortletContainerException {
    return findPluginByPAPPName(input.getInternalWindowID().getPortletApplicationName()).render(
        request, response, input);
  }

  // sessions replication
  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param attrs attrs
   * @param portletApplicationName app name
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#sendAttrs(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.util.Map,
   *      java.lang.String)
   */
  public final void sendAttrs(final HttpServletRequest request,
      final HttpServletResponse response,
      final Map<String, Object> attrs,
      final String portletApplicationName) throws PortletContainerException {
    findPluginByPAPPName(portletApplicationName).sendAttrs(request, response, attrs,
        portletApplicationName);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName app name
   * @param eventName event name
   * @param payload payload
   * @return do the types match
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerService#isEventPayloadTypeMatches(java.lang.String,
   *      javax.xml.namespace.QName, java.lang.Object)
   */
  public final boolean isEventPayloadTypeMatches(final String portletAppName,
      final QName eventName,
      final Object payload) throws PortletContainerException {
    return findPluginByPAPPName(portletAppName).isEventPayloadTypeMatches(portletAppName,
        eventName, payload);
  }

}
