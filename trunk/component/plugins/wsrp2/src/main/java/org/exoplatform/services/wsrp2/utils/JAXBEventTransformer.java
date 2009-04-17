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

package org.exoplatform.services.wsrp2.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.EventPayload;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua Oct 15, 2007.
 */
public class JAXBEventTransformer {

  /** The Constant log. */
  private static final Log log = ExoLogger.getLogger("org.exoplatform.services.wsrp2.utils.JAXBEventTransformer");

  // convert from "List<javax.portlet.Event>" to "org.exoplatform.services.wsrp2.type.Event[]"
  /**
   * Gets the events marshal.
   * 
   * @param eventsList the events list
   * 
   * @return the events marshal
   */
  public static List<Event> getEventsMarshal(List<javax.portlet.Event> eventsList) {
    List<Event> events = new ArrayList<Event>();
    for (javax.portlet.Event event : eventsList) {
      events.add(getEventMarshal(event));
    }
    return events;
  }

  // convert from "javax.portlet.Event" to "org.exoplatform.services.wsrp2.type.Event"
  /**
   * Gets the event marshal.
   * 
   * @param event the event
   * 
   * @return the event marshal
   */
  public static Event getEventMarshal(javax.portlet.Event event) {
    if (event == null)
      return null;

    QName eventName = event.getQName();
    Serializable eventValue = event.getValue();

    if (log.isDebugEnabled())
      log.debug("JAXBEventTransformer.getEventMarshal() eventName = " + eventName);
    if (log.isDebugEnabled())
      log.debug("JAXBEventTransformer.getEventMarshal() eventValue = " + eventValue);

    QName eventType = null;
    EventPayload eventPayload = null;

    if (eventValue != null) {

      eventType = new QName(eventValue.getClass().getName());

      org.w3c.dom.Document doc = getMarshalledDocument(eventValue, eventName);

      if (doc == null) {
        try {
          doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
          try {
            JAXBContext jaxbContext = JAXBContext.newInstance(eventValue.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(eventValue, doc);
          } catch (Exception e) {
            JAXBContext jaxbContext = JAXBContext.newInstance(eventValue.getClass()
                                                                        .getPackage()
                                                                        .getName());
            jaxbContext.createMarshaller()
                       .marshal(new JAXBElement(new QName("", eventValue.getClass().getSimpleName()),
                                                eventValue.getClass(),
                                                eventValue),
                                doc);
          }
        } catch (JAXBException je) {
          je.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
      org.w3c.dom.Element messageElement = doc.getDocumentElement();
      eventPayload = new EventPayload();
      eventPayload.setAny(messageElement);
    }

    Event newEvent = new Event();
    newEvent.setName(eventName);
    newEvent.setType(eventType);
    newEvent.setPayload(eventPayload);
    return newEvent;
  }

  // convert from "org.exoplatform.services.wsrp2.type.Event[]" to "List<javax.portlet.Event>"
  /**
   * Gets the events unmarshal.
   * 
   * @param eventList the event list
   * 
   * @return the events unmarshal
   */
  public static List<javax.portlet.Event> getEventsUnmarshal(List<Event> eventList) {
    if (eventList == null)
      return null;
    List<javax.portlet.Event> eventsList = new ArrayList<javax.portlet.Event>();
    for (Event event : eventList) {

      Object obj = null;
      if (event.getType() != null && event.getPayload() != null) {
        obj = getUnmarshalledObject(event.getType(), event.getPayload());

        if (obj == null) {
          try {
            String clazz = event.getType().getLocalPart();

            String pkg = clazz.substring(0, clazz.lastIndexOf("."));
            ClassLoader cle = Thread.currentThread().getContextClassLoader(); //was: this.getClass().getClassLoader();
            org.w3c.dom.Element messageElement = (org.w3c.dom.Element) event.getPayload().getAny();

            JAXBContext jaxbContext = JAXBContext.newInstance(pkg, cle);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            obj = unmarshaller.unmarshal(messageElement);
            //TODO do we need to do something with value which doesn't contain an XmlRootElement?
          } catch (JAXBException je) {
            je.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          } catch (Throwable t) {
            t.printStackTrace();
          }
        }

        if (log.isDebugEnabled())
          log.debug("JAXBEventTransformer.getEventsUnmarshal() o = " + obj);

      }
      javax.portlet.Event ev = new EventImpl(event.getName(), obj != null ? (Serializable) obj
                                                                         : null);
      eventsList.add(ev);
    }
    return eventsList;
  }

  /**
   * Gets the marshalled document.
   * 
   * @param value the value
   * @param name the name
   * 
   * @return the marshalled document
   */
  public static org.w3c.dom.Document getMarshalledDocument(Object value, QName name) {
    if (log.isDebugEnabled())
      log.debug("JAXBEventTransformer.getMarshalledDocument() value = " + value);
    try {
      String toenum = value.getClass().getSimpleName();
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getMarshalledDocument() toenum = " + toenum);
      StandardClasses t = null;
      try {
        t = StandardClasses.valueOf(toenum.toUpperCase());
      } catch (java.lang.IllegalArgumentException e) {
        return null;
      }
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getMarshalledDocument() t = " + t);
      return t.getMarshalledDocument(value, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gets the unmarshalled object.
   * 
   * @param type the type
   * @param payload the payload
   * 
   * @return the unmarshalled object
   */
  private static Object getUnmarshalledObject(QName type, EventPayload payload) {
    try {
      String eventType = type.getLocalPart();
      String toenum = eventType.substring(eventType.lastIndexOf(".") + 1);
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getUnmarshalledObject() toenum = " + toenum);
      StandardClasses t = null;
      try {
        t = StandardClasses.valueOf(toenum.toUpperCase());
      } catch (java.lang.IllegalArgumentException e) {
        return null;
      }
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getUnmarshalledObject() t = " + t);
      return t.getUnmarshalledObject(type, payload);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
