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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.type.EventPayload;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Aug 29, 2008
 */

public class StandardPayload<T> {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.wsrp2.utils.StandardPayload");

  public org.w3c.dom.Document getMarshalledDocument(Object value, QName name) {
    T val = (T) value;
    if (log.isDebugEnabled())
      log.debug("StandardPayload.getMarshalledDocument() val.getClass() = " + val.getClass());
    if (log.isDebugEnabled())
      log.debug("StandardPayload.getMarshalledDocument() eventName.getLocalPart() = "
          + name.getLocalPart());
    try {
      org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance()
                                                       .newDocumentBuilder()
                                                       .newDocument();
      JAXBElement<T> valueWrapper = new JAXBElement<T>(name, (Class<T>) val.getClass(), val);
      JAXBContext jaxb = JAXBContext.newInstance(val.getClass());
      Marshaller marshaller = jaxb.createMarshaller();
      marshaller.setProperty("jaxb.formatted.output", true);
      marshaller.setProperty("jaxb.fragment", true);
      marshaller.marshal(valueWrapper, doc);
      if (log.isDebugEnabled())
        log.debug("StandardPayload.getMarshalledDocument() doc = " + doc);
      return doc;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  public T getUnmarshalledObject(QName type, EventPayload payload) {
    org.w3c.dom.Element messageElement = (org.w3c.dom.Element) payload.getAny();
    if (log.isDebugEnabled())
      log.debug("StandardPayload.getUnmarshalledObject() messageElement = " + messageElement);
    if (log.isDebugEnabled())
      log.debug("StandardPayload.getUnmarshalledObject() type.getLocalPart() = "
          + type.getLocalPart());

    try {
      JAXBContext jaxb = JAXBContext.newInstance(Class.forName(type.getLocalPart()));
      Unmarshaller unmarshaller = jaxb.createUnmarshaller();
      Class<T> classT = (Class<T>) Class.forName(type.getLocalPart());
      JAXBElement<T> stdElement = unmarshaller.unmarshal(messageElement, classT);
      if (log.isDebugEnabled())
        log.debug("StandardPayload.getUnmarshalledObject() boolInput.getValue() = "
            + stdElement.getValue());
      return stdElement.getValue();
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }
}
