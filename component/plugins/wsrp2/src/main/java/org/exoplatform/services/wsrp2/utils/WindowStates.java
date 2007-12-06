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

import javax.portlet.WindowState;

import org.exoplatform.services.wsrp2.WSRPConstants;

public class WindowStates implements java.io.Serializable {
  private java.lang.String _value_;
  private static java.util.HashMap _table_ = new java.util.HashMap();

  // Constructor
  public WindowStates(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  // define the window states we can currently handle
  private static final java.lang.String normalString = WindowState.NORMAL.toString();
  private static final java.lang.String minimizedString = WindowState.MINIMIZED.toString();
  private static final java.lang.String maximizedString = WindowState.MAXIMIZED.toString();
  private static final java.lang.String soloString = "solo";
  
  public static final java.lang.String _normal = WSRPConstants.WSRP_PREFIX + normalString;
  public static final java.lang.String _minimized = WSRPConstants.WSRP_PREFIX + minimizedString;
  public static final java.lang.String _maximized = WSRPConstants.WSRP_PREFIX + maximizedString;
  public static final java.lang.String _solo = WSRPConstants.WSRP_PREFIX + soloString;
  
  public static final WindowStates normal = new WindowStates(_normal);
  public static final WindowStates minimized = new WindowStates(_minimized);
  public static final WindowStates maximized = new WindowStates(_maximized);
  public static final WindowStates solo = new WindowStates(_solo);

  public java.lang.String getValue() {
    return _value_;
  }

  /**
   * Returns the WSRP window state build from a string representation
   * If a not supported window state is requested, null is returned
   *
   * @param <code>String</string> representation of the WSRP window state
   * @return The WSRP <code>WindowStates</code> represented by the passed string
   */
  public static WindowStates fromValue(java.lang.String value) {
    return (WindowStates) _table_.get(value);
  }

  /**
   * Returns the WSRP window state build from a string representation
   * If a not supported window state is requested, null is returned
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
   * window states defined in the java portlet standard (JSR-168).
   * If the passed wsrp window state is null or can not be mapped
   * directly the normal state is returned.
   *
   * @return The <code>javax.portlet.WindowState</code> which corresponds to the given wsrp state.
   */
  
  public static WindowState getJsrPortletStateFromWsrpState(WindowStates wsrpState) {
    if (wsrpState == null) {
      throw new IllegalArgumentException("WSRP window state must not be null.");
    }
    return getJsrPortletStateFromWsrpState(wsrpState.toString());
  }
  
  public static WindowState getJsrPortletStateFromWsrpState(String wsrpState) {
    if (wsrpState == null)
      return null;
    String windowState = delAllPrefixWSRP(wsrpState).toLowerCase();
    if (windowState.equalsIgnoreCase(normalString)) {
      return WindowState.NORMAL;
    } else if (windowState.equalsIgnoreCase(maximizedString)) {
      return WindowState.MAXIMIZED;
    } else if (windowState.equalsIgnoreCase(minimizedString)) {
      return WindowState.MINIMIZED;
    } else if (windowState.equalsIgnoreCase(soloString)) {
      return new WindowState(soloString);
    }
    System.out.println("WindowStates.getJsrPortletStateFromWsrpState " + wsrpState + " changed with '" + normalString + "' state");
    //return new WindowState(jsrState.toLowerCase());
    return WindowState.NORMAL;
  }

  /**
   * This helper method maps portlet window states defined in tha java portlet standard (JSR-168)
   * to window states defined in wsrp. If the passed state can not be resolved wsrp:normal state
   * is returned.
   *
   * @param portletMode The <code>javax.portlet.WindowState</code> which should be resolved as
   *                    as portlet window state defined in wsrp.
   * @return
   */
  
  public static WindowStates getWsrpStateFromJsrPortletState(WindowState windowState) {
    if (windowState == null) {
      throw new IllegalArgumentException("Window state must not be null.");
    }
    return getWsrpStateFromJsrPortletState(windowState.toString());
  }
  
  public static WindowStates getWsrpStateFromJsrPortletState(String windowState) {
    if (windowState == null)
      return null;
    String wsrpState = addPrefixWSRP(windowState).toLowerCase();
    if (wsrpState.equalsIgnoreCase(_normal)) {
      return WindowStates.normal;
    } else if (wsrpState.equalsIgnoreCase(_maximized)) {
      return WindowStates.maximized;
    } else if (wsrpState.equalsIgnoreCase(_minimized)) {
      return WindowStates.minimized;
    } else if (wsrpState.equalsIgnoreCase(_solo)) {
      return WindowStates.solo;
    }
    System.out.println("WindowStates.getWsrpStateFromJsrPortletState " + windowState + " changed with '" + _normal + "' state");
    return WindowStates.normal;
  }

  public static String[] getWindowStatesAsStringArray() {
    return (String[]) _table_.keySet().toArray();
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
