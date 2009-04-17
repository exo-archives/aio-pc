/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import java.util.Locale;

import javax.portlet.WindowState;

import org.exoplatform.services.wsrp2.WSRPConstants;

/**
 * The Class WindowStates.
 */
public class WindowStates implements java.io.Serializable {
  /**
   * The serialVersionUID.
   */
  private static final long                              serialVersionUID = 3398729378472092729L;

  /** The _value_. */
  private java.lang.String                               _value_;

  /** The _table_. */
  private static java.util.HashMap<String, WindowStates> _table_          = new java.util.HashMap<String, WindowStates>();

  /**
   * Instantiates a new window states.
   * 
   * @param value the value
   */
  public WindowStates(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  // define the window states we can currently handle
  /** The Constant _normal_jsr. */
  private static final java.lang.String _normal_jsr     = WindowState.NORMAL.toString();

  /** The Constant _minimized_jsr. */
  private static final java.lang.String _minimized_jsr  = WindowState.MINIMIZED.toString();

  /** The Constant _maximized_jsr. */
  private static final java.lang.String _maximized_jsr  = WindowState.MAXIMIZED.toString();

  /** The Constant _solo_jsr. */
  private static final java.lang.String _solo_jsr       = "solo";

  /** The Constant _normal_wsrp. */
  public static final java.lang.String  _normal_wsrp    = WSRPConstants.WSRP_PREFIX + _normal_jsr;

  /** The Constant _minimized_wsrp. */
  public static final java.lang.String  _minimized_wsrp = WSRPConstants.WSRP_PREFIX
                                                            + _minimized_jsr;

  /** The Constant _maximized_wsrp. */
  public static final java.lang.String  _maximized_wsrp = WSRPConstants.WSRP_PREFIX
                                                            + _maximized_jsr;

  /** The Constant _solo_wsrp. */
  public static final java.lang.String  _solo_wsrp      = WSRPConstants.WSRP_PREFIX + _solo_jsr;

  /** The Constant NORMAL. */
  public static final WindowStates      NORMAL          = new WindowStates(_normal_wsrp);

  /** The Constant MINIMIZED. */
  public static final WindowStates      MINIMIZED       = new WindowStates(_minimized_wsrp);

  /** The Constant MAXIMIZED. */
  public static final WindowStates      MAXIMIZED       = new WindowStates(_maximized_wsrp);

  /** The Constant SOLO. */
  public static final WindowStates      SOLO            = new WindowStates(_solo_wsrp);

  /**
   * Gets the value.
   * 
   * @return the value
   */
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

  /**
   * Read resolve.
   * 
   * @return the java.lang. object
   * 
   * @throws ObjectStreamException the object stream exception
   */
  public java.lang.Object readResolve() throws java.io.ObjectStreamException {
    return fromValue(_value_);
  }

  /**
   * Gets the window states as string array.
   * 
   * @return the window states as string array
   */
  public static String[] getWindowStatesAsStringArray() {
    return (String[]) _table_.keySet().toArray();
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

  /**
   * Gets the wSRP state string.
   * 
   * @param jsrWindowState the jsr window state
   * 
   * @return the wSRP state string
   */
  public static String getWSRPStateString(WindowState jsrWindowState) {
    return addPrefixWSRP(jsrWindowState.toString());
  }

  /**
   * Adds the prefix wsrp.
   * 
   * @param forAddWSRP the for add wsrp
   * 
   * @return the string
   */
  public static String addPrefixWSRP(String forAddWSRP) {
    return WSRPConstants.WSRP_PREFIX + forAddWSRP.toLowerCase(Locale.ENGLISH);
  }

  /**
   * Deletes all prefixes wsrp.
   * 
   * @param forDelWSRP the for del wsrp
   * 
   * @return the string
   */
  public static String delAllPrefixesWSRP(String forDelWSRP) {
    forDelWSRP = forDelWSRP.toLowerCase(Locale.ENGLISH);
    while (forDelWSRP.startsWith(WSRPConstants.WSRP_PREFIX))
      forDelWSRP = forDelWSRP.substring(WSRPConstants.WSRP_PREFIX.length());
    return forDelWSRP;
  }

}
