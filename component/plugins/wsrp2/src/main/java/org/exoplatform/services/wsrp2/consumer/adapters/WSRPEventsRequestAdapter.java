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

package org.exoplatform.services.wsrp2.consumer.adapters;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.wsrp2.consumer.WSRPEventsRequest;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.StateChange;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 14.09.2007
 */
public class WSRPEventsRequestAdapter extends WSRPBaseRequestAdapter implements WSRPEventsRequest {

  private List<Event> events;

  private StateChange portletStateChange;

  public List<Event> getEvents() {
    if (events == null) {
      events = new ArrayList<Event>();
    }
    return this.events;
  }

  public StateChange getPortletStateChange() {
    return portletStateChange;
  }

  public void setPortletStateChange(StateChange portletStateChange) {
    this.portletStateChange = portletStateChange;
  }

}
