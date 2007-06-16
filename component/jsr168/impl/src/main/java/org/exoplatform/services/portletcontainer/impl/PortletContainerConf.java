package org.exoplatform.services.portletcontainer.impl;

import java.io.InputStream;
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
import org.exoplatform.services.config.ConfigurationService;
import org.exoplatform.services.config.ConfigurationServiceImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.impl.config.*;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.container.configuration.*;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ServiceConfiguration;
import org.exoplatform.container.xml.ObjectParameter;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 11:38:24
 */
public class PortletContainerConf {  

  private Map properties;
  private String portletContainerName;
  private int majorVersion = -1;
  private int minorVersion = -1;

  private Collection customModes;
  private Collection customStates;
  private Collection customModesWithDescriptions;
  private Collection customStatesWithDescriptions;
  private Collection supportedContents;

  private boolean isCacheEnable;
  private boolean isSharedSessionEnable;
  private PortletContainer containerConfs;

  private boolean isBundleLookupDelegated;
  
  
  public PortletContainerConf(InitParams params, ConfigurationManager cmanager) {
    try {
      ObjectParameter param2 = params.getObjectParam("conf");
      containerConfs = (PortletContainer) param2.getObject();
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

    SharedSession sharedSession = containerConfs.getSharedSession();
    if (sharedSession != null) {
      c = sharedSession.getEnable();
      if ("true".equals(c))
        isSharedSessionEnable = true;
    }
    
    c = containerConfs.getDelegatedBundle().getEnable();
    if ("true".equals(c))  isBundleLookupDelegated = true;        
  }
  
  public Map getProperties() {
    if (properties == null) {
      Map map = new HashMap();
      List l = containerConfs.getProperties();
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        Properties props = (Properties) iterator.next();
        map.put(props.getName(), props.getValue());
      }
      properties = map;
    }
    return properties;
  }

  public void setProperties(Map properties) {
    this.properties = properties;
  }

  public String getPortletContainerName() {
    if(portletContainerName == null){
      portletContainerName = containerConfs.getGlobal().getName();
    }
    return portletContainerName;
  }

  public void setPortletContainerName(String name){
    this.portletContainerName = name;
  }

  public int getMajorVersion() {
    if(majorVersion < 0){
      majorVersion = containerConfs.getGlobal().getMajorVersion();
    }
    return majorVersion;
  }

  public void setMajorVersion(int version){
    majorVersion = version;
  }

  public int getMinorVersion() {
    if(minorVersion < 0){
      minorVersion = containerConfs.getGlobal().getMinorVersion();
    }
    return minorVersion;
  }

  public void setMinorVersion(int version){
    minorVersion = version;
  }

  public Collection getSupportedContent() {
    if (supportedContents == null) {
      supportedContents = new ArrayList();
      List content = containerConfs.getSupportedContent();
      for (Iterator iter = content.iterator(); iter.hasNext();) {
        SupportedContent element = (SupportedContent) iter.next();
        supportedContents.add(element.getName());
      }
    }
    return supportedContents;
  }

  public Enumeration getSupportedPortletModes() {
    if (customModes == null) {
      Collection v = new ArrayList();
      v.add(PortletMode.EDIT);
      v.add(PortletMode.HELP);
      v.add(PortletMode.VIEW);
      List l = containerConfs.getCustomMode();
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        CustomMode customMode = (CustomMode) iterator.next();
        v.add(new PortletMode(customMode.getName()));
      }
      customModes = v;
    }
    return Collections.enumeration(customModes);
  }

  public Enumeration getSupportedWindowStates() {
    if (customStates == null) {
      Collection v = new ArrayList();
      v.add(WindowState.NORMAL);
      v.add(WindowState.MINIMIZED);
      v.add(WindowState.MAXIMIZED);
      List l = containerConfs.getCustomWindowState();
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        CustomWindowState customState = (CustomWindowState) iterator.next();
        v.add(new WindowState(customState.getName()));
      }
      customStates = v;
    }
    return Collections.enumeration(customStates);
  }

  public Collection getSupportedPortletModesWithDescriptions() {
    if (customModesWithDescriptions == null) {
      Collection v = new ArrayList();
      List l = containerConfs.getCustomMode();
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        CustomMode customMode = (CustomMode) iterator.next();
        List l2 = customMode.getDescription();
        List toBeReturned = new ArrayList();
        for (Iterator iter = l2.iterator(); iter.hasNext();) {
          Description element = (Description) iter.next();
          LocalisedDescription d = 
            new LocalisedDescription(new Locale(element.getLang()), element.getDescription());
          toBeReturned.add(d);
        }
        CustomModeWithDescription cMWD = 
          new CustomModeWithDescription(new PortletMode(customMode.getName()),toBeReturned);

        v.add(cMWD);
      }
      customModesWithDescriptions = v;
    }
    return customModesWithDescriptions;
  }

  public Collection getSupportedWindowStatesWithDescriptions() {
    if (customStatesWithDescriptions == null) {
      Collection v = new ArrayList();
      List l = containerConfs.getCustomWindowState();
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        CustomWindowState customState = (CustomWindowState) iterator.next();
        List l2 = customState.getDescription();
        List toBeReturned = new ArrayList();
        for (Iterator iter = l2.iterator(); iter.hasNext();) {
          Description element = (Description) iter.next();
          LocalisedDescription d = 
            new LocalisedDescription(new Locale(element.getLang()), element.getDescription());
          toBeReturned.add(d);
        }
        CustomWindowStateWithDescription cMWD = 
          new CustomWindowStateWithDescription(new WindowState(customState.getName()), toBeReturned);
        v.add(cMWD);
      }
      customStatesWithDescriptions = v;
    }
    return customStatesWithDescriptions;
  }

  public synchronized void setCustomModesWithDescriptions(Collection customModesWithDescriptions) {
    this.customModesWithDescriptions = customModesWithDescriptions;
    Collection temp = new ArrayList();
    for (Iterator iter = customModesWithDescriptions.iterator(); iter.hasNext();) {
      CustomModeWithDescription element = (CustomModeWithDescription) iter.next();
      temp.add(element.getPortletMode());
    }
    this.customModes = temp;
  }

  public synchronized void setCustomStatesWithDescriptions(Collection customStatesWithDescriptions) {
    this.customStatesWithDescriptions = customStatesWithDescriptions;
    Collection temp = new ArrayList();
    for (Iterator iter = customStatesWithDescriptions.iterator(); iter.hasNext();) {
      CustomWindowStateWithDescription element = (CustomWindowStateWithDescription) iter.next();
      temp.add(element.getWindowState());
    }
    this.customStates = temp;
  }

  public boolean isModeSupported(PortletMode mode) {
    Enumeration e = getSupportedPortletModes();
    while (e.hasMoreElements()) {
      PortletMode portletMode = (PortletMode) e.nextElement();
      if (portletMode.toString().equals(mode.toString()))
        return true;
    }
    return false;
  }

  public boolean isStateSupported(WindowState state) {
    Enumeration e = getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState windowState = (WindowState) e.nextElement();
      if (windowState.toString().equals(state.toString()))
        return true;
    }
    return false;

  }

  public boolean isCacheEnable() {
    return isCacheEnable;
  }

  public boolean isSharedSessionEnable() {
    return isSharedSessionEnable;
  }
  
  public boolean isBundleLookupDelegated() {
    return isBundleLookupDelegated;
  }   
}