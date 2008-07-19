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

import java.util.Locale;

import javax.portlet.PortletMode;

import org.exoplatform.services.wsrp.WSRPConstants;

public class Modes implements java.io.Serializable {

  /**
   * The serialVersionUID.
   */
  private static final long                   serialVersionUID = -6024244156120091359L;

  private java.lang.String                    _value_;

  private static java.util.Map<String, Modes> _table_          = new java.util.HashMap<String, Modes>();

  // Constructor with String
  public Modes(java.lang.String value) {
    _value_ = value.toLowerCase(Locale.ENGLISH);
    _table_.put(_value_, this);
  }

  // define the modes we can currently handle
  private static final java.lang.String _view_jsr     = PortletMode.VIEW.toString();

  private static final java.lang.String _edit_jsr     = PortletMode.EDIT.toString();

  private static final java.lang.String _help_jsr     = PortletMode.HELP.toString();

  private static final java.lang.String _preview_jsr  = "preview";

  public static final java.lang.String  _view_wsrp    = WSRPConstants.WSRP_PREFIX + _view_jsr;

  public static final java.lang.String  _edit_wsrp    = WSRPConstants.WSRP_PREFIX + _edit_jsr;

  public static final java.lang.String  _help_wsrp    = WSRPConstants.WSRP_PREFIX + _help_jsr;

  public static final java.lang.String  _preview_wsrp = WSRPConstants.WSRP_PREFIX + _preview_jsr;

  public static final Modes             VIEW          = new Modes(_view_wsrp);

  public static final Modes             EDIT          = new Modes(_edit_wsrp);

  public static final Modes             HELP          = new Modes(_help_wsrp);

  public static final Modes             PREVIEW       = new Modes(_preview_wsrp);

  public java.lang.String getValue() {
    return _value_;
  }

  /**
   * Returns the WSRP mode build from a string representation If a not supported
   * Mode is requested, null is returned
   * 
   * @param <code>String</string> representation of the WSRP mode
   * @return The WSRP <code>Mode</code> represented by the passed string
   */
  public static Modes fromValue(java.lang.String value) {
    return (Modes) _table_.get(value);
  }

  /**
   * Returns the WSRP mode build from a string representation If a not supported
   * Mode is requested, null is returned
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
   * defined in the java portlet standard (JSR-168). If the passed wsrp mode is
   * null or can not be mapped the VIEW mode is returned.
   * 
   * @return The <code>javax.portlet.PortletMode</code> which corresponds to
   *         the given wsrp mode.
   */

  public static PortletMode getJsrPortletMode(String mode) {
    if (mode == null) {
      throw new IllegalArgumentException("WSRP portlet mode must not be null.");
    }

    mode = delAllPrefixesWSRP(mode);

    if (mode.equalsIgnoreCase(_view_jsr)) {
      return PortletMode.VIEW;
    } else if (mode.equalsIgnoreCase(_edit_jsr)) {
      return PortletMode.EDIT;
    } else if (mode.equalsIgnoreCase(_help_jsr)) {
      return PortletMode.HELP;
    } else if (mode.equalsIgnoreCase(_preview_jsr)) {
      return new PortletMode(_preview_jsr);
    }
    return new PortletMode(mode);
  }
  
  public static String addPrefixWSRP(String forAddWSRP) {
    return WSRPConstants.WSRP_PREFIX + forAddWSRP.toLowerCase(Locale.ENGLISH);
  }
  
  public static String getWSRPModeString(PortletMode jsrPortletMode) {
    return addPrefixWSRP(jsrPortletMode.toString().toLowerCase(Locale.ENGLISH));
  }

  public static String delAllPrefixesWSRP(String forDelWSRP) {
    forDelWSRP = forDelWSRP.toLowerCase(Locale.ENGLISH);
    while (forDelWSRP.startsWith(WSRPConstants.WSRP_PREFIX))
      forDelWSRP = forDelWSRP.substring(WSRPConstants.WSRP_PREFIX.length());
    return forDelWSRP;
  }

}
