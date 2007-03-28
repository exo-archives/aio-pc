package org.exoplatform.services.portletcontainer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.pci.model.*;


/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 14:47:22
 */
public class PortletApplicationsHolder {

  private Map portletApps;
  private Log log;

  public PortletApplicationsHolder(LogService logService) {
    this.portletApps = new HashMap();
    this.log = logService.getLog("org.exoplatform.services.portletcontainer");
  }

  public void start() {

  }

  public void stop()  {

  }

  public PortletApp getPortletApplication(String portletAppName) {
    PortletApplicationHelper helper = (PortletApplicationHelper) portletApps.get(portletAppName);
    if(helper == null) {
      log.debug("Portlet application : " + portletAppName + " does not exist");
      return null;
    }
    return helper.getPortletApp();
  }

  public Collection getRoles(String portletAppName) {
    log.debug("getRoles() entered");
    PortletApplicationHelper helper = (PortletApplicationHelper) portletApps.get(portletAppName);
    return helper.getRoles();
  }

  public Map getAllPortletMetaData(ExoContainer container) {
    log.debug("getAllPortletMetaData() entered");
    HashMap all = new HashMap(20);
    Collection applicationsKeys = portletApps.keySet();
    Iterator iterator = applicationsKeys.iterator();
    while (iterator.hasNext()) {
      String key = (String) iterator.next();
      PortletApplicationHelper helper = (PortletApplicationHelper) portletApps.get(key);
      PortletApp pA = helper.getPortletApp();
      List securityContraints = pA.getSecurityConstraint();
      List portlets = pA.getPortlet();
      UserDataConstraint userDataConstraintType = null;
      for (int i = 0; i < portlets.size(); i++) {
        Portlet portlet = (Portlet) portlets.get(i);
        for (Iterator iter = securityContraints.iterator(); iter.hasNext();) {
          SecurityConstraint element = (SecurityConstraint) iter.next();
          List portletsList = element.getPortletCollection().getPortletName();
          for (Iterator iterator2 = portletsList.iterator(); iterator2.hasNext();) {
            String name  = (String) iterator2.next();
            if (name.equals(portlet.getPortletName())) {
              userDataConstraintType = element.getUserDataConstraint();
              break;
            }
          }
        }
        all.put(key + Constants.PORTLET_META_DATA_ENCODER + portlet.getPortletName(),
                new PortletDataImp(container, portlet,  userDataConstraintType, pA.getUserAttribute()));
      }
    }
    return all;
  }

  public Collection getPortletModes(String portletAppName, String portletName, String markup) {
    log.debug("getPortletModes() entered");
    Collection modes = new ArrayList();
//  modes.add(PortletMode.VIEW); // it's commented because of in jsr.168 XMLParser.java VIEW mode added as default
    List portlets = getPortletApplication(portletAppName).getPortlet();
    for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = (Portlet) iterator.next();
      if (portlet.getPortletName().equals(portletName)) {
        List l = portlet.getSupports();
        for (Iterator iterator2 = l.iterator(); iterator2.hasNext();) {
          Supports supports = (Supports) iterator2.next();
          String mimeType = supports.getMimeType();
          if (mimeType.equals(markup)) {
            List modesR = supports.getPortletMode();
            for (Iterator iterator1 = modesR.iterator(); iterator1.hasNext();) {
              String s = (String) iterator1.next();
              modes.add(new PortletMode(s));
            }
          }
        }
      }
    }
    return modes;
  }

  public boolean isModeSuported(String portletAppName, String portletName,
                                String markup, PortletMode mode) {
    log.debug("isModeSuported() entered");
    if (PortletMode.VIEW == mode)
      return true;
    Collection modesSupported = getPortletModes(portletAppName, portletName, markup);
    for (Iterator iterator = modesSupported.iterator(); iterator.hasNext();) {
      PortletMode portletMode = (PortletMode) iterator.next();
      if (portletMode.toString().equals(mode.toString()))
        return true;
    }
    return false;
  }

  public Collection getWindowStates(String portletAppName) {
    log.debug("getWindowStates() entered");
    Collection states = new ArrayList();
    states.add(WindowState.MINIMIZED);
    states.add(WindowState.NORMAL);
    states.add(WindowState.MAXIMIZED);
    List customStates = getPortletApplication(portletAppName).getCustomWindowState();
    for (Iterator iterator = customStates.iterator(); iterator.hasNext();) {
      CustomWindowState customWindowState = (CustomWindowState) iterator.next();
      WindowState state = new WindowState(customWindowState.getWindowState());
      states.add(state);
    }
    return states;
  }

  public boolean isStateSupported(WindowState state, String portletApplication) {
    log.debug("isStateSupported() entered");
    Collection c = getWindowStates(portletApplication);
    for (Iterator iterator = c.iterator(); iterator.hasNext();) {
      WindowState windowState = (WindowState) iterator.next();
      if (windowState.toString().equals(state.toString()))
        return true;
    }
    return false;
  }

  public void registerPortletApplication(String portletAppName, PortletApp portletApp,
                                         Collection roles) {
    PortletApplicationHelper helper = new PortletApplicationHelper(portletAppName, portletApp, roles);
    synchronized (portletApps) {
      portletApps.put(portletAppName, helper);
    }
  }

  public void removePortletApplication(String portletAppName) {
    synchronized (portletApps) {
      portletApps.remove(portletAppName);
    }
  }

  public Portlet getPortletMetaData(String portletApplication, String portlet) {
    log.debug("getPortletMetaData() entered");
    PortletApp portletApp = getPortletApplication(portletApplication);
    if(portletApp == null) return null;
    List l = portletApp.getPortlet();
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      Portlet portlet1 = (Portlet) iterator.next();
      if (portlet1.getPortletName().equals(portlet))
        return portlet1;
    }
    return null;
  }


 private class PortletApplicationHelper {
    private PortletApp portletApp;
    private Collection roles;
    private String portletAppName;

    public PortletApplicationHelper(String portletAppName,
                                    PortletApp portletApp,
                                    Collection roles) {
      this.portletApp = portletApp;
      this.roles = roles;
      this.portletAppName = portletAppName;
    }

    public PortletApp getPortletApp() {
      return portletApp;
    }

    public Collection getRoles() {
      return roles;
    }
  }
}
