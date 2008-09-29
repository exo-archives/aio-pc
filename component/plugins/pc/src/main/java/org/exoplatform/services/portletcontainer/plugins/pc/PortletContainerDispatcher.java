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
package org.exoplatform.services.portletcontainer.plugins.pc;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.WindowState;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.Environment;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerPlugin;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.CustomPortletMode;
import org.exoplatform.services.portletcontainer.pci.model.EventDefinition;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.Util;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.persistenceImp.PersistenceManager;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 */
public class PortletContainerDispatcher implements PortletContainerPlugin {

  /**
   * Input attribute.
   */
  public static final String              INPUT                    = "org.exoplatform.services.portletcontainer.pci.Input";

  /**
   * Output attribute.
   */
  public static final String              OUTPUT                   = "org.exoplatform.services.portletcontainer.pci.Output";

  /**
   * Window info attribute.
   */
  public static final String              WINDOW_INFO              = "org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.PortletWindowInternal";

  /**
   * Action type attribute.
   */
  public static final String              IS_ACTION                = "org.exoplatform.services.portletcontainer.plugins.pc.isAction";

  /**
   * Is to get bundle attribute.
   */
  public static final String              IS_TO_GET_BUNDLE         = "org.exoplatform.services.portletcontainer.plugins.pc.isToGetBundle";

  /**
   * Locale attribute.
   */
  public static final String              LOCALE_FOR_BUNDLE        = "org.exoplatform.services.portletcontainer.plugins.pc.LocaleForBundle";

  /**
   * Bundle attribute.
   */
  public static final String              BUNDLE                   = "org.exoplatform.services.portletcontainer.plugins.pc.Bundle";

  /**
   * Portlet app name attribute.
   */
  public static final String              PORTLET_APPLICATION_NAME = "org.exoplatform.services.portletcontainer.plugins.pc.PortletAppName";

  /**
   * Portlet name attribute.
   */
  public static final String              PORTLET_NAME             = "org.exoplatform.services.portletcontainer.plugins.pc.PortletName";

  /**
   * Container attribute.
   */
  public static final String              CONTAINER                = "org.exoplatform.container.ExoContainer";

  /**
   * Servlet mapping attribute.
   */
  public static final String              SERVLET_MAPPING          = "/PortletWrapper";

  /**
   * Attrs attribute.
   */
  public static final String              ATTRS                    = "org.exoplatform.services.portletcontainer.pci.ATTRS";

  /**
   * PC conf.
   */
  private final PortletContainerConf      config;

  /**
   * Persistence manager.
   */
  private final PersistenceManager        manager;

  /**
   * Application holder.
   */
  private final PortletApplicationsHolder portletApplications;

  /**
   * Standalone app handler.
   */
  private final PortletApplicationHandler standAloneHandler;

  /**
   * Logger.
   */
  private static final Log                       LOG                      = ExoLogger.getLogger(PortletContainerDispatcher.class);

  /**
   * Exo container.
   */
  protected ExoContainer                  container;

  /**
   * Plugin name.
   */
  private String                          pluginName;

  /**
   * @param containerConf PC conf
   * @param manager persistence manager
   * @param holder application holder
   * @param standAloneHandler standalone app handler
   * @param context exo container context
   */
  public PortletContainerDispatcher(final PortletContainerConf containerConf,
                                    final PersistenceManager manager,
                                    final PortletApplicationsHolder holder,
                                    final PortletApplicationHandler standAloneHandler,
                                    final ExoContainerContext context) {
    this.portletApplications = holder;
    this.config = containerConf;
    this.manager = manager;
    this.standAloneHandler = standAloneHandler;
    this.container = context.getContainer();
  }

  /**
   * Overridden method.
   * 
   * @param name plugin name
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#setName(java.lang.String)
   */
  public final void setName(final String name) {
    pluginName = name;
  }

  /**
   * Overridden method.
   * 
   * @return plugin name
   * @see org.exoplatform.container.component.ComponentPlugin#getName()
   */
  public final String getName() {
    return pluginName;
  }

  /**
   * Overridden method.
   * 
   * @param description description
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#setDescription(java.lang.String)
   */
  public final void setDescription(final String description) {
    config.setDescription(description);
  }

  /**
   * Overridden method.
   * 
   * @return description
   * @see org.exoplatform.container.component.ComponentPlugin#getDescription()
   */
  public final String getDescription() {
    return config.getDescription();
  }

  /**
   * Overridden method.
   * 
   * @param majorVersion major version
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#setMajorVersion(int)
   */
  public final void setMajorVersion(final int majorVersion) {
    config.setMajorVersion(majorVersion);
  }

  /**
   * Overridden method.
   * 
   * @param minorVersion minor version
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#setMinorVersion(int)
   */
  public final void setMinorVersion(final int minorVersion) {
    config.setMinorVersion(minorVersion);
  }

  /**
   * Overridden method.
   * 
   * @param properties properties
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#setProperties(java.util.Map)
   */
  public final void setProperties(final Map<String, String> properties) {
    config.setProperties(properties);
  }

  /**
   * Overridden method.
   * 
   * @return portlet modes
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getSupportedPortletModes()
   */
  public final Collection<PortletMode> getSupportedPortletModes() {
    return Collections.unmodifiableCollection(Collections.list(config.getSupportedPortletModes()));
  }

  /**
   * Overridden method.
   * 
   * @return window states
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getSupportedWindowStates()
   */
  public final Collection<WindowState> getSupportedWindowStates() {
    return Collections.unmodifiableCollection(Collections.list(config.getSupportedWindowStates()));
  }

  /**
   * Overridden method.
   * 
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @return portlet modes
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getPortletModes(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public final Collection<PortletMode> getPortletModes(final String portletAppName,
                                                       final String portletName,
                                                       final String markup) {
    Collection<PortletMode> filteredModes = new ArrayList<PortletMode>();
    Collection<PortletMode> nonFilteredModes = portletApplications.getPortletModes(portletAppName,
                                                                                   portletName,
                                                                                   markup);
    for (PortletMode mode : nonFilteredModes) {
      Enumeration<PortletMode> portalModes = config.getSupportedPortletModes();
      while (portalModes.hasMoreElements()) {
        PortletMode portalMode = portalModes.nextElement();
        if (mode.equals(portalMode))
          filteredModes.add(mode);
      }
    }
    return filteredModes;
  }

  public final String[] getPortalManagedPortletModes(final String portletAppName,
                                                     final String portletName) {
    PortletApp portletApp = getPortletApp(portletAppName);
    List<Supports> supports = portletApp.getPortlet(portletName).getSupports();
    List<CustomPortletMode> customPortletModes = portletApp.getCustomPortletMode();
    List<String> resultManagedModes = new ArrayList<String>();
    for (Supports support : supports) {
      List<String> portletModes = support.getPortletMode();
      for (String mode : portletModes) {
        for (CustomPortletMode customPortletMode : customPortletModes) {
          if (mode.equalsIgnoreCase(customPortletMode.getPortletMode())) {
            if (customPortletMode.isPortalManaged()) {
              resultManagedModes.add(customPortletMode.getPortletMode());
            }
          }
        }
      }
    }
    return (String[]) resultManagedModes.toArray(new String[resultManagedModes.size()]);
  }

  /**
   * Overridden method.
   * 
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @param mode portlet mode
   * @return is portlet mode supported
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#isModeSuported(java.lang.String,
   *      java.lang.String, java.lang.String, javax.portlet.PortletMode)
   */
  public final boolean isModeSuported(final String portletAppName,
                                      final String portletName,
                                      final String markup,
                                      final PortletMode mode) {
    boolean isPortalMode = false;
    Enumeration<PortletMode> portalModes = config.getSupportedPortletModes();
    while (portalModes.hasMoreElements()) {
      PortletMode portalMode = portalModes.nextElement();
      if (portalMode.equals(mode)) {
        isPortalMode = true;
        break;
      }
    }
    return portletApplications.isModeSuported(portletAppName, portletName, markup, mode)
        && isPortalMode;
  }

  /**
   * Overridden method.
   * 
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @return window states
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getWindowStates(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public final Collection<WindowState> getWindowStates(final String portletAppName,
                                                       final String portletName,
                                                       final String markup) {
    Collection<WindowState> filteredStates = new ArrayList<WindowState>();
    Collection<WindowState> nonFilteredStates = portletApplications.getWindowStates(portletAppName,
                                                                                    portletName,
                                                                                    markup);
    for (WindowState state : nonFilteredStates) {
      Enumeration<WindowState> portalStates = config.getSupportedWindowStates();
      while (portalStates.hasMoreElements()) {
        WindowState portalState = portalStates.nextElement();
        if (state.equals(portalState))
          filteredStates.add(state);
      }
    }
    return filteredStates;
  }

  /**
   * Overridden method.
   * 
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @param state window state
   * @return is window state supported
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#isStateSupported(java.lang.String,
   *      java.lang.String, java.lang.String, javax.portlet.WindowState)
   */
  public final boolean isStateSupported(final String portletAppName,
                                        final String portletName,
                                        final String markup,
                                        final WindowState state) {
    boolean isPortalState = false;
    Enumeration<WindowState> portalStates = config.getSupportedWindowStates();
    while (portalStates.hasMoreElements()) {
      WindowState portalState = portalStates.nextElement();
      if (portalState.equals(state)) {
        isPortalState = true;
        break;
      }
    }
    return portletApplications.isStateSupported(portletAppName, portletName, markup, state)
        && isPortalState;
  }

  /**
   * Overridden method.
   * 
   * @return all portlet metadata
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getAllPortletMetaData()
   */
  public final Map<String, PortletData> getAllPortletMetaData() {
    return portletApplications.getAllPortletMetaData();
  }

  /**
   * Get portlet app names.
   * 
   * @return collection of string
   */
  public final Collection<String> getPortletAppNames() {
    return portletApplications.getPortletAppNames();
  }

  /**
   * Overridden method.
   * 
   * @param portletAppName portlet application name
   * @return portlet app object
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getPortletApp(java.lang.String)
   */
  public final PortletApp getPortletApp(final String portletAppName) {
    return portletApplications.getPortletApplication(portletAppName);
  }

  /**
   * Overridden method.
   * 
   * @param portletAppName portlet application name
   * @param eventName event name
   * @param payload payload
   * @return is payload type matches
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#isEventPayloadTypeMatches(java.lang.String,
   *      javax.xml.namespace.QName, Serializable)
   */
  public final boolean isEventPayloadTypeMatches(final String portletAppName,
                                                 final QName eventName,
                                                 final Serializable payload) {
    PortletApp portletApp = portletApplications.getPortletApplication(portletAppName);
    List<EventDefinition> eds = portletApp.getEventDefinition();
    return isEventPayloadTypeMatches(eds, payload, eventName);
  }

  /**
   * Is event payload type matches. The portlet can send events which are not
   * declared in the portlet deployment descriptor at runtime using the setEvent
   * method on either the ActionResponse or EventResponse. cxlii.
   * 
   * @param eds
   * @param payload
   * @param eventName
   * @return boolean
   */
  public static boolean isEventPayloadTypeMatches(final List<EventDefinition> eds,
                                                  final java.io.Serializable payload,
                                                  final QName eventName) {
    if (eds == null || payload == null)
      return true;
    for (EventDefinition ed : eds) {
      if (!ed.getPrefferedName().equals(eventName) || ed.getAliases() == null
          || !ed.getAliases().contains(eventName))
        continue;
      if (ed.getJavaClass() == null) {
        return true;
      }
      Class jc = null;
      try {
        jc = payload.getClass().getClassLoader().loadClass(ed.getJavaClass());
      } catch (Exception e1) {
        try {
          jc = Class.forName(ed.getJavaClass(), true, payload.getClass().getClassLoader());
        } catch (Exception e2) {
        }
      }
      if (LOG.isDebugEnabled())
        LOG.debug("Event loaded class for eventName: '" + eventName + "' is: " + jc);
      if (jc != null) {
        return jc.isInstance(payload);
      }
      return false;
    }
    return true;
  }

  /**
   * Overridden method.
   * 
   * @param input input
   * @param preferencesMap preferences map
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#setPortletPreference(org.exoplatform.services.portletcontainer.pci.Input,
   *      java.util.Map)
   */
  public final void setPortletPreference(final Input input, final Map<String, String> preferencesMap) throws PortletContainerException {
    LOG.debug("try to set a portlet preference directly from the setPortletPreference() method");
    WindowID windowID = input.getInternalWindowID();
    Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(),
                                                            windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    PortletPreferencesImp preferences = (PortletPreferencesImp) windowInfos.getPreferences();
    // to allow restore of previous versions if a problem occurs
    preferences.setMethodCalledIsAction(/* true */PCConstants.ACTION_INT);
    Set<String> keys = preferencesMap.keySet();
    try {
      for (String key : keys) {
        try {
          preferences.setValue(key, preferencesMap.get(key));
        } catch (ReadOnlyException e) {
          LOG.error("Can not set a property that has a ReadOnly tag set to true", e);
        }
      }
      preferences.store();
    } catch (Exception e) {
      LOG.error("Can not store a portlet preference", e);
      throw new PortletContainerException(e);
    }
  }

  /**
   * Overridden method.
   * 
   * @param input input
   * @return portlet preference
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getPortletPreference(org.exoplatform.services.portletcontainer.pci.Input)
   */
  public final Map<String, String[]> getPortletPreference(final Input input) {
    return getPortletPreferences(input).getMap();
  }

  public PortletPreferences getPortletPreferences(Input input) {
    LOG.debug("Try to get a portlet preference directly from the getPortletPreference() method ");
    WindowID windowID = input.getInternalWindowID();
    Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(),
                                                            windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    PortletPreferencesImp preferences = (PortletPreferencesImp) windowInfos.getPreferences();
    return preferences;
  }

  public void setPortletPreference2(Input input, Map<String, String[]> preferencesMap) throws PortletContainerException {
    LOG.debug("try to set a portlet preference directly from the setPortletPreference2() method");
    WindowID windowID = input.getInternalWindowID();
    Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(),
                                                            windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    PortletPreferencesImp preferences = (PortletPreferencesImp) windowInfos.getPreferences();
    // to allow restore of previous versions if a problem occurs
    preferences.setMethodCalledIsAction(/* true */PCConstants.ACTION_INT);
    Set<String> keys = preferencesMap.keySet();
    try {
      for (String key : keys) {
        try {
          preferences.setValues(key, preferencesMap.get(key));
        } catch (ReadOnlyException e) {
          LOG.error("Can not set a property that has a ReadOnly tag set to true", e);
        }
      }
      preferences.store();
    } catch (Exception e) {
      LOG.error("Can not store a portlet preference", e);
      throw new PortletContainerException(e);
    }
  }

  public void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException {
    LOG.debug("try to set a portlet preference directly from the setPortletPreferences() method");
    WindowID windowID = input.getInternalWindowID();
    Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(),
                                                            windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    windowInfos.setPreferences(preferences);
  }

  /**
   * Overridden method.
   * 
   * @param request request
   * @param response response
   * @param portletAppName portlet application name
   * @param portletName portlet name
   * @param locale locale
   * @return resource bundle
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#getBundle(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.lang.String,
   *      java.lang.String, java.util.Locale)
   */
  public final java.util.ResourceBundle getBundle(final HttpServletRequest request,
                                                  final HttpServletResponse response,
                                                  final String portletAppName,
                                                  final String portletName,
                                                  final Locale locale) throws PortletContainerException {
    LOG.debug("Try to get a bundle object for locale : " + locale);

    int platform = Environment.getInstance().getPlatform();
    if (platform == Environment.STAND_ALONE) {
      URLClassLoader oldCL = (URLClassLoader) Thread.currentThread().getContextClassLoader();
      initTests();
      try {
        return standAloneHandler.getBundle(portletAppName, portletName, locale);
      } finally {
        Thread.currentThread().setContextClassLoader(oldCL);
      }
    }

    request.setAttribute(PortletContainerDispatcher.CONTAINER, container.getContext().getName());
    request.setAttribute(PortletContainerDispatcher.IS_TO_GET_BUNDLE, new Boolean(true));
    request.setAttribute(PortletContainerDispatcher.LOCALE_FOR_BUNDLE, locale);
    request.setAttribute(PortletContainerDispatcher.PORTLET_APPLICATION_NAME, portletAppName);
    request.setAttribute(PortletContainerDispatcher.PORTLET_NAME, portletName);
    dispatch(request, response, portletAppName);
    java.util.ResourceBundle bundle = (java.util.ResourceBundle) request.getAttribute(PortletContainerDispatcher.BUNDLE);
    request.removeAttribute(PortletContainerDispatcher.CONTAINER);
    request.removeAttribute(PortletContainerDispatcher.IS_TO_GET_BUNDLE);
    request.removeAttribute(PortletContainerDispatcher.LOCALE_FOR_BUNDLE);
    request.removeAttribute(PortletContainerDispatcher.PORTLET_APPLICATION_NAME);
    request.removeAttribute(PortletContainerDispatcher.PORTLET_NAME);
    request.removeAttribute(PortletContainerDispatcher.BUNDLE);
    return bundle;
  }

  /**
   * Overridden method.
   * 
   * @param httpServletRequest request
   * @param httpServletResponse response
   * @param actionInput input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#processAction(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.ActionInput)
   */
  public final ActionOutput processAction(final HttpServletRequest httpServletRequest,
                                          final HttpServletResponse httpServletResponse,
                                          final ActionInput actionInput) throws PortletContainerException {
    LOG.debug("ProcessAction method in PortletContainerDispatcher entered");
    return (ActionOutput) process(httpServletRequest,
                                  httpServletResponse,
                                  actionInput,
                                  PCConstants.ACTION_INT);
  }

  /**
   * Overridden method.
   * 
   * @param httpServletRequest request
   * @param httpServletResponse response
   * @param eventInput input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#processEvent(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.EventInput)
   */
  public final EventOutput processEvent(final HttpServletRequest httpServletRequest,
                                        final HttpServletResponse httpServletResponse,
                                        final EventInput eventInput) throws PortletContainerException {
    LOG.debug("ProcessEvent method in PortletContainerDispatcher entered");
    return (EventOutput) process(httpServletRequest,
                                 httpServletResponse,
                                 eventInput,
                                 PCConstants.EVENT_INT);
  }

  /**
   * Overridden method.
   * 
   * @param httpServletRequest requets
   * @param httpServletResponse response
   * @param renderInput input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#render(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.RenderInput)
   */
  public final RenderOutput render(final HttpServletRequest httpServletRequest,
                                   final HttpServletResponse httpServletResponse,
                                   final RenderInput renderInput) throws PortletContainerException {
    LOG.debug("Render method in PortletContainerDispatcher entered");
    return (RenderOutput) process(httpServletRequest,
                                  httpServletResponse,
                                  renderInput,
                                  PCConstants.RENDER_INT);
  }

  /**
   * Overridden method.
   * 
   * @param httpServletRequest request
   * @param httpServletResponse response
   * @param resourceInput input
   * @return output
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#serveResource(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse,
   *      org.exoplatform.services.portletcontainer.pci.ResourceInput)
   */
  public final ResourceOutput serveResource(final HttpServletRequest httpServletRequest,
                                            final HttpServletResponse httpServletResponse,
                                            final ResourceInput resourceInput) throws PortletContainerException {
    LOG.debug("ServeResource method in PortletContainerDispatcher entered");
    return (ResourceOutput) process(httpServletRequest,
                                    httpServletResponse,
                                    resourceInput,
                                    PCConstants.RESOURCE_INT);
  }

  /**
   * Overridden method.
   * 
   * @param request request
   * @param response response
   * @param attrs attrs
   * @param portletApplicationName portlet application name
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletContainerPlugin#sendAttrs(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.util.Map,
   *      java.lang.String)
   */
  public final void sendAttrs(final HttpServletRequest request,
                              final HttpServletResponse response,
                              final Map<String, Object> attrs,
                              final String portletApplicationName) throws PortletContainerException {
    request.setAttribute(PortletContainerDispatcher.CONTAINER, container.getContext().getName());
    try {
      Set<Entry<String, Object>> s = attrs.entrySet();
      Iterator<Entry<String, Object>> it = s.iterator();
      while (it.hasNext()) {
        Map.Entry<String, Object> entry = it.next();
        if (entry.getValue() == null) {
          it.remove();
          request.removeAttribute(entry.getKey());
        }
      }
      request.setAttribute(PortletContainerDispatcher.ATTRS, attrs);
      int platform = Environment.getInstance().getPlatform();
      if (platform == Environment.STAND_ALONE) {
      } else
        dispatch(request, response, portletApplicationName);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param request request
   * @param response response
   * @param input input
   * @param isAction action type
   * @return output
   * @throws PortletContainerException exception
   */
  private Output process(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final Input input,
                         final int isAction) throws PortletContainerException {
    LOG.debug("Process method in PortletContainerDispatcher entered");
    LOG.debug("Encoding used : " + request.getCharacterEncoding());
    // create the ActionOutput object
    Output output = null;
    if (isAction == PCConstants.ACTION_INT)
      output = new ActionOutput();
    else if (isAction == PCConstants.EVENT_INT)
      output = new EventOutput();
    else if (isAction == PCConstants.RESOURCE_INT)
      output = new ResourceOutput();
    else
      output = new RenderOutput();

    // create a PortletPreferencesImp object
    PortletWindowInternal windowInfos = getWindowInfos(request, input, isAction);
    String portletApplicationName = windowInfos.getWindowID().getPortletApplicationName();

    request.setAttribute(PortletContainerDispatcher.CONTAINER, container.getContext().getName());
    request.setAttribute(PortletContainerDispatcher.INPUT, input);
    request.setAttribute(PortletContainerDispatcher.OUTPUT, output);
    request.setAttribute(PortletContainerDispatcher.WINDOW_INFO, windowInfos);
    request.setAttribute(PortletContainerDispatcher.IS_ACTION, Util.actionToString(isAction));

    int platform = Environment.getInstance().getPlatform();
    if (platform == Environment.STAND_ALONE) {
      LOG.debug("Stand alone environement : direct call to handler");
      URLClassLoader oldCL = (URLClassLoader) Thread.currentThread().getContextClassLoader();
      initTests();
      try {
        ServletContext portalContext = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);
        standAloneHandler.process(portalContext,
                                  request,
                                  response,
                                  input,
                                  output,
                                  windowInfos,
                                  isAction);
      } finally {
        Thread.currentThread().setContextClassLoader(oldCL);
      }
    } else {
      LOG.debug("Embded environement : use servlet dispatcher to access handler");
      try {
        dispatch(request, response, portletApplicationName);
      } finally {
        ((PortletPreferencesImp) windowInfos.getPreferences()).discard();
      }
    }
    if (input.isStateSaveOnClient() && (isAction == PCConstants.ACTION_INT))
      try {
        LOG.debug("Serialize Portlet Preferences object to store it on the client");
        ((ActionOutput) output).setPortletState(IOUtil.serialize(windowInfos.getPreferences()));
      } catch (Exception e) {
        LOG.error("Can not serialize Portlet Preferences", e);
        throw new PortletContainerException("Can not serialize Portlet Preferences", e);
      }
    return output;
  }

  /**
   * @param request request
   * @param input input
   * @param isAction action type
   * @return portlet window internal object
   */
  private PortletWindowInternal getWindowInfos(final HttpServletRequest request,
                                               final Input input,
                                               final int isAction) {
    boolean stateChangeAuthorized = true;
    if (isAction == PCConstants.ACTION_INT)
      stateChangeAuthorized = ((ActionInput) input).isStateChangeAuthorized();

    PortletWindowInternal windowInfos = null;
    if (!input.isStateSaveOnClient()) {// state save on the server
      LOG.debug("Extract or create windows info (store in the server)");
      WindowInfosContainer windowInfosContainer = WindowInfosContainer.getInstance();
      String key = "SESSION_CONTAINER_KEY_ENCODER" + input.getInternalWindowID().generateKey();
      if (windowInfosContainer.getInfos(key) != null)
        windowInfos = windowInfosContainer.getInfos(key);
      else {
        WindowID windowID = input.getInternalWindowID();
        Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(),
                                                                windowID.getPortletName());
        ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
        windowInfos = manager.getWindow(input, defaultPrefs);
        windowInfosContainer.addInfos(key, windowInfos);
      }
    } else { // state change kept on the client (for example consumer in WSRP)
      LOG.debug("Extract or create windows info (sent by the client)");
      WindowID windowID = input.getInternalWindowID();
      Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(),
                                                              windowID.getPortletName());
      ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
      windowInfos = manager.getWindow(input, defaultPrefs);
    }
    ((PortletPreferencesImp) windowInfos.getPreferences()).setMethodCalledIsAction(isAction);
    ((PortletPreferencesImp) windowInfos.getPreferences()).setStateChangeAuthorized(stateChangeAuthorized);
    ((PortletPreferencesImp) windowInfos.getPreferences()).setStateSaveOnClient(input.isStateSaveOnClient());
    return windowInfos;
  }

  /**
   * @param request request
   * @param response response
   * @param portletApplicationName portlet application name
   * @throws PortletContainerException exception
   */
  private void dispatch(final HttpServletRequest request,
                        final HttpServletResponse response,
                        final String portletApplicationName) throws PortletContainerException {
    ServletContext portalContext = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);
    ServletContext portletContext = portalContext.getContext("/" + portletApplicationName);
    if (portletContext == null)
      LOG.error("Can't get servlet context for portlet app ["
          + portletApplicationName
          + "]. May be it's caused "
          + "by difference between context name (usually WAR file name) and content of tag <display-name/> in your "
          + "WEB-INF/web.xml");
    RequestDispatcher dispatcher = portletContext.getRequestDispatcher(PortletContainerDispatcher.SERVLET_MAPPING);
    try {
      LOG.debug("Dispatch request to the portlet application : " + portletApplicationName);

      dispatcher.include(request, response);
    } catch (ServletException e) {
      LOG.error("Servlet exception while dispatching to portlet", e);
      throw new PortletContainerException(e);
    } catch (IOException e) {
      LOG.error("In and out exception while dispatching to portlet", e);
      throw new PortletContainerException(e);
    }
  }

  /**
   * Init tests.
   */
  private void initTests() {
//     defined for test purposes
    final String PORTLET_APP_PATH = "file:./war_template/";
    String PORTLET_APP_PATH2 = "file:" + System.getProperty("testPath") + "/war_template";

    try {
      URL[] URLs = { new URL(PORTLET_APP_PATH + "WEB-INF/classes/"),
          new URL("file:./lib/portlet-api.jar"), new URL(PORTLET_APP_PATH + "WEB-INF/lib/") };
//     Thread.currentThread().setContextClassLoader(new URLClassLoader(URLs));
      Thread.currentThread()
            .setContextClassLoader(new URLClassLoader(URLs, Thread.currentThread()
                                                                  .getContextClassLoader()));
    } catch (MalformedURLException e) {
      LOG.error("Can not init tests", e);
    }
  }

  /**
   * Init tests.
   */
  private void initTests2() {
    // defined for test purposes
    // protected static final String PORTLET_APP_PATH = "file:./war_template2/";
    // String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") +
    // "/war_template2";

    // try {
    // URL[] URLs = { new URL(PORTLET_APP_PATH + "WEB-INF/classes/"), new
    // URL("file:./lib/portlet-api-2.0.jar"), new URL(PORTLET_APP_PATH +
    // "WEB-INF/lib/") };
    // Thread.currentThread().setContextClassLoader(new URLClassLoader(URLs));
    // } catch (MalformedURLException e) {
    // LOG.error("Can not init tests 2", e);
    // }
  }

}
