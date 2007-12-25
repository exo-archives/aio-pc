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

package org.exoplatform.services.wsrp.utils;

import javax.portlet.WindowState;

import org.exoplatform.services.wsrp.WSRPConstants;

public class WindowStates implements java.io.Serializable {
  private java.lang.String                           _value_;

  private static java.util.Map<String, WindowStates> _table_ = new java.util.HashMap<String, WindowStates>();

  // Constructor
  public WindowStates(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  // define the window states we can currently handle
  private static final java.lang.String normalString    = WindowState.NORMAL.toString();

  private static final java.lang.String minimizedString = WindowState.MINIMIZED.toString();

  private static final java.lang.String maximizedString = WindowState.MAXIMIZED.toString();

  private static final java.lang.String soloString      = "solo";

  public static final java.lang.String  _normal         = WSRPConstants.WSRP_PREFIX + normalString;

  public static final java.lang.String  _minimized      = WSRPConstants.WSRP_PREFIX
                                                            + minimizedString;

  public static final java.lang.String  _maximized      = WSRPConstants.WSRP_PREFIX
                                                            + maximizedString;

  public static final java.lang.String  _solo           = WSRPConstants.WSRP_PREFIX + soloString;

  public static final WindowStates      normal          = new WindowStates(_normal);

  public static final WindowStates      minimized       = new WindowStates(_minimized);

  public static final WindowStates      maximized       = new WindowStates(_maximized);

  public static final WindowStates      solo            = new WindowStates(_solo);

  public java.lang.String getValue() {
    return _value_;
  }

  /**
   * Returns the WSRP window state build from a string representation If a not
   * supported window state is requested, null is returned
   * 
   * @param <code>String</string> representation of the WSRP window state
   * @return The WSRP <code>WindowStates</code> represented by the passed string
   */
  public static WindowStates fromValue(java.lang.String value) {
    return (WindowStates) _table_.get(value);
  }

  /**
   * Returns the WSRP window state build from a string representation If a not
   * supported window state is requested, null is returned
   * 
   * @param <code>String</string> representation of the WSRP window state
   * @return The WSRP <code>WindowStates</code> represented by the passed string
   */
  public static WindowStates fromString(java.lang.String value) {
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
   * This helper method maps portlet window states defined in wsrp to portlet
   * window states defined in the java portlet standard (JSR-168). If the passed
   * wsrp window state is null or can not be mapped directly the normal state is
   * returned.
   * 
   * @return The <code>javax.portlet.WindowState</code> which corresponds to
   *         the given wsrp state.
   */

  public static WindowState getJsrPortletStateFromWsrpState(WindowStates wsrpState) {
    // was: if (wsrpState == null) return WindowState.NORMAL;
    if (wsrpState == null) {
      throw new IllegalArgumentException("WSRP portlet state must not be null.");
    }
    // if this wsrpState is already a suitable WindowState
    if (wsrpState.getValue().equalsIgnoreCase(normalString)) {
      return WindowState.NORMAL;
    } else if (wsrpState.getValue().equalsIgnoreCase(maximizedString)) {
      return WindowState.MAXIMIZED;
    } else if (wsrpState.getValue().equalsIgnoreCase(minimizedString)) {
      return WindowState.MINIMIZED;
    } else if (wsrpState.getValue().equalsIgnoreCase(soloString)) {
      return new WindowState(soloString);
    }
    // otherwise
    if (wsrpState.getValue().equalsIgnoreCase(_normal)) {
      return WindowState.NORMAL;
    } else if (wsrpState.getValue().equalsIgnoreCase(_maximized)) {
      return WindowState.MAXIMIZED;
    } else if (wsrpState.getValue().equalsIgnoreCase(_minimized)) {
      return WindowState.MINIMIZED;
    } else if (wsrpState.getValue().equalsIgnoreCase(_solo)) {
      return new WindowState(soloString);
    }
    System.out.println("WindowStates.getJsrPortletStateFromWsrpState " + wsrpState.getValue()
        + " changed with '" + normalString + "' state");
    return WindowState.NORMAL;
  }

  /**
   * This helper method maps portlet window states defined in tha java portlet
   * standard (JSR-168) to window states defined in wsrp. If the passed state
   * can not be resolved wsrp:normal state is returned.
   * 
   * @param portletMode The <code>javax.portlet.WindowState</code> which
   *          should be resolved as as portlet window state defined in wsrp.
   * @return
   */

  public static WindowStates getWsrpStateFromJsrPortletState(WindowState windowState) {
    if (windowState == null) {
      throw new IllegalArgumentException("Window state must not be null.");
    }
    // if this portletState is already a suitable wsrp state
    if (windowState.toString().equalsIgnoreCase(_normal)) {
      return WindowStates.normal;
    } else if (windowState.toString().equalsIgnoreCase(_maximized)) {
      return WindowStates.maximized;
    } else if (windowState.toString().equalsIgnoreCase(_minimized)) {
      return WindowStates.minimized;
    } else if (windowState.toString().equalsIgnoreCase(_solo)) {
      return WindowStates.solo;
    }
    // otherwise
    if (windowState.toString().equalsIgnoreCase(WindowState.MAXIMIZED.toString())) {
      return WindowStates.maximized;
    } else if (windowState.toString().equalsIgnoreCase(WindowState.MINIMIZED.toString())) {
      return WindowStates.minimized;
    } else if (windowState.toString().equalsIgnoreCase(WindowState.NORMAL.toString())) {
      return WindowStates.normal;
    } else if (windowState.toString().equalsIgnoreCase(soloString)) {
      return WindowStates.solo;
    }
    System.out.println("WindowStates.getWsrpStateFromJsrPortletState " + windowState.toString()
        + " changed with '" + _normal + "' mode");
    return WindowStates.normal;
  }

  public static String[] getWindowStatesAsStringArray() {
    return (String[]) _table_.keySet().toArray();
  }

  public static String delAllPrefixWSRP(String forDelWSRP) {
    while (forDelWSRP.startsWith(WSRPConstants.WSRP_PREFIX))
      forDelWSRP = forDelWSRP.substring(WSRPConstants.WSRP_PREFIX.length());
    return forDelWSRP;
  }

  public static String addPrefixWSRP(String forAddWSRP) {
    return WSRPConstants.WSRP_PREFIX + forAddWSRP;
  }

}
