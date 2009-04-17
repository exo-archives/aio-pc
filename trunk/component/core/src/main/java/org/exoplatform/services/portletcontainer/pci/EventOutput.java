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
package org.exoplatform.services.portletcontainer.pci;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.Event;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.xml.namespace.QName;

/**
 * @author: Roman Pedchenko
 * @email: roman.pedchenko@exoplatform.com.ua
 * @version: $Id$
 */
public class EventOutput extends Output {

  /**
   * Events list.
   */
  private List<Event> events = new ArrayList<Event>();

  /**
   * Next mode.
   */
  private PortletMode nextMode;

  /**
   * Next state.
   */
  private WindowState nextState;

  /**
   * Render parameters.
   */
  private Map<String, String[]> renderParameters = new HashMap<String, String[]>();

  /**
   * @return events list
   */
  public final List<Event> getEvents() {
    return events;
  }

  /**
   * @param eventName event name
   * @return event object
   */
  public final Event getEventByName(final String eventName) {
    for (Event event : events)
      if (event.getName().equals(eventName))
        return event;
    return null;
  }

  /**
   * @param eventName event name
   * @return event payload
   */
  public final Serializable getEventValueByName(final String eventName) {
    Event event = getEventByName(eventName);
    if (event != null)
      return event.getValue();
    return null;
  }

  /**
   * @param events events list
   */
  public final void setEvents(final List<Event> events) {
    this.events = events;
  }

  /**
   * @param name event name
   * @param event event object
   */
  public final void setEvent(final QName name, final Serializable event) {
    events.add(new EventImpl(name, event));
  }

  /**
   * @return next mode
   */
  public final PortletMode getNextMode() {
    return nextMode;
  }

  /**
   * @param nextMode next mode
   */
  public final void setNextMode(final PortletMode nextMode) {
    this.nextMode = nextMode;
  }

  /**
   * @return next state
   */
  public final WindowState getNextState() {
    return nextState;
  }

  /**
   * @param nextState next state
   */
  public final void setNextState(final WindowState nextState) {
    this.nextState = nextState;
  }

  /**
   * @return parameters
   */
  public final Map<String, String[]> getRenderParameters() {
    return renderParameters;
  }

  /**
   * @param renderParameters parameters
   */
  public final void setRenderParameters(final Map<String, String[]> renderParameters) {
    this.renderParameters = renderParameters;
  }

  /**
   * @param key key
   * @param value value
   */
  public final void setRenderParameter(final String key, final String value) {
    renderParameters.put(key, new String[] {value});
  }

  /**
   * @param key key
   * @param values values
   */
  public final void setRenderParameters(final String key, final String[] values) {
    renderParameters.put(key, values);
  }

}
