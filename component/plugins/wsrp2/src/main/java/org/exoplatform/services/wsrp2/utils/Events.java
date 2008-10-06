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

import javax.portlet.Event;

public class Events implements java.io.Serializable {

  private java.lang.String                     _value_;

  private static java.util.Map<String, Events> _table_ = new java.util.HashMap<String, Events>();

  //Constructor
  protected Events(java.lang.String value) {
    _value_ = value;
    _table_.put(_value_, this);
  }

  //define the events we can currently handle
  public static final java.lang.String _eventHandlingFailed         = "wsrp:eventHandlingFailed";

  public static final java.lang.String _newNavigationalContextScope = "wsrp:newNavigationalContextScope";

  public static final Events           eventHandlingFailed          = new Events(_eventHandlingFailed);

  public static final Events           newNavigationalContextScope  = new Events(_newNavigationalContextScope);

  public java.lang.String getValue() {
    return _value_;
  }

  public static Events fromValue(java.lang.String value) {
    return (Events) _table_.get(value);
  }

  public static Events fromString(java.lang.String value) {
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

  public static Events getJsrEventFromWsrpEvent(Event event) {
    if (event == null) {
      throw new IllegalArgumentException("Event must not be null.");
    }
    if (event.equals(Events.eventHandlingFailed)) {
      return eventHandlingFailed;
    } else if (event.equals(Events.newNavigationalContextScope)) {
      return newNavigationalContextScope;
    }

    return eventHandlingFailed;
  }

}
