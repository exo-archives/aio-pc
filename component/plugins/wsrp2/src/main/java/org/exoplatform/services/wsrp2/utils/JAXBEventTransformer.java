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

import java.awt.Image;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.message.MessageElement;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.EventImpl;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.EventPayload;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua Oct 15, 2007
 */
public class JAXBEventTransformer {

  private static final Log                 log           = ExoLogger.getLogger("org.exoplatform.services.wsrp2.utils.JAXBEventTransformer");

  private static final String[]            jaxb8_5_2List = new String[] { "java.lang.String",
      "java.math.BigInteger", "java.math.BigDecimal", "java.util.Calendar", "java.util.Date",
      "javax.xml.namespace.QName", "java.net.URI", "javax.xml.datatype.XMLGregorianCalendar",
      "javax.xml.datatype.Duration", "java.awt.Image", "javax.activation.DataHandler",
      "javax.xml.transform.Source", "java.util.UUID"    };

  private static final Enumeration<String> jaxbEnum      = Collections.enumeration(Arrays.asList(jaxb8_5_2List));

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
    if (event == null)
      return null;

    QName eventName = event.getQName();
    Serializable eventValue = event.getValue();

    if (log.isDebugEnabled())
      log.debug("JAXBEventTransformer.getEventMarshal() eventName = " + eventName);
    if (log.isDebugEnabled())
      log.debug("JAXBEventTransformer.getEventMarshal() eventValue = " + eventValue);

    Event newEvent = new Event();
    newEvent.setName(eventName);

    if (eventValue != null) {

      String eventType = eventValue.getClass().getName();//toString().substring("class ".length());
      System.out.println(">>> EXOMAN JAXBEventTransformer.getEventMarshal() eventType = "
          + eventType);

      newEvent.setType(new QName(eventType));

      EventPayload eventPayload = null;

      org.w3c.dom.Document doc = null;
      try {
        doc = getMarshalledDocument(eventValue, eventName);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      }

      if (doc == null) {
        try {

          doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
          JAXBContext jaxbContext = JAXBContext.newInstance(eventValue.getClass());
          Marshaller marshaller = jaxbContext.createMarshaller();
          marshaller.marshal(eventValue, System.out);
          marshaller.marshal(eventValue, doc);

        } catch (JAXBException e) {
          e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      org.apache.axis.message.MessageElement messageElement = new MessageElement(doc.getDocumentElement());
      org.apache.axis.message.MessageElement[] anyArray = new MessageElement[] { messageElement };
      eventPayload = new EventPayload();
      eventPayload.set_any(anyArray);
      newEvent.setPayload(eventPayload);
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
      Object obj = null;
      try {

        try {
          obj = getUnmarshalledObject(event.getType(), event.getPayload());
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }

        if (obj == null) {
          String clazz = event.getType().getLocalPart();

          String pkg = clazz.substring(0, clazz.lastIndexOf("."));
          ClassLoader cle = Thread.currentThread().getContextClassLoader();//was: this.getClass().getClassLoader();

          org.w3c.dom.Element messageElement = event.getPayload().get_any()[0];

          JAXBContext jaxbContext = JAXBContext.newInstance(pkg, cle);
          Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
          obj = unmarshaller.unmarshal(messageElement);

        }
      } catch (JAXBException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getEventsUnmarshal() o = " + obj);

      javax.portlet.Event ev = new EventImpl(event.getName(), obj != null ? (Serializable) obj
                                                                         : null);
      eventsList.add(ev);
      i++;
    }
    return eventsList;
  }

  // EXOMAN'S MAGIC ENUM
  public enum Types {
    STRING(new StandardPayload<String>()),
    BIGINTEGER(new StandardPayload<BigInteger>()),
    BIGDECIMAL(new StandardPayload<BigDecimal>()),
    CALENDAR(new StandardPayload<Calendar>()),
    DATE(new StandardPayload<Date>()),
    QNAME(new StandardPayload<QName>()),
    URI(new StandardPayload<URI>()),
    XMLGREGORIANCALENDAR(new StandardPayload<XMLGregorianCalendar>()),
    DURATION(new StandardPayload<Duration>()),
    IMAGE(new StandardPayload<Image>()),
//    DATAHANDLER(new StandardPayload<javax.activation.DataHandler>()),
    SOURCE(new StandardPayload<Source>()),
    UUID(new StandardPayload<java.util.UUID>());

    static class StandardPayload<T> {

      StandardPayload() {
      }

      public org.w3c.dom.Document getMarshalledDocument(Object value, QName name) {
        T val = (T) value;
        if (log.isDebugEnabled())
          log.debug("StandardPayload.getDocument() val.getClass() = " + val.getClass());
        if (log.isDebugEnabled())
          log.debug("StandardPayload.getDocument() eventName.getLocalPart() = "
              + name.getLocalPart());
        try {
          org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance()
                                                           .newDocumentBuilder()
                                                           .newDocument();
          JAXBElement<T> valueWrapper = new JAXBElement<T>(name, (Class<T>) val.getClass(), val);
          JAXBContext jaxb = JAXBContext.newInstance();
          Marshaller marshaller = jaxb.createMarshaller();
          marshaller.setProperty("jaxb.formatted.output", true);
          marshaller.setProperty("jaxb.fragment", true);
          marshaller.marshal(valueWrapper, System.out);
          marshaller.marshal(valueWrapper, doc);
          if (log.isDebugEnabled())
            log.debug("StandardPayload.getMarshalledDocument() doc = " + doc);
          return doc;
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

      public T getUnmarshalledObject(QName type, EventPayload payload) {
        org.w3c.dom.Element messageElement = payload.get_any()[0];
        if (log.isDebugEnabled())
          log.debug("StandardPayload.getUnmarshalledObject() messageElement = " + messageElement);
        if (log.isDebugEnabled())
          log.debug("StandardPayload.getUnmarshalledObject() type.getLocalPart() = "
              + type.getLocalPart());
        try {
          JAXBContext jaxb = JAXBContext.newInstance();
          Unmarshaller unmarshaller = jaxb.createUnmarshaller();
          StringReader reader = new StringReader(messageElement.toString());
          JAXBElement<T> stdElement = unmarshaller.unmarshal(new StreamSource(reader),
                                                             (Class<T>) Class.forName(type.getLocalPart()));
          if (log.isDebugEnabled())
            log.debug("StandardPayload.getUnmarshalledObject() boolInput.getValue() = "
                + stdElement.getValue());
          return stdElement.getValue();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    }

    private StandardPayload<?> sp;

    Types(StandardPayload<?> s) {
      sp = s;
    }

    public org.w3c.dom.Document getMarshalledDocument(Object value, QName name) {
      return sp.getMarshalledDocument(value, name);
    }

    public Object getUnmarshalledObject(QName type, EventPayload payload) {
      return sp.getUnmarshalledObject(type, payload);
    }
  }

  public static org.w3c.dom.Document getMarshalledDocument(Object value, QName name) {
    if (log.isDebugEnabled())
      log.debug("JAXBEventTransformer.getMarshalledDocument() value = " + value);
    try {
      String toenum = value.getClass().getSimpleName();
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getDocument() toenum = " + toenum);
      Types t = Types.valueOf(toenum.toUpperCase());
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getDocument() t = " + t);
      return t.getMarshalledDocument(value, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Object getUnmarshalledObject(QName type, EventPayload payload) {
    try {
      String source = type.getLocalPart();
      String toenum = source.substring(source.lastIndexOf(".") + 1);
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getUnmarshalledObject() toenum = " + toenum);
      Types t = Types.valueOf(toenum.toUpperCase());
      if (log.isDebugEnabled())
        log.debug("JAXBEventTransformer.getUnmarshalledObject() t = " + t);
      return t.getUnmarshalledObject(type, payload);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
