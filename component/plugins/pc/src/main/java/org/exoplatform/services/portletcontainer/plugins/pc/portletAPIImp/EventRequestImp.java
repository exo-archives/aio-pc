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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.io.Serializable;

import javax.portlet.Event;
import javax.portlet.EventRequest;

import org.exoplatform.services.portletcontainer.helper.EventUtil;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.portletcontainer.pci.EventInput;

/**
 * Created by The eXo Platform SAS Author : Roman Pedchenko
 * <roman.pedchenko@exoplatform.com.ua>
 */
public class EventRequestImp extends PortletRequestImp implements EventRequest {

  public EventRequestImp(final RequestContext reqCtx) {
    super(reqCtx);
  }

  /**
   * The event must always have a name and may optionally have a value.
   * PLT.15.2.2.
   */
  public Event getEvent() {
    Event event = ((EventInput) getInput()).getEvent();
    Serializable payload = event.getValue();
    if (payload == null || isCurrentClassLoader(payload)) {
      return event;
    } else {
      Serializable newPayload = getSerializeDeserialize(payload);
      Event newEvent = new EventImpl(event.getQName(), newPayload);
      return newEvent;
    }
  }

  public String getLifecyclePhase() {
    return EVENT_PHASE;
  }

  private boolean isCurrentClassLoader(Serializable payload) {
    boolean doNotSerialize = true;
    String fqn = payload.getClass().getCanonicalName();
    try {
      Class clazz = Thread.currentThread().getContextClassLoader().loadClass(fqn);
      doNotSerialize = clazz.isInstance(payload);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return doNotSerialize;
  }

  private Serializable getSerializeDeserialize(Serializable payload) {
    Serializable newPayload = null;
    try {
      byte[] bytes = EventUtil.serialize(payload);
      newPayload = (Serializable) EventUtil.deserializeInContextClassloader(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newPayload;
  }

}
