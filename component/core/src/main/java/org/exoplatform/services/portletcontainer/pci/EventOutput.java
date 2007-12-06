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

  private List<Event>           events           = new ArrayList<Event>();

  private PortletMode           nextMode;

  private WindowState           nextState;

  private Map<String, String[]> renderParameters = new HashMap<String, String[]>();

  public List<Event> getEvents() {
    return events;
  }

  public Event getEventByName(String eventName) {
    for (Event event : events) {
      if (event.getName().equals(eventName))
        return event;
    }
    return null;
  }

  public Serializable getEventValueByName(String eventName) {
    Event event = getEventByName(eventName);
    if (event != null)
      return event.getValue();
    return null;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }

  public void setEvent(QName name,
                       Serializable event) {
    events.add(new EventImpl(name, event));
  }

  public PortletMode getNextMode() {
    return nextMode;
  }

  public void setNextMode(PortletMode nextMode) {
    this.nextMode = nextMode;
  }

  public WindowState getNextState() {
    return nextState;
  }

  public void setNextState(WindowState nextState) {
    this.nextState = nextState;
  }

  public Map<String, String[]> getRenderParameters() {
    return renderParameters;
  }

  public void setRenderParameters(Map<String, String[]> renderParameters) {
    this.renderParameters = renderParameters;
  }

  public void setRenderParameter(String key,
                                 String value) {
    renderParameters.put(key, new String[] { value });
  }

  public void setRenderParameters(String key,
                                  String[] values) {
    renderParameters.put(key, values);
  }

}
