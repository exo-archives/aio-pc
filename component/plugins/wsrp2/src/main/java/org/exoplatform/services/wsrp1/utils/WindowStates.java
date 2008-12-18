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

package org.exoplatform.services.wsrp1.utils;

import java.util.Locale;

import javax.portlet.WindowState;

import org.exoplatform.services.wsrp.WindowStates;
import org.exoplatform.services.wsrp1.WSRPConstants;

public class WindowStates implements java.io.Serializable {
  /**
   * The serialVersionUID.
   */
  private static final long                          serialVersionUID = 2161181578039145431L;

  private java.lang.String                           _value_;

  private static java.util.Map<String, WindowStates> _table_          = new java.util.HashMap<String, WindowStates>();

  // Constructor with String
  public WindowStates(java.lang.String value) {
    _value_ = WSRPConstants.WSRP_PREFIX + value.toLowerCase(Locale.ENGLISH);
    _table_.put(_value_, this);
  }

  // define the window states we can currently handle
  private static final java.lang.String _normal_jsr     = WindowState.NORMAL.toString();

  private static final java.lang.String _minimized_jsr  = WindowState.MINIMIZED.toString();

  private static final java.lang.String _maximized_jsr  = WindowState.MAXIMIZED.toString();

  private static final java.lang.String _solo_jsr       = "solo";

  public static final java.lang.String  _normal_wsrp    = WSRPConstants.WSRP_PREFIX + _normal_jsr;

  public static final java.lang.String  _minimized_wsrp = WSRPConstants.WSRP_PREFIX
                                                            + _minimized_jsr;

  public static final java.lang.String  _maximized_wsrp = WSRPConstants.WSRP_PREFIX
                                                            + _maximized_jsr;

  public static final java.lang.String  _solo_wsrp      = WSRPConstants.WSRP_PREFIX + _solo_jsr;

  public static final WindowStates      NORMAL          = new WindowStates(_normal_wsrp);

  public static final WindowStates      MINIMIZED       = new WindowStates(_minimized_wsrp);

  public static final WindowStates      MAXIMIZED       = new WindowStates(_maximized_wsrp);

  public static final WindowStates      SOLO            = new WindowStates(_solo_wsrp);

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
    return (WindowStates) _table_.get(value.toLowerCase(Locale.ENGLISH));
  }

  /**
   * Returns the WSRP window state build from a string representation If a not
   * supported window state is requested, null is returned
   * 
   * @param <code>String</string> representation of the WSRP window state
   * @return The WSRP <code>WindowStates</code> represented by the passed string
   */
  public static WindowStates fromString(java.lang.String value) {
    return fromValue(value.toLowerCase(Locale.ENGLISH));
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

  public static String[] getWindowStatesAsStringArray() {
    return (String[]) _table_.keySet().toArray();
  }

  /**
   * This helper method maps portlet window states defined in wsrp to portlet
   * window states defined in the java portlet standard (JSR-168). If the passed
   * wsrp window state is null or can not be mapped directly the NORMAL state is
   * returned.
   * 
   * @return The <code>javax.portlet.WindowState</code> which corresponds to
   *         the given wsrp state.
   */

  public static WindowState getJsrWindowState(String state) {
    if (state == null) {
      throw new IllegalArgumentException("WSRP portlet state must not be null.");
    }
    state = delAllPrefixesWSRP(state);
    if (state.equalsIgnoreCase(_normal_jsr)) {
      return WindowState.NORMAL;
    } else if (state.equalsIgnoreCase(_maximized_jsr)) {
      return WindowState.MAXIMIZED;
    } else if (state.equalsIgnoreCase(_minimized_jsr)) {
      return WindowState.MINIMIZED;
    } else if (state.equalsIgnoreCase(_solo_jsr)) {
      return new WindowState(_solo_jsr);
    }
    return new WindowState(state);
  }

  public static String getWSRPStateString(WindowState jsrWindowState) {
    return addPrefixWSRP(jsrWindowState.toString());
  }

  public static String addPrefixWSRP(String forAddWSRP) {
    return WSRPConstants.WSRP_PREFIX + forAddWSRP.toLowerCase(Locale.ENGLISH);
  }

  public static String delAllPrefixesWSRP(String forDelWSRP) {
    forDelWSRP = forDelWSRP.toLowerCase(Locale.ENGLISH);
    while (forDelWSRP.startsWith(WSRPConstants.WSRP_PREFIX))
      forDelWSRP = forDelWSRP.substring(WSRPConstants.WSRP_PREFIX.length());
    return forDelWSRP;
  }

}
