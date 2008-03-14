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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.PortletContainerConf;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 6:15:45 PM
 */
public class PortalContextImp implements PortalContext {

  /**
   * Properties.
   */
  private Map<String, String> properties = new HashMap<String, String>();

  /**
   * PC conf.
   */
  private final PortletContainerConf conf;

  /**
   * @param conf PC conf
   */
  public PortalContextImp(final PortletContainerConf conf) {
    properties = conf.getProperties();
    this.conf = conf;
  }

  /**
   * Overridden method.
   *
   * @param s name
   * @return value
   * @see javax.portlet.PortalContext#getProperty(java.lang.String)
   */
  public final String getProperty(final String s) {
    return properties.get(s);
  }

  /**
   * Overridden method.
   *
   * @return property names
   * @see javax.portlet.PortalContext#getPropertyNames()
   */
  public final Enumeration<String> getPropertyNames() {
    return new Vector<String>(properties.keySet()).elements();
  }

  /**
   * @param s name
   * @return values
   */
  public final Enumeration<String> getProperties(final String s) {
    Vector<String> result = new Vector<String>();
    Iterator<String> keys = properties.keySet().iterator();
    while (keys.hasNext()) {
      String key = keys.next();
      if (key.equals(s))
        result.add(properties.get(key));
    }
    return result.elements();
  }

  /**
   * @param key key
   * @param value value
   */
  public final void addProperty(final String key, final String value) {
    properties.put(key, value);
  }

  /**
   * Overridden method.
   *
   * @return supported portlet modes
   * @see javax.portlet.PortalContext#getSupportedPortletModes()
   */
  public final Enumeration<PortletMode> getSupportedPortletModes() {
    return conf.getSupportedPortletModes();
  }

  /**
   * Overridden method.
   *
   * @return supported window states
   * @see javax.portlet.PortalContext#getSupportedWindowStates()
   */
  public final Enumeration<WindowState> getSupportedWindowStates() {
    return conf.getSupportedWindowStates();
  }

  /**
   * Overridden method.
   *
   * @return portal info
   * @see javax.portlet.PortalContext#getPortalInfo()
   */
  public final String getPortalInfo() {
    String name = conf.getName();
    int major = conf.getMajorVersion();
    int minor = conf.getMinorVersion();
    return name + major + "." + minor;
  }

}
