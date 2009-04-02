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

package org.exoplatform.services.wsrp2.producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ServiceAdministration complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceAdministration", propOrder = { "properties" })
public class ServiceAdministration implements Serializable {

  @XmlElement(required = true, nillable = true)
  protected Map<String, String> properties;

  public Map<String, String> getProperties() {
    if (properties == null) {
      properties = new HashMap<String, String>();
    }
    return this.properties;
  }

  public String getPropertiesAsString() {
    return getProperties().toString();
  }

}
