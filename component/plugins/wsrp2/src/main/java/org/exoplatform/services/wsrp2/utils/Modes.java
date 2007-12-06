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
 
package org.exoplatform.services.wsrp2.utils;

import javax.portlet.PortletMode;

import org.exoplatform.services.wsrp2.WSRPConstants;

public class Modes implements java.io.Serializable {
  private java.lang.String _value_;
  private static java.util.HashMap _table_ = new java.util.HashMap();

  // Constructor
  public Modes(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  // define the modes we can currently handle
  private static final java.lang.String viewString = PortletMode.VIEW.toString();
  private static final java.lang.String editString = PortletMode.EDIT.toString();
  private static final java.lang.String helpString = PortletMode.HELP.toString();
  private static final java.lang.String previewString = "preview";
  
  public static final java.lang.String _view = WSRPConstants.WSRP_PREFIX + viewString;
  public static final java.lang.String _edit = WSRPConstants.WSRP_PREFIX + editString;
  public static final java.lang.String _help = WSRPConstants.WSRP_PREFIX + helpString;
  public static final java.lang.String _preview = WSRPConstants.WSRP_PREFIX + previewString;
  
  public static final Modes view = new Modes(_view);
  public static final Modes edit = new Modes(_edit);
  public static final Modes help = new Modes(_help);
  public static final Modes preview = new Modes(_preview);

  public java.lang.String getValue() {
    return _value_;
  }

  /**
   * Returns the WSRP mode build from a string representation
   * If a not supported Mode is requested, null is returned
   *
   * @param <code>String</string> representation of the WSRP mode
   * @return The WSRP <code>Mode</code> represented by the passed string
   */
  public static Modes fromValue(java.lang.String value) {
    return (Modes) _table_.get(value);
  }

  /**
   * Returns the WSRP mode build from a string representation
   * If a not supported Mode is requested, null is returned
   *
   * @param <code>String</string> representation of the WSRP mode
   * @return The WSRP <code>Mode</code> represented by the passed string
   */
  public static Modes fromString(java.lang.String value) {
    return fromValue(value);
  }

  public boolean equals(java.lang.Object obj) {
    return (obj == this);
  }

  public int hashCode() {
    return toString().hashCode();
  }

  public java.lang.String toString() {
    return _value_;
  }

  public java.lang.Object readResolve() throws java.io.ObjectStreamException {
    return fromValue(_value_);
  }

  /**
   * This helper method maps portlet modes defined in wsrp to portlet modes
   * defined in the java portlet standard (JSR-168). If the passed wsrp mode
   * is null or can not be mapped the view mode is returned.
   *
   * @return The <code>javax.portlet.PortletMode</code> which corresponds to the given wsrp mode.
   */

  public static PortletMode getJsrPortletModeFromWsrpMode(Modes wsrpMode) {
    if (wsrpMode == null) {
      throw new IllegalArgumentException("WSRP portlet mode must not be null.");
    }
    return getJsrPortletModeFromWsrpMode(wsrpMode.toString());
  }
  
  public static PortletMode getJsrPortletModeFromWsrpMode(String wsrpMode) {
    if (wsrpMode == null) 
      return null;
    String portletMode = delAllPrefixWSRP(wsrpMode).toLowerCase();
    if (portletMode.equalsIgnoreCase(viewString)) {
      return PortletMode.VIEW;
    } else if (portletMode.equalsIgnoreCase(editString)) {
      return PortletMode.EDIT;
    } else if (portletMode.equalsIgnoreCase(helpString)) {
      return PortletMode.HELP;
    } else if (portletMode.equalsIgnoreCase(previewString)) {
      return new PortletMode(previewString);
    }
    System.out.println("Modes.getJsrPortletModeFromWsrpMode " + wsrpMode + " changed with '" + viewString + "' mode");
    //return new PortletMode(jsrMode.toLowerCase());
    return PortletMode.VIEW;
  }
  
  /**
   * This helper method maps portlet modes defined in tha java portlet standard (JSR-168)
   * to modes defined in wsrp. If the passed portlet mode can not be resolved wsrp:view mode
   * is returned.
   *
   * @param portletMode The <code>javax.portlet.PortletMode</code> which should be resolved as
   *                    as portlet mode defined in wsrp.
   * @return
   */
  
  public static Modes getWsrpModeFromJsrPortletMode(PortletMode portletMode) {
    if (portletMode == null) {
      throw new IllegalArgumentException("Portlet mode must not be null.");
    }
    return getWsrpModeFromJsrPortletMode(portletMode.toString());
  }
  
  public static Modes getWsrpModeFromJsrPortletMode(String portletMode) {
    if (portletMode == null)
      return null;
    String wsrpMode = addPrefixWSRP(portletMode).toLowerCase();;
    // if this portletMode is already a suitable wsrp mode
    if (wsrpMode.equalsIgnoreCase(_view)) {
      return Modes.view;
    } else if (wsrpMode.equalsIgnoreCase(_edit)) {
      return Modes.edit;
    } else if (wsrpMode.equalsIgnoreCase(_help)) {
      return Modes.help;
    } else if (wsrpMode.equalsIgnoreCase(_preview)) {
      return Modes.preview;
    }
    System.out.println("Modes.getWsrpModeFromJsrPortletMode " + portletMode + " changed with '" + _view + "' mode");
    //return new Modes(wsrpMode.toLowerCase());
    return Modes.view;
  }
  
  public static String addPrefixWSRP(String forAddWSRPPrefix) {
    return WSRPConstants.WSRP_PREFIX + forAddWSRPPrefix;
  }
  
  public static String delAllPrefixWSRP(String forDelWSRPPrefix) {
    if (forDelWSRPPrefix == null)
      return null;
    while (forDelWSRPPrefix.startsWith(WSRPConstants.WSRP_PREFIX))
      forDelWSRPPrefix = forDelWSRPPrefix.substring(WSRPConstants.WSRP_PREFIX.length());
    return forDelWSRPPrefix;
  }
  
}
