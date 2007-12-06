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
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerPlugin;
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
import org.exoplatform.services.portletcontainer.pci.model.EventDefinition;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.persistenceImp.PersistenceManager;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 */
public class PortletContainerDispatcher implements PortletContainerPlugin {

  public static final String        INPUT                    = "org.exoplatform.services.portletcontainer.pci.Input";

  public static final String        OUTPUT                   = "org.exoplatform.services.portletcontainer.pci.Output";

  public static final String        WINDOW_INFO              = "org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.PortletWindowInternal";

  public static final String        IS_ACTION                = "org.exoplatform.services.portletcontainer.plugins.pc.isAction";

  public static final String        IS_TO_GET_BUNDLE         = "org.exoplatform.services.portletcontainer.plugins.pc.isToGetBundle";

  public static final String        LOCALE_FOR_BUNDLE        = "org.exoplatform.services.portletcontainer.plugins.pc.LocaleForBundle";

  public static final String        BUNDLE                   = "org.exoplatform.services.portletcontainer.plugins.pc.Bundle";

  public static String              PORTLET_APPLICATION_NAME = "org.exoplatform.services.portletcontainer.plugins.pc.PortletAppName";

  public static String              PORTLET_NAME             = "org.exoplatform.services.portletcontainer.plugins.pc.PortletName";

  public static final String        CONTAINER                = "org.exoplatform.container.ExoContainer";

  public static final String        SERVLET_MAPPING          = "/PortletWrapper";

  public static final String        ATTRS                    = "org.exoplatform.services.portletcontainer.pci.ATTRS";

  private PortletContainerConf      config;

  private PersistenceManager        manager;

  private PortletApplicationsHolder portletApplications;

  private PortletApplicationHandler standAloneHandler;

  private Log                       log;

  protected ExoContainer            container;

  private String                    pluginName;

  public PortletContainerDispatcher(PortletContainerConf containerConf,
                                    PersistenceManager manager,
                                    PortletApplicationsHolder holder,
                                    PortletApplicationHandler standAloneHandler,
                                    ExoContainerContext context) {
    this.portletApplications = holder;
    this.config = containerConf;
    this.manager = manager;
    this.standAloneHandler = standAloneHandler;
    this.container = context.getContainer();
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  }

  public void setName(String name) {
    pluginName = name;
  }

  public String getName() {
    return pluginName;
  }

  public void setDescription(String description) {
    config.setDescription(description);
  }

  public String getDescription() {
    return config.getDescription();
  }

  public void setMajorVersion(int majorVersion) {
    config.setMajorVersion(majorVersion);
  }

  public void setMinorVersion(int minorVersion) {
    config.setMinorVersion(minorVersion);
  }

  public void setProperties(Map<String, String> properties) {
    config.setProperties(properties);
  }

  // public void setSupportedPortletModesWithDescriptions(Collection
  // portletModes) {
  // config.setCustomModesWithDescriptions(portletModes);
  // }

  // public void setSupportedWindowStatesWithDescriptions(Collection
  // customStates) {
  // config.setCustomStatesWithDescriptions(customStates);
  // }

  // public Collection getSupportedPortletModesWithDescriptions() {
  // return config.getSupportedPortletModesWithDescriptions();
  // }

  // public Collection getSupportedWindowStatesWithDescriptions() {
  // return config.getSupportedWindowStatesWithDescriptions();
  // }

  public Collection<PortletMode> getSupportedPortletModes() {
    return Collections.unmodifiableCollection(Collections.list(config.getSupportedPortletModes()));
  }

  public Collection<WindowState> getSupportedWindowStates() {
    return Collections.unmodifiableCollection(Collections.list(config.getSupportedWindowStates()));
  }

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    Collection<PortletMode> filteredModes = new ArrayList<PortletMode>();
    Collection<PortletMode> nonFilteredModes = portletApplications.getPortletModes(portletAppName, portletName, markup);
    for (Iterator<PortletMode> iter = nonFilteredModes.iterator(); iter.hasNext();) {
      PortletMode mode = iter.next();
      Enumeration<PortletMode> portalModes = config.getSupportedPortletModes();
      while (portalModes.hasMoreElements()) {
        PortletMode portalMode = portalModes.nextElement();
        if (mode.equals(portalMode))
          filteredModes.add(mode);
      }
    }
    return filteredModes;
  }

  public boolean isModeSuported(String portletAppName,
                                String portletName,
                                String markup,
                                PortletMode mode) {
    boolean isPortalMode = false;
    Enumeration<PortletMode> portalModes = config.getSupportedPortletModes();
    while (portalModes.hasMoreElements()) {
      PortletMode portalMode = portalModes.nextElement();
      if (portalMode.equals(mode)) {
        isPortalMode = true;
        break;
      }
    }
    return portletApplications.isModeSuported(portletAppName, portletName, markup, mode) && isPortalMode;
  }

  public Collection<WindowState> getWindowStates(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    Collection<WindowState> filteredStates = new ArrayList<WindowState>();
    Collection<WindowState> nonFilteredStates = portletApplications.getWindowStates(portletAppName, portletName, markup);
    for (Iterator<WindowState> iter = nonFilteredStates.iterator(); iter.hasNext();) {
      WindowState state = iter.next();
      Enumeration<WindowState> portalStates = config.getSupportedWindowStates();
      while (portalStates.hasMoreElements()) {
        WindowState portalState = portalStates.nextElement();
        if (state.equals(portalState))
          filteredStates.add(state);
      }
    }
    return filteredStates;
  }

  public boolean isStateSupported(String portletAppName,
                                  String portletName,
                                  String markup,
                                  WindowState state) {
    boolean isPortalState = false;
    Enumeration<WindowState> portalStates = config.getSupportedWindowStates();
    while (portalStates.hasMoreElements()) {
      WindowState portalState = portalStates.nextElement();
      if (portalState.equals(state)) {
        isPortalState = true;
        break;
      }
    }
    return portletApplications.isStateSupported(portletAppName, portletName, markup, state) && isPortalState;
  }

  public Map<String, PortletData> getAllPortletMetaData() {
    return portletApplications.getAllPortletMetaData(container);
  }

  public boolean isEventPayloadTypeMatches(String portletAppName,
                                           QName eventName,
                                           Object payload) {
    PortletApp portletApp = portletApplications.getPortletApplication(portletAppName);
    List<EventDefinition> eds = portletApp.getEventDefinition();
    for (Iterator<EventDefinition> i = eds.iterator(); i.hasNext();) {
      EventDefinition ed = i.next();
      if (!eventName.equals(ed.getPrefferedName()))
        if (!ed.getAliases().contains(eventName))
          continue;
      if (ed.getJavaClass() != null) {
        Class jc;
        try {
          jc = Thread.currentThread().getContextClassLoader().loadClass(ed.getJavaClass());
        } catch (Exception e) {
          jc = null;
        }
        if (jc == null)
          try {
            jc = Class.forName(ed.getJavaClass(), true, payload.getClass().getClassLoader());
          } catch (Exception e) {
            jc = null;
          }
        if (jc != null)
          return jc.isInstance(payload);
        return false;
      } else
        return true;
    }
    return true;
  }

  public void setPortletPreference(Input input,
                                   Map<String, String> preferencesMap) throws PortletContainerException {
    log.debug("try to set a portlet preference directly from the setPortletPreference() method");
    WindowID windowID = input.getInternalWindowID();
    Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    PortletPreferencesImp preferences = (PortletPreferencesImp) windowInfos.getPreferences();
    preferences.setMethodCalledIsAction(/* true */PCConstants.actionInt);// to
    // allow
    // restore
    // of
    // previous
    // versions
    // if a
    // problem
    // occurs
    Set<String> keys = preferencesMap.keySet();
    try {
      for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
        String key = iter.next();
        try {
          preferences.setValue(key, preferencesMap.get(key));
        } catch (ReadOnlyException e) {
          log.error("Can not set a property that has a ReadOnly tag set to true", e);
        }
      }
      preferences.store();
    } catch (Exception e) {
      log.error("Can not store a portlet preference", e);
      throw new PortletContainerException(e);
    }
  }

  public Map<String, String[]> getPortletPreference(Input input) {
    log.debug("Try to get a portlet preference directly from the getPortletPreference() method ");
    WindowID windowID = input.getInternalWindowID();
    Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    PortletPreferencesImp preferences = (PortletPreferencesImp) windowInfos.getPreferences();
    return preferences.getMap();
  }

  public java.util.ResourceBundle getBundle(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String portletAppName,
                                            String portletName,
                                            Locale locale) throws PortletContainerException {
    log.debug("Try to get a bundle object for locale : " + locale);
    if (Environment.getInstance().getPlatform() == Environment.STAND_ALONE) {
      URLClassLoader oldCL = (URLClassLoader) Thread.currentThread().getContextClassLoader();
      initTests();
      try {
        return standAloneHandler.getBundle(portletAppName, portletName, locale);
      } finally {
        Thread.currentThread().setContextClassLoader(oldCL);
      }
    }

    request.setAttribute(CONTAINER, container.getContext().getName());
    request.setAttribute(IS_TO_GET_BUNDLE, new Boolean(true));
    request.setAttribute(LOCALE_FOR_BUNDLE, locale);
    request.setAttribute(PORTLET_APPLICATION_NAME, portletAppName);
    request.setAttribute(PORTLET_NAME, portletName);
    dispatch(request, response, portletAppName);
    java.util.ResourceBundle bundle = (java.util.ResourceBundle) request.getAttribute(BUNDLE);
    request.removeAttribute(CONTAINER);
    request.removeAttribute(IS_TO_GET_BUNDLE);
    request.removeAttribute(LOCALE_FOR_BUNDLE);
    request.removeAttribute(PORTLET_APPLICATION_NAME);
    request.removeAttribute(PORTLET_NAME);
    request.removeAttribute(BUNDLE);
    return bundle;
  }

  public ActionOutput processAction(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    ActionInput actionInput) throws PortletContainerException {
    log.debug("ProcessAction method in PortletContainerDispatcher entered");
    return (ActionOutput) process(httpServletRequest, httpServletResponse, actionInput, PCConstants.actionInt);
  }

  public EventOutput processEvent(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  EventInput eventInput) throws PortletContainerException {
    log.debug("ProcessEvent method in PortletContainerDispatcher entered");
    return (EventOutput) process(httpServletRequest, httpServletResponse, eventInput, PCConstants.eventInt);
  }

  public RenderOutput render(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             RenderInput renderInput) throws PortletContainerException {
    log.debug("Render method in PortletContainerDispatcher entered");
    // flush the current buffer
    // commented out by lautarul
    /*
     * try { httpServletResponse.flushBuffer(); } catch (IOException e) {
     * log.error("Can not flush servlet response buffer"); // throw new
     * PortletContainerException("Can not flush servlet response // buffer", e); }
     */
    return (RenderOutput) process(httpServletRequest, httpServletResponse, renderInput, PCConstants.renderInt);
  }

  public ResourceOutput serveResource(HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse,
                                      ResourceInput resourceInput) throws PortletContainerException {
    log.debug("ServeResource method in PortletContainerDispatcher entered");
    // flush the current buffer
    // commented by EXOMAN, because GenericPortlet#serveResource forward: Cannot
    // forward after response has been committed
    /*
     * try { httpServletResponse.flushBuffer(); } catch (IOException e) {
     * log.error("Can not flush servlet response buffer"); // throw new
     * PortletContainerException("Can not flush servlet response // buffer", e); }
     */
    return (ResourceOutput) process(httpServletRequest, httpServletResponse, resourceInput, PCConstants.resourceInt);
  }

  public void sendAttrs(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> attrs,
                        String portletApplicationName) throws PortletContainerException {
    request.setAttribute(CONTAINER, container.getContext().getName());

    Set<Entry<String, Object>> s = attrs.entrySet();
    Iterator<Entry<String, Object>> it = s.iterator();
    while (it.hasNext()) {

      Map.Entry<String, Object> entry = it.next();
      if (entry.getValue() == null) {
        attrs.remove(entry.getKey());
        request.removeAttribute(entry.getKey());
      }

    }

    request.setAttribute(ATTRS, attrs);
    if (Environment.getInstance().getPlatform() == Environment.STAND_ALONE) {
    } else {
      dispatch(request, response, portletApplicationName);
    }
  }

  private Output process(HttpServletRequest request,
                         HttpServletResponse response,
                         Input input,
                         int isAction) throws PortletContainerException {
    log.debug("Process method in PortletContainerDispatcher entered");
    log.debug("Encoding used : " + request.getCharacterEncoding());
    // create the ActionOutput object
    Output output = null;
    if (isAction == PCConstants.actionInt) {
      output = new ActionOutput();
    } else if (isAction == PCConstants.eventInt) {
      output = new EventOutput();
    } else if (isAction == PCConstants.resourceInt) {
      output = new ResourceOutput();
    } else {
      output = new RenderOutput();
    }

    // create a PortletPreferencesImp object
    PortletWindowInternal windowInfos = getWindowInfos(request, input, isAction);
    String portletApplicationName = windowInfos.getWindowID().getPortletApplicationName();

    request.setAttribute(CONTAINER, container.getContext().getName());
    request.setAttribute(INPUT, input);
    request.setAttribute(OUTPUT, output);
    request.setAttribute(WINDOW_INFO, windowInfos);
    request.setAttribute(IS_ACTION, PCConstants.actionToString(isAction));

    if (Environment.getInstance().getPlatform() == Environment.STAND_ALONE) {
      log.debug("Stand alone environement : direct call to handler");
      URLClassLoader oldCL = (URLClassLoader) Thread.currentThread().getContextClassLoader();
      initTests();
      try {
        ServletContext portalContext = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);
        standAloneHandler.process(portalContext, request, response, input, output, windowInfos, isAction);
      } finally {
        Thread.currentThread().setContextClassLoader(oldCL);
      }
    } else {
      log.debug("Embded environement : use servlet dispatcher to access handler");
      try {
        dispatch(request, response, portletApplicationName);
      } finally {
        ((PortletPreferencesImp) windowInfos.getPreferences()).discard();
      }
    }
    if (input.isStateSaveOnClient() && isAction == PCConstants.actionInt) {
      try {
        log.debug("Serialize Portlet Preferences object to store it on the client");
        ((ActionOutput) output).setPortletState(IOUtil.serialize(windowInfos.getPreferences()));
      } catch (Exception e) {
        log.error("Can not serialize Portlet Preferences", e);
        throw new PortletContainerException("Can not serialize Portlet Preferences", e);
      }
    }
    return output;
  }

  private PortletWindowInternal getWindowInfos(HttpServletRequest request,
                                               Input input,
                                               int isAction) {
    boolean stateChangeAuthorized = true;
    if (isAction == PCConstants.actionInt) {
      stateChangeAuthorized = ((ActionInput) input).isStateChangeAuthorized();
    }

    PortletWindowInternal windowInfos = null;
    if (!input.isStateSaveOnClient()) {// state save on the server
      log.debug("Extract or create windows info (store in the server)");
      WindowInfosContainer windowInfosContainer = WindowInfosContainer.getInstance();
      String key = "SESSION_CONTAINER_KEY_ENCODER" + input.getInternalWindowID().generateKey();
      if (windowInfosContainer.getInfos(key) != null) {
        windowInfos = windowInfosContainer.getInfos(key);
      } else {
        WindowID windowID = input.getInternalWindowID();
        Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
        ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
        windowInfos = manager.getWindow(input, defaultPrefs);
        windowInfosContainer.addInfos(key, windowInfos);
      }
    } else { // state change kept on the client (for example consumer in WSRP)
      log.debug("Extract or create windows info (sent by the client)");
      WindowID windowID = input.getInternalWindowID();
      Portlet pDatas = portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
      ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
      windowInfos = manager.getWindow(input, defaultPrefs);
    }
    ((PortletPreferencesImp) windowInfos.getPreferences()).setMethodCalledIsAction(isAction);
    ((PortletPreferencesImp) windowInfos.getPreferences()).setStateChangeAuthorized(stateChangeAuthorized);
    ((PortletPreferencesImp) windowInfos.getPreferences()).setStateSaveOnClient(input.isStateSaveOnClient());
    return windowInfos;
  }

  private void dispatch(HttpServletRequest request,
                        HttpServletResponse response,
                        String portletApplicationName) throws PortletContainerException {
    ServletContext portalContext = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);
    ServletContext portletContext = portalContext.getContext("/" + portletApplicationName);
    if (portletContext == null) {
      log.error("Can't get servlet context for portlet app [" + portletApplicationName + "]. May be it's caused "
          + "by difference between context name (usually WAR file name) and content of tag <display-name/> in your " + "WEB-INF/web.xml");
    }
    RequestDispatcher dispatcher = portletContext.getRequestDispatcher(SERVLET_MAPPING);
    try {
      log.debug("Dispatch request to the portlet application : " + portletApplicationName);

      dispatcher.include(request, response);
    } catch (ServletException e) {
      log.error("Servlet exception while dispatching to portlet", e);
      throw new PortletContainerException(e);
    } catch (IOException e) {
      log.error("In and out exception while dispatching to portlet", e);
      throw new PortletContainerException(e);
    }
  }

  private void initTests() {
    // defined for test purposes
    // protected static final String PORTLET_APP_PATH = "file:./war_template/";
    // String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") +
    // "/war_template";

    // try {
    // URL[] URLs = { new URL(PORTLET_APP_PATH + "WEB-INF/classes/"), new
    // URL("file:./lib/portlet-api.jar"), new URL(PORTLET_APP_PATH +
    // "WEB-INF/lib/") };
    // Thread.currentThread().setContextClassLoader(new URLClassLoader(URLs));
    // } catch (MalformedURLException e) {
    // log.error("Can not init tests", e);
    // }
  }

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
    // log.error("Can not init tests 2", e);
    // }
  }

}
