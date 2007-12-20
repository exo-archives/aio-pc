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
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.exoplatform.Constants;

/**
 * @author: Roman Pedchenko
 * @email:  roman.pedchenko@exoplatform.com.ua
 * @version: $Id$
 */
public class PublicRenderParameter {
  private List<String> description;

  private String       identifier;

  private QName        qname;

  private String       name;

  private List<QName>  aliases;

  private String       id;

  public List<String> getDescription() {
    if (description == null)
      return Constants.EMPTY_LIST;
    return description;
  }

  public void setDescription(List<String> description) {
    this.description = description;
  }

  public void addDescription(String desc) {
    if (this.description == null)
      description = new ArrayList<String>();
    this.description.add(desc);
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String value) {
    this.identifier = value;
  }

  public void addAlias(QName value) {
    if (aliases == null)
      aliases = new ArrayList<QName>();
    aliases.add(value);
  }

  public List<QName> getAlias() {
    if (aliases == null)
      return Constants.EMPTY_LIST;
    return aliases;
  }
  
  public QName getQname() {
    return qname;
  }
  
  public void setQname(QName qname) {
    this.qname = qname;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getId() {
    return this.id;
  }

  public void setId(String value) {
    this.id = value;
  }

}
