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
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.SecurityConstraint;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserDataConstraint;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 11 nov. 2003 Time: 14:47:22
 */
public class PortletApplicationsHolder {

  private Map<String, PortletApplicationHelper> portletApps;

  private Log                                   log;

  public PortletApplicationsHolder() {
    this.portletApps = new HashMap<String, PortletApplicationHelper>();
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  }

  public void start() {

  }

  public void stop() {

  }

  public PortletApp getPortletApplication(String portletAppName) {
    PortletApplicationHelper helper = portletApps.get(portletAppName);
    if (helper == null) {
      log.debug("Portlet application : " + portletAppName + " does not exist");
      return null;
    }
    return helper.getPortletApp();
  }

  public List<PortletApp> getPortletAppList() {
    List<PortletApp> result = new ArrayList<PortletApp>();
    for (Iterator<String> i = portletApps.keySet().iterator(); i.hasNext();) {
      PortletApplicationHelper helper = portletApps.get(i.next());
      if (helper != null)
        result.add(helper.getPortletApp());
    }
    return result;
  }

  public Collection<String> getRoles(String portletAppName) {
    log.debug("getRoles() entered");
    PortletApplicationHelper helper = portletApps.get(portletAppName);
    return helper.getRoles();
  }

  public Map<String, PortletData> getAllPortletMetaData(ExoContainer container) {
    log.debug("getAllPortletMetaData() entered");
    HashMap<String, PortletData> all = new HashMap<String, PortletData>();
    Collection<String> applicationsKeys = portletApps.keySet();
    Iterator<String> iterator = applicationsKeys.iterator();
    // for each portlet app
    while (iterator.hasNext()) {
      String key = iterator.next();
      PortletApplicationHelper helper = portletApps.get(key);
      PortletApp portletApp = helper.getPortletApp();
      List<SecurityConstraint> securityContraints = portletApp.getSecurityConstraint();
      List<Portlet> portlets = portletApp.getPortlet();
      UserDataConstraint userDataConstraintType = null;
      // for each portlet
      for (int i = 0; i < portlets.size(); i++) {
        Portlet portlet = portlets.get(i);
        // for each security contraint (OPTION)
        for (Iterator<SecurityConstraint> iter = securityContraints.iterator(); iter.hasNext();) {
          SecurityConstraint securityConstraint = iter.next();
          List<String> portletNames = securityConstraint.getPortletCollection().getPortletName();
          for (Iterator<String> iterator2 = portletNames.iterator(); iterator2.hasNext();) {
            String portletName = iterator2.next();
            if (portletName.equals(portlet.getPortletName())) {
              userDataConstraintType = securityConstraint.getUserDataConstraint();
              break;
            }
          }
        }
        all.put(key + Constants.PORTLET_META_DATA_ENCODER + portlet.getPortletName(), new PortletDataImp(container,
                                                                                                         portlet,
                                                                                                         userDataConstraintType,
                                                                                                         portletApp.getUserAttribute()));
      }
    }
    return all;
  }

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    log.debug("getPortletModes() entered");
    Collection<PortletMode> modes = new ArrayList<PortletMode>();
    List<Portlet> portlets = getPortletApplication(portletAppName).getPortlet();
    for (Iterator<Portlet> iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = iterator.next();
      if (portlet.getPortletName().equals(portletName)) {
        List<Supports> l = portlet.getSupports();
        for (Iterator<Supports> iterator2 = l.iterator(); iterator2.hasNext();) {
          Supports supports = iterator2.next();
          String mimeType = supports.getMimeType();
          if (mimeType.equals(markup)) {
            List<String> modesR = supports.getPortletMode();
            for (Iterator<String> iterator1 = modesR.iterator(); iterator1.hasNext();) {
              String s = iterator1.next();
              modes.add(new PortletMode(s));
            }
          }
        }
      }
    }
    return modes;
  }

  public boolean isModeSuported(String portletAppName,
                                String portletName,
                                String markup,
                                PortletMode mode) {
    log.debug("isModeSuported() entered");
    if (PortletMode.VIEW == mode)
      return true;
    Collection<PortletMode> modesSupported = getPortletModes(portletAppName, portletName, markup);
    for (Iterator<PortletMode> iterator = modesSupported.iterator(); iterator.hasNext();) {
      PortletMode portletMode = iterator.next();
      if (portletMode.toString().equals(mode.toString()))
        return true;
    }
    return false;
  }

  public Collection<WindowState> getWindowStates(String portletAppName,
                                    String portletName,
                                    String markup) {
    log.debug("getWindowStates() entered");
    Collection<WindowState> states = new ArrayList<WindowState>();
    List<Portlet> portlets = getPortletApplication(portletAppName).getPortlet();
    for (Iterator<Portlet> iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = iterator.next();
      if (portlet.getPortletName().equals(portletName)) {
        List<Supports> l = portlet.getSupports();
        for (Iterator<Supports> iterator2 = l.iterator(); iterator2.hasNext();) {
          Supports supports = iterator2.next();
          String mimeType = supports.getMimeType();
          if (mimeType.equals(markup)) {
            List<String> statesR = supports.getWindowState();
            for (Iterator<String> iterator1 = statesR.iterator(); iterator1.hasNext();) {
              String s = iterator1.next();
              states.add(new WindowState(s));
            }
          }
        }
      }
    }
    return states;
  }

  public boolean isStateSupported(String portletAppName,
                                  String portletName,
                                  String markup,
                                  WindowState state) {
    log.debug("isStateSupported() entered");
    if (WindowState.MINIMIZED == state)
      return true;
    if (WindowState.NORMAL == state)
      return true;
    if (WindowState.MAXIMIZED == state)
      return true;
    Collection<WindowState> statesSupported = getWindowStates(portletAppName, portletName, markup);
    for (Iterator<WindowState> iterator = statesSupported.iterator(); iterator.hasNext();) {
      WindowState windowState = iterator.next();
      if (windowState.toString().equals(state.toString()))
        return true;
    }
    return false;
  }

  public void registerPortletApplication(String portletAppName,
                                         PortletApp portletApp,
                                         Collection<String> roles) {
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

  public Portlet getPortletMetaData(String portletApplication,
                                    String portlet) {
    log.debug("getPortletMetaData() entered");
    PortletApp portletApp = getPortletApplication(portletApplication);
    if (portletApp == null)
      return null;
    List<Portlet> l = portletApp.getPortlet();
    for (Iterator<Portlet> iterator = l.iterator(); iterator.hasNext();) {
      Portlet portlet1 = iterator.next();
      if (portlet1.getPortletName().equals(portlet))
        return portlet1;
    }
    return null;
  }

  private class PortletApplicationHelper {
    private PortletApp         portletApp;

    private Collection<String> roles;

    private String             portletAppName;

    public PortletApplicationHelper(String portletAppName,
                                    PortletApp portletApp,
                                    Collection<String> roles) {
      this.portletApp = portletApp;
      this.roles = roles;
      this.portletAppName = portletAppName;
    }

    public PortletApp getPortletApp() {
      return portletApp;
    }

    public Collection<String> getRoles() {
      return roles;
    }

    public String getPortletAppName() {
      return portletAppName;
    }
  }

}
