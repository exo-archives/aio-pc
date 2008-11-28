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
package org.exoplatform.services.wsrp2.producer.impl.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 21, 2008
 */
public class CalendarUtils {
  private static final Log LOG = ExoLogger.getLogger(CalendarUtils.class);

  public static XMLGregorianCalendar convertCalendar(Calendar calendar) {
    GregorianCalendar gc = (GregorianCalendar) calendar;
    XMLGregorianCalendar result = null;
    try {
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    } catch (Exception e) {
      LOG.error("Exception in the CalendarUtils class while creating XMLGregorianCalendar", e);
    }
    return result;
  }

  public static XMLGregorianCalendar getNow() {
    return convertCalendar(Calendar.getInstance());
  }

}
