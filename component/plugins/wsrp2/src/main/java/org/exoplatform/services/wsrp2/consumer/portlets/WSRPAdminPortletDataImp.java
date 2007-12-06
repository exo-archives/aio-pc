/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.consumer.portlets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletDataImp;
import org.exoplatform.services.wsrp2.WSRPConstants;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 07.06.2007
 */
public class WSRPAdminPortletDataImp extends PortletDataImp {

  protected WSRPAdminPortlet portletObj = null;

  public WSRPAdminPortletDataImp(ExoContainer cont,
                                 HashMap<String, String> adminPortletParams) {
    super(cont, null, null, new ArrayList<UserAttribute>());
    portlet_ = new Portlet();
    portlet_.setPortletName(WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
    portlet_.setPortletClass(WSRPConstants.WSRP_ADMIN_PORTLET_CLASS);
    String portletTitle = null;
    if (adminPortletParams != null)
      portletTitle = (String) adminPortletParams.get(WSRPConstants.WAP_portletTitle);
    if (portletTitle == null)
      portletTitle = WSRPConstants.WSRP_ADMIN_PORTLET_NAME;
    List<DisplayName> displayName = new ArrayList<DisplayName>();
    DisplayName dn = new DisplayName();
    dn.setDisplayName(portletTitle);
    dn.setLang("en");
    displayName.add(dn);
    portlet_.setDisplayName(displayName);
    Supports supports = new Supports();
    supports.setMimeType("text/html");
    portlet_.setSupports(supports);
    portletObj = new WSRPAdminPortlet();
    portletObj.init(cont);
  }

  public WSRPAdminPortlet getPortletObject() {
    return portletObj;
  }

  public Collection<PortletMode> getPortletModes(String markup) {
    if (markup.equals("text/html")) {
      ArrayList<PortletMode> r = new ArrayList<PortletMode>();
      r.add(PortletMode.VIEW);
      return r;
    }
    return null;
  }

  public boolean isModeSuported(String markup,
                                PortletMode mode) {
    return markup.equals("text/html") && (mode.equals(PortletMode.VIEW) || mode.equals(PortletMode.EDIT) || mode.equals(PortletMode.HELP));
  }

  public Collection<WindowState> getWindowStates(String markup) {
    if (markup.equals("text/html")) {
      ArrayList<WindowState> r = new ArrayList<WindowState>();
      r.add(WindowState.NORMAL);
      r.add(WindowState.MINIMIZED);
      r.add(WindowState.MAXIMIZED);
      return r;
    }
    return null;
  }

  public boolean isStateSupported(String markup,
                                  WindowState state) {
    return markup.equals("text/html")
        && (state.equals(WindowState.NORMAL) || state.equals(WindowState.MINIMIZED) || state.equals(WindowState.MAXIMIZED));
  }

  public boolean getEscapeXml() {
    return true;
  }

}
