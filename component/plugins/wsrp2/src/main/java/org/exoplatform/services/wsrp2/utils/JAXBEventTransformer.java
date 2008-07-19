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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.message.MessageElement;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.EventPayload;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua Oct 15, 2007
 */
public class JAXBEventTransformer {

  // convert from "javax.portlet.Event" to "org.exoplatform.services.wsrp2.type.Event[1]"
  public static Event[] getEventsMarshal(javax.portlet.Event event) {
    return new Event[] { getEventMarshal(event) };
  }

  // convert from "List<javax.portlet.Event>" to "org.exoplatform.services.wsrp2.type.Event[]"
  public static Event[] getEventsMarshal(List<javax.portlet.Event> eventsList) {
    Event[] events = new Event[eventsList.size()];
    int i = 0;
    for (javax.portlet.Event event : eventsList) {
      events[i++] = getEventMarshal(event);
    }
    return events;
  }

  // convert from "javax.portlet.Event" to "org.exoplatform.services.wsrp2.type.Event"
  public static Event getEventMarshal(javax.portlet.Event event) {
    Event newEvent = new Event();
    newEvent.setName(event.getQName());
    if (event.getValue() != null) {
      String eventType = event.getValue().getClass().toString().substring("class ".length());
      newEvent.setType(new QName(eventType));
      EventPayload eventPayload = null;
      try {
        eventPayload = new EventPayload();
        org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance()
                                                         .newDocumentBuilder()
                                                         .newDocument();
        Serializable o = event.getValue();
        JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
        jaxbContext.createMarshaller().marshal(o, doc);
        org.apache.axis.message.MessageElement messageElement = new MessageElement(doc.getDocumentElement());
        org.apache.axis.message.MessageElement[] anyArray = new MessageElement[] { messageElement };
        eventPayload.set_any(anyArray);
      } catch (JAXBException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      newEvent.setPayload(eventPayload);
    } else {

    }
    return newEvent;
  }

  // convert from "org.exoplatform.services.wsrp2.type.Event[]" to "List<javax.portlet.Event>"
  public static List<javax.portlet.Event> getEventsUnmarshal(Event[] eventArray) {
    if (eventArray == null)
      return null;
    List<javax.portlet.Event> eventsList = new ArrayList<javax.portlet.Event>();
    int i = 0;
    for (Event event : eventArray) {
      Object o = new Object();
      try {
        String clazz = event.getType().getLocalPart();
        String pkg = clazz.substring(0, clazz.lastIndexOf("."));
        ClassLoader cle = Thread.currentThread().getContextClassLoader();//was: this.getClass().getClassLoader();

        JAXBContext jaxbContext = JAXBContext.newInstance(pkg, cle);
        org.w3c.dom.Element messageElement = event.getPayload().get_any()[0];
        o = jaxbContext.createUnmarshaller().unmarshal(messageElement);
      } catch (JAXBException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      javax.portlet.Event ev = new EventImpl(event.getName(), o != null ? (Serializable) o : null);
      eventsList.add(ev);
      i++;
    }
    return eventsList;
  }

}
