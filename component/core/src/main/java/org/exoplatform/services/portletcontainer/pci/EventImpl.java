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
import javax.portlet.Event;
import javax.xml.namespace.QName;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 */
public class EventImpl implements Event {

  private QName name = null;
  private Serializable value = null;

  public EventImpl(QName name, Serializable value) {
    this.name = name;
    this.value = value;
  }

  public QName getQName() {
    return name;
  }

  public String getName() {
    return name.getLocalPart();
  }

  public Serializable getValue() {
    return value;
  }

}
