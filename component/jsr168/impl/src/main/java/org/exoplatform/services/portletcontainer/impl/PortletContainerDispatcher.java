/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.ReadOnlyException;
import javax.portlet.WindowState;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.Environment;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.persistenceImp.PersistenceManager;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 11:21:13
 */
public class PortletContainerDispatcher {
  
  public static final String INPUT = "org.exoplatform.services.portletcontainer.pci.Input";
  public static final String OUTPUT = "org.exoplatform.services.portletcontainer.pci.Output";
  public static final String WINDOW_INFO = "org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.PortletWindowInternal";
  public static final String IS_ACTION = "org.exoplatform.services.portletcontainer.impl.isAction";
  public static final String IS_TO_GET_BUNDLE = "org.exoplatform.services.portletcontainer.impl.isToGetBundle";
  public static final String LOCALE_FOR_BUNDLE = "org.exoplatform.services.portletcontainer.impl.LocaleForBundle";
  public static final String BUNDLE = "org.exoplatform.services.portletcontainer.impl.Bundle";
  public static String PORTLET_APPLICATION_NAME = "org.exoplatform.services.portletcontainer.impl.PortletAppName";
  public static String PORTLET_NAME = "org.exoplatform.services.portletcontainer.impl.PortletName";
  public static final String CONTAINER = "org.exoplatform.container.ExoContainer";
  public static final String SERVLET_MAPPING = "/PortletWrapper";
  public static final String ATTRS = "org.exoplatform.services.portletcontainer.pci.ATTRS";

  private PortletContainerConf containerConf;
  private PersistenceManager manager;
  private PortletApplicationsHolder portletApplications;
  private PortletApplicationHandler standAloneHandler;
  private Log log;
  protected ExoContainer cont;

  public PortletContainerDispatcher(PortletContainerConf containerConf,
                                    PersistenceManager manager, PortletApplicationsHolder holder,
                                    PortletApplicationHandler standAloneHandler,
                                    LogService logService,
                                    ExoContainerContext context) {
    portletApplications = holder;
    this.containerConf = containerConf;
    this.manager = manager;
    this.standAloneHandler = standAloneHandler;
    this.log = logService.getLog("org.exoplatform.services.portletcontainer");
    this.cont = context.getContainer();
  }

  public void setPortletContainerName(String containerName) {
    containerConf.setPortletContainerName(containerName);
  }

  public void setMajorVersion(int majorVersion) {
    containerConf.setMajorVersion(majorVersion);
  }

  public void setMinorVersion(int minorVersion) {
    containerConf.setMinorVersion(minorVersion);
  }

  public void setProperties(Map properties) {
    containerConf.setProperties(properties);
  }

  public void setSupportedPortletModesWithDescriptions(Collection portletModes) {
    containerConf.setCustomModesWithDescriptions(portletModes);
  }

  public void setSupportedWindowStatesWithDescriptions(Collection customStates) {
    containerConf.setCustomStatesWithDescriptions(customStates);
  }

  public Collection getSupportedPortletModesWithDescriptions() {
    return containerConf.getSupportedPortletModesWithDescriptions();
  }

  public Collection getSupportedWindowStatesWithDescriptions() {
    return containerConf.getSupportedWindowStatesWithDescriptions();
  }

  public Collection getSupportedPortletModes() {
    return Collections.list(containerConf.getSupportedPortletModes());
  }

  public Collection getSupportedWindowStates() {
    return Collections.list(containerConf.getSupportedWindowStates());
  }

  public Collection getPortletModes(String portletAppName, String portletName, String markup) {
    Collection filteredModes = new ArrayList();
    Collection nonFilteredModes =  portletApplications.getPortletModes(portletAppName, portletName, markup);
    for (Iterator iter = nonFilteredModes.iterator(); iter.hasNext();) {
      PortletMode mode = (PortletMode) iter.next();
      Enumeration portalModes = containerConf.getSupportedPortletModes();
      while (portalModes.hasMoreElements()) {
        PortletMode portalMode = (PortletMode) portalModes.nextElement();
        if(mode.equals(portalMode))
          filteredModes.add(mode);
      }
    }                    
    return filteredModes;
  }

  public boolean isModeSuported(String portletAppName, String portletName,
                                String markup, PortletMode mode) {
    boolean isPortalMode = false;
    Enumeration portalModes = containerConf.getSupportedPortletModes();
    while (portalModes.hasMoreElements()) {
      PortletMode portalMode = (PortletMode) portalModes.nextElement();
      if(portalMode.equals(mode)){
        isPortalMode = true;
        break;
      }
    }    
    return portletApplications.isModeSuported(portletAppName, portletName, markup, mode) && isPortalMode;
  }

  public Collection getWindowStates(String portletAppName) {
    Collection filteredStates = new ArrayList();
    Collection nonFilteredStates =  portletApplications.getWindowStates(portletAppName);
    for (Iterator iter = nonFilteredStates.iterator(); iter.hasNext();) {
      WindowState state = (WindowState) iter.next();
      Enumeration portalStates = containerConf.getSupportedWindowStates();
      while (portalStates.hasMoreElements()) {
        WindowState portalState = (WindowState) portalStates.nextElement();
        if(state.equals(portalState))
          filteredStates.add(state);
      }
    }                    
    return filteredStates;                
  }

  public boolean isStateSupported(WindowState state, String portletApplication) {
    boolean isPortalState = false;
    Enumeration portalStates = containerConf.getSupportedWindowStates();
    while (portalStates.hasMoreElements()) {
      WindowState portalState = (WindowState) portalStates.nextElement();
      if(portalState.equals(state)){
        isPortalState = true;
        break;
      }
    }        
    return portletApplications.isStateSupported(state, portletApplication) && isPortalState;
  }

  public Map getAllPortletMetaData() {
    return portletApplications.getAllPortletMetaData(cont);
  }

  public void setPortletPreference(Input input, Map preferencesMap) throws PortletContainerException {
    log.debug("try to set a portlet preference directly from the setPortletPreference() method");
    WindowID windowID = input.getWindowID();
    Portlet pDatas = 
      portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
    ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
    PortletWindowInternal windowInfos = manager.getWindow(input, defaultPrefs);
    PortletPreferencesImp preferences = (PortletPreferencesImp) windowInfos.getPreferences();
    preferences.setMethodCalledIsAction(true);//to allow restore of previous versions if a problem occurs
    Set keys = preferencesMap.keySet();
    try {
      for (Iterator iter = keys.iterator(); iter.hasNext();) {
        String key = (String) iter.next();
        try {
          preferences.setValue(key, (String) preferencesMap.get(key));
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

  public Map getPortletPreference(Input input) {
    log.debug("Try to get a portlet preference directly from the getPortletPreference() method ");
    WindowID windowID = input.getWindowID();
    Portlet pDatas = 
      portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
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
    
    request.setAttribute(CONTAINER, cont.getContext().getName());
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
                                    ActionInput actionInput)
      throws PortletContainerException {
    log.debug("ProcessAction method in PortletContainerDispatcher entered");
    return (ActionOutput) process(httpServletRequest, httpServletResponse, actionInput, true);
  }

  public RenderOutput render(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             RenderInput renderInput)
      throws PortletContainerException {
    log.debug("Render method in PortletContainerDispatcher entered");
    //flush the current buffer
    try {
      httpServletResponse.flushBuffer();
    } catch (IOException e) {
      log.error("Can not flush servlet response buffer");
      //throw new PortletContainerException("Can not flush servlet response buffer", e);
    }
    return (RenderOutput) process(httpServletRequest, httpServletResponse, renderInput, false);
  }

  public void sendAttrs(HttpServletRequest request, HttpServletResponse response,
    Map attrs, String portletApplicationName) throws PortletContainerException {

    request.setAttribute(CONTAINER, cont.getContext().getName());
    request.setAttribute(ATTRS, attrs);
    if (Environment.getInstance().getPlatform() == Environment.STAND_ALONE) {
    } else {
      dispatch(request, response, portletApplicationName);
    }
  }

  
  private Output process(HttpServletRequest request, HttpServletResponse response,
                         Input input, boolean isAction)
      throws PortletContainerException {
    log.debug("Process method in PortletContainerDispatcher entered");
    log.debug("Encoding used : " + request.getCharacterEncoding());
    //create the ActionOutput object
    Output output = null;
    if (isAction) {
      output = new ActionOutput();
    } else {
      output = new RenderOutput();
    }

    //create a PortletPreferencesImp object
    PortletWindowInternal windowInfos = getWindowInfos(request, input, isAction);
    String portletApplicationName = windowInfos.getWindowID().getPortletApplicationName();

    request.setAttribute(CONTAINER, cont.getContext().getName());
    request.setAttribute(INPUT, input);
    request.setAttribute(OUTPUT, output);
    request.setAttribute(WINDOW_INFO, windowInfos);
    request.setAttribute(IS_ACTION, new Boolean(isAction));

    if (Environment.getInstance().getPlatform() == Environment.STAND_ALONE) {
      log.debug("Stand alone environement : direct call to handler");
      URLClassLoader oldCL = (URLClassLoader) Thread.currentThread().getContextClassLoader();
      initTests();
      try {
        ServletContext portalContext = 
          (ServletContext)cont.getComponentInstanceOfType(ServletContext.class);
        standAloneHandler.process(portalContext, request, response, input, output, windowInfos, isAction);
        //return output;
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
    if (input.isStateSaveOnClient() && isAction) {
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

  private PortletWindowInternal getWindowInfos(HttpServletRequest request, Input input,
                                               boolean isAction) {
    boolean stateChangeAuthorized = true;
    if (isAction) {
      stateChangeAuthorized = ((ActionInput) input).isStateChangeAuthorized();
    }

    PortletWindowInternal windowInfos = null;
    if (!input.isStateSaveOnClient()) {//state save on the server
      log.debug("Extract or create windows info (store in the server)");
      WindowInfosContainer infosContainer = WindowInfosContainer.getInstance();
      String key = "SESSION_CONTAINER_KEY_ENCODER" + input.getWindowID().generateKey();
      if (infosContainer.getInfos(key) != null) {
        windowInfos = infosContainer.getInfos(key);
      } else {
        WindowID windowID = input.getWindowID();
        Portlet pDatas = 
          portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
        ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
        windowInfos = manager.getWindow(input, defaultPrefs);
        infosContainer.addInfos(key, windowInfos);
      }
    } else { //state change kept on the client (for example consumer in WSRP)
      log.debug("Extract or create windows info (sent by the client)");
      WindowID windowID = input.getWindowID();
      Portlet pDatas = 
        portletApplications.getPortletMetaData(windowID.getPortletApplicationName(), windowID.getPortletName());
      ExoPortletPreferences defaultPrefs = pDatas.getPortletPreferences();
      windowInfos = manager.getWindow(input, defaultPrefs);
    }
    ((PortletPreferencesImp) windowInfos.getPreferences()).setMethodCalledIsAction(isAction);
    ((PortletPreferencesImp) windowInfos.getPreferences()).setStateChangeAuthorized(stateChangeAuthorized);
    ((PortletPreferencesImp) windowInfos.getPreferences()).setStateSaveOnClient(input.isStateSaveOnClient());
    return windowInfos;
  }

  private void dispatch(HttpServletRequest request, HttpServletResponse response,
                        String portletApplicationName) throws PortletContainerException {
    ServletContext portalContext = 
      (ServletContext)cont.getComponentInstanceOfType(ServletContext.class);
    ServletContext portletContext = portalContext.getContext("/" + portletApplicationName);
    if (portletContext == null) {
      log.error("Can't get servlet context for portlet app [" + portletApplicationName + "]. May be it's caused " +
        "by difference between context name (usually WAR file name) and content of tag <display-name/> in your " +
        "WEB-INF/web.xml");
    }
    RequestDispatcher dispatcher = portletContext.getRequestDispatcher(SERVLET_MAPPING);
    try {
      log.debug("Dispatch resuest to the portlet application : " + portletApplicationName);
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
  //defined for test purposes
//  protected static final String PORTLET_APP_PATH = "file:./war_template/";
    String PORTLET_APP_PATH = "file:" + System.getProperty("testPath") + "/war_template";
  
    try {
      URL[] URLs = {new URL(PORTLET_APP_PATH + "WEB-INF/classes/"),
                    new URL("file:./lib/portlet-api.jar"),
                    new URL(PORTLET_APP_PATH + "WEB-INF/lib/")};
//      Thread.currentThread().setContextClassLoader(new URLClassLoader(URLs));
    } catch (MalformedURLException e) {
      log.error("Can not init tests", e);
    }
  }
}
