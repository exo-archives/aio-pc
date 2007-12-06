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
public class EventDefinition {
  private List<Description> description;
	private QName name;
  private List<QName> aliases;
  private String javaClass;
  private String id;

	public List<Description> getDescription() {
    if (description == null)
      return Constants.EMPTY_LIST;
		return description;
	}

	public void setDescription(List<Description> description) {
		this.description = description;
	}

  public void addDescription(Description desc) {
  	if (this.description == null)
  	  description = new ArrayList<Description>();
    this.description.add(desc);
  }

  public QName getPrefferedName() { return this.name; }

  public void setPrefferedName(QName value) {
    this.name = value;
  }

  public void addAlias(QName value) {
    if (aliases == null)
      aliases = new ArrayList<QName>();
    aliases.add(value);
  }

  public List<QName> getAliases() {
    if (aliases == null)
      return Constants.EMPTY_LIST;
    return aliases;
  }

  public String getJavaClass() { return this.javaClass; }

  public void setJavaClass(String value) { this.javaClass = value; }

  public String getId() { return this.id ; }

  public void setId(String value) { this.id = value ; }

  public void setAliases(List<QName> newAliases) {
    aliases = newAliases;
  }
}
