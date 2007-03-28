/**
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
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */


package org.exoplatform.services.wsrp.utils;

import javax.portlet.PortletMode;

public class Modes implements java.io.Serializable {
  private java.lang.String _value_;
  private static java.util.HashMap _table_ = new java.util.HashMap();

  // Constructor
  protected Modes(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  // define the modes we can currently handle
  public static final java.lang.String _view = "wsrp:view";
  public static final java.lang.String _edit = "wsrp:edit";
  public static final java.lang.String _help = "wsrp:help";
  public static final java.lang.String _preview = "wsrp:preview";
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
      return PortletMode.VIEW;
    } else if (wsrpMode.equals(Modes.edit)) {
      return PortletMode.EDIT;
    } else if (wsrpMode.equals(Modes.help)) {
      return PortletMode.HELP;
    } else if (wsrpMode.equals(Modes.view)) {
      return PortletMode.VIEW;
    }

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
    if (portletMode.equals(PortletMode.EDIT)) {
      return Modes.edit;
    } else if (portletMode.equals(PortletMode.HELP)) {
      return Modes.help;
    } else if (portletMode.equals(PortletMode.VIEW)) {
      return Modes.view;
    }
    return Modes.view;
  }
}
