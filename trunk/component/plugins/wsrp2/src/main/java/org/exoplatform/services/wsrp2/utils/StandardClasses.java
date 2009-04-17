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

import java.awt.Image;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.activation.DataHandler;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;

import org.exoplatform.services.wsrp2.type.EventPayload;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Aug 29, 2008
 */

public enum StandardClasses {

  // MAGIC ENUM

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
  DATAHANDLER(new StandardPayload<DataHandler>()),
  SOURCE(new StandardPayload<Source>()),
  UUID(new StandardPayload<java.util.UUID>());

  private StandardPayload<?> sp;

  StandardClasses(StandardPayload<?> s) {
    sp = s;
  }

  public org.w3c.dom.Document getMarshalledDocument(Object value, QName name) {
    return sp.getMarshalledDocument(value, name);
  }

  public Object getUnmarshalledObject(QName type, EventPayload payload) {
    return sp.getUnmarshalledObject(type, payload);
  }
}
