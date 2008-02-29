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
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 14:47:22
 */
public class PortletApplicationsHolder {

  /**
   * Portlet application map.
   */
  private final Map<String, PortletApplicationHelper> portletApps;

  /**
   * Log.
   */
  private final Log log;

  /**
   * Simple constructor.
   */
  public PortletApplicationsHolder() {
    this.portletApps = new HashMap<String, PortletApplicationHelper>();
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  }

  /**
   * start().
   */
  public void start() {
  }

  /**
   * stop().
   */
  public void stop() {
  }

  /**
   * @param portletAppName portlet app name
   * @return portlet app object
   */
  public final PortletApp getPortletApplication(final String portletAppName) {
    PortletApplicationHelper helper = portletApps.get(portletAppName);
    if (helper == null) {
      log.debug("Portlet application : " + portletAppName + " does not exist");
      return null;
    }
    return helper.getPortletApp();
  }

  /**
   * @return portlet app list
   */
  public final List<PortletApp> getPortletAppList() {
    List<PortletApp> result = new ArrayList<PortletApp>();
    for (String string : portletApps.keySet()) {
      PortletApplicationHelper helper = portletApps.get(string);
      if (helper != null)
        result.add(helper.getPortletApp());
    }
    return result;
  }

  /**
   * @param portletAppName portlet app name
   * @return roles
   */
  public final Collection<String> getRoles(final String portletAppName) {
    log.debug("getRoles() entered");
    PortletApplicationHelper helper = portletApps.get(portletAppName);
    return helper.getRoles();
  }

  /**
   * @param container exo container
   * @return all portlet metadata
   */
  public final Map<String, PortletData> getAllPortletMetaData(final ExoContainer container) {
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
        for (SecurityConstraint securityConstraint : securityContraints) {
          List<String> portletNames = securityConstraint.getPortletCollection().getPortletName();
          for (String portletName : portletNames) {
            if (portletName.equals(portlet.getPortletName())) {
              userDataConstraintType = securityConstraint.getUserDataConstraint();
              break;
            }
          }
        }
        all.put(key + Constants.PORTLET_META_DATA_ENCODER + portlet.getPortletName(),
            new PortletDataImp(container, portlet, userDataConstraintType, portletApp
                .getUserAttribute()));
      }
    }
    return all;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @return portlet modes
   */
  public final Collection<PortletMode> getPortletModes(final String portletAppName,
      final String portletName,
      final String markup) {
    log.debug("getPortletModes() entered");
    Collection<PortletMode> modes = new ArrayList<PortletMode>();
    List<Portlet> portlets = getPortletApplication(portletAppName).getPortlet();
    for (Portlet portlet : portlets) {
      if (portlet.getPortletName().equals(portletName)) {
        List<Supports> l = portlet.getSupports();
        for (Supports supports : l) {
          String mimeType = supports.getMimeType();
          if (mimeType.equals(markup)) {
            List<String> modesR = supports.getPortletMode();
            for (String s : modesR) {
              modes.add(new PortletMode(s));
            }
          }
        }
      }
    }
    return modes;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @param mode mode
   * @return either the specififed portlet mode supported
   */
  public final boolean isModeSuported(final String portletAppName,
      final String portletName,
      final String markup,
      final PortletMode mode) {
    log.debug("isModeSuported() entered");
    if (PortletMode.VIEW == mode)
      return true;
    Collection<PortletMode> modesSupported = getPortletModes(portletAppName, portletName, markup);
    for (PortletMode portletMode : modesSupported) {
      if (portletMode.toString().equals(mode.toString()))
        return true;
    }
    return false;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @return window states
   */
  public final Collection<WindowState> getWindowStates(final String portletAppName,
      final String portletName,
      final String markup) {
    log.debug("getWindowStates() entered");
    Collection<WindowState> states = new ArrayList<WindowState>();
    List<Portlet> portlets = getPortletApplication(portletAppName).getPortlet();
    for (Portlet portlet : portlets) {
      if (portlet.getPortletName().equals(portletName)) {
        List<Supports> l = portlet.getSupports();
        for (Supports supports : l) {
          String mimeType = supports.getMimeType();
          if (mimeType.equals(markup)) {
            List<String> statesR = supports.getWindowState();
            for (String s : statesR) {
              states.add(new WindowState(s));
            }
          }
        }
      }
    }
    return states;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param markup markup
   * @param state state
   * @return either the specified state supported
   */
  public final boolean isStateSupported(final String portletAppName,
      final String portletName,
      final String markup,
      final WindowState state) {
    log.debug("isStateSupported() entered");
    if (WindowState.MINIMIZED == state)
      return true;
    if (WindowState.NORMAL == state)
      return true;
    if (WindowState.MAXIMIZED == state)
      return true;
    Collection<WindowState> statesSupported = getWindowStates(portletAppName, portletName, markup);
    for (WindowState windowState : statesSupported) {
      if (windowState.toString().equals(state.toString()))
        return true;
    }
    return false;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletApp portlet app object
   * @param roles roles
   */
  public final void registerPortletApplication(final String portletAppName,
      final PortletApp portletApp,
      final Collection<String> roles) {
    PortletApplicationHelper helper = new PortletApplicationHelper(portletAppName,
        portletApp,
        roles);
    synchronized (portletApps) {
      portletApps.put(portletAppName, helper);
    }
  }

  /**
   * @param portletAppName portlet app name
   */
  public final void removePortletApplication(final String portletAppName) {
    synchronized (portletApps) {
      portletApps.remove(portletAppName);
    }
  }

  /**
   * @param portletApplication portlet app name
   * @param portlet portlet name
   * @return portlet metadata
   */
  public final Portlet getPortletMetaData(final String portletApplication, final String portlet) {
    log.debug("getPortletMetaData() entered");
    PortletApp portletApp = getPortletApplication(portletApplication);
    if (portletApp == null)
      return null;
    List<Portlet> l = portletApp.getPortlet();
    for (Portlet portlet1 : l) {
      if (portlet1.getPortletName().equals(portlet))
        return portlet1;
    }
    return null;
  }

  /**
   * Helper class to hold portlet app objects.
   */
  private class PortletApplicationHelper {

    /**
     * Portlet app object.
     */
    private final PortletApp portletApp;

    /**
     * Roles.
     */
    private final Collection<String> roles;

    /**
     * Portlet app name.
     */
    private final String portletAppName;

    /**
     * @param portletAppName portlet app name
     * @param portletApp portlet app object
     * @param roles roles
     */
    public PortletApplicationHelper(final String portletAppName,
        final PortletApp portletApp,
        final Collection<String> roles) {
      this.portletApp = portletApp;
      this.roles = roles;
      this.portletAppName = portletAppName;
    }

    /**
     * @return portlet app object
     */
    public PortletApp getPortletApp() {
      return portletApp;
    }

    /**
     * @return roles
     */
    public Collection<String> getRoles() {
      return roles;
    }

    /**
     * @return portlet app name
     */
    public String getPortletAppName() {
      return portletAppName;
    }
  }

}
