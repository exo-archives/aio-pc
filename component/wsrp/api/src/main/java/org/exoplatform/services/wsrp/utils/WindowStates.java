/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "WSRP4J",  and "Apache Software
 *    Foundation" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    or "WSRP4J", nor may "Apache" or "WSRP4J" appear in their name,
 *    without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.exoplatform.services.wsrp.utils;

import javax.portlet.WindowState;

public class WindowStates implements java.io.Serializable {
  private java.lang.String _value_;
  private static java.util.HashMap _table_ = new java.util.HashMap();

  // Constructor
  protected WindowStates(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  // define the window states we can currently handle
  public static final java.lang.String _normal = "wsrp:normal";
  public static final java.lang.String _minimized = "wsrp:minimized";
  public static final java.lang.String _maximized = "wsrp:maximized";
  public static final java.lang.String _solo = "wsrp:solo";
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
      return WindowState.NORMAL;
    } else if (wsrpState.equals(WindowStates.maximized)) {
      return WindowState.MAXIMIZED;
    } else if (wsrpState.equals(WindowStates.minimized)) {
      return WindowState.MINIMIZED;
    } else if (wsrpState.equals(WindowStates.normal)) {
      return WindowState.NORMAL;
    }

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
  public static WindowStates getWsrpStateFromJsrPortletState(WindowState portletState) {
    if (portletState.equals(WindowState.MAXIMIZED)) {
      return WindowStates.maximized;
    } else if (portletState.equals(WindowState.MINIMIZED)) {
      return WindowStates.minimized;
    } else if (portletState.equals(WindowState.NORMAL)) {
      return WindowStates.normal;
    }

    return WindowStates.normal;
  }

  public static String[] getWindowStatesAsStringArray() {
    return (String[]) _table_.keySet().toArray();
  }
}
