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

package org.exoplatform.services.wsrp2.consumer.adapters;

import org.exoplatform.services.wsrp2.consumer.WSRPEventsRequest;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.StateChange;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 14.09.2007
 */
public class WSRPEventsRequestAdapter extends WSRPBaseRequestAdapter implements WSRPEventsRequest {

  private Event[]     events;

  private StateChange portletStateChange; //WSRP2

  public org.exoplatform.services.wsrp2.type.Event[] getEvents() {
    return events;
  }

  public void setEvents(org.exoplatform.services.wsrp2.type.Event[] events) {
    this.events = events;
  }

  public org.exoplatform.services.wsrp2.type.Event getEvents(int i) {
    return events[i];
  }

  public void setEvents(int i, org.exoplatform.services.wsrp2.type.Event _value) {
    this.events[i] = _value;
  }

  public StateChange getPortletStateChange() {
    return portletStateChange;
  }

  public void setPortletStateChange(StateChange portletStateChange) {
    this.portletStateChange = portletStateChange;
  }

}
